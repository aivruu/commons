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
package me.aivr.commons.event.domain.registry;

import java.util.List;
import java.util.function.Predicate;
import me.aivr.commons.event.domain.subscribe.Subscription;

/**
 * Represents a registry of {@link Subscription}s.
 *
 * @param <E> the type of event this registry handles.
 * @since 1.0.0
 */
public interface EventRegistry<E> {
  /**
   * Returns the type of event this registry handles.
   *
   * @return this registry's event type.
   * @since 1.0.0
   */
  Class<E> type();

  /**
   * Returns whether there are any subscriptions for the given event-type.
   *
   * @param event the event-type to check.
   * @return whether there are subscriptions for that event.
   * @since 1.0.0
   */
  default boolean subscribed(final Class<? extends E> event) {
    return !this.subscriptions(event).isEmpty();
  }

  /**
   * Returns whether the given subscription is registered.
   *
   * @param subscription the subscription to check.
   * @return whether the subscription is registered.
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  default <T extends E> boolean subscribed(final Subscription<? super T> subscription) {
    return !this.subscriptions((Class<? extends E>) subscription.subscribedEventType()).isEmpty();
  }

  /**
   * Registers the subscription for the given event-type and returns the subscription.
   *
   * @param event the event-type to subscribe to.
   * @param subscription the subscription to register.
   * @return the registered subscription.
   * @since 1.0.0
   */
  <T extends E> Subscription<? super T> subscribe(final Class<T> event, final Subscription<? super T> subscription);

  /**
   * Unregisters the given subscription.
   *
   * @param subscription the subscription to unregister.
   * @since 1.0.0
   */
  <T extends E> void unsubscribe(final Subscription<? super T> subscription);

  /**
   * Unregisters all the subscriptions for the specified event-type.
   *
   * @param event the event-type to which unregister its subscriptions.
   * @return whether any subscriptions were removed.
   * @since 1.0.0
   */
  boolean unsubscribeAll(final Class<? extends E> event);

  /**
   * Unregisters all the subscriptions that match with the provide condition.
   *
   * @param condition the condition needed for a subscription to be unregistered.
   * @since 1.0.0
   */
  <T extends E> void unsubscribeIf(final Predicate<Subscription<? super T>> condition);

  /**
   * Returns all the subscriptions registered for the given event-type.
   *
   * @param event the event-type to get its subscriptions.
   * @return a subscriptions-list for the event or {@link List#of()} if event is not registered.
   * @since 1.0.0
   */
  List<Subscription<? super E>> subscriptions(final Class<? extends E> event);

  /**
   * Removes all the mappings to subscriptions (event-types included) from this registry.
   *
   * @since 1.0.0
   */
  void unregisterAll();
}
