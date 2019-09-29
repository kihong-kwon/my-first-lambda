package net.kkhstudy.myfirstlambda;

import net.kkhstudy.myfirstlambda.function.CreateEntityFunction;
import net.kkhstudy.myfirstlambda.function.GetEntityFunction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyFirstLambdaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyFirstLambdaApplication.class, args);
    }

    @Bean
    public CreateEntityFunction createEntityFunction() {
        return new CreateEntityFunction();
    }

    @Bean
    public GetEntityFunction getEntityFunction() {
        return new GetEntityFunction();
    }
}
