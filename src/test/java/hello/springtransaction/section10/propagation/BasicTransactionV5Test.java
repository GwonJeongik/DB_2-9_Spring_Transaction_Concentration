package hello.springtransaction.section10.propagation;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

/**
 * 6. 스프링 트랜잭션 전파5 - 내부 롤백
 * <p>
 * 외부 트랜잭션 시작하고, 내부 트랜잭션 시작
 * 내부 트랜잭션 롤백 [논리 트랜잭션]
 * `rollbackOnly = true`로 설정됨.
 * 외부 트랜잭션 커밋 [논리 트랜잭션], [외부 트랜잭션만 물리적인 커밋, 롤백을 처리]
 * 물리 트랜잭션 롤백
 */
@Slf4j
@SpringBootTest
public class BasicTransactionV5Test {

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
    void outer_rollback_inner_commit() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

        log.info("내부 트랜잭션 시작");
        TransactionStatus inner = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("내부 트랜잭션 롤백");
        transactionManager.rollback(inner); // `rollbackOnly = true`로 설정

        log.info("외부 트랜잭션 커밋");
        Assertions.assertThatThrownBy(() -> transactionManager.commit(outer))
                .isInstanceOf(UnexpectedRollbackException.class);
    }
}
