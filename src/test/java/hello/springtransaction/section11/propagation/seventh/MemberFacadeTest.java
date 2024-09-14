package hello.springtransaction.section11.propagation.seventh;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 7. 트랜잭션 전파 활용7 - 복구 REQUIRES_NEW
 * MemberFacade -> MemberService2 -> MemberRepository2
 * MemberFacade -> LogRepository2
 * <p>
 * REQUIRES_NEW 사용 시, 트랜잭션 2개 -> 커넥션 2개 사용 방지하는 방법중 하나로 `MemberFacade` 시도
 */
@SpringBootTest
class MemberFacadeTest {

    @Autowired
    MemberFacade memberFacade;

    @Autowired
    MemberService2 memberService2;

    @Autowired
    MemberRepository2 memberRepository2;

    @Autowired
    LogRepository2 logRepository2;

    @Test
    void callMemberServiceAndLogRepository() {
        //given
        String username = "callMemberServiceAndLogRepository";

        //when
        memberFacade.callMemberServiceAndLogRepositoryV1(username);

        //then
        assertThat(memberRepository2.find(username).isPresent()).isTrue();
        assertThat(logRepository2.find(username).isPresent()).isTrue();
    }
}