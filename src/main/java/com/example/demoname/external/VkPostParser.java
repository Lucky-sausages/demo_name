package com.example.demoname.external;

import com.example.demoname.dto.MediaDTO;
import com.example.demoname.dto.PostDTO;
import com.jayway.jsonpath.DocumentContext;
import net.minidev.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VkPostParser {

    private DocumentContext parsedData;
    private Map<String, String> queryMap;
    private String domain;

    public VkPostParser(DocumentContext parsedData) {
        this.parsedData = parsedData;
        queryMap = new HashMap<>();
        queryMap.put("owner_id", "$.response.items[%d].owner_id");
        queryMap.put("id", "$.response.items[%d].id");
        queryMap.put("date", "$.response.items[%d].date");
        queryMap.put("text", "$.response.items[%d].text");
        queryMap.put("attachments", "$.response.items[%d].attachments[*]");
        queryMap.put("original_id", "$.response.items[%d].copy_history[0].id");
        queryMap.put("original_owner_id", "$.response.items[%d].copy_history[0].owner_id");
       // queryMap.put("original_attachments", "$.response.items[%d].copy_history[%d].attachments[*]");
       // queryMap.put("original_text", "$.response.items[%d].copy_history[%d].text");
        }


    public List<Long> getId()
    {
        List<Long> outputList = new ArrayList<>();
        JSONArray array = parsedData.read(queryMap.get("id"));
        for (Object item : array)
            outputList.add((Long) item);
        return outputList;
    }


    public List<PostDTO> getPosts(){

        List<PostDTO> outputList = new ArrayList<>();
        List<Long> id = getId();
        int count = id.size();

        for (int i=0;i<count;i++){
            PostDTO post = new PostDTO();
            //String displayUrl = parsedData.read(String.format(queryMap.get("display_url"), i));
            String owner_id = parsedData.read(String.format(queryMap.get("owner_id"), i));
            String post_id = parsedData.read(String.format(queryMap.get("id"), i));
            post.setLink("https://vk.com/wall" + owner_id + "_" + post_id);

            String text = parsedData.read(String.format(queryMap.get("text"), i));
            try {
                String original_post_id = parsedData.read(String.format(queryMap.get("original_id"), i));
                String original_post_owner_id = parsedData.read(String.format(queryMap.get("original_owner_id"), i));
                text = text + "\n" + "https://vk.com/wall" + original_post_owner_id + "_" + original_post_id;
            }
            catch (Exception e){
            }

            post.setText(text);
            post.setDate(parsedData.read(String.format(queryMap.get("data"), i)));
            post.setMedia(getMedia(i));
            outputList.add(post);
        }
        return outputList;
    }

    public List<MediaDTO> getMedia(int i){
        List<MediaDTO> outputList = new ArrayList<>();

        return outputList;
    }

    public void debug() {
        List<String> outputList = new ArrayList<>();
        JSONArray array = parsedData.read(queryMap.get("owner_id"));
        for (Object idObj : array)
        {
            int id = (Integer) idObj;
            System.out.println(id);
        }
    }
}
