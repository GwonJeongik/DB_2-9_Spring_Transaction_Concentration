package hello.springtransaction.section11.propagation.seventh;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 7. 트랜잭션 전파 활용7 - 복구 REQUIRES_NEW
 * MemberFacade -> MemberService2 -> MemberRepository2
 * MemberFacade -> LogRepository2
 * <p>
 * REQUIRES_NEW 사용 시, 트랜잭션 2개 -> 커넥션 2개 사용 방지하는 방법중 하나로 `MemberFacade` 시도
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Log2 {

    @Id
    @GeneratedValue
    private Long id;
    private String message;

    public Log2(String message) {
        this.message = message;
    }
}
