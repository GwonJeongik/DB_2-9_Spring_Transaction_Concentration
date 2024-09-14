package hello.springtransaction.section11.propagation.fourth;

import hello.springtransaction.section11.propagation.first.LogRepository;
import hello.springtransaction.section11.propagation.first.MemberRepository;
import hello.springtransaction.section11.propagation.first.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 4. 트랜잭션 전파 활용4 - 전파 커밋
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
     * LogRepository    @Transaction : ON -> 논리 트랜잭션
     *
     */
    @Test
    void outer_transaction_on_success() {
        //given
        String username = "outer_transaction_on_success";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }
}
