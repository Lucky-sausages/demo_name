package com.example.demoname.external;

import com.jayway.jsonpath.DocumentContext;
import net.minidev.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VkProfileParser {

    private DocumentContext parsedData;
    private Map<String, String> queryMap;
    private String domain;

    public VkProfileParser(DocumentContext parsedData, String domain)
    {
        this.parsedData = parsedData;
        this.domain = domain;
        queryMap = new HashMap<>();
        queryMap.put("owner_id", "$.response.items[*].owner_id");
            queryMap.put("id", "$.response.items[*].id");
            queryMap.put("date", "$.response[*].items[*].date");
            queryMap.put("text", "$.response[*].items[*].text");
    }

    public void debug()
    {
        List<String> outputList = new ArrayList<>();
        JSONArray array = parsedData.read(queryMap.get("owner_id"));
        for (Object idObj : array)
        {
            int id = (Integer) idObj;
            System.out.println(id);
        }
    }
}
