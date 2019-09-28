package com.example.demoname.external;

import com.example.demoname.domain.Post;
import com.jayway.jsonpath.DocumentContext;

public class InstagramManager
{
    public static InstagramProfileParser getProfileParser(String profileName)
    {
        DocumentContext profileParsedJson = InstagramWebRequest.getInstance().getProfileParsedJson(profileName);
        return new InstagramProfileParser(profileParsedJson);
    }

    public static Post getPostByShortCode(String shortCode)
    {
        DocumentContext postParsedJson = InstagramWebRequest.getInstance().getPostParsedJson(shortCode);
        return InstagramPostParser.getInstance().parse(postParsedJson);
    }
}
