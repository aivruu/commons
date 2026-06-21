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
package me.aivr.commons.test.registry;

import static org.junit.jupiter.api.Assertions.*;

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.longs.LongCollection;
import me.aivr.commons.registry.domain.LocalRegistry;
import me.aivr.commons.registry.domain.ints.IntValueLocalRegistry;
import me.aivr.commons.registry.domain.longs.LongValueLocalRegistry;
import me.aivr.commons.registry.infrastructure.InMemoryLocalRegistry;
import me.aivr.commons.registry.infrastructure.ints.IntValueInMemoryLocalRegistry;
import me.aivr.commons.registry.infrastructure.longs.LongValueInMemoryLocalRegistry;
import org.junit.jupiter.api.Test;

class RegistriesOperationTest {
  @Test
  void basic() {
    final LocalRegistry<String, String> reg = new InMemoryLocalRegistry<>(false);
    assertFalse(reg.existsById("a"));

    final String value = "one";
    final String prev = reg.register("a", value);
    assertSame(value, prev);

    assertTrue(reg.existsById("a"));
    assertEquals(value, reg.findById("a"));

    final String removed = reg.unregister("a");
    assertEquals(value, removed);
    assertNull(reg.findById("a"));
  }

  @Test
  void overwriteReturnsPrevious() {
    final LocalRegistry<String, String> reg = new InMemoryLocalRegistry<>(false);
    reg.register("k", "v1");
    final String prev = reg.register("k", "v2");
    assertEquals("v1", prev);
    assertEquals("v2", reg.findById("k"));
  }

  @Test
  void findAllAndFilterValues() {
    final IntValueLocalRegistry<String> reg = new IntValueInMemoryLocalRegistry<>(false);
    reg.registerInt("a", 1);
    reg.registerInt("b", 2);
    reg.registerInt("c", 3);

    assertTrue(reg.findAllInts().size() >= 3);
    assertTrue(reg.findAllKeys().size() >= 3);

    final IntCollection evens = reg.filterInts(v -> (v & 1) == 0);
    assertTrue(evens.contains(2));
    assertFalse(evens.contains(1));
  }

  @Test
  void findAllKeys() {
    final LongValueLocalRegistry<String> reg = new LongValueInMemoryLocalRegistry<>(false);
    reg.registerLong("a", 1);
    reg.registerLong("b", 2);
    reg.registerLong("c", 3);

    assertTrue(reg.findAllLongs().size() >= 3);
    assertTrue(reg.findAllKeys().size() >= 3);

    final LongCollection evens = reg.filterLongs(v -> (v & 1) == 0);
    assertTrue(evens.contains(2));
    assertFalse(evens.contains(1));
  }
}
