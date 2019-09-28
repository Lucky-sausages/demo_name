package com.example.demoname;

import com.example.demoname.dto.PostDTO;
import com.example.demoname.external.VkPostParser;
import com.example.demoname.external.VkWebRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DemonameApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemonameApplication.class, args);

		VkWebRequest Request = new VkWebRequest();

		VkPostParser Parser = new VkPostParser(Request.getPostParsedJson("ranyarwen.sinderama", 1));
		List<PostDTO> outputList = Parser.getPosts();
		System.out.println(outputList.get(0).getDate());

	}

}
