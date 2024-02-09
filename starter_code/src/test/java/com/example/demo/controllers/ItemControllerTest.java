package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void testGetItems() {

        List<Item> items = Arrays.asList(new Item(), new Item());
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(items, response.getBody());
    }

    @Test
    public void testGetItemById() {

        Item item = new Item();
        item.setId(1L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(item, response.getBody());
    }

    @Test
    public void testGetItemById_ItemNotFound() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testGetItemsByName() {

        String itemName = "TestItem";
        List<Item> items = Arrays.asList(new Item(), new Item());
        when(itemRepository.findByName(itemName)).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(itemName);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(items, response.getBody());
    }

    @Test
    public void testGetItemsByName_ItemsNotFound() {

        String itemName = "NonexistentItem";
        when(itemRepository.findByName(anyString())).thenReturn(null);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(itemName);
        assertEquals(404, response.getStatusCodeValue());
    }
}
