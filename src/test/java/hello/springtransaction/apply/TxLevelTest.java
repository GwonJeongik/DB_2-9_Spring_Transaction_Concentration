package hello.springtransaction.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxLevelTest {

    @Autowired
    LevelService service;

    @Test
    void proxyCheck() {
        log.info("aop class={}", service);
        Assertions.assertThat(AopUtils.isAopProxy(service)).isTrue();
    }

    @Test
    void txLevelTest() {
        service.write();
        service.read();
    }

    @TestConfiguration
    static class TxApplyConfig {

        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }

    @Slf4j
    @Transactional(readOnly = true)
    static class LevelService {

        @Transactional(readOnly = false)
        public void write() {
            log.info("call write");
            printTxInfo();
        }

        public void read() {
            log.info("call read");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("txActive={}", txActive);
            boolean txReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("txReadOnly={}", txReadOnly);
        }
    }
}
