package shop.rns.receiver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.rns.receiver.config.domain.BaseEntity;
import shop.rns.receiver.config.type.MessageType;

import javax.persistence.*;

@NamedEntityGraph(name = "Message.with.Member.SenderNumber", attributeNodes = {
        @NamedAttributeNode(value = "member", subgraph = "member_company"),
        @NamedAttributeNode("senderNumber"),
},
        subgraphs = @NamedSubgraph(name = "member_company", attributeNodes = {
                @NamedAttributeNode("company")
        })
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Message extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_number_id")
    private SenderNumber senderNumber;

    private String subject;

    private String content;

    private long cost;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    // 편의 메서드
}
