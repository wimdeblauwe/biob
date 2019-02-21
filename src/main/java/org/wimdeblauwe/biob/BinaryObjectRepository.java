package org.wimdeblauwe.biob;

import java.io.InputStream;
import java.util.Optional;

public class BinaryObjectRepository<E, T> {
    private final BinaryObjectIdSupplier<T> binaryObjectIdSupplier;
    private final BinaryObjectIdToFilePathFunction<E, T> binaryObjectIdToFilePathFunction;
    private final BinaryObjectStorage storage;

    public BinaryObjectRepository(BinaryObjectIdSupplier<T> binaryObjectIdSupplier,
                                  BinaryObjectIdToFilePathFunction<E, T> binaryObjectIdToFilePathFunction,
                                  BinaryObjectStorage storage) {
        this.binaryObjectIdSupplier = binaryObjectIdSupplier;
        this.binaryObjectIdToFilePathFunction = binaryObjectIdToFilePathFunction;
        this.storage = storage;
    }

    public T store(E entity, BinaryObjectMetadata metadata, InputStream inputStream) {
        T t = binaryObjectIdSupplier.get();
        storage.store(binaryObjectIdToFilePathFunction.apply(entity, t), metadata, inputStream);
        return t;
    }

    public Optional<BinaryObject> retrieve(E entity, T id) {
        return storage.retrieve(binaryObjectIdToFilePathFunction.apply(entity, id));
    }

    public boolean hasBinaryObject(E entity, T id) {
        return storage.hasBinaryObject(binaryObjectIdToFilePathFunction.apply(entity, id));
    }

    public void delete(E entity, T id) {
        storage.delete(binaryObjectIdToFilePathFunction.apply(entity, id));
    }
}
