package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired ItemRepository itemRepository;
    @Autowired ItemService itemService;

    @Test
    public void 상품저장() throws Exception {
        //given
        Item item = getItem("JPA book", 10000, 100);
        itemService.save(item);

        //when
        Item findItem = itemService.findById(item.getId());

        //then
//        assertThat(item).isEqualTo(findItem);
        assertEquals(item, findItem);
        assertEquals("JPA book", findItem.getName());
    }

    @Test
    public void 상품조회_전체() throws Exception {

        //given
        Item item1 = getItem("JPA book1", 10000, 100);
        Item item2 = getItem("JPA book2", 20000, 100);
        itemService.save(item1);
        itemService.save(item2);

        //when
        List<Item> findAll = itemService.findAll();

        //then
        assertThat(findAll.size()).isEqualTo(2);
    }

    @Test
    public void 상품조회_단건() throws Exception {

        //given
        Item item1 = getItem("JPA book1", 10000, 100);
        Item item2 = getItem("JPA book2", 20000, 100);
        itemService.save(item1);
        itemService.save(item2);

        //when
        Item findItem1 = itemService.findById(item1.getId());
        Item findItem2 = itemService.findById(item2.getId());

        //then
        assertThat(item1).isEqualTo(findItem1);
        assertThat("JPA book1").isEqualTo(findItem1.getName());
        assertThat("JPA book2").isEqualTo(findItem2.getName());
    }

//    @Test(expected = Exception.class)
//    public void 상품조회_단건_실패() throws Exception {
//        //단건 조회에서 상품을 찾지 못했을 때 발생하는 실패 케이스에 대한 테스트 코드
//
//        //given
//        Item item = getItem("JPA book1", 10000, 100);
//        itemService.save(item);
//
//        Item findItem = itemService.findById(999L);
//
//        Assertions.fail("상품을 찾지 못해야 함");
//    }

//    @Test
//    public void 상품변경() throws Exception {
//
//        //given
//        Item item1 = getItem("JPA book1", 10000, 100);
//        Item item2 = getItem("JPA book2", 20000, 100);
//        item2.setId(item1.getId()); //같은 id일 경우 merge
//        itemService.save(item1);
//        itemService.save(item2);
//
//        //when
//        Item findItem = itemRepository.findById(item1.getId());
//
//        //then
////        assertEquals("JPA book2", findItem.getName());
//        assertEquals(item1.getId(), item2.getId());
//
//    }


    private static Item getItem(String name, int price, int stockQuantity) {
        Item item = new Book();
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        return item;
    }
}