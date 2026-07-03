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
package me.aivr.commons.event.domain;

/**
 * Represents a contract that can be implemented by events that're allowed to be cancelled by
 * {@link me.aivr.commons.event.domain.subscribe.Subscription}s.
 *
 * @since 1.0.0
 */
public interface Cancellable {
  /**
   * Checks whether the event was cancelled by a subscriber.
   *
   * @return {@code true} if it was cancelled, {@code false} otherwise.
   * @since 1.0.0
   */
  boolean wasCancelled();

  /**
   * Changes the {@link #wasCancelled()} state for the event.
   *
   * @param cancelEvent whether the event must be cancelled or not.
   * @since 1.0.0
   */
  void cancel(final boolean cancelEvent);
}
