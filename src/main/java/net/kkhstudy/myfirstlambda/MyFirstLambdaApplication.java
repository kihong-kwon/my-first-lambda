package net.kkhstudy.myfirstlambda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Function;

@SpringBootApplication
public class MyFirstLambdaApplication {

    @Bean
    public Function<String, String> uppercaser() {
        return (String request) -> request.toUpperCase();
    }

    public static void main(String[] args) {
        SpringApplication.run(MyFirstLambdaApplication.class, args);
    }

}
