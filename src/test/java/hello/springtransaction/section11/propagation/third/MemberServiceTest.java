package hello.springtransaction.section11.propagation.third;

import hello.springtransaction.section11.propagation.first.LogRepository;
import hello.springtransaction.section11.propagation.first.MemberRepository;
import hello.springtransaction.section11.propagation.first.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 3. 트랜잭션 전파 활용3 - 단일 트랜잭션
 * 서비스 계층에만 트랜잭션을 적용
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
     * MemberService    @Transaction : ON
     * MemberRepository @Transaction : OFF -> 트랜잭션 주석처리
     * LogRepository    @Transaction : OFF -> 트랜잭션 주석처리
     */
    @Test
    void singleTransaction() {
        //given
        String username = "singleTransaction";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }
}
