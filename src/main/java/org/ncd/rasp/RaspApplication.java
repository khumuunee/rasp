package org.ncd.rasp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Platform;

@SpringBootApplication
public class RaspApplication {

	public static void main(String[] args) {
		Platform.startup(() ->{});
		SpringApplication.run(RaspApplication.class, args);
	}

}
