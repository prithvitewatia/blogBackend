package dev.prithvis.blogbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.prithvis.blogbackend.models.BlogUser;
import dev.prithvis.blogbackend.payloads.BlogUserDTO;
import dev.prithvis.blogbackend.service.base.BlogUserService;
import dev.prithvis.blogbackend.utils.converter.BlogUserToDto;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BlogUsersController.class)
class BlogUsersControllerTest {
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BlogUserService userService;
    @Autowired
    private BlogUserToDto userToDto;

    @Test
    void givenValidBlogUserId_whenGetUserById_thenReturnBlogUser() throws Exception {
        BlogUser user = new BlogUser();
        user.setId(2);
        user.setName("User");
        user.setEmail("user@email.com");
        user.setPassword("userpassword");
        user.setAbout("userabout");

        BlogUserDTO userDTO = userToDto.convert(user);
        // starting test
        given(userService.getUserById(2)).willReturn(userDTO);

        mvc.perform(get("/blogusers/2").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("id", is(user.getId()))).
                andExpect(jsonPath("name", is(user.getName())));
    }

    @Test
    void givenBlogUsers_whenGetAllBlogUsers_thenReturnJsonArray() throws Exception {
        // creating test data
        BlogUser user1 = new BlogUser();
        user1.setId(1);
        user1.setName("User1");
        user1.setEmail("user1@email.com");
        user1.setPassword("user1password");
        user1.setAbout("user1about");

        BlogUser user2 = new BlogUser();
        user2.setId(2);
        user2.setName("User2");
        user2.setEmail("user2@email.com");
        user2.setPassword("user2password");
        user2.setAbout("user2about");

        // Converting our users to DTOs
        BlogUserDTO user1DTO = userToDto.convert(user1);
        BlogUserDTO user2DTO = userToDto.convert(user2);
        Page<BlogUserDTO> userDTOS = new PageImpl<>(Arrays.asList(user1DTO, user2DTO));

        // Starting test
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        given(userService.getUsers(pageable)).willReturn(userDTOS);

        mvc.perform(get("/blogusers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(jsonPath("content", hasSize(2)))
                .andExpect(jsonPath("content.[0].name", is(user1.getName())))
                .andExpect(jsonPath("content.[1].name", is(user2.getName())));

    }

    @Test
    void givenNewBlogUser_whenCreateUser_thenReturnCreatedUser() throws Exception {
        BlogUser user = new BlogUser();
        user.setId(1);
        user.setName("username");
        user.setEmail("username@email.com");
        user.setPassword("userpassword");
        user.setAbout("userabout");

        BlogUserDTO userDTO = userToDto.convert(user);

        JSONObject jsonObject = new JSONObject(ow.writeValueAsString(userDTO));

        // starting test
        given(userService.createUser(userDTO)).willReturn(userDTO);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/blogusers").
                        contentType(MediaType.APPLICATION_JSON).
                        content(jsonObject.toString());
        mvc.perform(builder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("name", is("username")));
    }

    @Test
    void givenUser_whenUpdateUser_thenReturnUpdatedUser() throws Exception {
        BlogUser user = new BlogUser();
        user.setId(1);
        user.setName("username");
        user.setEmail("username@email.com");
        user.setPassword("userpassword");
        user.setAbout("userabout");

        BlogUser updateUser = new BlogUser();
        updateUser.setId(1);
        updateUser.setName("user");
        updateUser.setEmail("user@email.com");
        updateUser.setPassword("userpassword");
        updateUser.setAbout("userabout");

        BlogUserDTO userDTO = userToDto.convert(user);
        BlogUserDTO updatedUserDTO = userToDto.convert(updateUser);
        // starting test
        given(userService.getUserById(1)).willReturn(userDTO);
        given(userService.updateUser(updatedUserDTO, 1)).willReturn(updatedUserDTO);

        JSONObject jsonObject = new JSONObject(ow.writeValueAsString(updatedUserDTO));

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/blogusers/1").
                        contentType(MediaType.APPLICATION_JSON).
                        content(jsonObject.toString());
        mvc.perform(builder)
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("id", is(1)))
                .andExpect(jsonPath("name", is("user")))
                .andExpect(jsonPath("email", is("user@email.com")))
                .andExpect(jsonPath("password", is("userpassword")))
                .andExpect(jsonPath("about", is("userabout")));
    }

    @Test
    void givenUser_whenDeleteUser_thenSuccessfulDeletion() throws Exception {
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.delete("/blogusers/1").
                        contentType(MediaType.APPLICATION_JSON);
        mvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("message", is("User deleted successfully")));
    }

    @Test
    void givenInvalidUserName_whenCreateUser_thenBadRequest() throws Exception {
        BlogUser invalidUser = new BlogUser();
        invalidUser.setId(1);
        invalidUser.setName(""); // passing blank name
        invalidUser.setEmail("testuser@email.com");
        invalidUser.setPassword("testuserpassword");
        invalidUser.setAbout("userabout");

        BlogUserDTO invalidUserDTO = userToDto.convert(invalidUser);

        JSONObject jsonObject = new JSONObject(ow.writeValueAsString(invalidUserDTO));
        // starting test
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/blogusers").
                        contentType(MediaType.APPLICATION_JSON).
                        content(jsonObject.toString());

        mvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("count", is(1)))
                .andExpect(jsonPath("errors.name", is("Name must not be blank")));
    }

    @Test
    void givenInvalidUserEmail_whenCreateUser_thenBadRequest() throws Exception {
        BlogUser invalidUser = new BlogUser();
        invalidUser.setId(1);
        invalidUser.setName("validname");
        invalidUser.setEmail("invalidemail");
        invalidUser.setPassword("testuserpassword");
        invalidUser.setAbout("userabout");
        BlogUserDTO invalidUserDTO = userToDto.convert(invalidUser);

        JSONObject jsonObject = new JSONObject(ow.writeValueAsString(invalidUserDTO));
        // starting test
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/blogusers").
                        contentType(MediaType.APPLICATION_JSON).
                        content(jsonObject.toString());

        mvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("count", is(1)))
                .andExpect(jsonPath("errors.email", is("Please enter a valid email")));
    }

    @Test
    void givenInvalidUserPassword_whenCreateUser_thenBadRequest() throws Exception {
        BlogUser invalidUser = new BlogUser();
        invalidUser.setId(1);
        invalidUser.setName("validname");
        invalidUser.setEmail("username@email.com");
        invalidUser.setPassword("passwor");
        invalidUser.setAbout("userabout");
        BlogUserDTO invalidUserDTO = userToDto.convert(invalidUser);

        JSONObject jsonObject = new JSONObject(ow.writeValueAsString(invalidUserDTO));
        // starting test
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/blogusers").
                        contentType(MediaType.APPLICATION_JSON).
                        content(jsonObject.toString());

        String expectedMessage="Password length must be 8-16 characters and can include letter, numbers and _";
        mvc.perform(builder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("success", is(false)))
                .andExpect(jsonPath("count", is(1)))
                .andExpect(jsonPath("errors.password", is(expectedMessage)));
    }

}