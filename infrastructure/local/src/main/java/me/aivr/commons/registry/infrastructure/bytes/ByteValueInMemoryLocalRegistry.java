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
import it.unimi.dsi.fastutil.objects.Object2ByteFunction;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteMaps;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import me.aivr.commons.registry.domain.bytes.ByteValueLocalRegistry;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * A registry-type that allows to use {@code byte} primitives as values for registry's entries to avoid unboxing/autoboxing
 * operations.
 *
 * @param <K> the type of id this registry uses.
 * @since 3.0.0-rc2
 */
public final class ByteValueInMemoryLocalRegistry<K> implements ByteValueLocalRegistry<K> {
  private final Object2ByteMap<K> cache;

  /**
   * Creates a new {@link ByteValueInMemoryLocalRegistry} with the provided information.
   * <p>
   * This registry will have a pre-defined expected initial-size established by {@link Object2ByteOpenHashMap#DEFAULT_INITIAL_SIZE}.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @since 3.0.0-rc2
   */
  public ByteValueInMemoryLocalRegistry(final boolean threadSafe) {
    this(threadSafe, Object2ByteOpenHashMap.DEFAULT_INITIAL_SIZE);
  }

  /**
   * Creates a new {@link ByteValueInMemoryLocalRegistry} with the provided information.
   *
   * @param threadSafe whether the registry must be safe for use between multiple threads.
   * @param expectedSize the initial-size that the registry is expected to have.
   * @since 3.0.0-rc2
   */
  public ByteValueInMemoryLocalRegistry(final boolean threadSafe, final int expectedSize) {
    this(threadSafe ? new Object2ByteOpenHashMap<>(expectedSize) : Object2ByteMaps.synchronize(new Object2ByteOpenHashMap<>(expectedSize)));
  }

  /**
   * Creates a new {@link ByteValueInMemoryLocalRegistry} with the provided parameter.
   *
   * @param cache the {@link Object2ByteMap} instance to use for cache-handling.
   * @since 3.0.0-rc2
   */
  public ByteValueInMemoryLocalRegistry(final Object2ByteMap<K> cache) {
    this.cache = cache;
  }

  @Override
  public byte getByteById(final K id) {
    return this.cache.getByte(id);
  }

  @Override
  public byte registerByte(final K id, final byte value) {
    final byte stored = this.cache.put(id, value);
    return stored == this.cache.defaultReturnValue() ? value : stored;
  }

  @Override
  public byte registerByteIfAbsent(final K id, final Object2ByteFunction<K> mappingValueFunc) {
    return this.cache.computeIfAbsent(id, mappingValueFunc);
  }

  @Override
  public byte registerByteIfPresent(final K id, final BiFunction<? super K, ? super Byte, ? extends Byte> mappingValueFunc) {
    return this.cache.computeByteIfPresent(id, mappingValueFunc);
  }

  @Override
  public ByteCollection findAllBytes(final ByteConsumer postFetchAction) {
    final ByteCollection registryValues = this.cache.values();
    final ByteCollection values = new ByteArrayList(registryValues.size());
    for (final byte value : registryValues) {
      values.add(value);
      postFetchAction.accept(value);
    }
    return values;
  }

  @Override
  public ByteCollection filterBytes(final BytePredicate condition) {
    final ByteCollection registryValues = this.cache.values();
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
    return this.cache.removeByte(id);
  }

  @Override
  public int unregisterByteIf(final BytePredicate filter) {
    int count = 0;
    for (final Object2ByteMap.Entry<K> entry : this.cache.object2ByteEntrySet()) {
      if (filter.test(entry.getByteValue())) {
        this.cache.removeByte(entry.getKey());
        ++count;
      }
    }
    return count;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <M extends Map<K, Byte>> M raw() {
    return (M) this.cache;
  }
}
