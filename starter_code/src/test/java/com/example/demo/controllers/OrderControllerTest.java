package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private final UserRepository userRepo = mock(UserRepository.class);
    private final OrderRepository orderRepo = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository", userRepo);
        TestUtils.injectObjects(orderController,"orderRepository", orderRepo);
    }

    @Test
    public void testCreateOrderByUserUsername(){
        User user= newUser();
        Cart cart = newCart();
        cart.setUser(user);
        user.setCart(cart);
        when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(user, userOrder.getUser());
        assertEquals(user.getCart().getItems(), userOrder.getItems());
        assertEquals(user.getCart().getTotal(), userOrder.getTotal());
    }

    @Test
    public void testGetOrderByUserUsername(){
        UserOrder userOrder = newOrder();
        when(userRepo.findByUsername(userOrder.getUser().getUsername())).thenReturn(userOrder.getUser());
        when(orderRepo.findByUser(userOrder.getUser())).thenReturn(Arrays.asList(userOrder,userOrder,userOrder));
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(userOrder.getUser().getUsername());
        List<UserOrder> userOrderList = response.getBody();
        assertNotNull(userOrderList);
        assertEquals(3, userOrderList.size());
        //check that every order is from th same user
        userOrderList.forEach(userOrder1 -> assertEquals(userOrder.getUser(), userOrder1.getUser()));
    }

    private static UserOrder newOrder() {
        Item item1 = newItem1();
        Item item2 = newItem2();
        User user = newUser();
        final List<Item> items = Arrays.asList(item1, item2);
        UserOrder r = new UserOrder();
        r.setId(1L);
        r.setItems(items);
        r.setUser(user);
        return r;
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
        Item item2 = newItem2();
        final List<Item> items = Arrays.asList(item1, item2);
        Cart r = new Cart();
        r.setId(1L);
        r.setItems(items);
        r.setTotal(new BigDecimal(300));
        return r;
    }
    private static User newUser() {
        Cart cart = newCart();
        User r = new User();
        r.setId(1);
        r.setUsername("ivo");
        r.setPassword("testPassword");
        r.setCart(cart);
        return r;
    }
}
