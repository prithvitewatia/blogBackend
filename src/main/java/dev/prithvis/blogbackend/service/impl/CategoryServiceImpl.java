package dev.prithvis.blogbackend.service.impl;

import dev.prithvis.blogbackend.exceptions.EntityNotFoundException;
import dev.prithvis.blogbackend.models.Category;
import dev.prithvis.blogbackend.models.Post;
import dev.prithvis.blogbackend.payloads.CategoryDTO;
import dev.prithvis.blogbackend.repository.CategoryRepository;
import dev.prithvis.blogbackend.service.base.CategoryService;
import dev.prithvis.blogbackend.utils.MessageUtil;
import dev.prithvis.blogbackend.utils.converter.CategoryToDto;
import dev.prithvis.blogbackend.utils.converter.DTOToCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    CategoryToDto categoryToDTO;
    @Autowired
    DTOToCategory dtoToCategory;

    @Override
    public CategoryDTO getCategoryById(int id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageUtil.getMessage("category.notFound", id))
        );
        return categoryToDTO.convert(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().
                stream().
                map(category -> categoryToDTO.convert(category)).
                toList();
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = dtoToCategory.convert(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryToDTO.convert(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, int id) {
        CategoryDTO categoryUpdate = getCategoryById(id);
        categoryUpdate.setName(categoryDTO.getName());
        Category saveCategory = dtoToCategory.convert(categoryUpdate);
        Category savedCategory = categoryRepository.save(saveCategory);
        return categoryToDTO.convert(savedCategory);
    }

    @Override
    public void deleteCategory(int id) {
        try {
            categoryRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EntityNotFoundException(messageUtil.getMessage("category.notFound",id));
        }
    }

    @Override
    public Category getPostCategory(Post post) {
        // a dummy algorithm to get post category
        if (categoryRepository.count() == 0) {
            Category category = new Category();
            category.setName("Unidentified");
            return categoryRepository.save(category);
        }
        return categoryRepository.findFirstBy();
    }
}
