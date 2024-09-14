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
    void singleTransaction_success() {
        //given
        String username = "singleTransaction_success";

        //when
        memberService.joinV1(username);

        //then
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * MemberService    @Transaction : ON
     * MemberRepository @Transaction : ON
     * LogRepository    @Transaction : OFF -> 트랜잭션 주석처리 -> Exception
     */
    @Test
    void singleTransaction_fail() {
        //given
        String username = "로그 예외_singleTransaction_fail";

        //when
        memberService.joinV2(username);

        //then :
        // logRepository.find(username).isPresent()가 `true`가 되는 이유는
        // `@Transactional`을 걸지 않아서,
        // `em.persist(..)이 먼저 호출돼서, 영속성 컨텍스트에 추가한 다음에 `if`문을 만나 예외가 발생한다.
        // `memberService`에서 예외 캐치 -> 정상흐름 -> 커밋
        // 문제는 커밋할 때, Log 데이터가 영속성 컨텍스트에 추가되어있어서 함께 커밋된다는 것이다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }
}
