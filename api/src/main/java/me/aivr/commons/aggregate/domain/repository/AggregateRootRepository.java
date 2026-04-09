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
package me.aivr.commons.aggregate.domain.repository;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import me.aivr.commons.aggregate.domain.AggregateRoot;
import org.jspecify.annotations.Nullable;

/**
 * Represents a repository (commonly local) that allows to store, retrieve, and delete data related to aggregate-roots.
 *
 * @param <AggregateType> an object that extends from {@link AggregateRoot}, which basically represents itself as an
 *                        aggregate-root.
 * @since 1.0.0
 */
public interface AggregateRootRepository<AggregateType extends AggregateRoot> {
  /**
   * Saves the given aggregate-root's data in this repository.
   *
   * @implNote Some implementations of this repository-type may or not return a plain or computed boolean-result for this
   * operation.
   *
   * @param aggregate the aggregate-root to save.
   * @return {@code true} if the information was saved correctly, {@code false} otherwise.
   * @since 1.0.0
   */
  boolean writeSync(final AggregateType aggregate);

  /**
   * Checks whether an aggregate-root with the given ID is stored by this repository.
   *
   * @param id the id of the aggregate-root.
   * @return {@code true} if so, {@code false} otherwise.
   * @see #findSync(String) Aggregate-root from-repository retrieving.
   * @since 1.0.0
   */
  default boolean existsSync(final String id) {
    return this.findSync(id) != null;
  }

  /**
   * Returns the aggregate-root object stored by this repository for the given id, if found.
   *
   * @param id the id of the aggregate-root.
   * @return the aggregate-root itself if found, otherwise {@code null}.
   * @since 1.0.0
   */
  @Nullable AggregateType findSync(final String id);

  /**
   * Returns all the IDs of the aggregate-roots stored by this repository within the given {@code limit}.
   *
   * @param limit the limit of IDs to retrieve.
   * @param <Identifiers> a generic that represents a string-collection.
   * @return a collection of IDs, or empty if the repository has no entries stored.
   * @since 1.0.0
   */
  <Identifiers extends Collection<String>> Identifiers findAllIdsSync(final IntFunction<Identifiers> limit);

  /**
   * Returns all the aggregate-roots stored by this repository within the given {@code limit}.
   *
   * @param limit the limit of aggregate-root objects to retrieve.
   * @param <Aggregates> a generic that represents an aggregate-root collection.
   * @return a collection of {@link AggregateRoot}s, or empty if the repository has none.
   * @see #findAllSync(Consumer, IntFunction) Main aggregate-roots fetch operation.
   * @since 1.0.0
   */
  default <Aggregates extends Collection<AggregateType>> Aggregates findAllSync(final IntFunction<Aggregates> limit) {
    return this.findAllSync(null, limit);
  }

  /**
   * Returns all the aggregate-roots stored by this repository within the given {@code limit}.
   * <p>
   * By each aggregate-root fetched, the {@code postFetchAction} will be executed for it, if it was configured.
   *
   * @param postFetchAction the action to perform after an aggregate-root was retrieved.
   * @param limit the limit of aggregate-root objects to retrieve.
   * @param <Aggregates> a generic that represents an aggregate-root collection.
   * @return a collection of {@link AggregateRoot}s, or empty if the repository has none.
   * @since 1.0.0
   */
  <Aggregates extends Collection<AggregateType>> Aggregates findAllSync(
      final @Nullable Consumer<AggregateType> postFetchAction,
      final IntFunction<Aggregates> limit);

  /**
   * Deletes the specified aggregate-root and returns a boolean-result for the operation.
   *
   * @param id the id of the aggregate-root.
   * @return {@code true} if the aggregate-root existed and was deleted, otherwise {@code false}.
   * @see #deleteAndRetrieveSync(String) Aggregate-root removal and object retrieving.
   * @since 1.0.0
   */
  default boolean deleteSync(final String id) {
    return this.deleteAndRetrieveSync(id) != null;
  }

  /**
   * Deletes the specified aggregate-root and returns its object if available.
   *
   * @param id the id of the aggregate-root.
   * @return the aggregate-root's object if existed, otherwise {@code null}.
   * @since 1.0.0
   */
  @Nullable AggregateType deleteAndRetrieveSync(final String id);

  /**
   * Deletes all the aggregate-roots stored by this repository.
   *
   * @since 1.0.0
   */
  void deleteAllSync();
}
