package com.example.demoname.external;

import com.example.demoname.dto.MediaDTO;
import com.example.demoname.dto.PostDTO;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.PathNotFoundException;
import net.minidev.json.JSONArray;

import java.util.*;

public class InstagramProfileParser
{
    private DocumentContext parsedData;
    private Map<String, String> queryMap;
    private String userId;

    InstagramProfileParser(DocumentContext parsedData)
    {
        this(parsedData, null, true);
    }

    private InstagramProfileParser(DocumentContext parsedData, String userId, boolean initialPage)
    {
        this.parsedData = parsedData;
        this.userId = userId;
        queryMap = new HashMap<>();
        if (initialPage)
        {
            queryMap.put("size", "$.graphql.user.edge_owner_to_timeline_media.edges.[*]");
            queryMap.put("shortcode", "$.graphql.user.edge_owner_to_timeline_media.edges.[*].node.shortcode");
            queryMap.put("user.id", "$.graphql.user.id");
            queryMap.put("has_next_page", "$.graphql.user.edge_owner_to_timeline_media.page_info.has_next_page");
            queryMap.put("end_cursor", "$.graphql.user.edge_owner_to_timeline_media.page_info.end_cursor");

            queryMap.put("typename", "$.graphql.user.edge_owner_to_timeline_media.edges[%d].node.__typename");
            queryMap.put("caption", "$.graphql.user.edge_owner_to_timeline_media.edges[%d].node.edge_media_to_caption.edges[0].node.text");
            queryMap.put("display_url", "$.graphql.user.edge_owner_to_timeline_media.edges[%d].node.display_url");
            queryMap.put("timestamp", "$.graphql.user.edge_owner_to_timeline_media.edges[%d].node.taken_at_timestamp");
            queryMap.put("timestamps", "$.graphql.user.edge_owner_to_timeline_media.edges[*].node.taken_at_timestamp");
        }
        else
        {
            queryMap.put("size", "$.data.user.edge_owner_to_timeline_media.edges.[*]");
            queryMap.put("shortcode", "$.data.user.edge_owner_to_timeline_media.edges.[*].node.shortcode");
            queryMap.put("has_next_page", "$.data.user.edge_owner_to_timeline_media.page_info.has_next_page");
            queryMap.put("end_cursor", "$.data.user.edge_owner_to_timeline_media.page_info.end_cursor");

            queryMap.put("typename", "$.data.user.edge_owner_to_timeline_media.edges[%d].node.__typename");
            queryMap.put("caption", "$.data.user.edge_owner_to_timeline_media.edges[%d].node.edge_media_to_caption.edges[0].node.text");
            queryMap.put("display_url", "$.data.user.edge_owner_to_timeline_media.edges[%d].node.display_url");
            queryMap.put("timestamp", "$.data.user.edge_owner_to_timeline_media.edges[%d].node.taken_at_timestamp");
            queryMap.put("timestamps", "$.data.user.edge_owner_to_timeline_media.edges[*].node.taken_at_timestamp");
        }
    }

    public List<String> getShortCodes()
    {
        List<String> outputList = new ArrayList<>();
        JSONArray array = parsedData.read(queryMap.get("shortcode"));
        for (Object item : array)
            outputList.add((String)item);
        return outputList;
    }

    public List<PostDTO> getPosts()
    {
        List<PostDTO> outputList = new ArrayList<>();
        List<String> shortCodes = getShortCodes();
        int count = shortCodes.size();
        for (int i = 0; i < count; i++)
        {
            String typeName = parsedData.read(String.format(queryMap.get("typename"), i));
            PostDTO post;
            if (Objects.equals(typeName, "GraphImage"))
            {
                post = new PostDTO();
                String link = String.format("https://instagram.com/p/%s/", shortCodes.get(i));
                String text;
                try
                {
                    text = parsedData.read(String.format(queryMap.get("caption"), i));
                }
                catch (PathNotFoundException e)
                {
                    text = null;
                }
                Date date = new Date((Integer) parsedData.read(String.format(queryMap.get("timestamp"), i)));
                String displayUrl = parsedData.read(String.format(queryMap.get("display_url"), i));
                MediaDTO media = new MediaDTO();
                media.setLink(displayUrl);
                post.setLink(link);
                post.setText(text);
                post.setDate(date);
                List<MediaDTO> mediaList = new ArrayList<>();
                mediaList.add(media);
                post.setMedia(mediaList);
            }
            else
                post = InstagramManager.getPostByShortCode(shortCodes.get(i));
            outputList.add(post);
        }
        return outputList;
    }

    public List<PostDTO> getRecentPosts(Date thresholdDate)
    {
        List<PostDTO> outputList = new ArrayList<>();
        List<String> shortCodes = getShortCodes();
        JSONArray dateArray = parsedData.read(queryMap.get("timestamps"));
        long thresholdTimestamp = thresholdDate.getTime() / 1000;
        for (int i = 0; i < dateArray.size(); i++)
        {
            int timestamp = parsedData.read(String.format(queryMap.get("timestamp"), i));
            if (timestamp <= thresholdTimestamp)
                break;

            String typeName = parsedData.read(String.format(queryMap.get("typename"), i));
            PostDTO post;
            if (Objects.equals(typeName, "GraphImage"))
            {
                post = new PostDTO();
                String link = String.format("https://instagram.com/p/%s/", shortCodes.get(i));
                String text;
                try
                {
                    text = parsedData.read(String.format(queryMap.get("caption"), i));
                }
                catch (PathNotFoundException e)
                {
                    text = null;
                }
                Date date = new Date((Integer) parsedData.read(String.format(queryMap.get("timestamp"), i)));
                String displayUrl = parsedData.read(String.format(queryMap.get("display_url"), i));
                MediaDTO media = new MediaDTO();
                media.setLink(displayUrl);
                post.setLink(link);
                post.setText(text);
                post.setDate(date);
                List<MediaDTO> mediaList = new ArrayList<>();
                mediaList.add(media);
                post.setMedia(mediaList);
            }
            else
                post = InstagramManager.getPostByShortCode(shortCodes.get(i));
            outputList.add(post);
        }
        return outputList;
    }

    int count()
    {
        JSONArray array = parsedData.read(queryMap.get("size"));
        return array.size();
    }

    public InstagramProfileParser getNext()
    {
        boolean hasNext = parsedData.read(queryMap.get("has_next_page"));
        if (hasNext)
        {
            String userId = this.userId == null ? parsedData.read(queryMap.get("user.id")) : this.userId;
            String cursor = parsedData.read(queryMap.get("end_cursor"));
            DocumentContext nextDocument = InstagramWebRequest.getInstance().getProfileNextPageParsedJson(userId, cursor);
            return new InstagramProfileParser(nextDocument, userId, false);
        }
        else
            return null;
    }
}
