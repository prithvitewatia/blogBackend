package dev.prithvis.blogbackend.controller;

import dev.prithvis.blogbackend.models.DataModels;
import dev.prithvis.blogbackend.payloads.BlogUserDTO;
import dev.prithvis.blogbackend.service.repo.BlogUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/blogusers")
public class BlogUsersController {
    @Autowired
    BlogUserService userService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BlogUserDTO getUserById(@PathVariable(name = "id") Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BlogUserDTO> getUsers(@RequestParam(required = false, defaultValue = "10") int size,
                                      @RequestParam(required = false, defaultValue = "0") int page,
                                      @RequestParam(required = false, defaultValue = "id") String sortOn,
                                      @RequestParam(required = false, defaultValue = "asc") String direction) {
        // if we encounter invalid values
        if (page < 0) {
            page = 0;
        }
        if (size < 0) {
            size = 10;
        }
        if (!DataModels.getAttributes("BlogUser").contains(sortOn)) {
            sortOn = "id";
        }
        if (!Set.of("asc", "desc").contains(direction)) {
            direction = "asc";
        }
        Pageable pageable;
        if (direction.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortOn));
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortOn).descending());
        }
        return userService.getUsers(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlogUserDTO createUser(@Valid @RequestBody BlogUserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BlogUserDTO updateUser(@Valid @RequestBody BlogUserDTO userDTO, @PathVariable(name = "id") int id) {
        return userService.updateUser(userDTO, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "id") int id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(Map.of("message","User deleted successfully"),HttpStatus.OK);
    }
}
