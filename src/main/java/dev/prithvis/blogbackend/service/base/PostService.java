package dev.prithvis.blogbackend.service.base;

import dev.prithvis.blogbackend.payloads.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface PostService {
    PostDTO getPostById(int id);

    Page<PostDTO> getAllPostsByUser(int userId, Pageable pageable);

    Page<PostDTO> getAllPostsByCategory(int categoryId, Pageable pageable);

    PostDTO createPostByUser(PostDTO postDTO,int userId);

    PostDTO updatePostByUser(PostDTO postDTO,int userId, int postId);

    ResponseEntity<Map<String,String>> deletePostByUser(int userId,int postId);

    Page<PostDTO> getAllPostsByUserInCategory(int userId, int categoryId, Pageable pageable);

    Page<PostDTO> searchPostsByTitle(String searchKeyWord,Pageable pageable);
}
