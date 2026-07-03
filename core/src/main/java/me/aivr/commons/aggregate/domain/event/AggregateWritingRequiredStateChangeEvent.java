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
 * An event that's recorded each time an {@link me.aivr.commons.aggregate.domain.AggregateRoot} is marked as required
 * to be saved.
 *
 * @param aggregateRootId the id of the aggregate-root involved.
 * @since 1.0.0
 */
public record AggregateWritingRequiredStateChangeEvent(String aggregateRootId) implements AggregateRootEvent {}
