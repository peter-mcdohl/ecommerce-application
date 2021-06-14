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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setCart(new Cart());
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

    @Test
    public void addTocart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(getUser());
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(getItem()));

        ResponseEntity<Cart> responseEntity = cartController.addTocart(request);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addTocartItemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(getUser());

        ResponseEntity<Cart> responseEntity = cartController.addTocart(request);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addTocartUserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> responseEntity = cartController.addTocart(request);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromcart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(getUser());
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(getItem()));

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart actualCart = responseEntity.getBody();
        assertNotNull(actualCart);
        assertEquals(BigDecimal.valueOf(0), actualCart.getTotal());
    }

    @Test
    public void removeFromcartItemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(getUser());

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeFromcartUserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1L);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

}
