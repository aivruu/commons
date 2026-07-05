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

import it.unimi.dsi.fastutil.bytes.ByteCollection;
import it.unimi.dsi.fastutil.bytes.ByteConsumer;
import it.unimi.dsi.fastutil.bytes.BytePredicate;
import me.aivr.commons.registry.domain.LocalRegistry;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A type of {@link LocalRegistry} that uses {@code byte} primitives as mapped-values for the registry's entries.
 *
 * @param <K> the type of key this registry uses.
 * @since 2.3.0
 */
public interface ByteValueLocalRegistry<K> extends LocalRegistry<K, Byte> {
  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #existsByteById(Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default boolean existsById(final K id) {
    return this.existsByteById(id);
  }

  /**
   * Checks whether there's an entry with the given id for this registry.
   *
   * @param id the entry's identifier.
   * @return {@code true} if exists an entry for that ID, {@code false} otherwise.
   * @see #getByteById(Object) Entry's value retrieving by ID
   * @since 2.3.0
   */
  default boolean existsByteById(final K id) {
    return this.getByteById(id) != 0;
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #getByteById(Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default Byte findById(final K id) {
    return this.getByteById(id);
  }

  /**
   * Returns the {@code byte} mapped to the given {@link K} key.
   *
   * @param id the entry's id.
   * @return the entry's mapped-value, or {@code 0} if not exist.
   * @since 2.3.0
   */
  byte getByteById(final K id);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #registerByte(Object, byte)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default Byte register(final K id, final Byte value) {
    return this.registerByte(id, value);
  }

  /**
   * Stores the given {@code byte} with the specified ID into this registry, and returns whether the given value or the
   * old-mapping already existed for that identifier.
   *
   * @param id the id to assign.
   * @param value the value to store.
   * @return the stored value, or the entry's previous-mapping for the specified id, if existed.
   * @since 2.3.0
   */
  byte registerByte(final K id, final byte value);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #findAllBytes()} instead.
   * @since 3.0.0-rc2
   */
  @Override
  @Deprecated
  @SuppressWarnings("unchecked")
  default <C extends Collection<Byte>> C findAllValues() {
    return (C) this.findAllBytes();
  }

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #findAllBytes(ByteConsumer)} instead.
   * @since 3.0.0-rc2
   */
  @Override
  @Deprecated
  @SuppressWarnings("unchecked")
  default <C extends Collection<Byte>> C findAllValues(final Consumer<Byte> postFetchAction) {
    return (C) this.findAllBytes((ByteConsumer) postFetchAction);
  }

  /**
   * Returns a collection with this registry's all byte values, this function will not perform an action when a value is retrieved.
   *
   * @return the collection of byte values.
   * @see #findAllBytes(ByteConsumer) Actual values-collection retrieving
   * @since 2.3.0
   */
  default ByteCollection findAllBytes() {
    return this.findAllBytes(v -> {});
  }

  /**
   * Returns a collection with this registry's all byte values, and triggers an action by each retrieved value.
   *
   * @param postFetchAction the action to execute for each value.
   * @return the collection of byte values.
   * @since 2.3.0
   */
  ByteCollection findAllBytes(final ByteConsumer postFetchAction);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #filterBytes(BytePredicate)} instead.
   * @since 3.0.0-rc2
   */
  @Override
  @Deprecated
  @SuppressWarnings("unchecked")
  default <C extends Collection<Byte>> C filter(final Predicate<Byte> condition) {
    return (C) this.filterBytes((BytePredicate) condition);
  }

  /**
   * Returns a collection with byte values from this registry that meets the condition given.
   *
   * @param condition the condition used to filter by the registry's values.
   * @return the collection of byte values.
   * @since 2.3.0
   */
  ByteCollection filterBytes(final BytePredicate condition);

  /**
   * {@inheritDoc}
   *
   * @deprecated use {@link #unregisterByte(Object)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default Byte unregister(final K id) {
    return this.unregisterByte(id);
  }

  /**
   * Removes the {@code byte} for the given {@link K} id from this registry, and returns the value if available.
   *
   * @param id the id.
   * @return the removed value or {@code 0} if no mapping was assigned for that identifier.
   * @since 2.3.0
   */
  byte unregisterByte(final K id);

  /**
    * {@inheritDoc}
   *
   * @deprecated use {@link #unregisterByteIf(BytePredicate)} instead.
   * @since 2.3.0
   */
  @Override
  @Deprecated
  default int unregisterIf(final Predicate<Byte> filter) {
    return this.unregisterByteIf((BytePredicate) filter);
  }

  /**
   * Removes the values from this registry that meets the {@code byte} filter given for the function, and returns a count of how many
   * values were removed.
   *
   * @param filter the {@link BytePredicate} to use for removal.
   * @return the count of removed values.
   * @since 2.3.0
   */
  int unregisterByteIf(final BytePredicate filter);
}
