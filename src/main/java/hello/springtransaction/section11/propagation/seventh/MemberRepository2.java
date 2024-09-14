package hello.springtransaction.section11.propagation.seventh;

import hello.springtransaction.section11.propagation.first.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.util.Optional;

/**
 * 7. 트랜잭션 전파 활용7 - 복구 REQUIRES_NEW
 * MemberFacade -> MemberService2 -> MemberRepository2
 * MemberFacade -> LogRepository2
 * <p>
 * REQUIRES_NEW 사용 시, 트랜잭션 2개 -> 커넥션 2개 사용 방지하는 방법중 하나로 `MemberFacade` 시도
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository2 {

    private final EntityManager em;

    @Transactional
    public void save(Member member) {
        Session session = em.unwrap(Session.class);
        Connection conn = session.doReturningWork(connection -> connection);
        log.info("사용중인 커넥션={}", conn);

        log.info("member 저장");
        em.persist(member);
    }

    public Optional<Member> find(String username) {
        return em.createQuery("select m from Member m where m.username=:username", Member.class)
                .setParameter("username", username)
                .getResultList().stream().findAny();
    }
}
