package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {

        //given
        Member member = createMember();

        int price = 10000;
        int count = 2;
        int stock = 10;
        Item book = createBook("JPA book", price, stock);

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), count);

        //then
        Order findOrder = orderRepository.findById(orderId);

        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, findOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확애햐 함", 1, findOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량임", price * count, findOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 함", stock - count, book.getStockQuantity());

    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("kim");
        member.setAddress(new Address("서울", "강가", "123"));
        em.persist(member);
        return member;
    }

    @Test(expected = NotEnoughStockException.class)
    public void 재고초과() throws Exception {

        //given
        Member member = createMember();
        Item book = createBook("JPA book", 10000, 10);
        int orderCount = 11; //재고보다 많은 수량

        //when
        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        fail("예외 발생 안했움");
    }

    @Test
    public void 주문취소() throws Exception {

        //given
        Member member = createMember();
        int stock = 10;
        Item book = createBook("JPA book", 10000, stock);
        int orderCount = 5;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
//        assertEquals("주문 시 재고가 감소해야 함", stock - orderCount, book.getStockQuantity());

        //when
        orderService.cancel(orderId);

        //then
        Order findOrder = orderRepository.findById(orderId);

        assertEquals("주문 취소 시 그만큼 재고가 증가해야 함", stock, book.getStockQuantity());
        assertEquals("주문 취소 시 상태가 CANCEL이어야 함", OrderStatus.CANCEL, findOrder.getStatus());
    }

}