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
package me.aivr.commons.event.domain.subscribe;

import me.aivr.commons.event.domain.registry.EventRegistry;
import org.jspecify.annotations.Nullable;

/**
 * Represents a subscription to an event.
 * <p>
 * Subscriptions are composed by the event-type to which it is subscribed, the {@link Subscriber} that handles any logic that
 * was set to execute when a posted-event is received, and whether this subscription will receive cancelled-events.
 * <p>
 * For domain-events (check {@link me.aivr.commons.aggregate.domain.event.AggregateRootEvent}) the
 * {@link #receivesCancelledEvents()} state will always be ignored by the {@link me.aivr.commons.event.application.EventBus}
 * when posting these kind of events as they cannot be cancelled.
 *
 * @param <E> the event-type this subscription handles.
 * @since 1.0.0
 */
public abstract class Subscription<E> {
  protected final Class<E> eventType;
  protected final boolean receiveCancelledEvents;
  private final EventRegistry<E> registry; // for subscription disposal operation.
  protected @Nullable Subscriber<? super E> handler; // Sometimes it may not be initialized so we need to mark it.

  protected Subscription(final Class<E> eventType, final EventRegistry<E> registry, final boolean receiveCancelledEvents) {
    this.eventType = eventType;
    this.registry = registry;
    // For non-cancellable events, this field is quite ignored.
    this.receiveCancelledEvents = receiveCancelledEvents;
  }

  /**
   * Returns the event-type this subscription types.
   *
   * @return the type of event.
   * @since 1.0.0
   */
  public final Class<E> subscribedEventType() {
    return this.eventType;
  }

  /**
   * Calls to the {@code handler} passing the specified event for this subscription to handle it whatever it requires to, if the
   * handler is not initialized, this function will call to the {@link #initHandler()} method to ensure it is.
   *
   * @param event the event posted by the event-bus.
   * @see #initHandler() Event-handler or Subscriber initialization
   * @since 1.0.0
   */
  public void listenAndHandle(final E event) {
    if (this.handler == null) this.initHandler();
    this.handler.handle(event);
  }

  /**
   * Executes the logic set for the method to initialize the {@link #handler} in preparation to handle upcoming received events.
   *
   * @since 1.0.0
   */
  public abstract void initHandler();

  /**
   * Returns the subscriber-object for this subscription, if available.
   *
   * @return the {@link Subscriber} for this subscription, or {@code null} if it hasn't been initialized.
   * @since 1.0.0
   */
  public final @Nullable Subscriber<? super E> subscriber() {
    return this.handler;
  }

  /**
   * Returns the state for the {@link #receiveCancelledEvents} attribute of this subscription.
   *
   * @return {@code true} if this subscription receives cancelled-events, {@code false} otherwise.
   * @since 1.0.0
   */
  public boolean receivesCancelledEvents() {
    return this.receiveCancelledEvents;
  }

  /**
   * Disposes this subscription from the registry, after this, no events will be posted for this subscription.
   * <p>
   * This is similar to make {@code EventRegistry.unsubscribe(subscription)}.
   *
   * @see EventRegistry#unsubscribe(Subscription) Subscription-disposal from registry
   * @since 1.0.0
   */
  public final void dispose() {
    this.registry.unsubscribe(this);
  }
}
