package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@SpringBootTest
@Transactional
@RunWith(SpringRunner.class)
public class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    // 회원가입 성공
    @Test
    public void 회원가입() throws Exception {
        // given
        Member member = new Member("kim");

        // when
        Long saveId = memberService.join(member);

        // then
        assertThat(memberRepository.findById(saveId)).isEqualTo(member);
    }

    // 중복 이름이 있을 때 예외 발생 여부
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member1");

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        Assert.fail("예외가 발생해야 함");

    }

}