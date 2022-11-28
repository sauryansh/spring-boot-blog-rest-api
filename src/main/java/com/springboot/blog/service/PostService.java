package com.springboot.blog.service;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;

import java.util.List;

public interface PostService {
    public PostDto createPost(PostDto postDto);
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy,String sortDirection);
    public PostDto getPostById(long Id);
    public PostDto updatePost(long Id, PostDto postDto);
    public String deletePost(long Id);

}
