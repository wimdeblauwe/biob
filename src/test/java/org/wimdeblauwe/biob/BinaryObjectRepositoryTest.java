package org.wimdeblauwe.biob;

import org.junit.jupiter.api.Test;
import org.wimdeblauwe.biob.storage.inmemory.InMemoryBinaryObjectStorage;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BinaryObjectRepositoryTest {

    @Test
    void testStoreAndRetrieve() {
        InMemoryBinaryObjectStorage storage = new InMemoryBinaryObjectStorage();
        BinaryObjectRepository<User, UUID> repository = new BinaryObjectRepository<>(UUID::randomUUID,
                                                                                     (user, uuid) -> user
                                                                                             .getId() + "/images/" + uuid
                                                                                             .toString(),
                                                                                     storage);
        User user = new User(1L);
        UUID id = repository.store(user,
                                   createExampleMetadata(),
                                   createExampleInputStream());
        assertThat(repository.retrieve(user, id)).hasValueSatisfying(binaryObject -> {
            assertThat(binaryObject.getInputStream()).hasSameContentAs(createExampleInputStream());
            assertThat(binaryObject.getMetadata()).isEqualTo(createExampleMetadata());
        });

        assertThat(storage.hasBinaryObject("1/images/" + id)).isTrue();
    }

    @Test
    void testGetMetadata() {
        InMemoryBinaryObjectStorage storage = new InMemoryBinaryObjectStorage();
        BinaryObjectRepository<User, UUID> repository = new BinaryObjectRepository<>(UUID::randomUUID,
                                                                                     (user, uuid) -> user
                                                                                             .getId() + "/images/" + uuid
                                                                                             .toString(),
                                                                                     storage);
        User user = new User(1L);
        UUID id = repository.store(user,
                                   createExampleMetadata(),
                                   createExampleInputStream());
        assertThat(repository.getMetadata(user, id)).hasValueSatisfying(binaryObjectMetadata -> {
            assertThat(binaryObjectMetadata).isEqualTo(createExampleMetadata());
        });
    }

    @Test
    void testHasBinaryObject() {
        InMemoryBinaryObjectStorage storage = new InMemoryBinaryObjectStorage();
        BinaryObjectRepository<User, UUID> repository = new BinaryObjectRepository<>(UUID::randomUUID,
                                                                                     (user, uuid) -> user
                                                                                             .getId() + "/images/" + uuid
                                                                                             .toString(),
                                                                                     storage);
        User user = new User(1L);
        UUID id = repository.store(user,
                                   createExampleMetadata(),
                                   createExampleInputStream());
        assertThat(repository.hasBinaryObject(user, id)).isTrue();
    }

    @Test
    void testHasBinaryObjectWithDifferentEntity() {
        InMemoryBinaryObjectStorage storage = new InMemoryBinaryObjectStorage();
        BinaryObjectRepository<User, UUID> repository = new BinaryObjectRepository<>(UUID::randomUUID,
                                                                                     (user, uuid) -> user
                                                                                             .getId() + "/images/" + uuid
                                                                                             .toString(),
                                                                                     storage);
        User user = new User(1L);
        UUID id = repository.store(user,
                                   createExampleMetadata(),
                                   createExampleInputStream());
        assertThat(repository.hasBinaryObject(new User(123L), id)).isFalse();
    }

    @Test
    void testDelete() {
        InMemoryBinaryObjectStorage storage = new InMemoryBinaryObjectStorage();
        BinaryObjectRepository<User, UUID> repository = new BinaryObjectRepository<>(UUID::randomUUID,
                                                                                     (user, uuid) -> user
                                                                                             .getId() + "/images/" + uuid
                                                                                             .toString(),
                                                                                     storage);
        User user = new User(1L);
        UUID id = repository.store(user,
                                   createExampleMetadata(),
                                   createExampleInputStream());
        repository.delete(user, id);
        assertThat(repository.hasBinaryObject(user, id)).isFalse();
    }

    @Test
    void testDeleteWithDifferentEntity() {
        InMemoryBinaryObjectStorage storage = new InMemoryBinaryObjectStorage();
        BinaryObjectRepository<User, UUID> repository = new BinaryObjectRepository<>(UUID::randomUUID,
                                                                                     (user, uuid) -> user
                                                                                             .getId() + "/images/" + uuid
                                                                                             .toString(),
                                                                                     storage);
        User user = new User(1L);
        UUID id = repository.store(user,
                                   createExampleMetadata(),
                                   createExampleInputStream());

        repository.delete(new User(123L), id);
        assertThat(repository.hasBinaryObject(user, id)).isTrue();
    }


    private ByteArrayInputStream createExampleInputStream() {
        return new ByteArrayInputStream(new byte[]{1, 2, 3});
    }

    private BinaryObjectMetadata createExampleMetadata() {
        return new BinaryObjectMetadata(3L, "test.png", "image/png");
    }


    private class User {
        private long id;
        private UUID avatar;

        public User(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public UUID getAvatar() {
            return avatar;
        }

        public void setAvatar(UUID avatar) {
            this.avatar = avatar;
        }
    }
}