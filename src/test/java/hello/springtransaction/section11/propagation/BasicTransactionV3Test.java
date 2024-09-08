package hello.springtransaction.section11.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

/**
 * 3. 스프링 트랜잭션 전파3 - 전파 기본
 * 4. 스프링 트랜잭션 전파3 - 전파 예제
 * <p>
 * 모든 논리 트랜잭션 커밋되면, 물리 트랜잭션은 커밋
 * 논리 트랜잭션중 하나라도 롤백 되면, 물리 트랜잭션은 롤백
 * <p>
 * 외부 트랜잭션 시작하고, 내부에서 다른 트랜잭션 시작한다.
 * 그리고 내부, 외부 둘 다 커밋하는 로직
 * <p>
 * 외부 트랜잭션 시작하고, 내부 트랜잭션 시작하면
 * 외부 트랜잭션에 참여한다.
 * <p>
 * .isNewTransaction() -> 트랜잭션을 처음 시작하는 지 확인할 수 있는 메서드
 */
@Slf4j
@SpringBootTest
public class BasicTransactionV3Test {

    @Autowired
    PlatformTransactionManager transactionManager;

    @TestConfiguration
    static class Config {

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Test
    void outer_inner_commit() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

        log.info("내부 트랜잭션 커밋");
        transactionManager.commit(inner);

        log.info("외부 트랜잭션 커밋");
        transactionManager.commit(outer);
    }
}
