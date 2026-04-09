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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;

/**
 * Represents an argument for a {@link RegistrableCommand} that can be resolved using the command's {@link CommandContext}
 * when its executed -> {@link com.mojang.brigadier.builder.LiteralArgumentBuilder#executes(Command)}.
 *
 * @since 1.0.0
 */
public interface ResolvableArgument {
  /**
   * Resolves the argument's logic using the context given for the command.
   *
   * @param context the context of the execution.
   * @return a status-code that indicates operation success, mostly is used with {@link Command#SINGLE_SUCCESS}.
   * @since 1.0.0
   */
  int consumeContext(final CommandContext<CommandSourceStack> context);
}
