package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertNotNull(responseEntity.getBody());
        List<Item> itemList = new ArrayList<>();
        assertEquals(itemList, responseEntity.getBody());
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));
        return item;
    }

    @Test
    public void getItemById() {
        ResponseEntity<Item> responseEntity;

        responseEntity = itemController.getItemById(1L);
        assertEquals(404, responseEntity.getStatusCodeValue());

        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(getItem()));
        responseEntity = itemController.getItemById(1L);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(getItem(), responseEntity.getBody());
    }

    @Test
    public void getItemsByName() {
        ResponseEntity<List<Item>> responseEntity;

        responseEntity = itemController.getItemsByName("test");
        assertEquals(404, responseEntity.getStatusCodeValue());

        when(itemRepository.findByName("test")).thenReturn(Arrays.asList(getItem()));
        responseEntity = itemController.getItemsByName("test");
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(Arrays.asList(getItem()), responseEntity.getBody());
    }
}
