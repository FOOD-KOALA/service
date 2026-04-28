package ro.unibuc.prodeng.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ro.unibuc.prodeng.exception.EntityNotFoundException;
import ro.unibuc.prodeng.model.MenuEntity;
import ro.unibuc.prodeng.repository.MenuRepository;
import ro.unibuc.prodeng.request.CreateMenuRequest;
import ro.unibuc.prodeng.response.MenuResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    void testCreateMenu_validRequest_createsAndReturnsMenu() {
        CreateMenuRequest request = new CreateMenuRequest("Burger Test", "Cel mai bun burger de test", 45.0);

        when(menuRepository.save(any(MenuEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuResponse response = menuService.createMenu(request);

        assertNotNull(response);
        assertEquals("Burger Test", response.name());
        assertEquals(45.0, response.price());
        verify(menuRepository, times(1)).save(any(MenuEntity.class));
    }

    @Test
    void testGetAllMenus_returnsListOfMenus() {
        MenuEntity menu1 = new MenuEntity("Burger 1", "Descriere 1", 30.0);
        MenuEntity menu2 = new MenuEntity("Burger 2", "Descriere 2", 40.0);
        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu1, menu2));

        List<MenuResponse> result = menuService.getAllMenus();

        assertEquals(2, result.size());
        assertEquals("Burger 1", result.get(0).name());
        verify(menuRepository, times(1)).findAll();
    }

    @Test
    void testDeleteMenu_existingId_deletesSuccessfully() {
        when(menuRepository.existsById("123")).thenReturn(true);

        menuService.deleteMenu("123");

        verify(menuRepository, times(1)).deleteById("123");
    }

    @Test
    void testDeleteMenu_nonExistingId_throwsException() {
        when(menuRepository.existsById("999")).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> menuService.deleteMenu("999"));
        verify(menuRepository, never()).deleteById(anyString());
    }

    @Test
    void testGetMenuById_existingId_returnsMenu() {
        MenuEntity menu = new MenuEntity("Burger", "Bun", 25.0);
        when(menuRepository.findById("123")).thenReturn(Optional.of(menu));

        MenuResponse response = menuService.getMenuById("123");

        assertNotNull(response);
        assertEquals("Burger", response.name());
        verify(menuRepository, times(1)).findById("123");
    }

    @Test
    void testGetMenuById_nonExistingId_throwsException() {
        when(menuRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> menuService.getMenuById("999"));
    }
}