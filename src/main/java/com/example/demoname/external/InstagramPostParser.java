package com.example.demoname.external;

import com.example.demoname.domain.Link;
import com.example.demoname.domain.Media;
import com.example.demoname.domain.Post;
import com.jayway.jsonpath.DocumentContext;
import net.minidev.json.JSONArray;

import java.util.*;

class InstagramPostParser
{
    private Map<String, String> queryMap;

    private InstagramPostParser()
    {
        queryMap = new HashMap<>();
        queryMap.put("shortcode", "$.graphql.shortcode_media.shortcode");
        queryMap.put("__typename", "$.graphql.shortcode_media.__typename");
        queryMap.put("display_url", "$.graphql.shortcode_media.display_url");
        queryMap.put("video_url", "$.graphql.shortcode_media.video_url");
        queryMap.put("caption", "$.graphql.shortcode_media.edge_media_to_caption.edges[0].node.text");
        queryMap.put("sidecar", "$.graphql.shortcode_media.edge_sidecar_to_children.edges[*].node");
    }

    Post parse(DocumentContext parsedDocument)
    {
        final String getPostTypePath = queryMap.get("__typename");
        String postType = parsedDocument.read(getPostTypePath);
        String shortCode = parsedDocument.read(queryMap.get("shortcode"));
        Post post = new Post();
        String caption = parsedDocument.read(queryMap.get("caption"));
        post.setText(caption);
        post.setLink(String.format("https://instagram.com/p/%s/", shortCode));
        switch (postType)
        {
            case "GraphImage":
                parseGraphImage(parsedDocument, post);
                break;
            case "GraphVideo":
                parseGraphVideo(parsedDocument, post);
                break;
            case "GraphSidecar":
                parseGraphSidecar(parsedDocument, post);
                break;
        }
        return post;
    }

    private void parseGraphImage(DocumentContext parsedDocument, Post post)
    {
        String displayUrl = parsedDocument.read(queryMap.get("display_url"));
        Media media = new Media();
        media.setLink(displayUrl);
        media.setPost(post);
        List<Media> mediaList = new ArrayList<>();
        mediaList.add(media);
        post.setMedia(mediaList);
    }

    private void parseGraphVideo(DocumentContext parsedDocument, Post post)
    {
        String videoUrl = parsedDocument.read(queryMap.get("video_url"));
        Media media = new Media();
        media.setLink(videoUrl);
        media.setPost(post);
        List<Media> mediaList = new ArrayList<>();
        mediaList.add(media);
        post.setMedia(mediaList);
    }

    private void parseGraphSidecar(DocumentContext parsedDocument, Post post)
    {
        JSONArray sidecarNodes = parsedDocument.read(queryMap.get("sidecar"));
        List<Media> mediaList = new ArrayList<>();
        for (Object nodeObj : sidecarNodes)
        {
            LinkedHashMap node = (LinkedHashMap) nodeObj;
            String typeName = (String) node.get("__typename");
            Media media = new Media();
            media.setPost(post);
            switch (typeName)
            {
                case "GraphImage":
                    media.setLink((String) node.get("display_url"));
                    break;
                case "GraphVideo":
                    media.setLink("video_url");
            }
            mediaList.add(media);
        }
        post.setMedia(mediaList);
    }

    private static InstagramPostParser instance;

    static InstagramPostParser getInstance()
    {
        if (instance == null)
            instance = new InstagramPostParser();
        return instance;
    }
}

