package jpabook.model;

import jpabook.model.entity.Member;
import jpabook.model.entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Created by 1001218 on 15. 4. 5..
 */
public class Main {

    public static void main(String[] args) {

        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        EntityManager em = emf.createEntityManager(); //엔티티 매니저 생성

        EntityTransaction tx = em.getTransaction(); //트랜잭션 기능 획득

        try {
            tx.begin(); //트랜잭션 시작
            //TODO 비즈니스 로직
            for (int i = 0; i < 5; i++) {
                Member member = new Member();
                member.setCity("New York");
                em.persist(member);
            }
            Member member = new Member();
            em.persist(member);

            for (int i = 0; i < 10; i++) {
                Order order = new Order();
                order.setMember(member);
                em.persist(order);
            }
            tx.commit();//트랜잭션 커밋
            System.out.println("One Commit End \n");

            EntityManager entityManager = emf.createEntityManager();
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();
//            findMemberWithManager(entityManager);
            List<Member> members =
                    entityManager.createQuery("select m from Member m", Member.class)
                    .getResultList();
            for (Member member1 : members) {
                System.out.println(member1.getOrders().size());
            }
            entityTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback(); //트랜잭션 롤백
        } finally {
            em.close(); //엔티티 매니저 종료
        }

        emf.close(); //엔티티 매니저 팩토리 종료
    }

    private static void findMemberWithManager(EntityManager entityManager) {
        Member findMember = entityManager.find(Member.class, 1L);
        for (Order order : findMember.getOrders()) {
            System.out.println(order.getId());
        }
    }

}
