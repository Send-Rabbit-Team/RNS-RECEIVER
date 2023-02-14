package shop.rns.receiver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import shop.rns.receiver.dto.message_result.KakaoMessageResultDto;

import static shop.rns.receiver.dlx.DlxProcessingErrorHandler.KAKAO_BROKER_DEAD_COUNT;

@Log4j2
@Service
@RequiredArgsConstructor
public class KakaoBrokerService {
    private final int TMP_MESSAGE_DURATION = 5 * 60;
    private final int VALUE_MESSAGE_DURATION = 30 * 60;

    private final KakaoBrokerCacheService kakaoBrokerCacheService;

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    // 알림톡 발송 실패 처리
    public void processMessageFailure(String brokerName, KakaoMessageResultDto messageResultDto){
        messageResultDto.setRetryCount(KAKAO_BROKER_DEAD_COUNT);
        kakaoBrokerCacheService.updateRMessageResult(messageResultDto, brokerName);
        kakaoBrokerCacheService.saveMessageResult(messageResultDto, brokerName);

        log.warn(brokerName + " broker got dead letter - {}", messageResultDto);
    }

    // Json 형태로 반환
    public String convertToJson(Object object){
        String sendMessageJson = null;
        try {
             sendMessageJson = objectMapper.writeValueAsString(object);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return sendMessageJson;
    }
}
