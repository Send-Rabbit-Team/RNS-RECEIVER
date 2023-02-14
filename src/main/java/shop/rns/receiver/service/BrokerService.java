package shop.rns.receiver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import shop.rns.receiver.domain.Contact;
import shop.rns.receiver.dto.message_result.MessageResultDto;

import static shop.rns.receiver.dlx.DlxProcessingErrorHandler.MESSAGE_BROKER_DEAD_COUNT;


@Log4j2
@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
@RequiredArgsConstructor
public class BrokerService {
    private final int TMP_MESSAGE_DURATION = 5 * 60;
    private final int VALUE_MESSAGE_DURATION = 10 * 60;

    private final ObjectMapper objectMapper;

    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, Contact> redisTemplate;

    private final BrokerCacheService brokerCacheService;
    private final PointService pointService;

    // 메시지 발송 실패 처리
    public void processMessageFailure(String brokerName, MessageResultDto messageResultDto) {
        messageResultDto.setRetryCount(MESSAGE_BROKER_DEAD_COUNT);
        brokerCacheService.saveMessageResultFailure(messageResultDto, brokerName);

        log.warn(brokerName + " broker got dead letter - {}", messageResultDto);
    }

    // Json 형태로 반환
    public String convertToJson(Object object) {
        String sendMessageJson = null;
        try {
            sendMessageJson = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sendMessageJson;
    }
}
