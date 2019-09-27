package com.example.demoname.external;

import com.jayway.jsonpath.DocumentContext;
import net.minidev.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstagramProfileParser
{
    private DocumentContext parsedData;
    private Map<String, String> queryMap;
    private String userId;

    public InstagramProfileParser(DocumentContext parsedData)
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
            queryMap.put("shortcode", "$.graphql.user.edge_owner_to_timeline_media.edges.[*].node.shortcode");
            queryMap.put("user.id", "$.graphql.user.id");
            queryMap.put("has_next_page", "$.graphql.user.edge_owner_to_timeline_media.page_info.has_next_page");
            queryMap.put("end_cursor", "$.graphql.user.edge_owner_to_timeline_media.page_info.end_cursor");
        }
        else
        {
            queryMap.put("shortcode", "$.data.user.edge_owner_to_timeline_media.edges.[*].node.shortcode");
            queryMap.put("has_next_page", "$.data.user.edge_owner_to_timeline_media.page_info.has_next_page");
            queryMap.put("end_cursor", "$.data.user.edge_owner_to_timeline_media.page_info.end_cursor");
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
