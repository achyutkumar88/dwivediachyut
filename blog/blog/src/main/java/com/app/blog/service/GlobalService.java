package com.app.blog.service;

import com.app.blog.dto.UpdatePostDTO;
import com.app.blog.models.Posts;
import com.app.blog.models.Users;
import com.app.blog.repository.PostRepository;
import com.app.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GlobalService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;


    public Object save(Posts post) {
        postRepository.save(post);
        return " Post added successfully ";
    }

    public List<Posts> getAllPost() {
        List<Posts> posts = postRepository.findAll();
        return posts;
    }

    public long getPostCount() {
        return postRepository.count();
    }

    public List<Posts> getPostByUser(Integer userId) {
        return postRepository.getPostByUser(userId);
    }

    public List<Posts> getPostforUser(Integer postId, String emailId) {
        return postRepository.getPostforUser(postId, emailId);
    }

    public String deletePost(Integer postId, String emailId) {
       // postRepository.deletePost(postId, emailId);
       // return "Post Deleted successfully ";
        Optional<Posts> post = postRepository.findById(postId);
        if(post.isPresent()){
            Integer userId = post.get().getPublishedBy().getUserId();
            Optional<Users> user = userRepository.findById(userId);
            if(user.get().getEmail().equals(emailId)){
                postRepository.deleteById(postId);
                return "Post Deleted";
            }
        }
        return "Post Not Found ";
    }

    public String updatePost(UpdatePostDTO updatePostDTO, String emailId) {
        Optional<Posts> post = postRepository.findById(updatePostDTO.getPost_id());
        if(post.isPresent()){
            Integer userId = post.get().getPublishedBy().getUserId();
            Optional<Users> user = userRepository.findById(userId);
            if(user.get().getEmail().equals(emailId)){
                postRepository.updatePost(updatePostDTO.getPost_id(), updatePostDTO.getBody(), updatePostDTO.getTitle());
                return "Post updated ";
            }
        }
        return "Post not present ";
    }
}
