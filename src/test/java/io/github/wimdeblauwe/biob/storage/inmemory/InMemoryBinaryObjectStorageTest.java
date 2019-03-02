package io.github.wimdeblauwe.biob.storage.inmemory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.wimdeblauwe.biob.BinaryObject;
import io.github.wimdeblauwe.biob.BinaryObjectMetadata;

import java.io.ByteArrayInputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryBinaryObjectStorageTest {

    private InMemoryBinaryObjectStorage storage;

    @BeforeEach
    void setUp() {
        storage = new InMemoryBinaryObjectStorage();
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

    private BinaryObjectMetadata createExampleMetadata() {
        return new BinaryObjectMetadata(3, "test.jpg", "image/jpg");
    }

}