package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.file.Files.readAllBytes;

public class FileStore implements BlobStore {

    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.name);

        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();


        try (FileOutputStream outputStream = new FileOutputStream(targetFile)){
            IOUtils.copy(blob.inputStream, outputStream);
        }

    }

    @Override
    public Optional<Blob> get(String name) throws IOException, URISyntaxException {
        File coverFile = new File(name);
        Path coverFilePath;

        if (coverFile.exists()) {
            coverFilePath = coverFile.toPath();
        } else {
            coverFilePath = Paths.get(getClass().getClassLoader().getResource("default-cover.jpg").toURI());
        }
        byte[] imageBytes = readAllBytes(coverFilePath);
        InputStream coverFileInputStream = new ByteArrayInputStream(imageBytes);

        String contentType = new Tika().detect(coverFilePath);
        return Optional.of(new Blob(name, coverFileInputStream, contentType));

    }

    @Override
    public void deleteAll() {
        // ...
    }
}
