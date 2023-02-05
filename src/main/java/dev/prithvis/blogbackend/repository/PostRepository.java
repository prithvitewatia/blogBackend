package dev.prithvis.blogbackend.repository;

import dev.prithvis.blogbackend.models.BlogUser;
import dev.prithvis.blogbackend.models.Category;
import dev.prithvis.blogbackend.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findAllByUserOrderByCreatedDateDesc(BlogUser user, Pageable pageable);

    Page<Post> findAllByCategory(Category category, Pageable pageable);

    Page<Post> findAllByUserAndCategoryOrderByCreatedDateDesc(BlogUser user,Category category,Pageable pageable);

    Page<Post> findAllByTitleContainingIgnoreCase(String searchKeyword,Pageable pageable);

}
