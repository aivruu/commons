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

import static org.junit.jupiter.api.Assertions.assertEquals;

import me.aivr.commons.util.domain.CoordinatePackingProvider;
import org.bukkit.Location;
import org.junit.jupiter.api.Test;

public final class CoordinatePackingTest {
  @Test
  void packAndUnpackLocation() {
    final int x = 1922;
    final int y = 65;
    final int z = -1923;
    final Location location = new Location(null, x, y, z);
    final long packed = CoordinatePackingProvider.packLocation(location);

    assertEquals(x, CoordinatePackingProvider.unpackX(packed));
    assertEquals(y, CoordinatePackingProvider.unpackY(packed));
    assertEquals(z, CoordinatePackingProvider.unpackZ(packed));
  }

  @Test
  void packAndUnpackRaw() {
    final int x = 154734;
    final int y = 36;
    final int z = -1928342;
    final long packed = CoordinatePackingProvider.pack(x, y, z);
    assertEquals(x, CoordinatePackingProvider.unpackX(packed));
    assertEquals(y, CoordinatePackingProvider.unpackY(packed));
    assertEquals(z, CoordinatePackingProvider.unpackZ(packed));
  }

  @Test
  void invalidValues() {
    final int x = Integer.MAX_VALUE;
    final int y = 36;
    final int z = -1928342;
    final long packed = CoordinatePackingProvider.pack(x, y, z);
    assertEquals(-1, packed);
  }
}
