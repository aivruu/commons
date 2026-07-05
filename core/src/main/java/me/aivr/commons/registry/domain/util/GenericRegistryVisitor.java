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
package me.aivr.commons.registry.domain.util;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.aivr.commons.util.domain.exception.ExceptionConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Provides utility-functions to iterate and retrieve information from collections.
 * <p>
 * Some of this class's functions uses FastUtil's implementations when returning new collections.
 *
 * @since 3.0.0-rc2-rc2
 */
public final class GenericRegistryVisitor {
  private GenericRegistryVisitor() {
    throw ExceptionConstants.NOT_INSTANTIABLE_EXCEPTION;
  }

  /**
   * Iterates the provided collection and copies all its information to a new collection about the same size, this
   * function will trigger the {@code postFetchAction} parameter for each element of the collection provided.
   * <p>
   * This function returns a {@link ObjectOpenHashSet} instance.
   *
   * @param <C> a type of {@link Set}.
   * @param providerCollection the collection to iterate.
   * @param postFetchAction the action to execute for every element.
   * @return a new {@link ObjectOpenHashSet}.
   * @since 3.0.0-rc2
   */
  @SuppressWarnings("unchecked")
  public static <T, C extends Set<T>> C visitKeys(final C providerCollection, final Consumer<T> postFetchAction) {
    final C keys = (C) new ObjectOpenHashSet<>(providerCollection.size());
    for (final T key : providerCollection) {
      keys.add(key);
      postFetchAction.accept(key);
    }
    return keys;
  }

  /**
   * Iterates the provided collection and copies all its information to a new collection about the same size, this
   * function will trigger the {@code postFetchAction} parameter for each element of the collection provided.
   *
   * @param <C> a type of {@link Collection}.
   * @param providerCollection the collection to iterate.
   * @param postFetchAction the action to execute for every element.
   * @return a new {@link ArrayList}.
   * @since 3.0.0-rc2
   */
  @SuppressWarnings("unchecked")
  public static <T, C extends Collection<T>> C visitValues(final C providerCollection, final Consumer<T> postFetchAction) {
    final C values = (C) new ArrayList<>(providerCollection.size());
    for (final T value : providerCollection) {
      values.add(value);
      postFetchAction.accept(value);
    }
    return values;
  }

  /**
   * Iterates the provided collection and copies all the elements that meets the provided {@code condition}
   * parameter.
   *
   * @param <C> a type of {@link Collection}.
   * @param providerCollection the collection to iterate.
   * @param condition the condition to filter by for elements.
   * @return a new {@link ArrayList}.
   * @since 3.0.0-rc2
   */
  @SuppressWarnings("unchecked")
  public static <T, C extends Collection<T>> C filter(final C providerCollection, final Predicate<T> condition) {
    final C values = (C) new ArrayList<>(providerCollection.size());
    for (final T value : providerCollection) {
      if (condition.test(value)) {
        values.add(value);
      }
    }
    return values;
  }
}
