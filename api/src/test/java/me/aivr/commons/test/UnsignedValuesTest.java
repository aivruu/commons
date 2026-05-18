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

import me.aivr.commons.util.domain.UnsignedProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class UnsignedValuesTest {
  @Test
  @SuppressWarnings("ConstantConditions")
  void unsignedParse() {
    final int raw = 243;
    final byte unsignedByte = UnsignedProvider.unsignedFromInt(raw);
    Assertions.assertEquals(raw, UnsignedProvider.signedFromByte(unsignedByte));
    Assertions.assertEquals((byte) 243, unsignedByte);
  }

  @Test
  void unsignedOutOfBounds() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> UnsignedProvider.unsignedFromInt(-1));
    Assertions.assertThrows(IllegalArgumentException.class, () -> UnsignedProvider.unsignedFromInt(256));
    Assertions.assertThrows(IllegalArgumentException.class, () -> UnsignedProvider.unsignedFromInt(1000));
  }
}
