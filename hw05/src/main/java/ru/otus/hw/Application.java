package ru.otus.hw;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.sql.SQLException;


@SpringBootApplication
public class Application {

//	public static void main(String[] args) {
//		SpringApplication.run(Application.class, args);
//	}

	public static void main(String[] args) throws Exception {

		ApplicationContext context = SpringApplication.run(Application.class);


		Console.main(args);
	}

}
