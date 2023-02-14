package shop.rns.receiver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import shop.rns.receiver.domain.MessageImage;

public interface MessageImageRepository extends JpaRepository<MessageImage, Long> {
}
