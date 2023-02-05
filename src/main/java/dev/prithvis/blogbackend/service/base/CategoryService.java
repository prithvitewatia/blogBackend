package dev.prithvis.blogbackend.service.base;

import dev.prithvis.blogbackend.models.Category;
import dev.prithvis.blogbackend.models.Post;
import dev.prithvis.blogbackend.payloads.CategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO getCategoryById(int id);

    List<CategoryDTO> getAllCategories();

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, int id);

    void deleteCategory(int id);

    Category getPostCategory(Post post);
}
