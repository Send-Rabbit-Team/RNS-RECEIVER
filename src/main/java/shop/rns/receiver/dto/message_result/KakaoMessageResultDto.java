package shop.rns.receiver.dto.message_result;

import lombok.*;
import shop.rns.receiver.config.status.MessageStatus;
import shop.rns.receiver.domain.redis.RKakaoMessageResult;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KakaoMessageResultDto {
    private String rMessageResultId;

    private long messageId;

    private long brokerId;

    private long contactId;

    private long retryCount;

    private MessageStatus messageStatus;

    private LocalDateTime createdAt;

    public static RKakaoMessageResult toRMessageResult(KakaoMessageResultDto dto){
        return RKakaoMessageResult.builder()
                .id(dto.getRMessageResultId())
                .kakaoMessageId(dto.getMessageId())
                .kakaoBrokerId(dto.getBrokerId())
                .contactId(dto.getContactId())
                .messageStatus(dto.getMessageStatus())
                .build();
    }
}
