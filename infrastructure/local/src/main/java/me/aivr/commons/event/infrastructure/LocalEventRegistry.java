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
package me.aivr.commons.event.infrastructure;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import java.util.function.Predicate;
import me.aivr.commons.event.domain.registry.EventRegistry;
import me.aivr.commons.event.domain.subscribe.Subscription;
import org.jspecify.annotations.NullMarked;

/**
 * Default built-in implementation for local-registry to store {@link Subscription}s for events.
 *
 * @param <E> the event-type this registry handles.
 * @since 1.0.0
 */
@NullMarked
public final class LocalEventRegistry<E> implements EventRegistry<E> {
  private final Object2ObjectMap<Class<? extends E>, ObjectList<Subscription<? super E>>> subscriptions = new Object2ObjectOpenHashMap<>();
  private final Class<E> eventType;

  public LocalEventRegistry(final Class<E> eventType) {
    this.eventType = eventType;
  }

  @Override
  public Class<E> type() {
    return this.eventType;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends E> Subscription<? super T> subscribe(final Class<T> event, final Subscription<? super T> subscription) {
    final ObjectList<Subscription<? super E>> subscribers = this.subscriptions.computeIfAbsent(event, k -> new ObjectArrayList<>());
    subscribers.add((Subscription<? super E>) subscription);
    return subscription;
  }

  @Override
  public <T extends E> void unsubscribe(final Subscription<? super T> subscription) {
    final ObjectList<Subscription<? super E>> subscribers = this.subscriptions.get(subscription.subscribedEventType());
    if (subscribers == null) {
      return;
    }
    subscribers.remove(subscription);
  }

  @Override
  public boolean unsubscribeAll(final Class<? extends E> event) {
    final ObjectList<Subscription<? super E>> subscribers = this.subscriptions.get(event);
    if (subscribers == null) {
      return false;
    }
    subscribers.clear();
    return true;
  }

  @Override
  public <T extends E> void unsubscribeIf(final Predicate<Subscription<? super T>> condition) {
    for (final ObjectList<Subscription<? super E>> subscribers : this.subscriptions.values()) {
      if (subscribers.removeIf(condition)) {
        break;
      }
    }
  }

  @Override
  public List<Subscription<? super E>> subscriptions(final Class<? extends E> event) {
    final ObjectList<Subscription<? super E>> subscribers = this.subscriptions.get(event);
    return subscribers == null ? List.of() : subscribers;
  }

  @Override
  public void unregisterAll() {
    this.subscriptions.clear();
  }
}
