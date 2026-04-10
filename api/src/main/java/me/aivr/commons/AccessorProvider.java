package me.aivr.commons;

import org.jspecify.annotations.Nullable;

/**
 * Utility-class that serves for providing and initialization of shared {@link PluginApiAccessor} instances.
 *
 * @since 1.0.0
 */
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
