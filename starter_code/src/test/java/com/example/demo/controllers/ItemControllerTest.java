package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);

    }

    @Test
    public void testGetItemList(){
        Item r = newItem();
        when(itemRepo.findAll()).thenReturn(Arrays.asList(r,r,r));
        ResponseEntity<List<Item>> response = itemController.getItems();
        List<Item> u = response.getBody();
        assertNotNull(u);
        assertEquals(3, u.size());
    }

    @Test
    public void testGetItemById(){
        Item r = newItem();
        when(itemRepo.findById(r.getId())).thenReturn(Optional.of(r));
        ResponseEntity<Item> response = itemController.getItemById(r.getId());
        Item u = response.getBody();
        assertNotNull(u);
        assertEquals(r.getId(), u.getId());
        assertEquals(r.getName(), u.getName());
        assertEquals(r.getDescription(), u.getDescription());
        assertEquals(r.getPrice(), u.getPrice());
    }

    @Test
    public void testGetItemByName(){
        Item r = newItem();
        when(itemRepo.findByName(r.getName())).thenReturn(Arrays.asList(r));
        ResponseEntity<List<Item>> response = itemController.getItemsByName(r.getName());
        List<Item> u = response.getBody();
        assertNotNull(u);
        assertEquals(1, u.size());
        assertEquals(r.getName(), u.get(0).getName());
        assertEquals(r.getDescription(), u.get(0).getDescription());
        assertEquals(r.getPrice(), u.get(0).getPrice());
    }

    private static Item newItem() {
        Item r = new Item();
        r.setId(1L);
        r.setName("Item 1");
        r.setDescription("Item Description");
        r.setPrice(new BigDecimal(100));
        return r;
    }
}
