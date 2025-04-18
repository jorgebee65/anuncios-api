package com.dom.adv.api.service;

import com.dom.adv.api.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
}
