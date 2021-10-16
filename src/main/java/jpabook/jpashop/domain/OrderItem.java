package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name ="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문수량

    /*
    * @NoArgsConstructor(access = AccessLevel.PROTECTED) 으로 대체 가능
    * 생성자를 protected로 사용하려는 이유 : 외부에서 new OrderItem() 사용을 막기위해 ( createOrderItem 사용으로 통일시키기 위함이다. )
    * */
//    protected OrderItem() {
//
//    }

    //== 생성메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }
    //== 비즈니스 로직 ==//
    public void cancel() {
        getItem().addStock(count);
    }

    //== 조회 로직 ==//
    /**
     * 주문상품 전체 가격 조회
     * */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
