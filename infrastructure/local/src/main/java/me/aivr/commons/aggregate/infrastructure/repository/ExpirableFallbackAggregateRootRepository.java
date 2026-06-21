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
package me.aivr.commons.aggregate.infrastructure.repository;

import com.github.benmanes.caffeine.cache.Cache;
import me.aivr.commons.aggregate.domain.AggregateRoot;
import me.aivr.commons.aggregate.domain.repository.AggregateRootRepository;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * Common-implementation for local-repositories that allows to store expirable-entries from configured Caffeine's {@link Cache}
 * instances.
 *
 * @param <AggregateType> an object that extends from AggregateRoot, which basically represents itself as an aggregate-root.
 * @since 1.0.0
 */
public class ExpirableFallbackAggregateRootRepository<AggregateType extends AggregateRoot>
    implements AggregateRootRepository<AggregateType> {
  protected final Cache<String, AggregateType> cache;

  /**
   * Creates a new {@link ExpirableFallbackAggregateRootRepository} with the provided parameter.
   *
   * @param cache the {@link Cache} instance to use for cache-handling.
   * @since 1.0.0
   */
  public ExpirableFallbackAggregateRootRepository(final Cache<String, AggregateType> cache) {
    this.cache = cache;
  }

  @Override
  public boolean writeSync(final AggregateType aggregate) {
    this.cache.put(aggregate.id(), aggregate);
    return true;
  }

  @Override
  public @Nullable AggregateType findSync(final String id) {
    return this.cache.getIfPresent(id);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Identifiers extends Collection<String>> Identifiers findAllIdsSync(final IntFunction<Identifiers> limit) {
    final Identifiers cachedIds = (Identifiers) this.cache.asMap().keySet();
    final Identifiers ids = limit.apply(cachedIds.size());
    ids.addAll(cachedIds);
    return ids;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Aggregates extends Collection<AggregateType>> Aggregates findAllSync(
      final @Nullable Consumer<AggregateType> postFetchAction,
      final IntFunction<Aggregates> limit) {
    final Aggregates cachedValues = (Aggregates) this.cache.asMap().values();
    final Aggregates aggregateRoots = limit.apply(cachedValues.size());
    aggregateRoots.addAll(cachedValues);
    return aggregateRoots;
  }

  @Override
  public @Nullable AggregateType deleteAndRetrieveSync(final String id) {
    final AggregateType current = this.cache.getIfPresent(id);
    if (current != null) {
      this.cache.invalidate(id);
    }
    return current;
  }

  @Override
  public void deleteAllSync() {
    this.cache.invalidateAll();
  }
}
