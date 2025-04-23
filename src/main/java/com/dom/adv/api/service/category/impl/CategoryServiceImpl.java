package com.dom.adv.api.service.category.impl;

import com.dom.adv.api.dto.CategoryDTO;
import com.dom.adv.api.mapper.CategoryMapper;
import com.dom.adv.api.repository.CategoryRepository;
import com.dom.adv.api.service.category.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryDTO)
                .toList();
    }
}
