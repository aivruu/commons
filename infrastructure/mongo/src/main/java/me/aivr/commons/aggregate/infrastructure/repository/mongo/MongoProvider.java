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
package me.aivr.commons.aggregate.infrastructure.repository.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import me.aivr.commons.aggregate.infrastructure.repository.mongo.codec.MongoCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.jspecify.annotations.Nullable;

/**
 * A provider of configurable {@link MongoClient} instances that can be used for a {@link MongoAsyncAggregateRootRepository}.
 *
 * @since 1.0.0
 */
public class MongoProvider {
  private static @Nullable MongoClient client;

  private MongoProvider() {
    throw new UnsupportedOperationException("This class is for utility and cannot be instantiated.");
  }

  /**
   * Checks whether the instance is still available for use.
   *
   * @return {@code true} if the instance hasn't been closed yet, otherwise {@code false}.
   * @since 1.0.0
   */
  public static boolean isAvailable() {
    return client != null;
  }

  /**
   * Returns the client-instance if available.
   *
   * @throws IllegalStateException if no instance has been configured for the provider.
   * @return the {@link MongoClient} instance.
   * @since 1.0.0
   */
  public static MongoClient client() {
    if (client == null) throw new IllegalStateException("Mongo-client instance has not been initialized yet.");
    return client;
  }

  /**
   * Configures a {@link MongoClient} instance with the provided parameters for the method.
   *
   * @param host the client's host-address.
   * @param database the client's database-name.
   * @param username the client's username.
   * @param password the client's password.
   * @param codecs the {@link MongoCodec}s to include within the {@link MongoClient} instance.
   * @since 1.0.0
   */
  public static void init(
      final String host,
      final String database,
      final String username,
      final String password,
      final MongoCodec<?>... codecs) {
    final MongoClientSettings clientSettings = MongoClientSettings.builder()
        .applyConnectionString(new ConnectionString("mongodb://" + host))
        .credential(MongoCredential.createCredential(username, database, password.toCharArray()))
        .codecRegistry(CodecRegistries.fromCodecs(codecs))
        .build();
    client = MongoClients.create(clientSettings);
  }

  /**
   * Closes the {@link MongoClient} instance.
   *
   * @since 1.0.0
   */
  public static void close() {
    if (client == null) return;

    client.close();
    client = null;
  }
}
