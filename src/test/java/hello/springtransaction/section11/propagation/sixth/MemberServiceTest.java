package hello.springtransaction.section11.propagation.sixth;

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
 * 6. 트랜잭션 전파 활용6 - 복구 REQUIRED
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
     * LogRepository    @Transaction : ON -> 논리 트랜잭션 -> Exception
     */
    @Test
    void recover_exception_fail() {
        //given
        String username = "로그 예외_recover_exception_fail";

        //when :
        // `joinV2`는 `LogRepository`에서 날아온 예외를 잡는다. -> 정상 흐름 반환
        // `LogRepository`는 예외 발생 후 -> `rollback-only` 설정을 표시한다.
        // `memberService 트랜잭션`은 올라온 예외를 잡고 정상흐름이다.
        // `memberService`는 신규트랜잭션[물리 트랜잭션]이므로 물리적으로 커밋하려한다.
        // 트랜잭션 매니저는 내부적으로 rollback-only 확인하고 UnexpectedRollbackException 발생시킨다.
        assertThatThrownBy(() -> memberService.joinV2(username))
                .isInstanceOf(UnexpectedRollbackException.class);

        //then : 모두 롤백된다.
        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }
}
