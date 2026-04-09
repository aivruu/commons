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
package me.aivr.commons.aggregate.infrastructure.repository.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.aivr.commons.aggregate.domain.AggregateRoot;
import me.aivr.commons.aggregate.domain.repository.AsyncAggregateRootRepositoryImpl;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class JsonAsyncAggregateRootRepository<AggregateType extends AggregateRoot>
    extends AsyncAggregateRootRepositoryImpl<AggregateType> {
  protected static final String FILE_EXTENSION = ".json";
  protected final Gson provider;
  protected final Path directoryPath;
  protected final Class<AggregateType> type;
  protected final TypeToken<AggregateType> gsonRequiredType;

  protected JsonAsyncAggregateRootRepository(
      final Executor executor,
      final Gson provider,
      final Path directoryPath,
      final Class<AggregateType> type) {
    super(executor);
    this.provider = provider;
    this.directoryPath = directoryPath;
    this.type = type;
    this.gsonRequiredType = TypeToken.get(type);
  }

  @Override
  public void start() {
    if (Files.exists(this.directoryPath)) return;

    try {
      Files.createDirectory(this.directoryPath);
    } catch (final IOException exception) {
      // In case you wonder why we do this, exception-throwing is handled later during async-operations
      // handling (CompletableFuture) and then notified (logged them as debugging).
      throw new RuntimeException(exception);
    }
  }

  @Override
  public @Nullable AggregateType findSync(final String id) {
    final Path file = this.directoryPath.resolve(id + FILE_EXTENSION);
    if (Files.notExists(file)) {
      return null;
    }
    try (final Reader reader = Files.newBufferedReader(file)) {
      // Generic-required functions require a [TypeToken] instead the plain class-type.
      return this.provider.fromJson(reader, this.gsonRequiredType);
    } catch (final IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  public <Identifiers extends Collection<String>> Identifiers findAllIdsSync(final IntFunction<Identifiers> limit) {
    try {
      try (final Stream<Path> stream = Files.list(this.directoryPath)) {
        // ugly af stream here
        return stream.filter(path -> path.getFileName().toString().endsWith(FILE_EXTENSION))
            .map(path -> {
              final String fileName = path.getFileName().toString();
              return fileName.substring(0, fileName.length() - FILE_EXTENSION.length());
            })
            .collect(Collectors.toCollection(() -> limit.apply(0)));
      }
    } catch (final IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  public boolean writeSync(final AggregateType aggregate) {
    final String id = aggregate.id();
    final Path file = this.directoryPath.resolve(id + FILE_EXTENSION);
    if (Files.exists(file)) {
      return false;
    }
    try (final Writer writer = Files.newBufferedWriter(file)) {
      this.provider.toJson(aggregate, this.type, writer);
      return true;
    } catch (final IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  public boolean deleteSync(final String id) {
    final Path file = this.directoryPath.resolve(id + FILE_EXTENSION);
    try {
      return Files.deleteIfExists(file);
    } catch (final IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  public void deleteAllSync() {
    try {
      try (final Stream<Path> stream = Files.list(this.directoryPath)) {
        // and here too
        stream.filter(path -> path.getFileName().toString().endsWith(FILE_EXTENSION)).forEach(path -> {
          try {
            Files.deleteIfExists(path);
          } catch (final IOException e) {
            throw new RuntimeException(e);
          }
        });
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
