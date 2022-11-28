package com.springboot.blog.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class  PostDto {
    private long id;
    //title should not be null or empty
    //title should have at least 2 characters.
    @NotEmpty(message = "Post title should not be null or empty")
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    //post description should not be null or empty
    //post description should have at least 10 characters
    @NotEmpty(message = "Post description should not be null or empty")
    @Size(min=10, message = "Post description should have at least have 10 Character")
    private String description;

    //Post content should not be null or empty
    @NotEmpty(message = "Post content should not be null or empty")
    private String content;
    private Set<CommentDto> comments;
}
