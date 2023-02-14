package shop.rns.receiver.repository.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import shop.rns.receiver.config.exception.BaseException;
import shop.rns.receiver.domain.KakaoMessage;
import shop.rns.receiver.domain.Message;
import shop.rns.receiver.repository.KakaoMessageRepository;
import shop.rns.receiver.repository.MessageRepository;

import static shop.rns.receiver.config.response.BaseResponseStatus.NOT_EXIST_MESSAGE;


@Slf4j
@Component
@RequiredArgsConstructor
public class MessageCacheRepository {

    private final MessageRepository messageRepository;

    private final KakaoMessageRepository kakaoMessageRepository;

    @Cacheable(value = "Message", key = "#messageId")
    public Message findMessageById(long messageId){
        return messageRepository.findMessageById(messageId).orElseThrow(() -> new BaseException(NOT_EXIST_MESSAGE));
    }

    @Cacheable(value = "KakaoMessage", key = "#messageId")
    public KakaoMessage findKakaoMessageById(long messageId){
        return kakaoMessageRepository.findKakaoMessageById(messageId).orElseThrow(() -> new BaseException(NOT_EXIST_MESSAGE));
    }
}
