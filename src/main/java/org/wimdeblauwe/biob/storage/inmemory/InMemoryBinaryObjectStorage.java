package org.wimdeblauwe.biob.storage.inmemory;

import com.google.common.io.ByteStreams;
import org.wimdeblauwe.biob.BinaryObject;
import org.wimdeblauwe.biob.BinaryObjectMetadata;
import org.wimdeblauwe.biob.BinaryObjectStorage;
import org.wimdeblauwe.biob.BinaryObjectStorageException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class InMemoryBinaryObjectStorage implements BinaryObjectStorage {

    private final Map<String, InMemoryBinaryObject> binaryObjects = new HashMap<>();

    @Override
    public void store(String filePath, BinaryObjectMetadata metadata, InputStream inputStream) {
        try {
            Objects.requireNonNull(filePath, "filePath should not be null");
            Objects.requireNonNull(metadata, "metadata should not be null");
            Objects.requireNonNull(inputStream, "inputStream should not be null");

            binaryObjects.put(filePath, new InMemoryBinaryObject(ByteStreams.toByteArray(inputStream), metadata));
        } catch (IOException e) {
            throw new BinaryObjectStorageException("Unable to store inputStream under path " + filePath, e);
        }
    }

    @Override
    public Optional<BinaryObject> retrieve(String filePath) {
        InMemoryBinaryObject binaryObject = binaryObjects.get(filePath);
        if (binaryObject != null) {
            return Optional.of(new BinaryObject(new ByteArrayInputStream(binaryObject.getBytes()),
                                                binaryObject.getMetadata()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean hasBinaryObject(String filePath) {
        return binaryObjects.containsKey(filePath);
    }

    @Override
    public void delete(String filePath) {
        binaryObjects.remove(filePath);
    }

    private static class InMemoryBinaryObject {
        private byte[] bytes;
        private BinaryObjectMetadata metadata;

        InMemoryBinaryObject(byte[] bytes, BinaryObjectMetadata metadata) {
            this.bytes = bytes;
            this.metadata = metadata;
        }

        byte[] getBytes() {
            return bytes;
        }

        BinaryObjectMetadata getMetadata() {
            return metadata;
        }
    }
}
