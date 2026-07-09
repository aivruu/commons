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

import me.aivr.commons.util.domain.exception.ExceptionConstants;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility-class to send debugging messages if is configured to do so.
 *
 * @since 1.0.0
 */
public final class Debugger {
  /**
   * The logger-instance to use for the messages, {@code null} if not configured yet.
   *
   * @since 3.0.0
   */
  private static @Nullable Logger internalLogger;
  /**
   * Indicates whether the debugger is enabled or not.
   *
   * @since 1.0.0
   */
  private static boolean enabled;
  /**
   * Indicates whether the debugger should use {@link Logger#debug(String, Object...)} rather that
   * {@link Logger#info(String, Object...)}.
   *
   * @since 3.0.0
   */
  private static boolean useDebugFunction;

  private Debugger() {
    throw ExceptionConstants.NOT_INSTANTIABLE_EXCEPTION;
  }

  /**
   * Creates a new {@link Logger} instance with the name provided for this function.
   *
   * @param customName the name the logger-instance will have.
   * @since 3.0.0
   */
  public static void initLogger(final String customName) {
    if (internalLogger == null) internalLogger = LoggerFactory.getLogger(customName);
  }

  /**
   * Checks whether the debugger is or not enabled.
   *
   * @return {@code true} if it's enabled, {@code false} otherwise.
   * @since 3.0.0
   */
  public static boolean enabled() {
    return enabled;
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
   * Checks whether for logging the debugger must use the {@link Logger#debug(String, Object...)} function.
   *
   * @return {@code true} if it should use it, {@code false} otherwise.
   * @since 3.0.0
   */
  public static boolean usingDebugFunction() {
    return useDebugFunction;
  }

  /**
   * Sets the {@link #useDebugFunction} state for the debugger to use debug-specific {@link Logger} functions.
   * <p>
   * Using this logger-level requires to have a binding configured (e.g. logback) in order to use that, otherwise, it can be
   * configured specifying the property for the JVM -> {@code -Dorg.slf4j.simpleLogger.defaultLogLevel=debug}.
   *
   * @param should whether allow the debugger to use the {@link Logger#debug(String, Object...)} function.
   * @since 3.0.0
   */
  public static void useDebugFunction(final boolean should) {
    useDebugFunction = should;
  }

  /**
   * Logs the given debug-message and will apply the given args as "placeholders", if the debugger is enabled.
   * <p>
   * If {@link #internalLogger} hasn't been initialized yet, this function will initialize it calling to the
   * {@link #initLogger(String)} function and giving this class's name as parameter.
   * <p>
   * If {@link #useDebugFunction} is configured to {@code true}, the debugger will use the {@link Logger#debug(String, Object...)}
   * instead.
   *
   * @param message the message to log.
   * @param args the values to replace in the message.
   * @see Class#getSimpleName() Get class's name for the Logger
   * @since 3.0.0
   */
  public static void write(final String message, final Object... args) {
    if (!enabled) return;

    if (internalLogger == null) {
      initLogger(Debugger.class.getSimpleName());
    }
    // if logger is configured...
    if (useDebugFunction) {
      internalLogger.debug(message, args);
      return;
    }
    internalLogger.info(message, args);
  }
}
