package net.kkhstudy.myfirstlambda.utils;

import com.amazonaws.services.lambda.model.FunctionCode;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.apache.commons.compress.utils.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LocalTestUtil {

    public static FunctionCode createFunctionCode(Class<?> clazz) throws Exception {
        FunctionCode code = new FunctionCode();
        ByteArrayOutputStream zipOut = new ByteArrayOutputStream();
        ByteArrayOutputStream jarOut = new ByteArrayOutputStream();
        // create zip file
        ZipOutputStream zipStream = new ZipOutputStream(zipOut);
        // create jar file
        JarOutputStream jarStream = new JarOutputStream(jarOut);

        // write class files into jar stream
        addClassToJar(clazz, jarStream);
        addClassToJar(SQSEvent.class, jarStream);
        addClassToJar(S3Event.class, jarStream);
        // write MANIFEST into jar stream
        JarEntry mfEntry = new JarEntry("META-INF/MANIFEST.MF");
        jarStream.putNextEntry(mfEntry);
        jarStream.closeEntry();
        jarStream.close();

        // write jar into zip stream
        ZipEntry codeEntry = new ZipEntry("LambdaCode.jar");
        zipStream.putNextEntry(codeEntry);
        zipStream.write(jarOut.toByteArray());
        zipStream.closeEntry();

        zipStream.close();
        code.setZipFile(ByteBuffer.wrap(zipOut.toByteArray()));

        return code;
    }

    private static void addClassToJar(Class<?> clazz, JarOutputStream jarStream) throws IOException {
        String resource = clazz.getName().replace(".", File.separator) + ".class";
        JarEntry jarEntry = new JarEntry(resource);
        jarStream.putNextEntry(jarEntry);
        IOUtils.copy(LocalTestUtil.class.getResourceAsStream("/" + resource), jarStream);
        jarStream.closeEntry();
    }

}