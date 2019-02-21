package org.wimdeblauwe.biob;

import java.io.InputStream;
import java.util.StringJoiner;

public class BinaryObject {
    private final InputStream inputStream;
    private final BinaryObjectMetadata metadata;

    public BinaryObject(InputStream inputStream, BinaryObjectMetadata metadata) {
        this.inputStream = inputStream;
        this.metadata = metadata;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public BinaryObjectMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BinaryObject.class.getSimpleName() + "[", "]")
                .add("inputStream=" + inputStream)
                .add("metadata=" + metadata)
                .toString();
    }
}
