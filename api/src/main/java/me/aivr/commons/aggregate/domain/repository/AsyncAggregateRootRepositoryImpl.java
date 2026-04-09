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
import java.util.function.Consumer;
import java.util.function.IntFunction;
import me.aivr.commons.aggregate.domain.AggregateRoot;
import org.jspecify.annotations.Nullable;

/**
 * The base-implementation of {@link AsyncAggregateRootRepository} that implement logic for async-functions declared in
 * the contract, as well, common-logic for future implementations.
 * <p>
 * Implemented-functions in this class are mostly simple wrappers that serves async-calls over the sync-functions declared
 * by the {@link AggregateRootRepository} contract.
 *
 * @param <AggregateType> an object that extends from AggregateRoot, which basically represents itself as an aggregate-root.
 * @since 1.0.0
 */
public abstract class AsyncAggregateRootRepositoryImpl<AggregateType extends AggregateRoot>
    implements AsyncAggregateRootRepository<AggregateType> {
  protected final Executor executor;

  protected AsyncAggregateRootRepositoryImpl(final Executor executor) {
    this.executor = executor;
  }

  @Override
  public final Executor executor() {
    return this.executor;
  }

  @Override
  public final CompletableFuture<Boolean> writeAsync(final AggregateType aggregate) {
    return CompletableFuture.supplyAsync(() -> aggregate.shouldSave() && this.writeSync(aggregate), this.executor);
  }

  @Override
  public final CompletableFuture<Boolean> existsAsync(final String id) {
    return CompletableFuture.supplyAsync(() -> this.existsSync(id), this.executor);
  }

  @Override
  public final CompletableFuture<@Nullable AggregateType> findAsync(final String id) {
    return CompletableFuture.supplyAsync(() -> this.findSync(id), this.executor);
  }

  @Override
  public final <Identifiers extends Collection<String>> CompletableFuture<Identifiers> findAllIdsAsync(
      final IntFunction<Identifiers> limit) {
    return CompletableFuture.supplyAsync(() -> this.findAllIdsSync(limit), this.executor);
  }

  @Override
  public <Aggregates extends Collection<AggregateType>> Aggregates findAllSync(
      final @Nullable Consumer<AggregateType> postFetchAction,
      final IntFunction<Aggregates> limit) {
    throw new UnsupportedOperationException("Unsupported operation #findAllSync() for AsyncAggregateRootRepository.");
  }

  @Override
  public @Nullable AggregateType deleteAndRetrieveSync(final String id) {
    throw new UnsupportedOperationException("Unsupported operation #deleteAndRetrieveSync() for AsyncAggregateRootRepository.");
  }

  @Override
  public final CompletableFuture<Boolean> deleteAsync(final String id) {
    return CompletableFuture.supplyAsync(() -> this.deleteSync(id), this.executor);
  }

  @Override
  public final CompletableFuture<Void> deleteAllAsync() {
    return CompletableFuture.runAsync(this::deleteAllSync, this.executor);
  }
}
