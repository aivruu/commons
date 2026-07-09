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

import me.aivr.commons.util.application.Debugger;
import org.junit.jupiter.api.Test;

public final class DebuggerTest {
  @Test
  void customNameDebugger() {
    Debugger.enable(true);
    Debugger.initLogger("MyCustomDebugger");

    Debugger.write("This is a debug-message sent by the Debugger using a custom-name for the logger.");
    this.testExceptionDebugging();
  }

  private void testExceptionDebugging() {
    try {
      Integer.parseInt("abc");
    } catch (final NumberFormatException exception) {
      Debugger.write("Expected exception for wrong string-to-number conversion.", exception);
    }
  }

  @Test
  void useInternalDebug() {
    Debugger.enable(true);
    Debugger.useDebugFunction(true);
    Debugger.initLogger("InternalDebug");

    Debugger.write("This is a message printed using the 'DEBUG' level.");
    this.testExceptionDebugging();
  }

  @Test
  void defaultNameDebugger() {
    Debugger.enable(true);
    Debugger.write("This is a debug-message sent by the Debugger using a default-name for the logger.");
    this.testExceptionDebugging();
  }
}
