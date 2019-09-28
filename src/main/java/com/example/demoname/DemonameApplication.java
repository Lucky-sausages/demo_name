package com.example.demoname;

import com.example.demoname.external.VkProfileParser;
import com.example.demoname.external.VkWebRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemonameApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemonameApplication.class, args);

		VkWebRequest Request = new VkWebRequest();

		VkProfileParser Parser = new VkProfileParser(Request.getPostParsedJson("ranyarwen.sinderama", 1), "ranyarwen.sinderama");
		Parser.debug();
	}

}
