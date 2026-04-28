package ro.unibuc.prodeng.service;

import org.springframework.stereotype.Service;
import ro.unibuc.prodeng.model.MenuEntity;
import ro.unibuc.prodeng.repository.MenuRepository;
import ro.unibuc.prodeng.request.CreateMenuRequest;
import ro.unibuc.prodeng.response.MenuResponse;
import ro.unibuc.prodeng.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuResponse> getAllMenus() {
        return menuRepository.findAll().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public MenuResponse createMenu(CreateMenuRequest request) {
        MenuEntity menu = new MenuEntity(
            request.name(),
            request.description(),
            request.price()
        );
        MenuEntity saved = menuRepository.save(menu);
        return toResponse(saved);
    }

    public void deleteMenu(String id) {
        if (!menuRepository.existsById(id)) {
            throw new EntityNotFoundException("Meniul cu id-ul " + id + " nu a fost gasit");
        }
        menuRepository.deleteById(id);
    }

    private MenuResponse toResponse(MenuEntity menu) {
        return new MenuResponse(
            menu.id(),
            menu.name(),
            menu.description(),
            menu.price(),
            menu.available()
        );
    }
    public MenuResponse getMenuById(String id) {
        MenuEntity entity = menuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meniul cu id-ul " + id + " nu a fost gasit"));
        
        return toResponse(entity);
    }
}

