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
package me.aivr.commons.event.application.exception;

import me.aivr.commons.event.application.EventBus;
import me.aivr.commons.event.domain.subscribe.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default built-in implementation for {@link EventBus.EventExceptionHandler}.
 *
 * @since 3.0.0
 */
public enum BasicEventExceptionHandler implements EventBus.EventExceptionHandler {
  INSTANCE;

  private static final Logger LOGGER = LoggerFactory.getLogger("BasicEventExceptionHandler");

  @Override
  @SuppressWarnings("unchecked")
  public <E> void handleCaughtException(final EventBus<? super E> bus, final ExceptionContextValueObject<? super E> context) {
    final E event = (E) context.event();
    final Throwable throwable = context.exception();
    // Though it could be null, it won't, as when this function is called due to an exception when posting the event for this
    // subscription, its handler would have been already initialized.
    final Subscriber<? super E> subscriber = context.subscription().subscriber();
    try {
      LOGGER.warn("Failed when trying to publish event {} to subscriber {} using EventBus-impl {}.", event, subscriber, bus, throwable);
    } catch (final Throwable t) {
      LOGGER.error("Failed when handling caught-exception for event-publishing with {} to subscriber {} using EventBus implementation {}.",
          event, subscriber, bus, t);
    }
  }
}
