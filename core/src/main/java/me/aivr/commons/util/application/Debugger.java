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
package me.aivr.commons.util.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility-class to send debugging messages if is configured to do so.
 *
 * @since 1.0.0
 */
public final class Debugger {
  /**
   * The logger-instance to use for the messages.
   *
   * @since 1.0.0
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(Debugger.class);
  /**
   * Indicates whether the debugger is enabled or not.
   *
   * @since 1.0.0
   */
  private static boolean enabled;

  private Debugger() {
    throw new UnsupportedOperationException("This class is for utility and cannot be instantiated.");
  }

  /**
   * Sets the {@link #enabled} state for the debugger.
   *
   * @param enableDebug whether enable the debugger or not.
   * @since 1.0.0
   */
  public static void enable(final boolean enableDebug) {
    enabled = enableDebug;
  }

  /**
   * Logs the given debug-message and will apply the given args as "placeholders", if the debugger is enabled.
   *
   * @param message the message to log.
   * @param args the values to replace in the message.
   * @since 1.0.0
   */
  public static void write(final String message, final Object... args) {
    if (enabled) LOGGER.debug(message, args);
  }
}
