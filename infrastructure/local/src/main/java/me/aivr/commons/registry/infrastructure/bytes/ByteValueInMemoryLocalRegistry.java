// This file is part of "commons", licensed under the GNU License.
//
// Copyright (c) 2026 aivruu
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <https://www.gnu.org/licenses/>.
package me.aivr.commons.registry.infrastructure.bytes;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMaps;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import me.aivr.commons.registry.domain.bytes.ByteValueLocalRegistry;
import me.aivr.commons.registry.infrastructure.AbstractInMemoryLocalRegistry;
import org.jspecify.annotations.NullMarked;

/**
 * A registry-type that allows to use {@code byte} primitives as values for registry's entries to avoid unboxing/autoboxing
 * operations.
 *
 * @param <K> the type of id this registry uses.
 * @since 2.3.0
 */
@NullMarked
public final class ByteValueInMemoryLocalRegistry<K> extends AbstractInMemoryLocalRegistry<K, Byte, Object2ByteMap<K>>
    implements ByteValueLocalRegistry<K> {
  /**
   * Creates a new {@link ByteValueInMemoryLocalRegistry} with the provided information.
   * <p>
   * This registry will have a pre-defined expected initial-size established by {@link Object2ByteOpenHashMap#DEFAULT_INITIAL_SIZE}.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @since 2.3.0
   */
  public ByteValueInMemoryLocalRegistry(final boolean threadSafe) {
    this(threadSafe, Object2ByteOpenHashMap.DEFAULT_INITIAL_SIZE);
  }

  /**
   * Creates a new {@link ByteValueInMemoryLocalRegistry} with the provided information.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @param expectedSize the initial-size that the registry is expected to have.
   * @since 2.3.0
   */
  public ByteValueInMemoryLocalRegistry(final boolean threadSafe, final int expectedSize) {
    this(threadSafe ? new Object2ByteOpenHashMap<>(expectedSize) : Object2ByteMaps.synchronize(new Object2ByteOpenHashMap<>(expectedSize)));
  }

  /**
   * Creates a new {@link ByteValueInMemoryLocalRegistry} with the provided parameter.
   *
   * @param cache the {@link Object2ByteMap} instance to use for cache-handling.
   * @since 2.3.0
   */
  public ByteValueInMemoryLocalRegistry(final Object2ByteMap<K> cache) {
    super(cache);
  }

  @Override
  public byte getByteById(final K id) {
    return super.cache.getByte(id);
  }

  @Override
  public byte registerByte(final K id, final byte value) {
    final byte stored = super.cache.put(id, value);
    return stored == super.cache.defaultReturnValue() ? value : stored;
  }

  @Override
  public ByteCollection findAllBytes(final ByteConsumer postFetchAction) {
    final ByteCollection registryValues = super.cache.values();
    final ByteCollection values = new ByteArrayList(registryValues.size());
    values.addAll(registryValues);
    return values;
  }

  @Override
  public ByteCollection filterBytes(final BytePredicate condition) {
    final ByteCollection registryValues = super.cache.values();
    final ByteCollection values = new ByteArrayList(registryValues.size());
    for (final byte value : registryValues) {
      if (condition.test(value)) {
        values.add(value);
      }
    }
    return values;
  }

  @Override
  public byte unregisterByte(final K id) {
    return super.cache.removeByte(id);
  }

  @Override
  public int unregisterByteIf(final BytePredicate filter) {
    int count = 0;
    for (final Object2ByteMap.Entry<K> entry : super.cache.object2ByteEntrySet()) {
      if (filter.test(entry.getByteValue())) {
        super.cache.removeByte(entry.getKey());
        ++count;
      }
    }
    return count;
  }
}
