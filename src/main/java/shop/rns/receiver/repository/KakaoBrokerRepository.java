package shop.rns.receiver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import shop.rns.receiver.config.status.BaseStatus;
import shop.rns.receiver.domain.KakaoBroker;

import java.util.List;

public interface KakaoBrokerRepository extends JpaRepository<KakaoBroker, Long> {
    List<KakaoBroker> findAllByStatus(BaseStatus status);
}
