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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.IntFunction;
import me.aivr.commons.aggregate.domain.AggregateRoot;
import org.jspecify.annotations.Nullable;

/**
 * An extension of {@link AggregateRootRepository} that allows to perform repository-related operations asynchronously over
 * aggregate-roots.
 *
 * @param <AggregateType> an object that extends from AggregateRoot, which basically represents itself as an aggregate-root.
 * @since 1.0.0
 */
public interface AsyncAggregateRootRepository<AggregateType extends AggregateRoot> extends AggregateRootRepository<AggregateType> {
  /**
   * Executes any configured startup-logic for this repository.
   *
   * @since 1.0.0
   */
  void start();

  /**
   * Returns the executor, or thread-pool, used for this repository's async operations.
   *
   * @return this repository's executor for async-operations.
   * @since 1.0.0
   */
  Executor executor();

  /**
   * Saves the given aggregate-root's data asynchronously in this repository.
   *
   * @param aggregate the aggregate-root to save.
   * @return {@code true} if the aggregate-root was saved correctly, otherwise {@code false}, the result is encapsulated and
   * then returned by a {@link CompletableFuture}.
   * @since 1.0.0
   */
  CompletableFuture<Boolean> writeAsync(final AggregateType aggregate);

  /**
   * Checks asynchronously whether exists an aggregate-root with the specified ID in this repository.
   *
   * @param id the id of the aggregate-root.
   * @return {@code true} if it exists, {@code false} otherwise, the result is encapsulated and then returned by a
   * {@link CompletableFuture}.
   * @since 1.0.0
   */
  CompletableFuture<Boolean> existsAsync(final String id);

  /**
   * Returns the aggregate-root object stored by this repository asynchronously for the given id, if found.
   *
   * @param id the id of the aggregate-root.
   * @return the aggregate-root itself, or {@code null} if it doesn't exist, the result is encapsulated and then returned by a
   * {@link CompletableFuture}.
   * @since 1.0.0.
   */
  CompletableFuture<@Nullable AggregateType> findAsync(final String id);

  /**
   * Returns all the IDs of the aggregate-roots stored by this repository asynchronously within the given {@code limit}.
   *
   * @param limit the limit of IDs to retrieve.
   * @param <Identifiers> a generic that represents a string-collection.
   * @return a collection of IDs, or empty if the repository has no information stored, the result is encapsulated and then
   * returned by a {@link CompletableFuture}.
   * @since 1.0.0
   */
  <Identifiers extends Collection<String>> CompletableFuture<Identifiers> findAllIdsAsync(final IntFunction<Identifiers> limit);

  /**
   * Deletes the specified aggregate-root asynchronously and returns a boolean-result for the operation.
   *
   * @param id the id of the aggregate-root.
   * @return {@code true} if the aggregate-root existed and was deleted, otherwise {@code false}, the result is encapsulated
   * and then returned by a {@link CompletableFuture}.
   * @since 1.0.0
   */
  CompletableFuture<Boolean> deleteAsync(final String id);

  /**
   * Deletes asynchronously all the aggregate-roots stored by this repository.
   *
   * @return a {@link CompletableFuture} that allows to compute and handle results for the async-operation.
   * @since 1.0.0
   */
  CompletableFuture<Void> deleteAllAsync();
}
