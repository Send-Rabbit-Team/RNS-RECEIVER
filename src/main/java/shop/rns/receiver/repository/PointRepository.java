package shop.rns.receiver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import shop.rns.receiver.domain.Point;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByMemberId(Long memberId);
}
