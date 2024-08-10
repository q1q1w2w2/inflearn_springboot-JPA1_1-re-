package jpabook.jpashop.service;

import jpabook.jpashop.domain.Book;
import jpabook.jpashop.domain.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ItemServiceTest {

    @Autowired ItemRepository itemRepository;
    @Autowired ItemService itemService;

    @Test
    public void 상품저장() {
        // given
        Item book = new Book("book1", 10000, 10);
        itemService.saveItem(book);

        // when
        Item findItem = itemService.findOne(book.getId());

        // then
        assertThat(findItem).isEqualTo(book);
    }

    @Test
    public void 상품단건조회() {
        // given
        Item book = new Book("book1", 10000, 10);
        itemService.saveItem(book);

        // when
        Item findItem = itemService.findOne(book.getId());

        // then
        assertThat(findItem).isEqualTo(book);

        // 상품저장 테스트랑 같은데?
    }

    @Test
    public void 상품전체조회() {
        // given
        Item book1 = new Book("book1", 10000, 10);
        Item book2 = new Book("book2", 20000, 15);
        itemService.saveItem(book1);
        itemService.saveItem(book2);

        // when
        List<Item> items = itemService.findItems();

        // then
        assertThat(items.size()).isEqualTo(2);
    }




}
