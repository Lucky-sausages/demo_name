package com.example.demoname.external;

import com.jayway.jsonpath.DocumentContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VkWebRequest extends WebRequest{

    @Value("${vk.key}")
    private String secret;

    private static VkWebRequest instance;
    private String filter = "owner";


    static VkWebRequest getInstance()
    {
        if (instance == null)
            instance = new VkWebRequest();
        return instance;
    }


    public DocumentContext getPostParsedJson(String domain, Integer count, Integer offset)
    {
        secret = "7e6f03dd7e6f03dd7e6f03ddd77e021f8377e6f7e6f03dd23e153cd52192e2ce2913c04";
        String url = String.format("https://api.vk.com/method/wall.get?domain=" + domain + "&offset="+ offset + "&count=" + count + "&filter=" + filter +"&access_token=" + secret + "&v=5.101");
        return getParsedJson(url);
    }


}
