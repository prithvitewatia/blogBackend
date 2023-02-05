package dev.prithvis.blogbackend.controller;

import dev.prithvis.blogbackend.payloads.PostDTO;
import dev.prithvis.blogbackend.service.base.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO getPostById(@PathVariable("id") int id) {
        return postService.getPostById(id);
    }

    @GetMapping("/blogusers/{userId}/posts")
    public Page<PostDTO> getAllPostsByUser(@PathVariable("userId")int userId){
        return postService.getAllPostsByUser(userId, Pageable.unpaged());
    }

    @GetMapping ("/categories/{categoryId}/posts")
    public Page<PostDTO> getAllPostsByCategory(@PathVariable("categoryId")int categoryId){
        return postService.getAllPostsByCategory(categoryId,Pageable.unpaged());
    }

    @GetMapping("/blogusers/{userId}/categories/{categoryId}/posts")
    public Page<PostDTO> getAllPostsByUserInCategory(@PathVariable("userId") int userId,
                                                     @PathVariable("categoryId") int categoryId){
        return postService.getAllPostsByUserInCategory(userId,categoryId,Pageable.unpaged());
    }

    @GetMapping("/posts")
    public Page<PostDTO> searchForPostsByTitle(@RequestParam(value = "title")String searchKeyWords){
        return postService.searchPostsByTitle(searchKeyWords,Pageable.unpaged());
    }

    @PostMapping("/blogusers/{userId}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDTO createPostByUser(@Valid @RequestBody PostDTO post,@PathVariable("userId")int userId) {
        return postService.createPostByUser(post,userId);
    }

    @PutMapping("/blogusers/{userId}/posts/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDTO updatePostByUser(@Valid @RequestBody PostDTO postDTO,@PathVariable("userId")int userId, @PathVariable("postId") int postId) {
        return postService.updatePostByUser(postDTO, userId,postId);
    }

    @DeleteMapping("/blogusers/{userId}/posts/{postId}")
    public ResponseEntity<Map<String,String>> deletePostByUser(@PathVariable("userId")int userId,@PathVariable("postId") int postId) {
        return postService.deletePostByUser(userId,postId);
    }

}
