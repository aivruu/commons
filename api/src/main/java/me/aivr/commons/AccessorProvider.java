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
package me.aivr.commons;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

/**
 * Utility-class that serves for providing and initialization of shared {@link PluginApiAccessor} instances.
 *
 * @deprecated use a custom-made provider for a {@link PluginApiAccessor} implementation, as other plugins could disable the
 *             provider when calling to `onDisable()` or related.
 * @since 1.0.0
 */
@Deprecated(since = "2.2.0")
@ApiStatus.ScheduledForRemoval(inVersion = "3.0.0")
public final class AccessorProvider {
  private static @Nullable PluginApiAccessor instance;

  private AccessorProvider() {
    throw new UnsupportedOperationException("This class is for utility and cannot be instantiated.");
  }

  /**
   * Returns the current instance for {@link PluginApiAccessor} if available.
   *
   * @throws IllegalStateException if the {@link #instance} haven't been initialized before calling this function.
   * @return the {@link PluginApiAccessor} instance.
   * @since 1.0.0
   */
  public static PluginApiAccessor get() {
    if (instance == null) throw new IllegalStateException("API-provider has not been initialized yet.");
    return instance;
  }

  /**
   * Initializes the {@link #instance} field with the provided object.
   * <p>
   * This function is only called during {@code onEnable} function invocation, trying to access the instance before the plugin
   * have reached that point will result in an exception.
   *
   * @throws IllegalStateException if the {@link #instance} already was initialized before calling this function.
   * @param instance the instance to pass as value for the field.
   * @since 1.0.0
   */
  public static void init(final PluginApiAccessor instance) {
    if (AccessorProvider.instance != null) throw new IllegalStateException("API-provider has already been initialized.");

    AccessorProvider.instance = instance;
  }

  /**
   * Removes the reference the {@link #instance} field was pointing to.
   * <p>
   * This function shouldn't be called by external plugins.
   *
   * @since 1.0.0
   */
  public static void disable() {
    instance = null;
  }
}
