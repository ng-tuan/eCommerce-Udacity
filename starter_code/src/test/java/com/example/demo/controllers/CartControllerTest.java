package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private CartRepository cartRepository = mock(CartRepository.class);

    @Mock
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddToCart() {

        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("TestItem");
        item.setPrice(new BigDecimal("10.00"));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(2);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(200, response.getStatusCodeValue());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testRemoveFromCart() {

        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setName("TestItem");
        item.setPrice(new BigDecimal("10.00"));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(1);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testAddToCart_UserNotFound() {

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("nonexistentUser");

        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(cartRepository, never()).save(any(Cart.class));
    }
}
