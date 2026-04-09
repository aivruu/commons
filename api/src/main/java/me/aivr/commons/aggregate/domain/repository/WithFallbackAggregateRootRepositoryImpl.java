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
 * The base-implementation of {@link WithFallbackAggregateRootRepository} that implements logic for async-functions
 * declared in the contract, as well, common-logic for future implementations.
 * <p>
 * Operations that may return {@code boolean} results encapsulated by {@link CompletableFuture} objects will use as function
 * return-value results for main-repository operations, skipping ones for fallback-repository operations.
 *
 * @param <AggregateType> an object that extends from AggregateRoot, which basically represents itself as an aggregate-root.
 * @since 1.0.0.
 */
public abstract class WithFallbackAggregateRootRepositoryImpl<AggregateType extends AggregateRoot>
    implements WithFallbackAggregateRootRepository<AggregateType> {
  private final AsyncAggregateRootRepository<AggregateType> mainRepository;
  private final AggregateRootRepository<AggregateType> fallbackRepository;

  protected WithFallbackAggregateRootRepositoryImpl(
      final AsyncAggregateRootRepository<AggregateType> mainRepository,
      final AggregateRootRepository<AggregateType> fallbackRepository) {
    this.mainRepository = mainRepository;
    this.fallbackRepository = fallbackRepository;
  }

  @Override
  public AggregateRootRepository<AggregateType> fallbackRepository() {
    return this.fallbackRepository;
  }

  @Override
  public AsyncAggregateRootRepository<AggregateType> mainRepository() {
    return this.mainRepository;
  }

  @Override
  public <C extends Collection<String>> CompletableFuture<C> findAllIdsInMainAsync(final IntFunction<C> limit) {
    return this.mainRepository.findAllIdsAsync(limit);
  }

  @Override
  public <C extends Collection<String>> C findAllIdsInFallbackAsync(final IntFunction<C> limit) {
    return this.fallbackRepository.findAllIdsSync(limit);
  }

  @Override
  public <C extends Collection<AggregateType>> C findAllAggregatesInFallbackSync(final IntFunction<C> limit) {
    return this.fallbackRepository.findAllSync(limit);
  }

  @Override
  public CompletableFuture<@Nullable AggregateType> findInBothAsync(final String id) {
    final AggregateType currentLocal = this.fallbackRepository.findSync(id);
    return currentLocal != null ? CompletableFuture.completedFuture(currentLocal) : this.mainRepository.findAsync(id);
  }

  @Override
  public CompletableFuture<Boolean> writeInBothAsync(final AggregateType aggregate) {
    this.fallbackRepository.writeSync(aggregate);
    return this.mainRepository.writeAsync(aggregate);
  }

  @Override
  public CompletableFuture<Boolean> existsInBothAsync(final String id) {
    return this.fallbackRepository.existsSync(id) ? CompletableFuture.completedFuture(true) : this.mainRepository.existsAsync(id);
  }

  @Override
  public CompletableFuture<Boolean> deleteFromBothAsync(final String id) {
    this.fallbackRepository.deleteSync(id); // ignore result here
    return this.mainRepository.deleteAsync(id);
  }

  @Override
  public @Nullable AggregateType findInFallbackSync(final String id) {
    return this.fallbackRepository.findSync(id);
  }

  @Override
  public boolean existsInFallbackSync(final String id) {
    return this.fallbackRepository.existsSync(id);
  }

  @Override
  public boolean writeToFallbackSync(final AggregateType aggregate) {
    return this.fallbackRepository.writeSync(aggregate);
  }

  @Override
  public boolean deleteFromFallbackSync(final String id) {
    return this.fallbackRepository.deleteSync(id);
  }

  @Override
  public CompletableFuture<@Nullable AggregateType> findInMainAsync(final String id) {
    return this.mainRepository.findAsync(id);
  }

  @Override
  public CompletableFuture<Boolean> existsInMainAsync(final String id) {
    return this.mainRepository.existsAsync(id);
  }

  @Override
  public CompletableFuture<Boolean> writeToMainAsync(final AggregateType aggregate) {
    return this.mainRepository.writeAsync(aggregate);
  }

  @Override
  public CompletableFuture<Boolean> deleteFromMainAsync(final String id) {
    return this.mainRepository.deleteAsync(id);
  }

  @Override
  public void deleteAllInFallbackSync() {
    this.fallbackRepository.deleteAllSync();
  }

  @Override
  public CompletableFuture<Void> deleteAllInMainAsync() {
    return this.mainRepository.deleteAllAsync();
  }

  @Override
  public CompletableFuture<Void> deleteAllInBothAsync() {
    this.fallbackRepository.deleteAllSync();
    return this.mainRepository.deleteAllAsync();
  }
}
