package com.dom.adv.api.mapper;

import com.dom.adv.api.dto.CategoryDTO;
import com.dom.adv.api.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toCategoryDTO(Category category);
}
