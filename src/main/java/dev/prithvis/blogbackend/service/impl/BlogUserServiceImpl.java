package dev.prithvis.blogbackend.service.impl;

import dev.prithvis.blogbackend.exceptions.EntityNotFoundException;
import dev.prithvis.blogbackend.models.BlogUser;
import dev.prithvis.blogbackend.payloads.BlogUserDTO;
import dev.prithvis.blogbackend.repository.BlogUserRepository;
import dev.prithvis.blogbackend.service.repo.BlogUserService;
import dev.prithvis.blogbackend.utils.BlogUserToDto;
import dev.prithvis.blogbackend.utils.DTOToBlogUser;
import dev.prithvis.blogbackend.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogUserServiceImpl implements BlogUserService {
    @Autowired
    BlogUserRepository userRepository;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    BlogUserToDto userToDto;
    @Autowired
    DTOToBlogUser dtoToBlogUser;

    @Override
    public BlogUserDTO getUserById(Integer id) {
        BlogUser user = this.userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(messageUtil.getMessage("blogUser.NotFoundById", id)));
        return userToDto.convert(user);
    }

    @Override
    public Page<BlogUserDTO> getUsers(Pageable pageable) {
        Page<BlogUser> usersPage = userRepository.findAll(pageable);
        List<BlogUserDTO> blogUsers = new ArrayList<>();
        usersPage.forEach(user -> {
            BlogUserDTO userDto = userToDto.convert(user);
            blogUsers.add(userDto);
        });
        return new PageImpl<>(blogUsers, pageable, usersPage.getTotalElements());
    }

    @Override
    public BlogUserDTO createUser(BlogUserDTO userDTO) {
        BlogUser user = dtoToBlogUser.convert(userDTO);
        BlogUser savedUser = this.userRepository.save(user);
        return userToDto.convert(savedUser);
    }

    @Override
    public BlogUserDTO updateUser(BlogUserDTO userDTO, Integer userId) {
        BlogUserDTO user = getUserById(userId);
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAbout(userDTO.getAbout());
        BlogUser savedUser = this.userRepository.save(dtoToBlogUser.convert(user));
        return userToDto.convert(savedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(messageUtil.getMessage("blogUser.NotFoundById", id));
        }
    }
}
