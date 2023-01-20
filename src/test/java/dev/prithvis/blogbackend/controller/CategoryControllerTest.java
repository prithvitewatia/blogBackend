package dev.prithvis.blogbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.prithvis.blogbackend.exceptions.EntityNotFoundException;
import dev.prithvis.blogbackend.payloads.CategoryDTO;
import dev.prithvis.blogbackend.service.repo.CategoryService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    @Autowired
    MockMvc mvc;

    @MockBean
    CategoryService categoryService;

    @Test
    void givenValidId_whenGetCategoryById_thenReturnCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1);
        categoryDTO.setName("category1");
        // mocking service layer
        given(categoryService.getCategoryById(1)).willReturn(categoryDTO);

        // performing test
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.
                get("/categories/1").
                contentType(MediaType.APPLICATION_JSON);
        mvc.perform(builder).
                andExpect(status().isOk()).
                andExpect(jsonPath("id", is(1))).
                andExpect(jsonPath("name", is("category1")));
    }

    @Test
    void givenInvalidId_whenGetCategoryById_thenExpectError() throws Exception {
        String message = "Category with id 1 was not found";
        // mocking user service
        when(categoryService.getCategoryById(1)).thenThrow(new EntityNotFoundException(message));
        // performing test
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.
                get("/categories/1").
                contentType(MediaType.APPLICATION_JSON);
        mvc.perform(builder).
                andExpect(status().isNotFound()).
                andExpect(jsonPath("message", is(message)));
    }

    @Test
    void givenCategories_whenCategoryById_thenAllCategories() throws Exception {
        CategoryDTO categoryDTO1 = new CategoryDTO();
        categoryDTO1.setId(1);
        categoryDTO1.setName("category1");

        CategoryDTO categoryDTO2 = new CategoryDTO();
        categoryDTO2.setId(2);
        categoryDTO2.setName("category2");

        List<CategoryDTO> categoryDTOS = Arrays.asList(categoryDTO1, categoryDTO2);
        // mocking service layer
        given(categoryService.getAllCategories()).willReturn(categoryDTOS);
        // performing test
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.
                get("/categories").
                contentType(MediaType.APPLICATION_JSON);
        mvc.perform(builder).andExpect(status().isOk()).
                andExpect(jsonPath("$[0].id", is(1))).
                andExpect(jsonPath("$[0].name", is("category1"))).
                andExpect(jsonPath("$[1].id", is(2))).
                andExpect(jsonPath("$[1].name", is("category2")));
    }

    @Test
    void givenNewCategory_whenCreateCategory_thenReturnCreatedCategory() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("cricket");
        // mocking service layer
        given(categoryService.createCategory(categoryDTO)).willReturn(categoryDTO);
        // creating payload
        JSONObject payload = new JSONObject(ow.writeValueAsString(categoryDTO));
        // performing test
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.
                post("/categories").
                contentType(MediaType.APPLICATION_JSON).
                content(payload.toString());
        mvc.perform(request).
                andExpect(status().isCreated()).
                andExpect(jsonPath("name", is("cricket")));
    }

    @Test
    void givenInvalidCategoryDTO_whenCreateCategory_thenExpectError() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("");
        // creating payload
        JSONObject payload = new JSONObject(ow.writeValueAsString(categoryDTO));
        // performing test
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.
                post("/categories").
                contentType(MediaType.APPLICATION_JSON).
                content(payload.toString());
        mvc.perform(request).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("success", is(false))).
                andExpect(jsonPath("count", is(1))).
                andExpect(jsonPath("errors.name", is("name must not be blank")));
    }

    @Test
    void givenCategory_whenUpdateCategory_thenReturnUpdatedCategory() throws Exception {
        CategoryDTO updatedCategoryDTO = new CategoryDTO();
        updatedCategoryDTO.setId(1);
        updatedCategoryDTO.setName("football");
        // mocking category service update method
        given(categoryService.updateCategory(updatedCategoryDTO, 1)).willReturn(updatedCategoryDTO);
        // creating payload
        JSONObject payload = new JSONObject(ow.writeValueAsString(updatedCategoryDTO));
        // performing test
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.
                put("/categories/1").
                contentType(MediaType.APPLICATION_JSON).
                content(payload.toString());
        mvc.perform(builder).
                andExpect(status().isOk()).
                andExpect(jsonPath("id", is(1))).
                andExpect(jsonPath("name", is("football")));
    }

    @Test
    void givenValidId_whenDeleteCategory_thenDeleteSuccessfully() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.
                delete("/categories/1").
                contentType(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(jsonPath("message", is("Category deleted successfully")));
    }

}