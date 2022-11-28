package com.springboot.blog.service;

import com.springboot.blog.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(long postId, CommentDto commentDto);
    List<CommentDto> getCommentByPostId(long id);
    CommentDto getCommentById(Long postId,Long commentId);
    CommentDto updateCommentById(Long postId, Long commentId,CommentDto commentRequest);
    void deleteCommentById(Long postId,Long commentId);
}

