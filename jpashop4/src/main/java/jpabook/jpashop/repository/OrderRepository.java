package jpabook.jpashop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(em);
    }

    // 상품 주문, 내역 조회, 주문 취소
    public void save(Order order) {
        em.persist(order);
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    // 검색어 검색
//    public List<Order> findAll(OrderSearch orderSearch) {
//        QOrder o = QOrder.order;
//        QMember m = QMember.member;
//
//        // 주문 상태 검색
//        if (orderSearch.getOrderStatus() != null) {
//            return queryFactory.selectFrom(o)
//                    .join(m)
//                    .where(
//                            m.name.contains(orderSearch.getMemberName())
//                                    .and(o.status.eq(orderSearch.getOrderStatus()))
//                    )
//                    .fetch();
//        } else {
//            return queryFactory.selectFrom(o)
//                    .join(m)
//                    .where(
//                            m.name.contains(orderSearch.getMemberName())
//                    )
//                    .fetch();
//        }
//    }

    public List<Order> findAll(OrderSearch orderSearch) {
        QMember m = QMember.member;
        QOrder o = QOrder.order;

        BooleanBuilder builder = new BooleanBuilder();

        if (orderSearch.getOrderStatus() != null) {
            builder.and(o.status.eq(orderSearch.getOrderStatus()));
        }
        if(StringUtils.hasText(orderSearch.getMemberName())) {
//            builder.and(m.name.like("%" + orderSearch.getMemberName() + "%"));
            builder.and(m.name.contains(orderSearch.getMemberName()));
        }
        return queryFactory
                .selectFrom(o)
                .join(o.member, m)
                .where(builder)
                .limit(1000)
                .fetch();
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

}
