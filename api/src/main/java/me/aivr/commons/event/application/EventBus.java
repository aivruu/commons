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

import java.util.Iterator;
import me.aivr.commons.event.application.exception.ExceptionContextValueObject;

/**
 * Represents an event-bus.
 * <p>
 * An event-bus is responsible to post provided event-objects to different
 * {@link me.aivr.commons.event.domain.subscribe.Subscription}s depending on the event-type that was published, as well,
 * handling these posting-operations for any error due to the handler's
 * ({@link me.aivr.commons.event.domain.subscribe.Subscriber}) or internal's own.
 *
 * @param <E> the event-type this bus handles.
 * @since 1.0.0
 */
public interface EventBus<E> {
  /**
   * Posts the given event to the subscriptions for this event.
   *
   * @param event the event to post to the subscribers.
   * @since 1.0.0
   */
  void postSingle(final E event);

  /**
   * Posts the given events to the subscriptions for each event given.
   *
   * @param events an {@link Iterator} of events for posting.
   * @see #postSingle(Object) Single-event posting operation
   * @since 1.0.0
   */
  default void postBunch(final Iterator<? extends E> events) {
    while (events.hasNext()) {
      this.postSingle(events.next());
    }
  }

  /**
   * Returns the current implementation that's being used to handle exceptions during posting-related operations.
   *
   * @return the current {@link EventExceptionHandler} in use.
   * @since 1.0.0
   */
  EventExceptionHandler inUseExceptionHandler();

  /**
   * Represents a handler that catches and notifies about exceptions occurred during posting-related operations.
   *
   * @since 1.0.0
   */
  @FunctionalInterface
  interface EventExceptionHandler {
    /**
     * Executes the handling-logic for the caught-exception that was configured for this handler.
     *
     * @param bus the bus used to post the event.
     * @param context the context on which the exception occurred.
     * @param <E> the event-type that was handled during the operation.
     * @since 1.0.0
     */
    <E> void handleCaughtException(final EventBus<? super E> bus, final ExceptionContextValueObject<? super E> context);
  }
}
