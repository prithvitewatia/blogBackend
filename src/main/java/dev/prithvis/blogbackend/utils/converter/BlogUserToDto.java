package dev.prithvis.blogbackend.utils.converter;

import dev.prithvis.blogbackend.models.BlogUser;
import dev.prithvis.blogbackend.payloads.BlogUserDTO;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BlogUserToDto implements Converter<BlogUser, BlogUserDTO> {
    @Override
    public BlogUserDTO convert(@NonNull BlogUser source) {
        BlogUserDTO userDTO=new BlogUserDTO();
        userDTO.setId(source.getId());
        userDTO.setName(source.getName());
        userDTO.setEmail(source.getEmail());
        userDTO.setPassword(source.getPassword());
        userDTO.setAbout(source.getAbout());
        return userDTO;
    }
}
