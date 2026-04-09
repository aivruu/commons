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
package me.aivr.commons.event.application;

import java.util.List;
import me.aivr.commons.event.application.exception.BasicEventExceptionHandler;
import me.aivr.commons.event.application.exception.ExceptionContextValueObject;
import me.aivr.commons.event.domain.Cancellable;
import me.aivr.commons.event.domain.registry.EventRegistry;
import me.aivr.commons.event.domain.subscribe.Subscription;

/**
 * Default built-in implementation for {@link EventBus}.
 *
 * @param <E> the event-type this bus handles.
 * @since 1.0.0
 */
public final class SimpleEventBus<E> implements EventBus<E> {
  private final EventRegistry<E> eventRegistry;
  private final EventExceptionHandler exceptionHandler;

  /**
   * Creates a new {@link SimpleEventBus} with the provided registry instance.
   *
   * @param eventRegistry the event-registry to use with this event-bus.
   * @since 1.0.0
   */
  public SimpleEventBus(final EventRegistry<E> eventRegistry) {
    this.eventRegistry = eventRegistry;
    this.exceptionHandler = new BasicEventExceptionHandler();
  }

  /**
   * Creates a new {@link SimpleEventBus} with the provided parameters.
   *
   * @param eventRegistry the event-registry to use with this event-bus.
   * @param exceptionHandler the handler to use with exceptions occurred during event-posting.
   * @since 1.0.0
   */
  public SimpleEventBus(final EventRegistry<E> eventRegistry, final EventExceptionHandler exceptionHandler) {
    this.eventRegistry = eventRegistry;
    this.exceptionHandler = exceptionHandler;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void postSingle(final E event) {
    final List<Subscription<? super E>> subscriptions = this.eventRegistry.subscriptions((Class<? extends E>) event.getClass());
    if (subscriptions.isEmpty()) {
      return;
    }
    for (final Subscription<? super E> subscription : subscriptions) {
      if (!this.canBePosted(event, subscription)) {
        continue;
      }
      try {
        subscription.listenAndHandle(event);
      } catch (final Throwable throwable) {
        this.exceptionHandler.handleCaughtException(this, new ExceptionContextValueObject<>(event, subscription, throwable));
      }
    }
  }

  private boolean canBePosted(final E event, final Subscription<? super E> subscription) {
    if (!(event instanceof Cancellable cancellableEvent)) return true;

    return !cancellableEvent.wasCancelled() || subscription.receivesCancelledEvents();
  }

  @Override
  public EventExceptionHandler inUseExceptionHandler() {
    return this.exceptionHandler;
  }
}
