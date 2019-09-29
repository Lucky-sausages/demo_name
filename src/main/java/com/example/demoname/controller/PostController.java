package com.example.demoname.controller;

import com.example.demoname.domain.User;
import com.example.demoname.dto.PostDTO;
import com.example.demoname.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/posts")
public class PostController extends ApiController
{
    private PostService postService;

    public PostController(PostService postService)
    {
        this.postService = postService;
    }

    @PostMapping("/update")
    public void updatePosts(User user)
    {
        postService.updatePosts(user);
    }

    @GetMapping
    public List<PostDTO> getPosts(User user)
    {
        return postService.getPosts(user);
    }
}
