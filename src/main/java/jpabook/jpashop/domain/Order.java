package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // N : 1 ( order : Member )
    @JoinColumn(name = "member_id") // foreign KEY
    private Member member;

    /*
    * CascadeType.ALL : 연관되어 persist 해준다.
    * orderItemA, orderItemB, orderItemC 가 있을때
    * persist(orderItemA), persist(orderItemB), persist(orderItemC), persist(order) 해주어야 하는데  ALL로 하게되면
    * persist(order)만 해주어도 전체 다 적용된다.
    * */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL ) // 1: N mappedBy는 1 인 곳에 설정해준다.(매핑되어있다.)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id") // Delivery에 둬도 상관이 없지만 DB Access가 많은 곳에 두는게 좋다.
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //== 연관관계 메서드 ==//
    public void setMember(Member member) {
        this.member= member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder((this));
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);

    }


}
