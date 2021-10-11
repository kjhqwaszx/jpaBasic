package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext    lombook의 @RequiredArgsConstructor으로 생략 가능하다.
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    // 회원조회 by id
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    // 회원 전체 조회.
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    // 회원조회 by name
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
