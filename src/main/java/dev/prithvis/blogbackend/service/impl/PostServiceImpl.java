package dev.prithvis.blogbackend.service.impl;

import dev.prithvis.blogbackend.exceptions.EntityNotFoundException;
import dev.prithvis.blogbackend.exceptions.UnauthorizedException;
import dev.prithvis.blogbackend.models.BlogUser;
import dev.prithvis.blogbackend.models.Category;
import dev.prithvis.blogbackend.models.Post;
import dev.prithvis.blogbackend.payloads.BlogUserDTO;
import dev.prithvis.blogbackend.payloads.CategoryDTO;
import dev.prithvis.blogbackend.payloads.PostDTO;
import dev.prithvis.blogbackend.repository.PostRepository;
import dev.prithvis.blogbackend.service.base.BlogUserService;
import dev.prithvis.blogbackend.service.base.CategoryService;
import dev.prithvis.blogbackend.service.base.PostService;
import dev.prithvis.blogbackend.utils.MessageUtil;
import dev.prithvis.blogbackend.utils.converter.DTOToBlogUser;
import dev.prithvis.blogbackend.utils.converter.DTOToCategory;
import dev.prithvis.blogbackend.utils.converter.PostDTOToPost;
import dev.prithvis.blogbackend.utils.converter.PostToPostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostDTOToPost postDTOToPost;
    @Autowired
    DTOToCategory dtoToCategory;
    @Autowired
    PostToPostDTO postToPostDTO;
    @Autowired
    DTOToBlogUser dtoToBlogUser;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    BlogUserService userService;
    @Autowired
    CategoryService categoryService;

    @Override
    public PostDTO getPostById(int id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageUtil.getMessage("post.notFound", id)));
        return postToPostDTO.convert(post);
    }

    @Override
    public Page<PostDTO> getAllPostsByUser(int userId, Pageable pageable) {
        BlogUserDTO userDTO = userService.getUserById(userId);
        BlogUser user = dtoToBlogUser.convert(userDTO);
        Page<Post> allByUserOrderByCreatedDateDesc = postRepository.findAllByUserOrderByCreatedDateDesc(user, pageable);
        List<PostDTO> postDTOS = new LinkedList<>();
        allByUserOrderByCreatedDateDesc.forEach(
                post -> postDTOS.add(postToPostDTO.convert(post))
        );
        return new PageImpl<>(postDTOS, pageable, allByUserOrderByCreatedDateDesc.getTotalElements());
    }

    @Override
    public Page<PostDTO> getAllPostsByCategory(int categoryId, Pageable pageable) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        Category category = dtoToCategory.convert(categoryDTO);
        Page<Post> allByCategory = postRepository.findAllByCategory(category, pageable);
        List<PostDTO> postDTOS = new LinkedList<>();
        allByCategory.forEach(
                post -> postDTOS.add(postToPostDTO.convert(post))
        );
        return new PageImpl<>(postDTOS, pageable, allByCategory.getTotalElements());
    }

    @Override
    public PostDTO createPostByUser(PostDTO postDTO, int userId) {
        BlogUserDTO userDTO = userService.getUserById(userId);
        BlogUser user = dtoToBlogUser.convert(userDTO);
        Post post = postDTOToPost.convert(postDTO);
        post.setCreatedDate(LocalDate.now());
        post.setUser(user);
        Category category = categoryService.getPostCategory(post);
        post.setCategory(category);
        Post savedPost = postRepository.save(post);
        return postToPostDTO.convert(savedPost);
    }

    @Override
    public PostDTO updatePostByUser(PostDTO postDTO, int userId, int postId) {
        PostDTO updatePostDTO = getPostById(postId);
        // checking if current user is same as author of the post
        if (updatePostDTO.getUserId() != userId) {
            throw new UnauthorizedException(messageUtil.getMessage("unauthorized"));
        }
        Post updatePost = new Post();
        updatePost.setId(postId);
        updatePost.setTitle(postDTO.getTitle());
        updatePost.setContent(postDTO.getContent());
        updatePost.setImageURL(postDTO.getImageURL());
        // formatting our dates
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TemporalAccessor createdTimeAccessor = timeFormatter.parse(updatePostDTO.getCreatedDate());
        // setting our created time
        updatePost.setCreatedDate(LocalDate.from(createdTimeAccessor));
        updatePost.setUpdatedDate(LocalDate.now());
        BlogUserDTO userDTO = userService.getUserById(updatePostDTO.getUserId());
        BlogUser user = dtoToBlogUser.convert(userDTO);
        updatePost.setUser(user);
        // setting post category
        Category category = categoryService.getPostCategory(updatePost);
        updatePost.setCategory(category);
        Post updatedPost = postRepository.save(updatePost);
        return postToPostDTO.convert(updatedPost);
    }

    @Override
    public ResponseEntity<Map<String, String>> deletePostByUser(int userId, int postId) {
        PostDTO postDTO = getPostById(postId);
        if (postDTO.getUserId() != userId) {
            throw new UnauthorizedException(messageUtil.getMessage("unauthorized"));
        }
        postRepository.deleteById(postId);
        return new ResponseEntity<>(Map.of("message", "Post was successfully deleted"), HttpStatus.OK);
    }

    @Override
    public Page<PostDTO> getAllPostsByUserInCategory(int userId, int categoryId, Pageable pageable) {
        BlogUserDTO userDTO = userService.getUserById(userId);
        BlogUser user = dtoToBlogUser.convert(userDTO);
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        Category category = dtoToCategory.convert(categoryDTO);
        Page<Post> foundPosts = postRepository.findAllByUserAndCategoryOrderByCreatedDateDesc(user, category, pageable);
        List<PostDTO> postDTOS = new LinkedList<>();
        foundPosts.forEach(
                post -> postDTOS.add(postToPostDTO.convert(post))
        );
        return new PageImpl<>(postDTOS, pageable, foundPosts.getTotalElements());
    }

    @Override
    public Page<PostDTO> searchPostsByTitle(String searchKeyWord,Pageable pageable) {
        Page<Post> foundPosts = postRepository.findAllByTitleContainingIgnoreCase(searchKeyWord,pageable);
        List<PostDTO> postDTOS=new LinkedList<>();
         foundPosts.forEach(
                 post -> postDTOS.add(postToPostDTO.convert(post))
         );
         return new PageImpl<>(postDTOS,pageable, foundPosts.getTotalElements());
    }
}
