package com.dom.adv.api.config;

import com.dom.adv.api.entity.Category;
import com.dom.adv.api.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void init() {
        if (categoryRepository.count() == 0) {
            List<Category> categories = List.of(
                    new Category("INMUEBLES", "sports", "blue"),
                    new Category("MASCOTAS", "electronics", "yellow"),
                    new Category("ELECTRONICOS", "home", "red"),
                    new Category("MUEBLES", "forniture", "white"),
                    new Category("VARIOS", "several", "orange")
            );
            categoryRepository.saveAll(categories);
            System.out.println("✅ Categorías cargadas por defecto.");
        } else {
            System.out.println("📦 La tabla de categorías ya tiene datos.");
        }
    }
}