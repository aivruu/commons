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
import me.aivr.commons.event.domain.registry.EventRegistry;
import me.aivr.commons.event.domain.subscribe.Subscription;
import me.aivr.commons.event.infrastructure.LocalEventRegistry;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EventListeningTest {
  final EventRegistry<Object> genericRegistry = new LocalEventRegistry<>();
  final EventBus<Object> genericEventBus = new SimpleEventBus<>(this.genericRegistry);

  {
    this.genericRegistry.subscribe(TestEvent.class, new Listener());
  }

  @Test
  void basicListening() {
    this.genericEventBus.postSingle(new TestEvent("a", "b"));
  }

  @Test
  void exceptionThrownListening() {
    this.genericEventBus.postSingle(new TestEvent(null, "b"));
  }

  static final class Listener extends Subscription<TestEvent> {
    private final Logger logger = LoggerFactory.getLogger(Listener.class);

    public Listener() {
      super(TestEvent.class, null, true);
    }

    @Override
    public void initHandler() {
      super.handler = event -> {
        if (event.previousValue().equals("a")) this.logger.info("Previous-value for event was 'a'.");

        this.logger.info("Received instance for event [TestEvent] with values {}, {}.", event.previousValue(), event.newValue());
      };
    }
  }
}
