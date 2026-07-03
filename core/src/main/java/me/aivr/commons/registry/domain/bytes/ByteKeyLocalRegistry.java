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
package me.aivr.commons.registry.domain.bytes;

import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import me.aivr.commons.registry.domain.LocalRegistry;
import org.jspecify.annotations.Nullable;

import java.util.Set;

/**
 * A type of {@link LocalRegistry} that uses {@code byte} primitives as keys for the registry's entries.
 *
 * @param <V> the type of value this registry handles.
 * @since 2.3.0
 */
public interface ByteKeyLocalRegistry<V> extends LocalRegistry<Byte, V> {
  /**
   * Reusable {@link Set} instance used by original deprecated-functions that handles wrapper-types instead of primitives.
   *
   * @since 2.3.0
   */
  Set<Byte> CACHED_SET_FOR_DEPRECATED_FUNCTIONS = Set.of();

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #existsByByteId(byte)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default boolean existsById(final Byte id) {
    return this.existsByByteId(id);
  }

  /**
   * Checks whether there's an entry with the given byte-id for this registry.
   *
   * @param id the entry's unique-identifier.
   * @return {@code true} if exists an entry for that ID, {@code false} otherwise.
   * @see #getByByteId(byte) Entry's value retrieving by {@code byte} ID
   * @since 2.3.0
   */
  default boolean existsByByteId(final byte id) {
    return this.getByByteId(id) != null;
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #getByByteId(byte)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default @Nullable V findById(final Byte id) {
    return this.getByByteId(id);
  }

  /**
   * Returns, if available, the object mapped to the given {@code byte} key.
   *
   * @param id the entry's id.
   * @return the entry's mapped-value, or {@code null} if not exist.
   * @since 2.3.0
   */
  @Nullable V getByByteId(final byte id);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #registerByte(byte, Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default V register(final Byte id, final V value) {
    return this.registerByte(id, value);
  }

  /**
   * Stores the given value with the specified {@code byte} identifier into this registry, and returns whether the given value or the
   * old-mapping already existed for that identifier.
   *
   * @param id the id to assign.
   * @param value the value to store.
   * @return the stored value, or the entry's previous-mapping for the specified id, if existed.
   * @since 2.3.0
   */
  V registerByte(final byte id, final V value);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #findAllByteKeys()} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  @SuppressWarnings("unchecked")
  default <C extends Set<Byte>> C findAllKeys() {
    return (C) CACHED_SET_FOR_DEPRECATED_FUNCTIONS;
  }

  /**
   * Returns a collection with this registry's all byte keys, this function will not perform an action when a value is retrieved.
   *
   * @return the collection of byte keys.
   * @since 2.3.0
   */
  default ByteSet findAllByteKeys() {
    return this.findAllByteKeys(k -> {});
  }

  /**
   * Returns a collection with this registry's all byte keys, and triggers an action by each retrieved value.
   *
   * @return the collection of byte keys.
   * @since 2.3.0
   */
  ByteSet findAllByteKeys(final ByteConsumer postFetchAction);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #unregisterByte(byte)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default @Nullable V unregister(final Byte id) {
    return this.unregisterByte(id);
  }

  /**
   * Removes the value for the specified identifier from this registry, and returns the value if existed.
   *
   * @param id the {@code byte} id.
   * @return the removed value or {@code null} if no mapping was assigned for that identifier.
   * @since 2.3.0
   */
  @Nullable V unregisterByte(final byte id);
}
