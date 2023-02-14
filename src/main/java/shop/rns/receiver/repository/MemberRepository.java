package shop.rns.receiver.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.rns.receiver.config.status.BaseStatus;
import shop.rns.receiver.config.type.LoginType;
import shop.rns.receiver.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph("Member.with.Company")
    Optional<Member> findByIdAndStatus(long memberId, BaseStatus baseStatus);

    Optional<Member> findByEmailIgnoreCase(String email);

    Optional<Member> findByEmailIgnoreCaseAndLoginType(String email, LoginType loginType);
}
