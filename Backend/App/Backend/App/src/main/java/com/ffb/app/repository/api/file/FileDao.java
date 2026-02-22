package com.ffb.app.repository.api.file;

import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.PushbackInputStream;
import java.net.URI;
import java.util.UUID;

public interface FileDao {

    URI createNewImage(UUID imageId, PushbackInputStream inputData);
}
