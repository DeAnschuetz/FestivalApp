package com.ffb.app.repository.impl.file;

import com.ffb.app.repository.api.file.FileDao;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.*;
import java.net.URI;
import java.util.UUID;

@ApplicationScoped
public class FileDaoImpl implements FileDao {

    @ConfigProperty(name = "ffb.image.path")
    String filePath;

    @Override
    public URI createNewImage(UUID imageId,PushbackInputStream inputData) {
        String appDir = System.getProperty("user.dir");
        String resolvedDir = filePath.replace("{appDir}", appDir);
        File targetDir = new File(resolvedDir);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            throw new RuntimeException("Could not create directory: " + targetDir.getAbsolutePath());
        }
        File target = new File(targetDir, imageId.toString() + ".jpg");

        try (
                InputStream data = inputData;
             OutputStream out = new BufferedOutputStream(new FileOutputStream(target))
        ) {

            byte[] buffer = new byte[8192];
            int n;
            while ((n = data.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return target.toURI();
    }
}
