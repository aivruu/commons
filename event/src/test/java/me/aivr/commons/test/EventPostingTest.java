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
package me.aivr.commons.test;

import me.aivr.commons.event.application.EventBus;
import me.aivr.commons.event.application.SimpleEventBus;
import me.aivr.commons.event.infrastructure.LocalEventRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;

public final class EventPostingTest {
  final EventBus<Object> genericEventBus = new SimpleEventBus<>(new LocalEventRegistry<>());

  @Test
  void singleEventPost() {
    this.genericEventBus.postSingle(new TestEvent("a", "b"));
  }

  @Test
  void bunchEventPost() {
    this.genericEventBus.postBunch(List.of(new TestEvent("a", "b"), new TestEvent("c", "d"), new TestEvent("e", "f")));
  }
}
