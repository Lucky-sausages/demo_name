package com.example.demoname.external;

import com.example.demoname.dto.PostDTO;
import com.jayway.jsonpath.DocumentContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InstagramManager
{
    public static InstagramProfileParser getProfileParser(String profileName)
    {
        DocumentContext profileParsedJson = InstagramWebRequest.getInstance().getProfileParsedJson(profileName);
        return new InstagramProfileParser(profileParsedJson);
    }

    public static List<PostDTO> getRecentPosts(String profileName, Date thresholdDate)
    {
        List<PostDTO> dtoList = new ArrayList<>();
        int curSize;
        int totalSize;
        InstagramProfileParser parser = getProfileParser(profileName);
        do
        {
            List<PostDTO> recentPosts = parser.getRecentPosts(thresholdDate);
            dtoList.addAll(recentPosts);
            curSize = recentPosts.size();
            totalSize = parser.count();
            parser = parser.getNext();
        }
        while (parser != null && curSize == totalSize);
        return dtoList;
    }

    static PostDTO getPostByShortCode(String shortCode)
    {
        DocumentContext postParsedJson = InstagramWebRequest.getInstance().getPostParsedJson(shortCode);
        return InstagramPostParser.getInstance().parse(postParsedJson);
    }
}
