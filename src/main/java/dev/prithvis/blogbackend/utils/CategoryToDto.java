package dev.prithvis.blogbackend.utils;

import dev.prithvis.blogbackend.models.Category;
import dev.prithvis.blogbackend.payloads.CategoryDTO;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToDto implements Converter<Category, CategoryDTO> {

    @Override
    public CategoryDTO convert(@NonNull Category source) {
        CategoryDTO categoryDTO=new CategoryDTO();
        categoryDTO.setId(source.getId());
        categoryDTO.setName(source.getName());
        return categoryDTO;
    }
}
