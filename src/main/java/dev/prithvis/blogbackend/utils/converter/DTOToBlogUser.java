package dev.prithvis.blogbackend.utils.converter;

import dev.prithvis.blogbackend.models.BlogUser;
import dev.prithvis.blogbackend.payloads.BlogUserDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class DTOToBlogUser implements Converter<BlogUserDTO, BlogUser> {

    @Override
    public BlogUser convert(@NonNull BlogUserDTO source) {
        BlogUser blogUser=new BlogUser();
        blogUser.setId(source.getId());
        blogUser.setName(source.getName());
        blogUser.setEmail(source.getEmail());
        blogUser.setPassword(source.getPassword());
        blogUser.setAbout(source.getAbout());
        return blogUser;
    }
}
