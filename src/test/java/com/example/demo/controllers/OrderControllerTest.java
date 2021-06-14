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

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        Cart userCart = new Cart();
        userCart.setId(1L);
        userCart.setUser(user);
        userCart.setItems(Arrays.asList(getItem()));
        userCart.setTotal(BigDecimal.valueOf(2.99));
        user.setCart(userCart);

        return user;
    }

    private Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(BigDecimal.valueOf(2.99));
        return item;
    }

    private UserOrder getUserOrder() {
        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setUser(getUser());
        order.setItems(Arrays.asList(getItem()));
        order.setTotal(BigDecimal.valueOf(2.99));
        return order;
    }

    @Test
    public void submitOrder() {
        when(userRepository.findByUsername("test")).thenReturn(getUser());
        ResponseEntity<UserOrder> responseEntity = orderController.submit("test");
        assertEquals(200, responseEntity.getStatusCodeValue());

        UserOrder order = responseEntity.getBody();
        assertNotNull(order);
        assertEquals(BigDecimal.valueOf(2.99), order.getTotal());
    }

    @Test
    public void submitOrderUserNotFound() {
        ResponseEntity<UserOrder> responseEntity = orderController.submit("test");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser() {
        User user = getUser();
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(getUserOrder()));
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("test");
        assertEquals(200, responseEntity.getStatusCodeValue());

        List<UserOrder> orderList = responseEntity.getBody();
        assertNotNull(orderList);
        assertEquals(1, orderList.size());
        assertEquals(BigDecimal.valueOf(2.99), orderList.get(0).getTotal());
    }

    @Test
    public void getOrdersForUserNotFound() {
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("test");
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
}
