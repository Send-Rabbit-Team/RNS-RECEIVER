package shop.rns.receiver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import shop.rns.receiver.domain.Contact;
import shop.rns.receiver.domain.Message;
import shop.rns.receiver.domain.MessageResult;

import java.util.List;
import java.util.Optional;

public interface MessageResultRepository extends JpaRepository<MessageResult, Long> {
    List<MessageResult> findAllByMessageIdOrderByIdDesc(long messageId);

    Optional<MessageResult> findByContactAndMessage(Contact contact, Message message);

    List<MessageResult> findAllByDescriptionLike(String description);
}
