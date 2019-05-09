package org.superbiz.moviefun;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.superbiz.moviefun.blobstore.Blob;
import org.superbiz.moviefun.blobstore.BlobStore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

public class S3Store implements BlobStore {
    private AmazonS3Client s3Client;
    private String photoStorageBucketName;

    public S3Store(AmazonS3Client s3Client, String photoStorageBucketName) {

        this.s3Client = s3Client;
        this.photoStorageBucketName = photoStorageBucketName;
    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata contentType = new ObjectMetadata();
        contentType.setContentType(blob.contentType);
        PutObjectResult result = s3Client.putObject(photoStorageBucketName,blob.name, blob.inputStream, contentType);

    }

    @Override
    public Optional<Blob> get(String name) throws IOException, URISyntaxException {
        S3Object foundBlob = s3Client.getObject(photoStorageBucketName, name);
        S3ObjectInputStream blobInputStream = foundBlob.getObjectContent();
        ObjectMetadata blobContentType = foundBlob.getObjectMetadata();
        String contentType = blobContentType.getContentType();
        return Optional.of(new Blob(name, blobInputStream, contentType));

    }

    @Override
    public void deleteAll() {

    }
}
