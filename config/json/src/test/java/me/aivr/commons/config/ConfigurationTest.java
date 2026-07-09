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

import me.aivr.commons.config.application.ConfigurationProvider;
import me.aivr.commons.config.infrastructure.ConfigType;
import me.aivr.commons.config.infrastructure.ConfigurationProviderImpl;
import me.aivr.commons.config.infrastructure.container.ContainerBuilder;
import me.aivr.commons.config.infrastructure.container.json.JsonContainerBuilder;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ConfigurationTest {
  static final Path DIRECTORY = Paths.get("src", "test", "test-output");

  @Test
  void loadConfig() {
    final ConfigurationProvider<TestYamlConfig> provider = this.buildConfigurationProvider();
    this.deletePreviousIfExists(provider);
    final ContainerBuilder containerBuilder = this.createContainer(provider);
    Assertions.assertTrue(provider.load(containerBuilder));
  }

  private ContainerBuilder createContainer(final ConfigurationProvider<TestYamlConfig> provider) {
    return JsonContainerBuilder.create(DIRECTORY)
        .fileName(provider.configName())
        .clazz(provider.modelClassType());
  }

  private void deletePreviousIfExists(final ConfigurationProvider<TestYamlConfig> provider) {
    try {
      Files.deleteIfExists(provider.configDirectory().resolve(provider.configName() + ConfigType.JSON.fileExtension()));
    } catch (final IOException ignored) {}
  }

  @Test
  void builderMissingParameters() {
    final ConfigurationProvider<TestYamlConfig> provider = this.buildConfigurationProvider();
    this.deletePreviousIfExists(provider);
    // try with no file-name specified.
    ContainerBuilder containerBuilder;
    containerBuilder = JsonContainerBuilder.create(DIRECTORY).clazz(provider.modelClassType());
    Assertions.assertThrows(IllegalArgumentException.class, containerBuilder::build);

    this.deletePreviousIfExists(provider);
    // try with no class specification.
    containerBuilder = JsonContainerBuilder.create(DIRECTORY).fileName(provider.configName());
    Assertions.assertThrows(NullPointerException.class, containerBuilder::build);
  }

  @Test
  void configAccessException() {
    final ConfigurationProvider<TestYamlConfig> provider = this.buildConfigurationProvider();
    Assertions.assertThrows(IllegalStateException.class, provider::get);
  }

  ConfigurationProviderImpl<TestYamlConfig> buildConfigurationProvider() {
    return new ConfigurationProviderImpl<>(DIRECTORY, "test", TestYamlConfig.class, ComponentLogger.logger("ConfigurationTest"));
  }
}
