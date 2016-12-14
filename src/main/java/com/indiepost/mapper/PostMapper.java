package com.indiepost.mapper;

import com.indiepost.model.Post;
import dto.request.AdminPostRequestDto;
import dto.response.AdminPostResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Created by jake on 16. 12. 14.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class})
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "editor.id", target = "editorId")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "original.id", target = "originalId")
    AdminPostResponseDto postToAdminPostResponseDto(Post post);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "original", ignore = true)
    Post adminPostRequestDtoToPost(AdminPostRequestDto adminPostRequestDto);
}
