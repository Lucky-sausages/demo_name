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

		VkPostParser Parser = new VkPostParser(Request.getPostParsedJson("ranyarwen.sinderama", 4,0));
		List<PostDTO> outputList = Parser.getPosts();
		for (int i=0; i<outputList.size(); i++){
			System.out.println(outputList.get(i).getLink());
			System.out.println(outputList.get(i).getDate());
			System.out.println(outputList.get(i).getText());
			for (int j=0; j<outputList.get(i).getMedia().size();j++){
				System.out.println((outputList.get(i).getMedia().get(j).getLink()));
			}
		}

	}

}
