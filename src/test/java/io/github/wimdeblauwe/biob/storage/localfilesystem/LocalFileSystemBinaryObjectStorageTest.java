package io.github.wimdeblauwe.biob.storage.localfilesystem;

import io.github.wimdeblauwe.biob.BinaryObject;
import io.github.wimdeblauwe.biob.BinaryObjectMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LocalFileSystemBinaryObjectStorageTest {

    @TempDir
    Path basePath;
    private LocalFileSystemBinaryObjectStorage storage;

    @BeforeEach
    void setUp() {
        storage = new LocalFileSystemBinaryObjectStorage(basePath);
    }

    @Test
    void testStoreAndRetrieve() {
        BinaryObjectMetadata metadata = createExampleMetadata();
        storage.store("images/1", metadata, new ByteArrayInputStream(new byte[]{1, 2, 3}));

        assertThat(storage.retrieve("images/1"))
                .hasValueSatisfying(binaryObject ->
                                    {
                                        assertThat(binaryObject.getInputStream())
                                                .hasSameContentAs(new ByteArrayInputStream(new byte[]{1, 2, 3}));
                                        assertThat(binaryObject.getMetadata())
                                                .isEqualTo(createExampleMetadata());
                                    });
        assertThat(basePath.resolve("images/1")).exists();
    }

    @Test
    void testRetrieveIfNotKnown() {
        Optional<BinaryObject> optional = storage.retrieve("unknown/path/1");
        assertThat(optional).isEmpty();
    }

    @Test
    void testGetMetadata() {
        BinaryObjectMetadata metadata = createExampleMetadata();
        storage.store("images/1", metadata, new ByteArrayInputStream(new byte[]{1, 2, 3}));

        assertThat(storage.getMetadata("images/1"))
                .hasValueSatisfying(binaryObjectMetadata ->
                                    {
                                        assertThat(binaryObjectMetadata)
                                                .isEqualTo(createExampleMetadata());
                                    });
        assertThat(basePath.resolve("images/1")).exists();
    }

    @Test
    void testGetMetadataIfNotKnown() {
        Optional<BinaryObjectMetadata> optional = storage.getMetadata("unknown/path/1");
        assertThat(optional).isEmpty();
    }

    @Test
    void testHasBinaryObject() {
        storage.store("images/1", createExampleMetadata(),
                      new ByteArrayInputStream(new byte[]{1, 2, 3}));

        assertThat(storage.hasBinaryObject("images/1")).isTrue();
    }

    @Test
    void testHasBinaryObjectIfNotKnown() {
        assertThat(storage.hasBinaryObject("unknown/path/1")).isFalse();
    }

    @Test
    void testDelete() {
        BinaryObjectMetadata metadata = createExampleMetadata();
        storage.store("images/1", metadata, new ByteArrayInputStream(new byte[]{1, 2, 3}));

        storage.delete("images/1");

        assertThat(basePath.resolve("images/1")).doesNotExist();
    }

    private BinaryObjectMetadata createExampleMetadata() {
        return new BinaryObjectMetadata(3, "test.jpg", "image/jpg");
    }
}