package com.example.demoname.external;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

class WebRequest
{
    private String getWebContent(String url)
    {
        try
        {
            URLConnection connection = new URL(url).openConnection();

            InputStream is = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            char[] buffer = new char[256];
            int rc;

            StringBuilder sb = new StringBuilder();

            while ((rc = reader.read(buffer)) != -1)
                sb.append(buffer, 0, rc);

            reader.close();

            return sb.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected DocumentContext getParsedJson(String url)
    {
        String webContent = getWebContent(url);
        return JsonPath.parse(webContent);
    }
}
