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

import java.nio.file.Path;
import me.aivr.commons.config.infrastructure.ConfigType;
import me.aivr.commons.test.config.TestConfigurationProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class ConfigurationTest {
  static final String YAML_TEST_HEADER = """
      - Test Configuration File
      -
      - Options described in this file are only for testing purposes, and have no real purpose or use on production environments.""";

  @Test
  void yamlConfigLoad() {
    final TestConfigurationProvider testConfigurationProvider = buildTestConfigurationProvider(ConfigType.YAML);
    Assertions.assertTrue(testConfigurationProvider.load(YAML_TEST_HEADER));
  }

  @Test
  void jsonConfigLoad() {
    final TestConfigurationProvider testConfigurationProvider = buildTestConfigurationProvider(ConfigType.JSON);
    Assertions.assertTrue(testConfigurationProvider.load(null));
  }

  public static TestConfigurationProvider buildTestConfigurationProvider(final ConfigType type) {
    return new TestConfigurationProvider(Path.of(System.getProperty("user.dir")), type);
  }

  @Test
  void configAccessException() {
    final TestConfigurationProvider testConfigurationProvider = buildTestConfigurationProvider(ConfigType.YAML);
    Assertions.assertThrows(IllegalStateException.class, testConfigurationProvider::get);
  }
}
