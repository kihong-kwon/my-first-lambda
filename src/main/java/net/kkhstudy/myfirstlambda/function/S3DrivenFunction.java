package net.kkhstudy.myfirstlambda.function;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.function.Function;

@Slf4j
@Component
public class S3DrivenFunction implements Function<S3Event, String> {

    private static final int IMG_WIDTH = 100;
    private static final int IMG_HEIGHT = 100;

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public String apply(S3Event s3Event) {

        // Get the object from the event and show its content type
        String bucket = s3Event.getRecords().get(0).getS3().getBucket().getName();
        String key = s3Event.getRecords().get(0).getS3().getObject().getKey();
        try {
            S3Object response = amazonS3.getObject(new GetObjectRequest(bucket, key));
            String contentType = response.getObjectMetadata().getContentType();

            resizeImageAndSaveToS3(response);
            return contentType;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void resizeImageAndSaveToS3(S3Object response) {
        try (InputStream s3ObjectData = response.getObjectContent()) {
            File sourceImageFile = File.createTempFile("sourceImgFile", "");
            Files.copy(s3ObjectData, sourceImageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            BufferedImage originalImage = ImageIO.read(sourceImageFile);
            int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

            BufferedImage resizeImageJpg = resizeImage(originalImage, type);
            File resizedImageFile = File.createTempFile("resizedimage", "");
            ImageIO.write(resizeImageJpg, "jpg", resizedImageFile);

            // Push the image to the other bucket
            amazonS3.putObject(new PutObjectRequest("image-resized.kkh-study" , response.getKey(), resizedImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private BufferedImage resizeImage(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }
}
