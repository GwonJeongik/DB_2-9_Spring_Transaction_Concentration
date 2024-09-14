package hello.springtransaction.section11.propagation.fifth;

import hello.springtransaction.section11.propagation.first.LogRepository;
import hello.springtransaction.section11.propagation.first.MemberRepository;
import hello.springtransaction.section11.propagation.first.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 5. 트랜잭션 전파 활용5 - 전파 롤백
 */
@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    /**
     * MemberService    @Transaction : ON -> 논리 트랜잭션 [신규 트랜잭션 => 물리 트랜잭션]
     * MemberRepository @Transaction : ON -> 논리 트랜잭션
     * LogRepository    @Transaction : ON -> 논리 트랜잭션 -> Exception
     */
    @Test
    void outer_transaction_on_fail() {
        //given
        String username = "로그 예외_outer_transaction_on_fail";

        //when :
        // `joinV1`은 `LogRepository`에서 날아온 예외를 잡지 않는다.
        // `@Transaction AOP Proxy`까지 예외 전달
        // rollback-only 설정을 확인하지 않아도 롤백함
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        //then : 모두 롤백된다.
        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }
}
