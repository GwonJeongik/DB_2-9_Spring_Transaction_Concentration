package hello.springtransaction.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    private final OrderService service;
    private final OrderRepository repository;

    @Autowired
    public OrderServiceTest(OrderService service, OrderRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @Test
    void complete() throws NoEnoughMoneyException {
        //given
        Order order = new Order();
        order.setUsername("정상");
        //when
        service.order(order);
        //then
        Order findOrder = repository.findById(order.getId()).get();
        log.info("findOrder.getId()={}", findOrder.getId());
        assertThat(findOrder.getPayStatus()).isEqualTo("완료");
    }

    @Test
    void runtimeException() throws NoEnoughMoneyException {
        //given
        Order order = new Order();
        order.setUsername("예외");
        //when, then

        assertThatThrownBy(() -> service.order(order))
                .isInstanceOf(RuntimeException.class);

        assertThat(repository.findById(order.getId()).isEmpty()).isTrue();
    }

    @Test
    void noEnoughMoneyException() {
        //given
        Order order = new Order();
        order.setUsername("잔고부족");
        //when
        try {
            service.order(order);
            fail("잔고부족 예외가 발생해야합니다");
        } catch (NoEnoughMoneyException e) {
            log.info("고객에게 잔고 부족을 알리고, 별도의 계좌로 입금하도록 안내");
        }
        //then
        Optional<Order> findOrder = repository.findById(order.getId());
        assertThat(findOrder.isEmpty()).isFalse();
        assertThat(findOrder.get().getPayStatus()).isEqualTo("대기");
    }
}