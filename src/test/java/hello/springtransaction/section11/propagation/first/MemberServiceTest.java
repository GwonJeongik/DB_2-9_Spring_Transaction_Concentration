package hello.springtransaction.section11.propagation.first;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 트랜잭션 전파 활용1 - 예제 프로젝트 시작
 */
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    LogRepository logRepository;

    /**
     * MemberService    @Transactional : OFF
     * MemberRepository @Transactional : ON
     * LogRepository    @Transactional : ON
     */
    @Test
    void outer_transactional_off_success() {
        //given
        String username = "outer_transactional_off_success";

        //when
        memberService.joinV1(username);

        //then
        assertThat(memberRepository.find(username).isPresent()).isTrue();
        assertThat(logRepository.find(username).isPresent()).isTrue();
    }
}