package com.springboot.blog.service.impl;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.entity.Post;
import com.springboot.blog.dto.ResourceNotFoundException;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    //Post repository dependency injection
    private PostRepository postRepository;
    private ModelMapper modelMapper;
    //Constructor injection
    @Autowired
    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper){
        this.postRepository=postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = PostMapToEntity(postDto);
        //This will help to save a record into Database
        Post newPost = postRepository.save(post);
        //Convert Entity to DTO
        PostDto postResponse = mapToDTO(newPost);
        return  postResponse;
    }


     @Override
    public PostResponse  getAllPosts(int pageNo, int pageSize,String sortBy,String sortDirection) {
        //This will help to get all the records from Database
        //List<Post> posts=postRepository.findAll();
        //Convert Entity to DTO using Stream API and return the list of DTO
        //return posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        //Pagination implementation
        //Pageable pageable = PageRequest.of(pageNo,pageSize);

        //Sorting implementation using fixed order
        //Pageable pageable =  PageRequest.of(pageNo,pageSize, Sort.by(sortBy).descending());

        //Sorting implementation using dynamic order
         Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
         Pageable pageable =  PageRequest.of(pageNo,pageSize,sort);

        //This will help to get all the records from Database
        Page<Post> posts = postRepository.findAll(pageable);
        //Get Content from Page object
        List<Post> postList = posts.getContent();
        //Convert Entity to DTO using Stream API and return the list of DTO
        List<PostDto> content = postList.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        // Create PostResponse object
        PostResponse postResponse = new PostResponse(content, posts.getNumber(), posts.getSize(), posts.getTotalElements(), posts.getTotalPages(), posts.isLast());

        /*
            postResponse.setContent(content);
            postResponse.setPageNo(posts.getNumber());
            postResponse.setPageSize(posts.getSize());
            postResponse.setTotalPages(posts.getTotalPages());
            postResponse.setTotalElements(posts.getTotalElements());
            postResponse.setLast(posts.isLast());
        */
        //Return PostResponse object
        return postResponse;
    }

    @Override
    public PostDto getPostById(long Id) {
        //This will help to get a record from Database
        Post post = postRepository.findById(Id).orElseThrow(()-> new ResourceNotFoundException("Post","Id",Id));
        //Convert Entity to DTO
        PostDto postResponse = mapToDTO(post);
        //Return DTO
        return postResponse;
    }

    @Override
    public PostDto  updatePost(long Id, PostDto postDto) {
        //get post by id from the database
        Post post = postRepository.findById(Id).orElseThrow(()-> new ResourceNotFoundException("Post","Id",Id));
        //set the new values
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        //save the post to the database
        Post updatedPost = postRepository.save(post);
        //convert entity to DTO
        PostDto postResponse = mapToDTO(updatedPost);
        return postResponse;
    }

    public String deletePost(long Id) {
        //get post by id from the database
        Post post = postRepository.findById(Id).orElseThrow(()-> new ResourceNotFoundException("Post","Id",Id));
        //delete the post from the database
        postRepository.delete(post);
        return "Post deleted successfully";
    }
    //Convert DTO into Entity
    private Post PostMapToEntity(PostDto postDto) {
        Post post = modelMapper.map(postDto,Post.class);

/*        //Convert DTO to entity
        Post post = new Post( );
        //set the values
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());*/
        //return the entity
        return post;
    }
    //Convert Entity into DTO
    private PostDto mapToDTO(Post post){
        PostDto postDto= modelMapper.map(post,PostDto.class);
       /* PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setDescription(post.getDescription());*/
        return postDto;
    }
}
