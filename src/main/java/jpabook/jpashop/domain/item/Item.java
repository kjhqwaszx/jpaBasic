package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Book, Album, Movie 를 싱글테이블로 한다.
@DiscriminatorColumn(name="dtype") // Book, Album, Movie의 구분값이다. 각 엔티티에서 @DiscriminatorValue 설정값을 통해 구분가능하다.
@Getter @Setter
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name ="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // ==비즈니스 로직== //
    /**
     * 재고 증가
     * */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
    /**
     * 재고 감소
     * */
    public void removeStock(int quantity){
        int resStock = this.stockQuantity - quantity;
        if (resStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = resStock;
    }
}
