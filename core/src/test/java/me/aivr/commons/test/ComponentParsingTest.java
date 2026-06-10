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
package me.aivr.commons.test;

import java.util.List;
import me.aivr.commons.component.application.type.MiniMessageParserImpl;
import me.aivr.commons.component.application.type.PlainComponentParserImpl;
import me.aivr.commons.component.domain.type.MiniMessageParser;
import me.aivr.commons.component.domain.type.PlainComponentParser;
import me.aivr.commons.config.infrastructure.ConfigType;
import me.aivr.commons.test.config.TestConfig;
import me.aivr.commons.test.config.TestConfigurationProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class ComponentParsingTest {
  private final ComponentLogger logger = ComponentLogger.logger(ComponentParsingTest.class);

  @Test
  void basicParsing() {
    final TestConfigurationProvider configurationProvider = ConfigurationTest.buildTestConfigurationProvider(ConfigType.YAML);
    Assertions.assertTrue(configurationProvider.load(null));

    final PlainComponentParser plainComponentParser = new PlainComponentParserImpl(); // use default-constructor provided
    final TestConfig config = configurationProvider.get();
    this.logger.info(plainComponentParser.parseSingle(config.str));
    this.logger.info(plainComponentParser.unparseSingle(config.component));
  }

  @Test
  void contextualParsing() {
    final TestConfigurationProvider configurationProvider = ConfigurationTest.buildTestConfigurationProvider(ConfigType.YAML);
    Assertions.assertTrue(configurationProvider.load(null));

    final Location testLocation = new Location(null, 192.2303, 60, 432234.231);
    final MiniMessageParser miniMessageParser = new MiniMessageParserImpl();
    miniMessageParser.staticPlaceholderResolver()
        .registerSingle(Placeholder.parsed("version", "2.3.2"))
        .registerSingle(Placeholder.parsed("provider", miniMessageParser.providerInstance().toString()));
    miniMessageParser.contextualPlaceholderResolver().registerSingleContextual(Location.class, context -> {
      final StringBuilder formattedLocationBuilder = new StringBuilder();
      formattedLocationBuilder.append(context.getBlockX())
          .append(", ")
          .append(context.getBlockY())
          .append(", ")
          .append(context.getBlockZ());
      return Placeholder.parsed("location", formattedLocationBuilder.toString());
    });
    for (final Component line : miniMessageParser.parseBunchFromContexts(List.of(configurationProvider.get().contextBasedMessages), testLocation)) {
      this.logger.info(line);
    }
  }
}
