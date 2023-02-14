package shop.rns.receiver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.rns.receiver.domain.Broker;

import java.util.List;

public interface BrokerRepository extends JpaRepository<Broker, Long> {
    Broker findByName(String name);

    List<Broker> findAll();
}
