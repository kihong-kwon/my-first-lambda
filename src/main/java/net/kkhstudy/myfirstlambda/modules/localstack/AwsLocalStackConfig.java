package net.kkhstudy.myfirstlambda.modules.localstack;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.testcontainers.containers.localstack.LocalStackContainer;

@Configuration
public class AwsLocalStackConfig implements ImportAware {
    private AnnotationAttributes annotationAttributes;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(importMetadata
                .getAnnotationAttributes(EnableAwsLocalStack.class.getName(), false));
        Assert.notNull(this.annotationAttributes,
                "@EnableAwsLocalStack is not present on importing class "
                        + importMetadata.getClassName());
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public LocalStackContainer localStackContainer() {
        LocalStackContainer.Service[] values = (LocalStackContainer.Service[]) annotationAttributes.get("value");
        Assert.isTrue(values.length > 0, "LocalStackで使用するサービスを選択してください。.");

        return new LocalStackContainer().withServices(values);
    }
}