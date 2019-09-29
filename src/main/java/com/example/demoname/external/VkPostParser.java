package com.example.demoname.external;

import com.example.demoname.dto.MediaDTO;
import com.example.demoname.dto.PostDTO;
import com.jayway.jsonpath.DocumentContext;
import net.minidev.json.JSONArray;

import java.util.*;

public class VkPostParser {

    private DocumentContext parsedData;
    private Map<String, String> queryMap;
    private String domain;

    public VkPostParser(DocumentContext parsedData) {
        this.parsedData = parsedData;
        queryMap = new HashMap<>();
        queryMap.put("owner_id", "$.response.items[%d].owner_id");
        queryMap.put("id", "$.response.items[%d].id");
        queryMap.put("ids", "$.response.items[*].id");
        queryMap.put("date", "$.response.items[%d].date");
        queryMap.put("text", "$.response.items[%d].text");
        queryMap.put("attachments_types", "$.response.items[%d].attachments[*].type");
        queryMap.put("attachment", "$.response.items[%d].attachments[%d]");
        queryMap.put("original_id", "$.response.items[%d].copy_history[0].id");
        queryMap.put("original_owner_id", "$.response.items[%d].copy_history[0].owner_id");
        queryMap.put("photo_link", "$.response.items[%d].attachments[%d].photo.sizes[*].url");
       // queryMap.put("original_attachments", "$.response.items[%d].copy_history[%d].attachments[*]");
       // queryMap.put("original_text", "$.response.items[%d].copy_history[%d].text");
        }


    public List<Integer> getId()
    {
        List<Integer> outputList = new ArrayList<>();
        JSONArray array = parsedData.read(queryMap.get("ids"));
        for (Object item : array)
            outputList.add((Integer)item);
        return outputList;
    }

    public List<String> getAttachmentsTypes(int id)
    {
        List<String> outputList = new ArrayList<>();
        JSONArray array = parsedData.read(String.format(queryMap.get("attachments_types"), id));
        for (Object item : array)
            outputList.add((String)item);
        return outputList;
    }

    public String getPhotoLink(int post_number, int attachment_number ){

        List<String> outputList = new ArrayList<>();
        JSONArray array = parsedData.read(String.format(queryMap.get("photo_link"), post_number, attachment_number));
        for (Object item : array)
            outputList.add((String)item);
        return outputList.get(outputList.size()-1);
    }


    public List<PostDTO> getPosts(){

        List<PostDTO> outputList = new ArrayList<>();
        List<Integer> id = getId();
        int count = id.size();

        for (int i=0;i<count;i++){
            PostDTO post = new PostDTO();
            String owner_id = parsedData.read(String.format(queryMap.get("owner_id"), i)).toString();
            String post_id = parsedData.read(String.format(queryMap.get("id"), i)).toString();
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
            int timestamp = parsedData.read(String.format(queryMap.get("date"), i));
            post.setDate(new Date(timestamp * 1000L));
            post.setMedia(getMedia(i));
            outputList.add(post);
        }
        return outputList;
    }

    public List<MediaDTO> getMedia(int post_number){
        List<MediaDTO> outputList = new ArrayList<>();
        try {
            List<String> types = getAttachmentsTypes(post_number);
            String link="";
            for (int j=0;j<types.size();j++){
                if (types.get(j).equals("photo")){
                    link = getPhotoLink(post_number,j);
                }

                MediaDTO  mediaDTO = new MediaDTO();
                mediaDTO.setLink(link);

                outputList.add(mediaDTO);
            }
        } catch (Exception e){}

        return outputList;
    }

}
