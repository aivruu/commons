package me.aivr.commons;

import me.aivr.commons.event.application.EventBus;
import me.aivr.commons.event.domain.registry.EventRegistry;
import org.jspecify.annotations.Nullable;

/**
 * Access-point for API-related lifecycle components.
 *
 * @since 1.0.0
 */
public abstract class PluginApiAccessor {
  /**
   * Thrown when a field has not been initialized yet and a function tries to access to it.
   *
   * @since 1.0.0
   */
  protected static final IllegalStateException NOT_INITIALIZED_COMPONENT_EXCEPTION
      = new IllegalStateException("API-component trying to be accessed has not been initialized yet.");
  // API-related components fields.
  //
  // Fields can be null to indicate that they have not been initialized yet, and can throw an exception if
  // they're called when null.
  protected @Nullable EventRegistry<Object> eventRegistry;
  protected @Nullable EventBus<Object> eventBus;

  /**
   * Loads the API-components defined in this class during the plugin loading process.
   *
   * @since 1.0.0
   */
  abstract void load();

  /**
   * Shutdowns the API-components that're still available and may need to be triggered for own shutdown process.
   *
   * @since 1.0.0
   */
  abstract void shutdown();

  /**
   * Returns the instance for the {@link EventRegistry}.
   *
   * @throws IllegalStateException if it wasn't initialized before calling this function.
   * @return the {@link EventRegistry}.
   * @since 1.0.0
   */
  public final EventRegistry<Object> eventRegistry() {
    if (this.eventRegistry == null) throw NOT_INITIALIZED_COMPONENT_EXCEPTION;
    return this.eventRegistry;
  }

  /**
   * Returns the instance for the {@link EventBus}.
   *
   * @throws IllegalStateException if it wasn't initialized before calling this function.
   * @return the {@link EventBus}.
   * @since 1.0.0
   */
  public final EventBus<Object> eventBus() {
    if (this.eventBus == null) throw NOT_INITIALIZED_COMPONENT_EXCEPTION;
    return this.eventBus;
  }
}
