package com.example.demoname.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PostDTO
{
    String link;
    String text;
    Date date;
    List<MediaDTO> media;
}
