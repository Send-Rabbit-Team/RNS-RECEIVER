package shop.rns.receiver.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.rns.receiver.config.status.MessageStatus;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RMessageResult {
    private String id;

    private Long messageId;

    private Long contactId;

    private Long brokerId;

    private String description;

    private MessageStatus messageStatus = MessageStatus.PENDING;

    // 편의 메서드
    public void changeMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public void addDescription(String description){
        this.description += " " + description;
    }

    public void requeueDescription(String brokerName) {
        this.description = brokerName;
    }

    public void resendOneDescription(String brokerName) {
        switch (brokerName) {
            case "kt":
                this.description = "lg -> kt";
                break;

            case "skt":
                this.description = "kt -> skt";
                break;

            case "lg":
                this.description = "skt -> lg";
                break;
        }
    }

    public void resendTwoDescription(String brokerName) {
        switch (brokerName) {
            case "kt":
                this.description = "skt -> lg -> kt";
                break;

            case "skt":
                this.description = "lg -> kt -> skt";
                break;

            case "lg":
                this.description = "kt -> skt -> lg";
                break;
        }
    }

    public void changeId(String id) {
        this.id = id;
    }
}
