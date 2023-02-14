package shop.rns.receiver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.rns.receiver.config.status.BaseStatus;
import shop.rns.receiver.domain.SenderNumber;

import java.util.List;
import java.util.Optional;

public interface SenderNumberRepository extends JpaRepository<SenderNumber, Long> {
    Optional<SenderNumber> findByMemberIdAndPhoneNumberAndStatus(Long memberId, String phoneNumber, BaseStatus status);

    @Query(value = "select sn from SenderNumber sn where sn.member.id = :memberId and sn.status = :status",
    countQuery = "select count(sn) from SenderNumber sn where sn.member.id = :memberId and sn.status = :status")
    Page<SenderNumber> findAllSenderNumber(Long memberId, BaseStatus status, Pageable pageable);

    List<SenderNumber> findByMemberIdAndStatusOrderByUpdatedAtDesc(Long memberId, BaseStatus status);

    Optional<SenderNumber> findByIdAndStatus(Long senderNumberId, BaseStatus status);

    Optional<SenderNumber> findByBlockNumberAndStatus(String blockNumber, BaseStatus status);

}
