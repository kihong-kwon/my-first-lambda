package net.kkhstudy.myfirstlambda.modules.localstack;

import org.springframework.context.annotation.Import;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AwsLocalStackConfig.class)
public @interface EnableAwsLocalStack {
    LocalStackContainer.Service[] value();
}