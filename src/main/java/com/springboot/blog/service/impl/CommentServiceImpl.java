package com.springboot.blog.service.impl;

import com.springboot.blog.dto.CommentDto;
import com.springboot.blog.dto.ResourceNotFoundException;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//Step1: @Service annotation is used to mark a class as a service provider.
@Service
public class CommentServiceImpl implements CommentService {
    //Step2: Comment repository dependency injection
    private CommentRepository commentRepository;
    //Step3: Post repository dependency injection
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    //Step4: Constructor injection for Comment repository, Post repository
    //Step5: @Autowired annotation is used to inject the object dependency implicitly.
    //It is optional if there is only one constructor in the class.
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }
    //Step6: This method will help to create a comment for a post
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        System.out.println(commentDto);
        //Step7: Map CommentDto to Comment Entity
        Comment comment = mapToEntity(commentDto);

        //Retrieve the post entity by postId
        Post post = postRepository
                    .findById(postId)
                    .orElseThrow(() -> new ResourceNotFoundException("Post","id", postId));

        comment.setPost(post);

        //This will help to save a record into Database
        Comment newComment= commentRepository.save(comment);

        //Convert Entity to DTO
        CommentDto commentResponse = mapToDTO(newComment);
        return commentResponse;
    }

    @Override
    public List<CommentDto> getCommentByPostId(long postId) {
        //retrieve comment by postId
        List<Comment> comments = commentRepository.findByPostId(postId);
        //Convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        //Retrieve the post entity by postId
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","id", postId));

        //retrieve comment by id
        Comment comment = commentRepository
                        .findById(commentId)
                        .orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment doesn't belong to a post");
        }
        return mapToDTO(comment);
    }

    @Override
    public CommentDto updateCommentById(Long postId, Long commentId, CommentDto commentRequest) {
        //Retrieve the post entity by postId
        Post post = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post","id", postId));

        //retrieve comment by id
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        if(comment.getPost().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment doesn't belong to a post");
        }
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);
    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        Post post = postRepository
                .findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        Comment comment = commentRepository
                            .findById(commentId)
                .orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"Comment does not belongs to post");
        }

        commentRepository.delete(comment);
    }

    //This method will help to convert Comment DTO to Comment Entity
    private CommentDto mapToDTO(@NotNull Comment comment){
        CommentDto commentDto = modelMapper.map(comment,CommentDto.class);
/*       CommentDto commentDto= new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());*/
        return commentDto;
    }
    //This method will help to convert Comment Entity to Comment DTO
    private  Comment mapToEntity(@NotNull CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto,Comment.class);
/*        Comment comment= new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());*/
        return comment;
    }
}
