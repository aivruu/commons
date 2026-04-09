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

/**
 * Represents a subscriber for a posted event.
 *
 * @param <E> the event-type to which it's subscribed.
 * @since 1.0.0
 */
@FunctionalInterface
public interface Subscriber<E> {
  /**
   * Executes the event-handling this subscriber wants to apply for the event.
   *
   * @param event the event to received by the subscriber.
   * @since 1.0.0
   */
  void handle(final E event);
}
