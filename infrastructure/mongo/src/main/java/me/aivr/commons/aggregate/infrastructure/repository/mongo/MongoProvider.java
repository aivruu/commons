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
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class MongoProvider {
  private static @Nullable MongoClient client;

  private MongoProvider() {
    throw new UnsupportedOperationException("This class is for utility and cannot be instantiated.");
  }

  public static boolean isAvailable() {
    return client != null;
  }

  public static MongoClient client() {
    if (client == null) throw new IllegalStateException("Mongo-client instance has not been initialized yet.");
    return client;
  }

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

  public static void close() {
    if (client == null) return;

    client.close();
    client = null;
  }
}
