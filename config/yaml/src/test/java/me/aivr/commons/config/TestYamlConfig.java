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
package me.aivr.commons.config;

import me.aivr.commons.config.application.Configuration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public final class TestYamlConfig implements Configuration {
  @Comment("""
      Enables the debug-mode which provides a more-verbose logging during the plugin's operation,
      this provides more-detailed logging about plugin's operation in runtime, as well shows the stack-trace for
      possible errors that may happen during operation.

      Don't enable it unless it's required.""")
  public boolean debug = true;
  @Comment(value = "A simple string to test configuration access.", afterLineBreak = true)
  public String str = "<yellow>hello";
  public String[] messages = {
      "Hello!",
      "This is a test message."
  };
  @Comment("Component used to test components serialization/deserialization for the config's nodes.")
  public Component component = Component.text("Component", NamedTextColor.RED);
  @Comment(value = "Messages that're used to test static and contextual placeholder-resolving for component-parsers.", afterLineBreak = true)
  public String[] contextBasedMessages = {
      "Testing contextual messages parsing.",
      "<version> on running",
      "using <provider> provider for parsing.",
      "",
      "Expected coordinates: <location>"
  };
}
