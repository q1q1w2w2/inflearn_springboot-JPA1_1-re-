package jpabook.jpashop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static jpabook.jpashop.domain.QMember.*;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    private JPAQueryFactory queryFactory;

//    private final JPAQueryFactory queryFactory;

//    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
    // 컴파일 시점에 em이 초기화되지 않음 -> 생성자로 초기화 / @PostConstruct로 초기화

    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(em);
    }

//    public MemberRepository(EntityManager em) {
//        this.em = em;
//        this.queryFactory = new JPAQueryFactory(em);
//    }

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return queryFactory
                .selectFrom(member)
                .fetch();
//        return em.createQuery("select m from Member m").getResultList();
    }

    public List<Member> findByName(String name) {
        return queryFactory.selectFrom(member)
                .where(member.name.eq(name))
                .fetch();
//        return em.createQuery("select m from Member m where name = :name", Member.class)
//                .setParameter("name", name)
//                .getResultList();
    }

}
