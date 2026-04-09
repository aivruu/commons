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
package me.aivr.commons.command.application;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.jspecify.annotations.Nullable;

/**
 * Represents a basic command-registry to simplify {@link RegistrableCommand} registration.
 *
 * @since 1.0.0
 */
public final class CommandRegistry {
  private @Nullable Commands registrar;

  /**
   * Creates a new {@link me.aivr.commons.event.domain.registry.EventRegistry} with the provided parameters.
   *
   * @param lifecycleEventManager the server's lifecycle event-manager to access {@link Commands} instance.
   * @since 1.0.0
   */
  public CommandRegistry(final LifecycleEventManager<?> lifecycleEventManager) {
    lifecycleEventManager.registerEventHandler(LifecycleEvents.COMMANDS, handler -> setup(handler.registrar()));
  }

  /**
   * Initializes the {@link #registrar} field with the given {@link Commands} instance.
   *
   * @param registrar the instance of {@link Commands} that'll be used for commands-registration.
   * @since 1.0.0
   */
  public void setup(final Commands registrar) {
    if (this.registrar == null) this.registrar = registrar;
  }

  /**
   * Registers the given command if the provider is initialized.
   *
   * @param command the command to register.
   * @since 1.0.0
   */
  public void register(final RegistrableCommand command) {
    if (this.registrar != null) this.registrar.register(command.build(), command.aliases());
  }

  /**
   * Registers the array of commands if the provider is initialized.
   *
   * @param commands the commands to register.
   * @since 1.0.0
   */
  public void register(final RegistrableCommand... commands) {
    for (final RegistrableCommand command : commands) this.register(command);
  }
}
