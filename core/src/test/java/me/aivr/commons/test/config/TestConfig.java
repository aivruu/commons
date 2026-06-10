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
package me.aivr.commons.test.config;

import me.aivr.commons.config.application.Configuration;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public final class TestConfig implements Configuration {
  public boolean debug = true;
  public String str = "<yellow>hello";
  public String[] messages = {
      "Hello!",
      "This is a test message."
  };
  public Component component = Component.text("Component", NamedTextColor.RED);
  public String[] contextBasedMessages = {
      "Testing contextual messages parsing.",
      "<version> on running",
      "using <provider> provider for parsing.",
      "",
      "Expected coordinates: <location>"
  };
}
