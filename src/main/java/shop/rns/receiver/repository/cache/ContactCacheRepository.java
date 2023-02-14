package shop.rns.receiver.repository.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import shop.rns.receiver.config.exception.BaseException;
import shop.rns.receiver.domain.Contact;
import shop.rns.receiver.repository.ContactGroupRepository;
import shop.rns.receiver.repository.ContactRepository;
import shop.rns.receiver.repository.redis.RedisHashRepository;

import static shop.rns.receiver.config.response.BaseResponseStatus.NOT_EXIST_CONTACT;


@Slf4j
@Component
@RequiredArgsConstructor
public class ContactCacheRepository {
    private final ObjectMapper objectMapper;

    private final RedisHashRepository<String> redisHashRepository;

    private final ContactRepository contactRepository;

    private final ContactGroupRepository contactGroupRepository;

    @Cacheable(value = "Contact", key = "#contactId")
    public Contact findContactById(long contactId){
        return contactRepository.findContactById(contactId).orElseThrow(() -> new BaseException(NOT_EXIST_CONTACT));
    }

    // 레디스에서 직접 조회 (브로커에서 메시지 저장 캐싱 용도)
    public Contact findContactByContactIdAndMessageId(long contactId, long messageId) {
        String contactKey = "message.contact." + messageId;
        String contactJson = redisHashRepository.findByContactId(contactKey, String.valueOf(contactId));

        Contact contact = null;

        try {
            contact = objectMapper.readValue(contactJson, Contact.class);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }

        return contact;
    }
}
