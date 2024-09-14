package hello.springtransaction.section11.propagation.first;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 1. 트랜잭션 전파 활용1 - 예제 프로젝트 시작
 * <p>
 * 비즈니스 요구사항
 * 회원을 등록하고 조회한다.
 * 회원에 대한 변경 이력을 추적할 수 있도록 회원 데이터가 변경될 때 변경 이력을 DB LOG 테이블에 남겨야 한
 * 다.
 * 여기서는 예제를 단순화 하기 위해 회원 등록시에만 DB LOG 테이블에 남긴다.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class LogRepository {

    private final EntityManager em;

//    @Transactional
    public void save(Log logMessage) {
        log.info("log 저장");
        em.persist(logMessage);

        if (logMessage.getMessage().contains("로그 예외")) {
            log.info("log 저장 시 예외 발생");
            throw new RuntimeException("예외 발생");
        }
    }

    public Optional<Log> find(String message) {
        return em.createQuery("select l from Log l where l.message=:message", Log.class)
                .setParameter("message", message)
                .getResultList().stream().findAny();
    }
}
