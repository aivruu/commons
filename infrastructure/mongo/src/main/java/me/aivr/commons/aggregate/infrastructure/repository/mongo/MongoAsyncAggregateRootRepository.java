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

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.function.IntFunction;
import me.aivr.commons.aggregate.domain.AggregateRoot;
import me.aivr.commons.aggregate.domain.repository.AsyncAggregateRootRepositoryImpl;
import org.bson.Document;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class MongoAsyncAggregateRootRepository<AggregateType extends AggregateRoot>
    extends AsyncAggregateRootRepositoryImpl<AggregateType> {
  protected static final Document EMPTY_DOCUMENT = new Document();
  protected final MongoClient client;
  protected final String databaseName;
  protected final String collectionName;
  protected final Class<AggregateType> type;
  protected MongoCollection<AggregateType> values;

  protected MongoAsyncAggregateRootRepository(
      final Executor executor,
      final MongoClient client,
      final String databaseName,
      final String collectionName,
      final Class<AggregateType> type) {
    super(executor);
    this.client = client;
    this.databaseName = databaseName;
    this.collectionName = collectionName;
    this.type = type;
  }

  @Override
  public void start() {
    final MongoDatabase database;
    try {
      database = this.client.getDatabase(this.databaseName);
    } catch (final IllegalArgumentException exception) {
      throw new RuntimeException(exception);
    }
    this.values = database.getCollection(this.collectionName, this.type);
  }

  @Override
  public @Nullable AggregateType findSync(final String id) {
    return this.values.find(new Document("_id", id)).first();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <Identifiers extends Collection<String>> Identifiers findAllIdsSync(final IntFunction<Identifiers> limit) {
    final Collection<String> ids = new ArrayList<>();
    this.values.listIndexes().map(document -> ids.add(document.getString("_id")));
    return (Identifiers) ids;
  }

  @Override
  public boolean writeSync(final AggregateType aggregate) {
    final UpdateResult result = this.values.updateOne(new Document("_id", aggregate.id()), new Document("$set", aggregate));
    return (result.getMatchedCount() == 0 && this.values.insertOne(aggregate).wasAcknowledged()) || result.wasAcknowledged();
  }

  @Override
  public boolean deleteSync(final String id) {
    return this.values.deleteOne(new Document("_id", id)).wasAcknowledged();
  }

  @Override
  public void deleteAllSync() {
    this.values.deleteMany(EMPTY_DOCUMENT);
  }
}
