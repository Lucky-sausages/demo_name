package com.example.demoname.external;

import com.jayway.jsonpath.DocumentContext;

public class InstagramWebRequest extends WebRequest
{
    public DocumentContext getProfileParsedJson(String profileName)
    {
        String url = String.format("https://instagram.com/%s/?__a=1", profileName);
        return getParsedJson(url);
    }

    public DocumentContext getPostParsedJson(String postShortCode)
    {
        String url = String.format("https://instagram.com/p/%s/?__a=1", postShortCode);
        return getParsedJson(url);
    }
}
