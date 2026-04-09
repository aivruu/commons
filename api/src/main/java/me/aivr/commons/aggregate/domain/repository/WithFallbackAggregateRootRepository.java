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
import java.util.function.IntFunction;
import me.aivr.commons.aggregate.domain.AggregateRoot;
import org.jspecify.annotations.Nullable;

/**
 * Represents a repository-type that allows to handle sync and async repository-related operations on a single one,
 * providing functionality to work with both repositories easily.
 * <p>
 * This repository is primary represented by two other repositories, a {@link AggregateRootRepository} and a
 * {@link AsyncAggregateRootRepository}, the first one is used as a local/fallback repository that is used as the primary
 * in terms of reading-operations for the aggregate-roots, the second one is used as the main/persistence repository, as it
 * says, it is used for the persistence of the aggregate-roots data, as well serving as secondary reading-point to allow
 * data-reading from this one if the {@link #fallbackRepository()} requires it.
 *
 * @param <AggregateType> an object that extends from AggregateRoot, which basically represents itself as an aggregate-root.
 * @since 1.0.0
 */
public interface WithFallbackAggregateRootRepository<AggregateType extends AggregateRoot> {
  /**
   * Returns the repository used as local/fallback storage.
   *
   * @return the fallback-repository.
   * @since 1.0.0
   */
  AggregateRootRepository<AggregateType> fallbackRepository();

  /**
   * Returns the repository used as main-storage.
   *
   * @return the main-repository.
   * @since 1.0.0
   */
  AsyncAggregateRootRepository<AggregateType> mainRepository();

  /**
   * Calls to the {@link #mainRepository()} indicating to return a collection with the IDs of the aggregate-roots
   * stored by that repository.
   *
   * @param limit the limit of IDs to retrieve.
   * @param <C> a generic that represents a string-collection.
   * @return a {@link CompletableFuture} that contains the actual result of the operation, check the repository's function
   * to know what other results can be returned.
   * @see AsyncAggregateRootRepository#findAllIdsAsync(IntFunction) Main-repository IDs collection retrieving
   * @since 1.0.0
   */
  <C extends Collection<String>> CompletableFuture<C> findAllIdsInMainAsync(final IntFunction<C> limit);

  /**
   * Calls to the {@link #fallbackRepository()} indicating to return a collection with the IDs of the aggregate-roots
   * stored by that repository.
   *
   * @param limit the limit of IDs to retrieve.
   * @param <C> a generic that represents a string-collection.
   * @return the collection of IDs requested, check the repository's function to know what other results can be returned.
   * @see AggregateRootRepository#findAllIdsSync(IntFunction) Fallback-repository IDs collection retrieving
   * @since 1.0.0
   */
  <C extends Collection<String>> C findAllIdsInFallbackAsync(final IntFunction<C> limit);

  /**
   * Calls to the {@link #fallbackRepository()} indicating to return a collection with the the {@link AggregateRoot}
   * objects stored by that repository.
   *
   * @param limit the limit of aggregate-roots to retrieve.
   * @param <C> a generic that represents an aggregate-root collection.
   * @return the collection of aggregate-roots requested, check the repository's function to know what other results can be returned.
   * @see AggregateRootRepository#findAllSync(IntFunction) Fallback-repository aggregate-roots collection retrieving
   * @since 1.0.0
   */
  <C extends Collection<AggregateType>> C findAllAggregatesInFallbackSync(final IntFunction<C> limit);

  /**
   * Calls to the {@link #fallbackRepository()} indicating to find and return a {@link AggregateType} object for the
   * specified id.
   * <p>
   * Otherwise, in case no aggregate-root was found for the fallback-repository, the {@link #mainRepository()} is called
   * instead indicating to find and return the aggregate-root for the specified id.
   *
   * @param id the id of the aggregate-root.
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check both repositories' functions
   * to know what results can be expected.
   * @see AggregateRootRepository#findSync(String) Fallback-repository reading operation
   * @see AsyncAggregateRootRepository#findAsync(String) Main-repository reading operation
   * @since 1.0.0
   */
  CompletableFuture<@Nullable AggregateType> findInBothAsync(final String id);

  /**
   * Calls to both {@link #fallbackRepository()} and {@link #mainRepository()} indicating to save within themselves the
   * data for the given aggregate-root for this function.
   *
   * @param aggregate the aggregate-root to save.
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check both repositories' functions
   * to know what results should be expected for this case.
   * @see AggregateRootRepository#writeSync(AggregateRoot) Fallback-repository writing operation
   * @see AsyncAggregateRootRepository#writeAsync(AggregateRoot) Main-repository writing operation
   * @since 1.0.0
   */
  CompletableFuture<Boolean> writeInBothAsync(final AggregateType aggregate);

  /**
   * Calls to the {@link #fallbackRepository()} indicating to check if there's an aggregate-root with the given id.
   * <p>
   * Otherwise, if the operation returned {@code false}, the {@link #mainRepository()} is called instead indicating to
   * check by existing of the aggregate-root within its entries.
   *
   * @param id the id of the aggregate-root.
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check both repositories' functions
   * to know what results can be returned for the operation.
   * @see AggregateRootRepository#existsSync(String) Fallback-repository existence-check operation
   * @see AsyncAggregateRootRepository#existsAsync(String) Main-repository existence-check operation
   * @since 1.0.0
   */
  CompletableFuture<Boolean> existsInBothAsync(final String id);

  /**
   * Calls to both {@link #fallbackRepository()} and {@link #mainRepository()} indicating to delete data-related to the
   * aggregate-root specified by the given id.
   *
   * @param id the id of the aggregate-root.
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check both repositories' functions
   * to know what results can be returned for the operation.
   * @see AggregateRootRepository#deleteSync(String) Fallback-repository delete operation
   * @see AsyncAggregateRootRepository#deleteAsync(String) Main-repository delete operation
   * @since 1.0.0
   */
  CompletableFuture<Boolean> deleteFromBothAsync(final String id);

  /**
   * Calls to the {@link #fallbackRepository()} indicating to find and return an aggregate-root for the specified id
   * for this function.
   *
   * @param id the id of the aggregate-root.
   * @return the aggregate-root if found, otherwise {@code null}, check the repository's function to know what other results
   * can be expected.
   * @see AggregateRootRepository#findSync(String) Fallback-repository reading operation
   * @since 1.0.0
   */
  @Nullable AggregateType findInFallbackSync(final String id);

  /**
   * Calls to the {@link #fallbackRepository()} indicating to check if exists an aggregate-root with the specified id.
   *
   * @param id the id of the aggregate-root.
   * @return {@code true} if the aggregate-root exists, {@code false} otherwise.
   * @see AggregateRootRepository#existsSync(String) Fallback-repository existence-check operation
   * @since 1.0.0
   */
  boolean existsInFallbackSync(final String id);

  /**
   * Calls to the {@link #fallbackRepository()} indicating to save the given aggregate-root's data.
   *
   * @param aggregate the aggregate-root to save.
   * @return {@code true} if the aggregate-root was saved correctly, {@code false} otherwise.
   * @see AggregateRootRepository#writeSync(AggregateRoot) Fallback-repository writing operation
   * @since 1.0.0
   */
  boolean writeToFallbackSync(final AggregateType aggregate);

  /**
   * Calls to the {@link #fallbackRepository()} indicating to delete the aggregate-root for the specified id.
   *
   * @param id the id of the aggregate-root.
   * @return {@code true} if the aggregate-root existed and was deleted, {@code false} otherwise.
   * @see AggregateRootRepository#deleteSync(String) Fallback-repository delete operation
   * @since 1.0.0
   */
  boolean deleteFromFallbackSync(final String id);

  /**
   * Calls to the {@link #mainRepository()} indicating to find and return the aggregate-root for the specified id,
   * if found.
   *
   * @param id the id of the aggregate-root.
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check the repository's function
   * to know what results can be returned for the operation.
   * @see AsyncAggregateRootRepository#findAsync(String) Main-repository reading operation
   * @since 1.0.0
   */
  CompletableFuture<@Nullable AggregateType> findInMainAsync(final String id);

  /**
   * Calls to the {@link #mainRepository()} indicating to check if an aggregate-root with the given id exists in that
   * repository.
   *
   * @param id the id of the aggregate-root.
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check the repository's function
   * to know what results can be returned for the operation.
   * @see AsyncAggregateRootRepository#existsAsync(String) Main-repository existence-check operation
   * @since 1.0.0
   */
  CompletableFuture<Boolean> existsInMainAsync(final String id);

  /**
   * Calls to the {@link #mainRepository()} indicating to save the given aggregate-root's data within the repository.
   *
   * @param aggregate the aggregate-root to save.
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check the repository's function
   * to know what results can be returned for the operation.
   * @see AsyncAggregateRootRepository#writeAsync(AggregateRoot) Main-repository writing operation
   * @since 1.0.0
   */
  CompletableFuture<Boolean> writeToMainAsync(final AggregateType aggregate);

  /**
   * Calls to the {@link #mainRepository()} indicating to delete the aggregate-root with the specified id.
   *
   * @param id the id of the aggregate-root.
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check the repository's function
   * to know what results can be returned for the operation.
   * @see AsyncAggregateRootRepository#deleteAsync(String) Main-repository delete operation
   * @since 1.0.0
   */
  CompletableFuture<Boolean> deleteFromMainAsync(final String id);

  /**
   * Calls to the {@link #fallbackRepository()} indicating to delete all the aggregate-roots stored by that repository.
   *
   * @see AggregateRootRepository#deleteAllSync() Fallback-repository clear operation
   * @since 1.0.0
   */
  void deleteAllInFallbackSync();

  /**
   * Calls to the {@link #mainRepository()} indicating to delete all the aggregate-roots stored by that repository.
   *
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check the repository's function
   * to know what results can be returned for the operation.
   * @see AsyncAggregateRootRepository#deleteAllAsync() Main-repository clear operation
   * @since 1.0.0
   */
  CompletableFuture<Void> deleteAllInMainAsync();

  /**
   * Calls both {@link #fallbackRepository()} and {@link #mainRepository()} indicating to delete all the aggregate-roots
   * stored by these repositories.
   *
   * @return a {@link CompletableFuture} that contains the actual result for the operation, check repositories' functions
   * to know what results can be returned for the operation.
   * @see AggregateRootRepository#deleteAllSync() Fallback-repository clear operation
   * @see AsyncAggregateRootRepository#deleteAllAsync() Main-repository clear operation
   * @since 1.0.0
   */
  CompletableFuture<Void> deleteAllInBothAsync();
}
