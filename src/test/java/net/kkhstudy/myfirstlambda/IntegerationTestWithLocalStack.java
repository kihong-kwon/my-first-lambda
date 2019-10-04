package net.kkhstudy.myfirstlambda;

import cloud.localstack.DockerTestUtils;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.CreateFunctionRequest;
import com.amazonaws.services.lambda.model.Environment;
import com.amazonaws.services.lambda.model.Runtime;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import net.kkhstudy.myfirstlambda.handler.S3EventHandler;
import net.kkhstudy.myfirstlambda.utils.LocalTestUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.entity.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = {"s3", "lambda"}, randomizePorts = true)
public class IntegerationTestWithLocalStack {

    @Test
    public void testS3LambdaIntegration() throws Exception {
        AWSLambda lambda = DockerTestUtils.getClientLambda();
        AmazonS3 s3 = DockerTestUtils.getClientS3();
        String functionName = UUID.randomUUID().toString();
        String keyName = "test.jpg";
        s3.createBucket("image-source.kkh-study");
        s3.createBucket("image-resized.kkh-study");

        // create function
        CreateFunctionRequest request = new CreateFunctionRequest();
        Environment env = new Environment();
        Map<String, String> map = new HashMap<>();
        map.put("FUNCTION_NAME", "s3DrivenFunction");
        map.put("SPRING_PROFILES_ACTIVE", "aws");
        env.setVariables(map);

        request.setFunctionName(functionName);
        request.setRuntime(Runtime.Java8);
        request.setRole("r1");
        request.setCode(LocalTestUtil.createFunctionCode(S3EventHandler.class));
        request.setHandler(S3EventHandler.class.getName());
        request.setEnvironment(env);
        lambda.createFunction(request);

        File file = new File("test.jpg");
        String str = file.getAbsolutePath();
        FileInputStream fis = new FileInputStream(file);

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(file.length());

        // upload file
        PutObjectRequest putObjectRequest = new PutObjectRequest("image-source.kkh-study", keyName,
                fis, metaData);
        s3.putObject(putObjectRequest);

        // check uploaded file
        GetObjectRequest getRequest = new GetObjectRequest("image-source.kkh-study", keyName);
        S3Object result1 = s3.getObject(getRequest);
        Assertions.assertThat(result1.getKey()).isNotNull();

        // created file
        getRequest = new GetObjectRequest("image-resized.kkh-study", keyName);
        S3Object result2 = s3.getObject(getRequest);
        Assertions.assertThat(result2.getKey()).isNotNull();
    }
}
