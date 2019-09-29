package com.example.demoname.service;

import com.example.demoname.domain.Media;
import com.example.demoname.domain.People;
import com.example.demoname.domain.Post;
import com.example.demoname.domain.User;
import com.example.demoname.dto.MediaDTO;
import com.example.demoname.dto.PostDTO;
import com.example.demoname.external.InstagramManager;
import com.example.demoname.repository.MediaRepository;
import com.example.demoname.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService
{
    private PostRepository postRepository;
    private MediaRepository mediaRepository;

    public PostService(PostRepository postRepository, MediaRepository mediaRepository)
    {
        this.postRepository = postRepository;
        this.mediaRepository = mediaRepository;
    }
    
    private MediaDTO mediaToMediaDTO(Media media)
    {
        MediaDTO dto = new MediaDTO();
        dto.setLink(media.getLink());
        return dto;
    }

    private Media mediaDTOToMedia(MediaDTO dto)
    {
        Media media = new Media();
        media.setLink(dto.getLink());
        return media;
    }

    private List<MediaDTO> mediaListToMediaDTOList(List<Media> mediaList)
    {
        List<MediaDTO> mediaDTOList = new ArrayList<>();
        mediaList.forEach(it -> mediaDTOList.add(mediaToMediaDTO(it)));
        return mediaDTOList;
    }

    private List<Media> mediaDTOListToMediaList(List<MediaDTO> dtoList)
    {
        List<Media> mediaList= new ArrayList<>();
        dtoList.forEach(mediaDTO -> mediaList.add(mediaDTOToMedia(mediaDTO)));
        return mediaList;
    }

    private PostDTO postToPostDTO(Post post)
    {
        PostDTO dto = new PostDTO();
        dto.setLink(post.getLink());
        dto.setDate(post.getDate());
        dto.setText(post.getText());
        dto.setMedia(mediaListToMediaDTOList(post.getMedia()));
        return dto;
    }

    private Post postDTOToPost(PostDTO dto, People people) {
        Post post = new Post();
        post.setLink(dto.getLink());
        post.setDate(dto.getDate());
        post.setPeople(people);
        if (dto.getText() != null) {
            byte[] utf8 = dto.getText().getBytes(StandardCharsets.UTF_8);
            post.setText(new String(utf8, StandardCharsets.UTF_8));
        } else post.setText("");
        post.setMedia(mediaDTOListToMediaList(dto.getMedia()));
        post.getMedia().forEach(media -> { media.setPost(post); mediaRepository.save(media); });
        return post;
    }

    private Post postDTOtoPost(PostDTO dto)
    {
        Post post = new Post();
        post.setLink(dto.getLink());
        String text = dto.getText();
        byte[] utf8Bytes = text.getBytes(StandardCharsets.UTF_8);
        text = new String(utf8Bytes);
        post.setText(text);
        post.setDate(dto.getDate());
        return post;
    }

    public List<PostDTO> getPosts(User user)
    {
        List<People> peopleList = user.getPeople();
        List<Post> feed = postRepository.findAllByPeopleInOrderByDateAsc(peopleList);
        List<PostDTO> feedDTO = new ArrayList<>();
        feed.forEach(it -> feedDTO.add(postToPostDTO(it)));
        return feedDTO;
    }

    public void updatePosts(User user)
    {
        List<People> peopleList = user.getPeople();
        for (People people : peopleList)
        {
            List<String> instagramProfileNames = people.getLinks()
                    .stream()
                    .filter(link -> link.getLink().startsWith("https://instagram.com/"))
                    .map(link -> link.getLink().substring("https://instagram.com/".length()))
                    .collect(Collectors.toList());

            //Date threshold = postRepository.findFirstByPeopleOrderByDateAsc(people).getDate();
            Post last = postRepository.findFirstByPeopleOrderByDateAsc(people);
            Date threshold = last == null ? new Date(0) : last.getDate();

            for (String profileName : instagramProfileNames)
            {
                List<PostDTO> recentPostDTOs = InstagramManager.getRecentPosts(profileName, threshold);
                List<Post> recentPosts = new ArrayList<>();
                recentPostDTOs.forEach(dto -> recentPosts.add(postDTOToPost(dto, people)));
                postRepository.saveAll(recentPosts);
            }
        }
    }
}
