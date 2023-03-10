package shop.rns.receiver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import shop.rns.receiver.config.domain.BaseEntity;

import javax.persistence.*;

@NamedEntityGraph(name = "Contact.with.Member.ContactGroup", attributeNodes = {
        @NamedAttributeNode(value = "member", subgraph = "member"),
        @NamedAttributeNode("contactGroup")
        },
        subgraphs = @NamedSubgraph(name = "member", attributeNodes = {
                @NamedAttributeNode("company")
        })
)
@Log4j2
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Builder
public class Contact extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="group_id")
    private ContactGroup contactGroup;

    private String phoneNumber;

    private String memo;

    public void changeContactGroup(ContactGroup contactGroup){this.contactGroup = contactGroup;}
    private void changePhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}
    private void changeMemo(String memo){this.memo = memo;}
    public void quitContactGroup() {
        this.contactGroup = null;
    }
}
