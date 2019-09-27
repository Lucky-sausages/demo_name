package com.example.demoname.external;

import com.jayway.jsonpath.DocumentContext;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.UTF_8;

public class InstagramWebRequest extends WebRequest
{
    private static InstagramWebRequest instance;

    static InstagramWebRequest getInstance()
    {
        if (instance == null)
            instance = new InstagramWebRequest();
        return instance;
    }

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

    DocumentContext getProfileNextPageParsedJson(String userId, String cursor)
    {
        String queryHash = "58b6785bea111c67129decbe6a448951";
        int postCount = 50;
        String param;
        try
        {
            param = URLEncoder.encode(String.format(
                    "{" +
                        "\"id\": \"%s\"," +
                        "\"first\":%d," +
                        "\"after\":\"%s\"" +
                    "}", userId, postCount, cursor), UTF_8.toString());
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
        String url = String.format("https://www.instagram.com/graphql/query/?query_hash=%s&variables=%s", queryHash, param);

        return getParsedJson(url);
    }
}
