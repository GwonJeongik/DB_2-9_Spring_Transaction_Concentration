package hello.springtransaction.section11.propagation.seventh;

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
public class LogRepository2 {

    private final EntityManager em;

    @Transactional
    public void save(Log2 logMessage) {
        Session session = em.unwrap(Session.class);
        Connection conn = session.doReturningWork(connection -> connection);
        log.info("사용중인 커넥션={}", conn);

        log.info("log2 저장");
        em.persist(logMessage);

        if (logMessage.getMessage().contains("로그 예외")) {
            log.info("log 저장 시 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    public Optional<Log2> find(String message) {
        return em.createQuery("select l from Log2 l where l.message=:message", Log2.class)
                .setParameter("message", message)
                .getResultList().stream().findAny();
    }
}
