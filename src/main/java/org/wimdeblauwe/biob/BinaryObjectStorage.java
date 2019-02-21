package org.wimdeblauwe.biob;

import java.io.InputStream;
import java.util.Optional;

public interface BinaryObjectStorage {
    void store(String filePath, BinaryObjectMetadata metadata, InputStream inputStream);

    Optional<BinaryObject> retrieve(String filePath);

    boolean hasBinaryObject(String filePath);

    void delete(String filePath);
}
