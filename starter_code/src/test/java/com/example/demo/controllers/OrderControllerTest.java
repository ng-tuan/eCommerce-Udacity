package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository = mock(UserRepository.class);

    @Mock
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testSubmitOrder() {
        User user = new User();
        user.setUsername("testUser");

        Cart cart = new Cart();
        cart.setItems(Arrays.asList());
        user.setCart(cart);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(orderRepository.save(any(UserOrder.class))).thenReturn(new UserOrder());

        ResponseEntity<UserOrder> response = orderController.submit("testUser");
        assertEquals(200, response.getStatusCodeValue());
        verify(orderRepository, times(1)).save(any(UserOrder.class));
    }


    @Test
    public void testSubmitOrder_UserNotFound() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("nonexistentUser");
        assertEquals(404, response.getStatusCodeValue());
        verify(orderRepository, never()).save(any(UserOrder.class));
    }

    @Test
    public void testGetOrdersForUser() {

        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.singletonList(new UserOrder()));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    public void testGetOrdersForUser_UserNotFound() {
        when(userRepository.findByUsername("nonexistentUser")).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("nonexistentUser");
        assertEquals(404, response.getStatusCodeValue());
    }
}
