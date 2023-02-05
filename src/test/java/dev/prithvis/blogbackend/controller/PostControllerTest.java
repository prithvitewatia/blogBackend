package dev.prithvis.blogbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.prithvis.blogbackend.exceptions.EntityNotFoundException;
import dev.prithvis.blogbackend.payloads.PostDTO;
import dev.prithvis.blogbackend.service.base.PostService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
class PostControllerTest {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Autowired
    MockMvc mvc;

    @MockBean
    PostService postService;

    @Test
    void givenValidPostId_whenGetPostById_willReturnPostDTO() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1);
        postDTO.setTitle("Title");
        postDTO.setContent("content");
        postDTO.setCreatedDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        postDTO.setCategoryId(1);
        // mocking our service layer
        given(postService.getPostById(1)).willReturn(postDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/posts/1").
                contentType(MediaType.APPLICATION_JSON);
        // performing a get request
        mvc.perform(requestBuilder).
                andExpect(status().isOk()).
                andExpect(jsonPath("id", is(1)));
    }

    @Test
    void givenInvalidId_whenGetPostById_willThrowError() throws Exception {
        // mocking our service layer
        String errorMessage = "Post with id 1 was not found";
        given(postService.getPostById(1)).willThrow(new EntityNotFoundException(errorMessage));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/posts/1").
                contentType(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).
                andExpect(status().isNotFound()).
                andExpect(jsonPath("message", is(errorMessage)));
    }

    @Test
    void givenNewPost_whenCreatePost_willReturnCreatedPost() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(1);
        postDTO.setTitle("Title");
        postDTO.setContent("Content");
        String dateNow = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        postDTO.setCreatedDate(dateNow);
        postDTO.setUserId(1);
        postDTO.setCategoryId(1);

        JSONObject payload = new JSONObject(ow.writeValueAsString(postDTO));
        // mocking our service layer
        given(postService.createPostByUser(postDTO, 1)).willReturn(postDTO);
        // creating our request
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/blogusers/1/posts").
                contentType(MediaType.APPLICATION_JSON).
                content(payload.toString());
        // performing our request
        mvc.perform(request).
                andExpect(status().isCreated()).
                andExpect(jsonPath("id", is(1))).
                andExpect(jsonPath("title", is("Title"))).
                andExpect(jsonPath("content", is("Content"))).
                andExpect(jsonPath("createdDate", is(dateNow))).
                andExpect(jsonPath("updatedDate", nullValue())).
                andExpect(jsonPath("userId", is(1))).
                andExpect(jsonPath("categoryId", is(1)));
    }


    @Test
    void givenUpdatedPost_whenUpdatePost_shouldReturnUpdatedPost() throws Exception {
        PostDTO updatePostDto = new PostDTO();
        updatePostDto.setId(1);
        updatePostDto.setTitle("Title");
        updatePostDto.setContent("Content");
        String dateNow = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        updatePostDto.setCreatedDate(dateNow);
        updatePostDto.setUpdatedDate(dateNow);
        updatePostDto.setCategoryId(1);

        JSONObject payload = new JSONObject(ow.writeValueAsString(updatePostDto));

        // mocking our service layer
        given(postService.updatePostByUser(updatePostDto, 1, 1)).willReturn(updatePostDto);

        // creating our request
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/blogusers/1/posts/1").
                contentType(MediaType.APPLICATION_JSON).
                content(payload.toString());

        // performing test
        mvc.perform(request).
                andExpect(status().isOk()).
                andExpect(jsonPath("id", is(1))).
                andExpect(jsonPath("title", is("Title"))).
                andExpect(jsonPath("createdDate", is(dateNow))).
                andExpect(jsonPath("updatedDate", is(dateNow)));
    }

    @Test
    void givenPostId_whenDeletePost_shouldDeletePost() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/blogusers/1/posts/1").
                contentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map<String, String>> response = new ResponseEntity<>(
                Map.of("message", "Post was deleted successfully"), HttpStatus.OK);
        given(postService.deletePostByUser(1, 1)).willReturn(response);
        mvc.perform(request).
                andExpect(status().isOk()).
                andExpect(jsonPath("message", is("Post was deleted successfully")));
    }

}