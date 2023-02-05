package dev.prithvis.blogbackend.utils.converter;

import dev.prithvis.blogbackend.models.Post;
import dev.prithvis.blogbackend.payloads.PostDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PostToPostDTO implements Converter<Post, PostDTO> {
    @Override
    public PostDTO convert(@NonNull Post source) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(source.getId());
        postDTO.setTitle(source.getTitle());
        postDTO.setContent(source.getContent());
        postDTO.setImageURL(source.getImageURL());
        LocalDate createdDate = source.getCreatedDate();
        LocalDate updatedDate = source.getUpdatedDate();
        if (createdDate != null) {
            postDTO.setCreatedDate(createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        if(updatedDate != null) {
            postDTO.setUpdatedDate(updatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        postDTO.setUserId(source.getUser().getId());
        postDTO.setCategoryId(source.getCategory().getId());
        return postDTO;
    }
}
