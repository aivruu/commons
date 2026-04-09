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

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.Collection;

/**
 * Represents an executable-command that can be registered with the {@link CommandRegistry}.
 *
 * @since 1.0.0
 */
public interface RegistrableCommand {
  /**
   * Returns a collection of aliases for this command.
   *
   * @return this command's aliases.
   * @since 1.0.0
   */
  Collection<String> aliases();

  /**
   * Builds the tree-node for this command.
   *
   * @return the {@link LiteralCommandNode} for this command.
   * @since 1.0.0
   */
  LiteralCommandNode<CommandSourceStack> build();
}
