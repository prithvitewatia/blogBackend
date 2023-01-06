package dev.prithvis.blogbackend.repository;

import dev.prithvis.blogbackend.models.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogUserRepository extends JpaRepository<BlogUser,Integer> {
}
