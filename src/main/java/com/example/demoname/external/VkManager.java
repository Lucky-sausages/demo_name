package com.example.demoname.external;

import com.example.demoname.domain.Media;
import com.example.demoname.domain.People;
import com.example.demoname.domain.Post;
import com.example.demoname.dto.PostDTO;
import com.jayway.jsonpath.DocumentContext;

import java.util.ArrayList;
import java.util.List;

public class VkManager {

    public static VkWebRequest WebRequest = VkWebRequest.getInstance();

    private void getPostsWithCount(People people, Integer count){ // разобраться с добавлением???
        Integer offset = 0;

        while (count>=100){
            getPosts(people, count, offset);
            count = count-100;
            offset = offset+100;
        }
        getPosts(people, count, offset);
    }

    private List<Post> getPosts (People people, Integer count, Integer offset){

        DocumentContext postsParsedJson = WebRequest.getPostParsedJson(people.getDomain(), count, offset);

        VkPostParser Parser = new VkPostParser(postsParsedJson);
        List<PostDTO> dtoPosts = Parser.getPosts();
        List<Post> posts = new ArrayList<>();
        for (int i=0; i<dtoPosts.size();i++){
            Post post = new Post();
            post.setLink(dtoPosts.get(i).getLink());
            post.setPeople(people);
            post.setText(dtoPosts.get(i).getText());
            List<Media> Media = new ArrayList<>();
            for (int j=0;j<dtoPosts.get(i).getMedia().size();j++){
                Media media = new Media();
                media.setPost(post);
                media.setLink(dtoPosts.get(i).getMedia().get(j).getLink());
                Media.add(media);
            }
            post.setMedia(Media);
            post.setDate(dtoPosts.get(i).getDate());
            posts.add(post);
        }
        return posts;
    }
}
