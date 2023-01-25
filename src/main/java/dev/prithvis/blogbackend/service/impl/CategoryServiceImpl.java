package dev.prithvis.blogbackend.service.impl;

import dev.prithvis.blogbackend.exceptions.EntityNotFoundException;
import dev.prithvis.blogbackend.models.Category;
import dev.prithvis.blogbackend.models.Post;
import dev.prithvis.blogbackend.payloads.CategoryDTO;
import dev.prithvis.blogbackend.repository.CategoryRepository;
import dev.prithvis.blogbackend.service.base.CategoryService;
import dev.prithvis.blogbackend.utils.converter.CategoryToDto;
import dev.prithvis.blogbackend.utils.converter.DTOToCategory;
import dev.prithvis.blogbackend.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

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
                ()->new EntityNotFoundException(messageUtil.getMessage("category.notFound",id))
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
        Category category=dtoToCategory.convert(categoryDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryToDTO.convert(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, int id) {
        CategoryDTO categoryUpdate=getCategoryById(id);
        categoryUpdate.setName(categoryDTO.getName());
        Category saveCategory=dtoToCategory.convert(categoryUpdate);
        Category savedCategory = categoryRepository.save(saveCategory);
        return categoryToDTO.convert(savedCategory);
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category getPostCategory(Post post) {
        Category category=new Category();
        if(categoryRepository.count()==0){
            category.setName("Unidentified");
            categoryRepository.save(category);
        }
        long totalCategories=categoryRepository.count();
        /*
         * since we don't have and AI algorithm to determine we will for time being
         * assign any random category
         */
        Random random=new SecureRandom();
        int categoryNumber=(int)random.nextLong(totalCategories);
        return categoryRepository.findById(categoryNumber).orElse(category);
    }
}
