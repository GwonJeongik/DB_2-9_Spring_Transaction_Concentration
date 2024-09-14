package hello.springtransaction.section11.propagation.seventh;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 7. 트랜잭션 전파 활용7 - 복구 REQUIRES_NEW
 * MemberFacade -> MemberService -> MemberRepository
 * MemberFacade -> LogRepository
 * <p>
 * REQUIRES_NEW 사용 시, 트랜잭션 2개 -> 커넥션 2개 사용 방지하는 방법중 하나로 `MemberFacade` 시도
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberFacade {

    private final MemberService2 memberService2;
    private final LogRepository2 logRepository2;

    public void callMemberServiceAndLogRepositoryV1(String username) {
        Log2 logMessage = new Log2(username);

        log.info("== memberService2 호출 시작 ==");
        memberService2.joinV1(username);
        log.info("== memberService2 호출 종료 ==");

        log.info("== logRepository2 호출 시작 ==");
        logRepository2.save(logMessage);
        log.info("== logRepository2 호출 종료 ==");
    }

    public void callMemberServiceAndLogRepositoryV2(String username) {
        Log2 logMessage = new Log2(username);

        log.info("memberService2 호출 시작");
        memberService2.joinV1(username);
        log.info("memberService2 호출 종료");

        log.info("== logRepository 호출 시작==");
        try {
            logRepository2.save(logMessage);
        } catch (
                RuntimeException e) {
            log.info("log 저장에 실패했습니다. logMessage={}", logMessage.getMessage());
            log.info("정상 흐름 반환");
        }
        log.info("== logRepository 호출 종료==");
    }
}
