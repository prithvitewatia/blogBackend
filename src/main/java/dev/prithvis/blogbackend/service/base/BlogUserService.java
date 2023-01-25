package dev.prithvis.blogbackend.service.base;

import dev.prithvis.blogbackend.payloads.BlogUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogUserService {
    BlogUserDTO getUserById(Integer id);

    Page<BlogUserDTO> getUsers(Pageable pageable);

    BlogUserDTO createUser(BlogUserDTO userDTO);

    BlogUserDTO updateUser(BlogUserDTO userDTO, Integer userId);

    void deleteUser(Integer id);
}
