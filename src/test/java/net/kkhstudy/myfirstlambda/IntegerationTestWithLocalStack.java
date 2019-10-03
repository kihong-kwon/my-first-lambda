package net.kkhstudy.myfirstlambda;

import cloud.localstack.DockerTestUtils;
import cloud.localstack.docker.LocalstackDockerExtension;
import cloud.localstack.docker.annotation.LocalstackDockerProperties;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.CreateFunctionRequest;
import com.amazonaws.services.lambda.model.Runtime;
import com.amazonaws.services.s3.AmazonS3;
import lombok.extern.slf4j.Slf4j;
import net.kkhstudy.myfirstlambda.handler.S3EventHandler;
import net.kkhstudy.myfirstlambda.utils.LocalTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@ExtendWith(LocalstackDockerExtension.class)
@LocalstackDockerProperties(services = {"lambda", "s3"})
public class IntegerationTestWithLocalStack {

    @Test
    public void testS3LambdaIntegration() throws Exception {
        /*AWSLambda lambda = DockerTestUtils.getClientLambda();
        AmazonS3 s3 = DockerTestUtils.getClientS3();
        String functionName = UUID.randomUUID().toString();

        // create function
        CreateFunctionRequest request = new CreateFunctionRequest();
        request.setFunctionName(functionName);
        request.setRuntime(Runtime.Java8);
        request.setRole("r1");
        request.setCode(LocalTestUtil.createFunctionCode(S3EventHandler.class));
        request.setHandler(S3EventHandler.class.getName());

        lambda.createFunction(request);*/

        AmazonS3 s3 = DockerTestUtils.getClientS3();

        String bucketName = "test-s3";
        s3.createBucket(bucketName);
        log.info("버킷을 생성했습니다. bucketName={}", bucketName);

        String content = "Hello World";
        String key = "s3-key";
        s3.putObject(bucketName, key, content);
        log.info("파일을 업로드하였습니다. bucketName={}, key={}, content={}", bucketName, key, content);

        List<String> results = IOUtils.readLines(s3.getObject(bucketName, key).getObjectContent(), "utf-8");
        log.info("파일을 가져왔습니다. bucketName={}, key={}, results={}", bucketName, key, results);

    }
}
