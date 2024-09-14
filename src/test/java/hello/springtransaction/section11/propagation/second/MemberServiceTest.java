package hello.springtransaction.section11.propagation.second;

import hello.springtransaction.section11.propagation.first.LogRepository;
import hello.springtransaction.section11.propagation.first.MemberRepository;
import hello.springtransaction.section11.propagation.first.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 2. 트랜잭션 전파 활용2 - 커밋, 롤백
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
     * MemberService    @Transaction : OFF
     * MemberRepository @Transaction : ON
     * LogRepository    @Transaction : ON -> Exception
     */
    @Test
    void outer_transaction_off_fail() {
        //given
        String username = "로그 예외_outer_transaction_off_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username));

        //then : log 데이터는 롤백 된다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }
}
