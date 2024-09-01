package hello.springtransaction.apply;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Call;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV1Test {

    @Autowired
    CallService service;

    @TestConfiguration
    static class InternalCallV1Config {

        @Bean
        public CallService callService() {
            return new CallService();
        }
    }

    @Test
    void printProxy() {
        log.info("printProxy={}", service);
    }

    @Test
    void external() {
        service.external();
    }

    @Test
    void internal() {
        service.internal();
    }

    @Slf4j
    static class CallService {

        public void external() {
            log.info("call external");
            printTxInfo();
            internal();
        }

        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("tx active={}", txActive);
        }
    }
}
