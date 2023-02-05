package dev.prithvis.blogbackend.utils.converter;

import dev.prithvis.blogbackend.models.Post;
import dev.prithvis.blogbackend.payloads.PostDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class PostDTOToPost implements Converter<PostDTO, Post> {

    @Override
    public Post convert(@NonNull PostDTO source) {
        Post post = new Post();
        post.setTitle(source.getTitle());
        post.setContent(source.getContent());
        post.setImageURL(source.getImageURL());
        return post;
    }
}
