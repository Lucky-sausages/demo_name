package com.example.demoname.dto;

import com.example.demoname.domain.User;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
@Data
public class PeopleDTO {
    @NotNull
    String name;
    String inst_name;
    String vk_name;
}
