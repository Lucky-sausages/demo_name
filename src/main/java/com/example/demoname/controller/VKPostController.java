package com.example.demoname.controller;

import com.example.demoname.domain.Link;
import com.example.demoname.domain.People;
import com.example.demoname.domain.Post;
import com.example.demoname.domain.User;
import com.example.demoname.dto.PostDTO;
import com.example.demoname.external.VkManager;
import com.example.demoname.external.VkPostParser;
import com.example.demoname.external.VkWebRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/vk_posts")
public class VKPostController {
    VkManager vkManager;

    public VKPostController(VkManager vkManager) {
        this.vkManager = vkManager;
    }

    @GetMapping
    List<Post> getPosts(User user) {
        List<People> peoples = user.getPeople();
        List<Post> posts = new ArrayList<>();
        VkWebRequest Request = new VkWebRequest();
        if (peoples!= null) {
            for (People people : peoples) {
                posts.addAll(vkManager.getPosts(people, 5, 0));

                //VkPostParser Parser = new VkPostParser(Request.getPostParsedJson("ranyarwen.sinderama", 4,0));
            }
        }
        return posts;
    }
}
