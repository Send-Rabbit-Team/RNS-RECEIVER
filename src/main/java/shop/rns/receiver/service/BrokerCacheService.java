package shop.rns.receiver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import shop.rns.receiver.config.status.MessageStatus;
import shop.rns.receiver.domain.Broker;
import shop.rns.receiver.domain.Contact;
import shop.rns.receiver.domain.Message;
import shop.rns.receiver.domain.MessageResult;
import shop.rns.receiver.domain.redis.RMessageResult;
import shop.rns.receiver.dto.message_result.MessageResultDto;
import shop.rns.receiver.repository.MessageResultRepository;
import shop.rns.receiver.repository.cache.BrokerCacheRepository;
import shop.rns.receiver.repository.cache.ContactCacheRepository;
import shop.rns.receiver.repository.cache.MessageCacheRepository;
import shop.rns.receiver.repository.redis.RedisHashRepository;


@Log4j2
@Service
@RequiredArgsConstructor
public class BrokerCacheService {
    private final ObjectMapper objectMapper;

    private final PointService pointService;

    private final BrokerCacheRepository brokerCacheRepository;
    private final MessageCacheRepository messageCacheRepository;
    private final ContactCacheRepository contactCacheRepository;

    private final RedisHashRepository redisHashRepository;

    private final MessageResultRepository messageResultRepository;


    public void updateRMessageResult(final MessageResultDto messageResultDto, String brokerName) {
        String rMessageResultId = messageResultDto.getRMessageResultId();

        // Redis에서 상태 가져오기
        String statusKey = "message.status." + messageResultDto.getMessageId();

        // Redis에 해당 데이터가 없을 경우 종료
        if (!redisHashRepository.isExist(statusKey, rMessageResultId))
            return;

        String jsonRMessageResult = redisHashRepository.findByRMessageResultId(statusKey, rMessageResultId);

        // 상태 업데이트 및 저장
        RMessageResult rMessageResult = convertToRMessageResult(jsonRMessageResult);

        // 재전송 여부인지 확인
        // TODO 추후에 알고리즘 작성해서 코드 간략화하기
        long retryCount = messageResultDto.getRetryCount();
        if (retryCount >= 1) {
            rMessageResult.changeMessageStatus(MessageStatus.RESEND);

            if (retryCount == 1) {
                rMessageResult.requeueDescription(brokerName);
            } else if (retryCount == 2) {
                rMessageResult.resendOneDescription(brokerName);
            } else if (retryCount == 3) {
                rMessageResult.resendTwoDescription(brokerName);
            }

        } else {
            rMessageResult.changeMessageStatus(MessageStatus.SUCCESS);
        }

        redisHashRepository.update(statusKey, rMessageResultId, rMessageResult);
    }

    public void saveMessageResult(final MessageResultDto messageResultDto, String brokerName) {
        Message message = messageCacheRepository.findMessageById(messageResultDto.getMessageId());
        Contact contact = contactCacheRepository.findContactByContactIdAndMessageId(messageResultDto.getContactId(), messageResultDto.getMessageId());
        Broker broker = brokerCacheRepository.findBrokerById(messageResultDto.getBrokerId());

        MessageResult messageResult = MessageResult.builder()
                .message(message)
                .contact(contact)
                .broker(broker)
                .messageStatus(messageResultDto.getMessageStatus())
                .build();

        // 재전송 여부인지 확인
        // TODO 추후에 알고리즘 작성해서 코드 간략화하기
        long retryCount = messageResultDto.getRetryCount();

        if (retryCount >= 1) {
            messageResult.changeMessageStatus(MessageStatus.RESEND);

            if (retryCount == 1) {
                messageResult.requeueDescription(brokerName);
            } else if (retryCount == 2) {
                messageResult.resendOneDescription(brokerName);
            } else if (retryCount == 3) {
                messageResult.resendTwoDescription(brokerName);
            }
        }

        messageResultRepository.save(messageResult);
        log.info("[" + messageResult.getMessageStatus() + "] " + "[" + brokerName + "]" + " MessageResult 객체가 저장되었습니다. id : {}", messageResult.getId());
    }

    public void saveMessageResultFailure(final MessageResultDto messageResultDto, String brokerName){
        // RDBMS SAVE
        Message message = messageCacheRepository.findMessageById(messageResultDto.getMessageId());
        Contact contact = contactCacheRepository.findContactByContactIdAndMessageId(messageResultDto.getContactId(), messageResultDto.getMessageId());
        Broker broker = brokerCacheRepository.findBrokerById(messageResultDto.getBrokerId());

        MessageResult messageResult = MessageResult.builder()
                .message(message)
                .contact(contact)
                .broker(broker)
                .messageStatus(messageResultDto.getMessageStatus())
                .build();

        messageResult.resendTwoDescription(brokerName);
        messageResult.addDescription("중계사 오류");
        messageResult.changeMessageStatus(MessageStatus.FAIL);

        messageResultRepository.save(messageResult);
        log.info("[" + messageResult.getMessageStatus() + "] " + "[" + brokerName + "]" + " MessageResult 객체가 저장되었습니다. id : {}", messageResult.getId());

        // 환불
        int refundSmsPoint = pointService.refundMessagePoint(message.getMember(), 1, message.getMessageType());
        messageResult.addDescription(refundSmsPoint + " 문자당근 환불");

        // 상태 DB (REDIS)
        String rMessageResultId = messageResultDto.getRMessageResultId();

        // Redis에서 상태 가져오기
        String statusKey = "message.status." + messageResultDto.getMessageId();

        // Redis에 해당 데이터가 없을 경우 종료
        if (!redisHashRepository.isExist(statusKey, rMessageResultId))
            return;

        String jsonRMessageResult = redisHashRepository.findByRMessageResultId(statusKey, rMessageResultId);

        // 상태 업데이트 및 저장
        RMessageResult rMessageResult = convertToRMessageResult(jsonRMessageResult);

        rMessageResult.resendTwoDescription(brokerName);
        rMessageResult.addDescription("중계사 오류");
        rMessageResult.changeMessageStatus(MessageStatus.FAIL);

        rMessageResult.addDescription(refundSmsPoint + " 문자당근 환불");

        redisHashRepository.update(statusKey, rMessageResultId, rMessageResult);
    }

    public RMessageResult convertToRMessageResult(String json) {
        RMessageResult rMessageResult = null;
        try {
            rMessageResult = objectMapper.readValue(json, RMessageResult.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rMessageResult;
    }
}
