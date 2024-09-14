package hello.springtransaction.section11.propagation.seventh;

import hello.springtransaction.section11.propagation.first.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 1. 트랜잭션 전파 활용1 - 예제 프로젝트 시작
 * <p>
 * 비즈니스 요구사항
 * 회원을 등록하고 조회한다.
 * 회원에 대한 변경 이력을 추적할 수 있도록 회원 데이터가 변경될 때 변경 이력을 DB LOG 테이블에 남겨야 한
 * 다.
 * 여기서는 예제를 단순화 하기 위해 회원 등록시에만 DB LOG 테이블에 남긴다.
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
