package jpabook.jpashop2.service;

import jpabook.jpashop2.Domain.Book;
import jpabook.jpashop2.Domain.Item;
import jpabook.jpashop2.repository.ItemRepository;
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

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        //영속상태로 꺼냄 -> 끝나면 커밋 -> flush 발생 -> 변경감지 -> 업데이트
        Item findItem = itemRepository.findOne(itemId);
//        findItem.change(price, name, stockQuantity); // 이렇게 의미있는 메서드로 만들어야 함 setter말고
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}

