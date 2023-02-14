package shop.rns.receiver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import shop.rns.receiver.config.status.BaseStatus;
import shop.rns.receiver.domain.ContactGroup;

import java.util.List;
import java.util.Optional;


public interface ContactGroupRepository extends JpaRepository<ContactGroup,Long> {
    Optional<ContactGroup> findByNameAndStatus(String name, BaseStatus status);

    List<ContactGroup>findByMemberIdAndStatusOrderByUpdatedAtDesc(long memberId, BaseStatus baseStatus);

    @Query(value = "select cg from ContactGroup cg where cg.member.id = :memberId and cg.status = :status",
    countQuery = "select count(cg) from ContactGroup cg where cg.member.id = :memberId and cg.status = :status")
    Page<ContactGroup> findAllContactGroup(long memberId, BaseStatus status, Pageable pageable);

    Optional<ContactGroup> findByIdAndStatus(long id, BaseStatus status);
}
