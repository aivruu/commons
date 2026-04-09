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
package me.aivr.commons.aggregate.domain.event;

/**
 * A contract that represents domain-events specifically for {@link me.aivr.commons.aggregate.domain.AggregateRoot}s.
 * <p>
 * These event-types are "notifications" that're posted after an action was done over an aggregate-root that had changed
 * its state, by that, this events cannot be cancelled as difference from application-related events which can do so.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface AggregateRootEvent {
  /**
   * Returns the ID of the aggregate-root involved in the event.
   *
   * @return the aggregate-root's id.
   * @since 1.0.0
   */
  String aggregateRootId();
}
