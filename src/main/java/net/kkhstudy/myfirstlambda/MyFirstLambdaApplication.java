package net.kkhstudy.myfirstlambda;

import net.kkhstudy.myfirstlambda.function.CreateEntityFunction;
import net.kkhstudy.myfirstlambda.function.GetEntityFunction;
import net.kkhstudy.myfirstlambda.function.S3DrivenFunction;
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

    @Bean
    public S3DrivenFunction s3DrivenFunction() {
        return new S3DrivenFunction();
    }

}
