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

import me.aivr.commons.event.domain.subscribe.Subscription;

/**
 * Represents the context on which an exception occurred when posting an event through an
 * {@link me.aivr.commons.event.application.EventBus}.
 *
 * @param event the event that was posted.
 * @param subscription the subscription that received this event.
 * @param exception the exception that occurred when handling the event.
 * @param <E> a generic that represents the event-type involved.
 * @since 1.0.0
 */
public record ExceptionContextValueObject<E>(E event, Subscription<? super E> subscription, Throwable exception) {}
