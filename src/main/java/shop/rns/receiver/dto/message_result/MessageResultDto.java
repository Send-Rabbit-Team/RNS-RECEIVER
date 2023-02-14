package shop.rns.receiver.dto.message_result;

import lombok.*;
import shop.rns.receiver.config.status.MessageStatus;
import shop.rns.receiver.domain.redis.RMessageResult;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageResultDto {
    private String rMessageResultId;

    private long messageId;

    private long brokerId;

    private long contactId;

    private MessageStatus messageStatus;

    private long retryCount;

    private LocalDateTime createdAt;

    public static RMessageResult toRMessageResult(MessageResultDto dto){
        return RMessageResult.builder()
                .id(dto.getRMessageResultId())
                .messageId(dto.messageId)
                .brokerId(dto.brokerId)
                .contactId(dto.contactId)
                .messageStatus(dto.messageStatus)
                .build();
    }
}
