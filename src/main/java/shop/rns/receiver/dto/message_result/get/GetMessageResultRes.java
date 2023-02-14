package shop.rns.receiver.dto.message_result.get;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import shop.rns.receiver.config.status.MessageStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetMessageResultRes {
    private String contactPhoneNumber;

    private String contactGroup;

    private String memo;

    @JsonIgnore
    private long brokerId;

    private String brokerName;

    private String description;

    private MessageStatus messageStatus;

    private String createdAt;
}
