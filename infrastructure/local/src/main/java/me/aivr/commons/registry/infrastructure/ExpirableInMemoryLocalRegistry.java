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
package me.aivr.commons.registry.infrastructure;

import com.github.benmanes.caffeine.cache.Cache;
import me.aivr.commons.registry.domain.LocalRegistry;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

/**
 * A {@link LocalRegistry} implementation that handles data storing/accessing which uses expirable-entries.
 *
 * @param <K> the type of id this registry uses.
 * @param <V> the type of value this registry handles.
 * @since 2.3.0
 */
public final class ExpirableInMemoryLocalRegistry<K, V> extends AbstractInMemoryLocalRegistry<K, V, ConcurrentMap<K, V>> {
  /**
   * The object to use for cache-handling within this specific registry-type.
   *
   * @since 2.3.0
   */
  private final Cache<K, V> temporalCache;

  /**
   * Creates a new {@link ExpirableInMemoryLocalRegistry} object from the given {@code cache} parameter.
   *
   * @param cache the {@link Cache} to use with this registry.
   * @since 2.3.0
   */
  public ExpirableInMemoryLocalRegistry(final Cache<K, V> cache) {
    super(cache.asMap());
    this.temporalCache = cache;
  }

  @Override
  public @Nullable V findById(final K id) {
    return this.temporalCache.getIfPresent(id);
  }

  @Override
  public V register(final K id, final V value) {
    final V current = this.temporalCache.getIfPresent(id);
    if (current != null) {
      return current;
    }
    this.temporalCache.put(id, value);
    return value;
  }

  @Override
  public @Nullable V unregister(final K id) {
    final V current = this.temporalCache.getIfPresent(id);
    if (current != null) {
      this.temporalCache.invalidate(id);
    }
    return current;
  }

  @Override
  public int unregisterIf(final Predicate<V> filter) {
    int count = 0;
    for (final Map.Entry<K, V> entry : this.temporalCache.asMap().entrySet()) {
      if (filter.test(entry.getValue())) {
        this.temporalCache.invalidate(entry.getKey());
        ++count;
      }
    }
    return count;
  }

  @Override
  public void unregisterAll() {
    this.temporalCache.invalidateAll();
  }
}
