package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 준영속 엔티티를 수정하는 방법 1
     * ( ItemController의 updateItem 함수 부분 )
     * 준영속 엔티티는 변경감지가 되지않아 Update가 되지 않는다.
     * 영속성 컨텍스트(Book)에서 엔티티를 다시 조회해 영속성 엔티티로 만든후 값을 변경한다.
     * 영속성 엔티티는 변경감지되어 자동 Update된다.
     * 아래 예제는 name, price, stockQuantity만 변경된다고 가정
     */

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity ) {
        //연속성 컨텍스트에서 조회하여 영속성 엔티티로 만든다.
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        // 등등 ... 원하는 값 변경 후 ItemService.saveItem(book)을 하지 않아도 된다.( 변경감지되어 자동 Update)

    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
