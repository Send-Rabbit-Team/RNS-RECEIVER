package shop.rns.receiver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.rns.receiver.config.domain.BaseTimeEntity;
import shop.rns.receiver.config.status.MessageStatus;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class MessageResult extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "broker_id")
    private Broker broker;

    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;

    private String description;

    // 편의 메서드
    public void changeMessageStatus(MessageStatus messageStatus){
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
}
