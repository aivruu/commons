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

import java.nio.file.Path;
import me.aivr.commons.config.infrastructure.ConfigType;
import me.aivr.commons.config.infrastructure.ConfigurationProviderImpl;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public final class TestConfigurationProvider extends ConfigurationProviderImpl<TestConfig> {
  public TestConfigurationProvider(final Path directory, final ConfigType fileType) {
    super(directory, "test", fileType, TestConfig.class, ComponentLogger.logger("ConfigTestLogger"));
  }
}
