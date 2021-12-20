package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final ItemRepository itemRepo = mock(ItemRepository.class);
    private final CartRepository cartRepo = mock(CartRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void testAddToCart(){
        ModifyCartRequest request = newModifyCartRequest();
        when(userRepo.findByUsername(request.getUsername())).thenReturn(newUser());
        when(itemRepo.findById(request.getItemId())).thenReturn(Optional.of(newItem1()));
        ResponseEntity<Cart> response = cartController.addTocart(request);
        Cart cart = response.getBody();
        cart.setUser(newUser());
        assertNotNull(cart);
        assertEquals(request.getUsername(), cart.getUser().getUsername());
        //check if our new item is added to cart item list.
        assertTrue(cart.getItems().stream().anyMatch(item -> item.getId() == request.getItemId()));
    }

    @Test
    public void testRemoveFromCart(){
        ModifyCartRequest request = removeModifyCartRequest();
        User user2 = newUser2();
        when(userRepo.findByUsername(request.getUsername())).thenReturn(user2);
        when(itemRepo.findById(request.getItemId())).thenReturn(Optional.of(newItem2()));
        //check that the cart has 2 items
        assertEquals(2, user2.getCart().getItems().size());
        ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Cart cart = response.getBody();
        assertNotNull(cart);
        cart.setUser(newUser2());
        assertEquals(request.getUsername(), cart.getUser().getUsername());
        //check that only 1 item remains after removal
        assertEquals(1, cart.getItems().size());
        //make sure that the item id is removed from cart item list
        assertFalse(cart.getItems().stream().anyMatch(item -> item.getId() == request.getItemId()));
    }


    private static ModifyCartRequest newModifyCartRequest() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(newItem1().getId());
        cartRequest.setUsername(newUser().getUsername());
        cartRequest.setQuantity(3);
        return cartRequest;
    }

    private static ModifyCartRequest removeModifyCartRequest() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setItemId(newItem2().getId());
        cartRequest.setUsername(newUser2().getUsername());
        cartRequest.setQuantity(1);
        return cartRequest;
    }

    private static Item newItem1() {
        Item r = new Item();
        r.setId(1L);
        r.setName("Item 1");
        r.setDescription("Item 1 Description");
        r.setPrice(new BigDecimal(100));
        return r;
    }

    private static Item newItem2() {
        Item r = new Item();
        r.setId(2L);
        r.setName("Item 2");
        r.setDescription("Item 2 Description");
        r.setPrice(new BigDecimal(200));
        return r;
    }

    private static Cart newCart() {
        Item item1 = newItem1();
        List<Item> items = new ArrayList<>(Arrays.asList(item1));
        Cart r = new Cart();
        r.setId(1L);
        r.setItems(items);
        return r;
    }

    private static Cart removeItem2FromCart() {
        List<Item> items = new ArrayList<>(Arrays.asList(newItem1(),newItem2()));
        Cart r = new Cart();
        r.setId(1L);
        r.setItems(items);
        return r;
    }
    private static User newUser2(){
        User user2 = new User();
        user2.setUsername("User 2");
        user2.setId(2L);
        user2.setCart(removeItem2FromCart());
        user2.setPassword("123456789");
        return user2;
    }

    private static User newUser() {
        User r = new User();
        r.setId(1);
        r.setUsername("ivo");
        r.setPassword("testPassword");
        r.setCart(newCart());
        return r;
    }
}
