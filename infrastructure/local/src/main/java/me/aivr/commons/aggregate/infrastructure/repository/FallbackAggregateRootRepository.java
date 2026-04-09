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

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import me.aivr.commons.aggregate.domain.AggregateRoot;
import me.aivr.commons.aggregate.domain.repository.AggregateRootRepository;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Common-implementation for local-repositories that allows to store entries "permanently" and delete them whenever it's
 * required.
 *
 * @param <AggregateType> an object that extends from AggregateRoot, which basically represents itself as an aggregate-root.
 * @since 1.0.0
 */
@NullMarked
public class FallbackAggregateRootRepository<AggregateType extends AggregateRoot> implements AggregateRootRepository<AggregateType> {
  protected final Object2ObjectMap<String, AggregateType> cache;

  public FallbackAggregateRootRepository(final Object2ObjectMap<String, AggregateType> cache) {
    this.cache = cache;
  }

  @Override
  public boolean writeSync(final AggregateType aggregate) {
    this.cache.put(aggregate.id(), aggregate);
    return true;
  }

  @Override
  public @Nullable AggregateType findSync(final String id) {
    return this.cache.get(id);
  }

  @Override
  public <Identifiers extends Collection<String>> Identifiers findAllIdsSync(final IntFunction<Identifiers> limit) {
    final Set<String> keySet = this.cache.keySet();
    final Identifiers ids = limit.apply(keySet.size());
    ids.addAll(keySet);
    return ids;
  }

  @Override
  public <Aggregates extends Collection<AggregateType>> Aggregates findAllSync(
      final @Nullable Consumer<AggregateType> postFetchAction,
      final IntFunction<Aggregates> limit) {
    final Collection<AggregateType> values = this.cache.values();
    final Aggregates aggregateRoots = limit.apply(values.size());
    aggregateRoots.addAll(values);
    return aggregateRoots;
  }

  @Override
  public @Nullable AggregateType deleteAndRetrieveSync(final String id) {
    return this.cache.remove(id);
  }

  @Override
  public void deleteAllSync() {
    this.cache.clear();
  }
}
