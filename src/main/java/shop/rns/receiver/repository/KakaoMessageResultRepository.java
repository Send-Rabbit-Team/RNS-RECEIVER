package shop.rns.receiver.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import shop.rns.receiver.domain.KakaoMessageResult;

import java.util.List;

public interface KakaoMessageResultRepository extends JpaRepository<KakaoMessageResult, Long> {

    List<KakaoMessageResult> findKakaoMessageResultByKakaoMessageId(Long kakaoMessageId);
}
