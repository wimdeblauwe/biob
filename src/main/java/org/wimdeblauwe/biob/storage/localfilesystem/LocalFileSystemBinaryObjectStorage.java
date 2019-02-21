package org.wimdeblauwe.biob.storage.localfilesystem;

import org.wimdeblauwe.biob.BinaryObject;
import org.wimdeblauwe.biob.BinaryObjectMetadata;
import org.wimdeblauwe.biob.BinaryObjectStorage;
import org.wimdeblauwe.biob.BinaryObjectStorageException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class LocalFileSystemBinaryObjectStorage implements BinaryObjectStorage {

    private final Path basePath;

    public LocalFileSystemBinaryObjectStorage(Path basePath) {
        this.basePath = Objects.requireNonNull(basePath, "basePath should not be null");
    }

    @Override
    public void store(String filePath, BinaryObjectMetadata metadata, InputStream inputStream) {
        Path targetPath = this.basePath.resolve(filePath);
        try {
            File file = targetPath.toFile();
            File parentFile = file.getParentFile();
            parentFile.mkdirs();

            Files.copy(inputStream, targetPath);

            Properties properties = new Properties();
            properties.setProperty("fileSize", String.valueOf(metadata.getFileSize()));
            properties.setProperty("originalFilename", metadata.getOriginalFilename());
            properties.setProperty("contentType", metadata.getContentType());

            File metadataFile = getMetadataFile(targetPath);
            try (FileOutputStream outputStream = new FileOutputStream(metadataFile)) {
                properties.store(outputStream, "");
            }
        } catch (IOException e) {
            throw new BinaryObjectStorageException("Unable to store inputStream at path " + targetPath, e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    @Override
    public Optional<BinaryObject> retrieve(String filePath) {
        Path targetPath = this.basePath.resolve(filePath);
        if (targetPath.toFile().exists()) {
            try {
                InputStream inputStream = Files.newInputStream(targetPath);
                BinaryObjectMetadata metadata = loadMetadata(targetPath);
                return Optional.of(new BinaryObject(inputStream, metadata));
            } catch (IOException e) {
                throw new BinaryObjectStorageException("Unable to retrieve binary object at path " + targetPath, e);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean hasBinaryObject(String filePath) {
        return this.basePath.resolve(filePath).toFile().exists();
    }

    private BinaryObjectMetadata loadMetadata(Path targetPath) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(getMetadataFile(targetPath))) {
            properties.load(inputStream);
        }

        return new BinaryObjectMetadata(Long.valueOf(properties.getProperty("fileSize")),
                                        properties.getProperty("originalFilename"),
                                        properties.getProperty("contentType"));
    }

    @Override
    public void delete(String filePath) {
        try {
            Path targetPath = this.basePath.resolve(filePath);
            FileUtils.deleteDirectory(targetPath);
            FileUtils.deleteDirectory(getMetadataFile(targetPath).toPath());
        } catch (IOException e) {
            throw new BinaryObjectStorageException("Unable to delete " + filePath, e);
        }
    }

    private File getMetadataFile(Path targetPath) {
        String name = targetPath.getName(targetPath.getNameCount() - 1).toString();

        return targetPath.resolveSibling(name + "-metadata.properties").toFile();
    }
}
