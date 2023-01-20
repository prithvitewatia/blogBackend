package dev.prithvis.blogbackend.utils;

import dev.prithvis.blogbackend.models.Category;
import dev.prithvis.blogbackend.payloads.CategoryDTO;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DTOToCategory implements Converter<CategoryDTO, Category> {

    @Override
    public Category convert(@NonNull CategoryDTO source) {
        Category category=new Category();
        category.setId(source.getId());
        category.setName(source.getName());
        return category;
    }
}
