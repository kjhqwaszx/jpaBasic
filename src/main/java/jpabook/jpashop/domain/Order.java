package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    /*
    * 모든 연관관계는 지연로딩으로 설정해야한다. ( LAZY )
    *  -> Member를 EAGER로 설정한 상태에서 order 데이터를 100개 조회 할 경우 Member 조회 쿼리가 100개 날라간다.
    *  -> LAZY로 설정 할 경우 order데이터만 가져온다.
    *  -> Member Data도 가져오고 싶다면 fetch join을 해주면 된다. ( 기본편 )
    * xxxToOne 은 EAGER이 디폴트, xxxToMany는 LAZY가 디폴트이다.
    * */
    @ManyToOne(fetch = FetchType.LAZY) // N : 1 ( order : Member )
    @JoinColumn(name = "member_id") // foreign KEY
    private Member member;

    /*
    * CascadeType.ALL : 연관되어 persist 해준다.
    * orderItemA, orderItemB, orderItemC 가 있을때
    * persist(orderItemA), persist(orderItemB), persist(orderItemC), persist(order) 해주어야 하는데  ALL로 하게되면
    * persist(order)만 해주어도 전체 다 적용된다. -> delivery도 마찬가지.
    * */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL ) // 1: N mappedBy는 1 인 곳에 설정해준다.(매핑되어있다.)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id") // Delivery에 둬도 상관이 없지만 DB Access가 많은 곳에 두는게 좋다.
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //== 연관관계 메서드 ==// _ setter
    public void setMember(Member member) {
        this.member= member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //== 생성 메서드 ==//
     // 주문 생성의 복잡한 로직을 한 곳에 모아놓고 createOrder만 한번 호출하면 된다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비즈니스 로직 ==//

    /**
     * 주문취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }
        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //== 조회 로직 ==//
    /**
     * 전체 주문가격 조회
     * */
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
