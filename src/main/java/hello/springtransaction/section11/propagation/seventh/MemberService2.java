package hello.springtransaction.section11.propagation.seventh;

import hello.springtransaction.section11.propagation.first.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 7. 트랜잭션 전파 활용7 - 복구 REQUIRES_NEW
 * MemberFacade -> MemberService2 -> MemberRepository2
 * MemberFacade -> LogRepository2
 * <p>
 * REQUIRES_NEW 사용 시, 트랜잭션 2개 -> 커넥션 2개 사용 방지하는 방법중 하나로 `MemberFacade` 시도
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService2 {

    private final MemberRepository2 memberRepository2;

    /**
     * 회원가입 로그 저장 성공
     */
    @Transactional
    public void joinV1(String username) {
        Member member = new Member(username);

        log.info("== memberRepository2 호출 시작==");
        memberRepository2.save(member);
        log.info("== memberRepository2 호출 종료==");
    }
}
