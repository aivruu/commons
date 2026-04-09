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
package me.aivr.commons.aggregate.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.aivr.commons.aggregate.domain.event.AggregateRootEvent;
import me.aivr.commons.aggregate.domain.event.AggregateWritingRequiredStateChangeEvent;

/**
 * Represents an aggregate-root.
 * <p>
 * An aggregate-root is the main entry-point used by the API to access and represent data for domain-entities across the
 * domain-layer, as well the application and infrastructure layers. Abstractions/contracts and classes within this package
 * serves to represent main-domain for the project, also providing secure-access to data related with aggregate-roots.
 *
 * @since 1.0.0
 */
public abstract class AggregateRoot {
  protected final List<AggregateRootEvent> events = new ArrayList<>();
  protected final String id;
  protected boolean shouldSave;

  protected AggregateRoot(final String id) {
    this.id = id;
  }

  /**
   * Returns this aggregate-root's id.
   *
   * @return the aggregate-root's identifier.
   * @since 1.0.0
   */
  public final String id() {
    return this.id;
  }

  /**
   * Returns an iterator with the recorded-events for this aggregate-root.
   * <p>
   * After the iterator is created, the original events-collection ({@link #events} is cleared.
   *
   * @return an {@link Iterator} with this aggregate-root's recorded-events.
   * @since 1.0.0
   */
  public final Iterator<AggregateRootEvent> pullEvents() {
    final Iterator<AggregateRootEvent> iterator = List.copyOf(this.events).iterator();
    this.events.clear();
    return iterator;
  }

  /**
   * Records an event for a done action that changed the aggregate-root's state.
   *
   * @param event the event to record.
   * @since 1.0.0
   */
  protected final void recordEvent(final AggregateRootEvent event) {
    this.events.add(event);
  }

  /**
   * Marks this aggregate-root as required to be, or not, saved into infrastructure.
   * <p>
   * If the {@code shouldSave} parameter was {@code true}, the {@link AggregateWritingRequiredStateChangeEvent} will be recorded
   * for this aggregate-root, listeners to this event can perform actions over the aggregate-root involved.
   *
   * @param shouldSave whether the aggregate-root should be saved now.
   * @since 1.0.0.
   */
  protected final void shouldSave(final boolean shouldSave) {
    if (shouldSave) this.recordEvent(new AggregateWritingRequiredStateChangeEvent(this.id));

    this.shouldSave = shouldSave;
  }

  /**
   * Checks whether this aggregate-root should be saved.
   *
   * @return {@code true} if it should be, {@code false} otherwise.
   * @since 1.0.0
   */
  public final boolean shouldSave() {
    return this.shouldSave;
  }
}
