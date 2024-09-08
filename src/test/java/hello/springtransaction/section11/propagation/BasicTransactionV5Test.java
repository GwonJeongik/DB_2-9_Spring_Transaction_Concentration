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
 * 5. 스프링 트랜잭션 전파5 - 외부 롤백
 *
 * 외부 트랜잭션 시작하고, 내부 트랜잭션 시작
 * 내부 트랜잭션 커밋 [논리 트랜잭션]
 * 외부 트랜잭션 롤백 [논리 트랜잭션], [외부 트랜잭션만 물리적인 커밋, 롤백을 처리]
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
        log.info("내부 트랜잭션 커밋");
        transactionManager.commit(inner);

        log.info("외부 트랜잭션 롤백");
        transactionManager.rollback(outer);
    }
}
