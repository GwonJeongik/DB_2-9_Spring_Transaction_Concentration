package hello.springtransaction.section11.propagation.seventh;

import hello.springtransaction.section11.propagation.first.LogRepository;
import hello.springtransaction.section11.propagation.first.MemberRepository;
import hello.springtransaction.section11.propagation.first.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 7. 트랜잭션 전파 활용7 - 복구 REQUIRES_NEW
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
     * MemberService    @Transaction : ON -> 논리 트랜잭션 [신규 트랜잭션 => 물리 트랜잭션] -> LogRepository 예외 캐치 -> 정상흐름
     * MemberRepository @Transaction : ON -> 논리 트랜잭션
     * LogRepository    @Transaction : ON[REQUIRES_NEW] -> 논리 트랜잭션 -> Exception
     */
    @Test
    void recover_exception_fail() {
        //given
        String username = "로그 예외_recover_exception_fail";

        //when :
        // `joinV2`는 `LogRepository`에서 날아온 예외를 잡는다. -> 정상 흐름 반환
        // `LogRepository`는 `REQUIRES_NEW`이므로 신규트랜잭션 -> 예외 발생 후 -> 물리적으로 롤백
        // `memberService 트랜잭션`은 올라온 예외를 잡고 정상흐름이다.
        // `memberService`는 신규트랜잭션[물리 트랜잭션]이므로 물리적으로 커밋
        memberService.joinV2(username);

        //then : `member`는 저장, `log`는 롤백
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }
}
