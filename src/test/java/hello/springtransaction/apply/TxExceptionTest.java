package hello.springtransaction.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class TxExceptionTest {

    @Autowired
    ExceptionService service;

    @Test
    void runtimeException() {
        assertThatThrownBy(() -> service.runtimeException())
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void exception() {
        assertThatThrownBy(() -> service.exception())
                .isInstanceOf(Exception.class);
    }

    @Test
    void myException() {
        assertThatThrownBy(() -> service.myException())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class TxExceptionTestConfig {

        @Bean
        ExceptionService exceptionService() {
            return new ExceptionService();
        }
    }

    @Slf4j
    @Transactional
    static class ExceptionService {

        //런타임 예외: 롤백
        public void runtimeException() {
            throw new RuntimeException();
        }

        //체크 예외: 커밋
        public void exception() throws Exception {
            throw new Exception();
        }

        //체크 예외: 롤백 -> rollbackFor 사용
        @Transactional(rollbackFor = MyException.class)
        public void myException() throws MyException {
            throw new MyException();
        }
    }

    static class MyException extends Exception {
    }
}
