package io.github.wimdeblauwe.biob;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface BinaryObjectIdToFilePathFunction<E,T> extends BiFunction<E,T, String> {
}
