package com.example.demoname.external;

import com.jayway.jsonpath.DocumentContext;

public class InstagramManager
{
    public static InstagramProfileParser getProfileParser(String profileName)
    {
        DocumentContext profileParsedJson = InstagramWebRequest.getInstance().getProfileParsedJson(profileName);
        return new InstagramProfileParser(profileParsedJson);
    }
}
