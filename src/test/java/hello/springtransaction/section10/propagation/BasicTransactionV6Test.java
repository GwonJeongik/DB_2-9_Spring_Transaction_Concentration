package hello.springtransaction.section10.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

/**
 * 7. 스프링 트랜잭션 전파7 - REQUIRES_NEW
 * <p>
 * `외부` 트랜잭션 시작 [물리 트랜잭션]
 * REQUIRES_NEW 설정하면 외부 트랜잭션에 참여하는 게 아니라 새로운 트랜잭션을 생성한다.
 * `내부` 트랜잭션 시작 [물리 트랜잭션]
 * 외부 트랜잭션 보류 새로 시작한 `내부` 트랜잭션 사용
 * <p>
 * 내부 트랜잭션 롤백하면, 참여한 트랜잭션이 아니라서 바로 롤백됨.
 * <p>
 * 외부 트랜잭션 커밋하면 커밋됨.
 */
@Slf4j
@SpringBootTest
public class BasicTransactionV6Test {

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
    void inner_rollback_requires_new() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outer = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("outer.isNewTransaction()={}", outer.isNewTransaction());

        log.info("내부 트랜잭션 시작");

        // 외부 트랜잭션과 별개로 내부 트랜잭션을 새로 만드는 설정
        DefaultTransactionAttribute definition = new DefaultTransactionAttribute();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        TransactionStatus inner = transactionManager.getTransaction(definition);
        log.info("inner.isNewTransaction()={}", inner.isNewTransaction());

        log.info("내부 트랜잭션 롤백");
        transactionManager.rollback(inner);

        log.info("외부 트랜잭션 커밋");
        transactionManager.commit(outer);
    }
}
