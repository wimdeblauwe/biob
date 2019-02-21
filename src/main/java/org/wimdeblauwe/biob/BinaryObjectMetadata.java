package org.wimdeblauwe.biob;

import java.util.Objects;
import java.util.StringJoiner;

public class BinaryObjectMetadata {
    private final long fileSize;
    private final String originalFilename;
    private final String contentType;

    public BinaryObjectMetadata(long fileSize, String originalFilename, String contentType) {
        this.fileSize = fileSize;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BinaryObjectMetadata metadata = (BinaryObjectMetadata) o;
        return fileSize == metadata.fileSize &&
                Objects.equals(originalFilename, metadata.originalFilename) &&
                Objects.equals(contentType, metadata.contentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileSize, originalFilename, contentType);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BinaryObjectMetadata.class.getSimpleName() + "[", "]")
                .add("fileSize=" + fileSize)
                .add("originalFilename='" + originalFilename + "'")
                .add("contentType='" + contentType + "'")
                .toString();
    }
}
