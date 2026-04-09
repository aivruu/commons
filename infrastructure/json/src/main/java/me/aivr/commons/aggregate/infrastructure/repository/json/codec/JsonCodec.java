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
package me.aivr.commons.aggregate.infrastructure.repository.json.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a codec that serves to provide custom JSON serialization and deserialization for objects, extending capacities of
 * {@link JsonSerializer} and {@link JsonDeserializer} by providing additional functions for arrays and lists/collections reading
 * and writing.
 *
 * @param <T> a generic object.
 * @since 1.0.0
 */
@NullMarked
public interface JsonCodec<T> extends JsonSerializer<T>, JsonDeserializer<T> {
  /**
   * Returns the type of object this codec handles.
   *
   * @return this codec's object-type to handle.
   * @since 1.0.0
   */
  Class<T> type();

  /**
   * Tries to read and return a {@link JsonArray} object from the given path if available.
   *
   * @param jsonData the {@link JsonObject} to get the JSON-array.
   * @param path the path where read the array.
   * @return a {@link JsonArray} if path contains an array, otherwise {@code null}.
   * @since 1.0.0
   */
  default @Nullable JsonArray jsonArrayIfAvailable(final JsonObject jsonData, final String path) {
    final JsonElement arrayJson = jsonData.get(path);
    // direct call to [getAsJsonArray] may throw an exception.
    return arrayJson.isJsonArray() ? arrayJson.getAsJsonArray() : null;
  }

  /**
   * Reads an array from the given path using the provided {@link JsonObject} and
   * {@link JsonDeserializationContext} parameters.
   *
   * @param <R> the expected component-type for the array.
   * @param jsonData the {@link JsonObject} from which read the data.
   * @param context the context used for object-deserialization.
   * @param path the path from where read the array.
   * @return the expected array if available, otherwise {@code null}.
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  default <R> R @Nullable [] readArray(
      final JsonObject jsonData,
      final Class<R> type,
      final JsonDeserializationContext context,
      final String path) {
    final JsonArray array = this.jsonArrayIfAvailable(jsonData, path);
    if (array == null) {
      return null;
    }
    final R[] valuesArray = (R[]) new Object[array.size()];
    for (int i = 0; i < valuesArray.length; i++) {
      // We delegate the object-decoding to the Decoder linked with the provided class-type, so we can use
      // another custom-decoder if needed.
      valuesArray[i] = context.deserialize(array.get(i), type);
    }
    return valuesArray;
  }

  /**
   * Reads a string-array from the given path using the provided {@link JsonObject} parameter.
   *
   * @param jsonData the {@link JsonObject} from which read the data.
   * @param path the path from where read the array.
   * @return the expected string-array if available, otherwise {@code null}.
   * @see #jsonArrayIfAvailable(JsonObject, String) Get a JSON-array object
   * @since 1.0.0
   */
  default String @Nullable [] readStringArray(final JsonObject jsonData, final String path) {
    final JsonArray array = this.jsonArrayIfAvailable(jsonData, path);
    if (array == null) {
      return null;
    }
    final String[] stringsArray = new String[array.size()];
    for (int i = 0; i < stringsArray.length; i++) {
      stringsArray[i] = array.get(i).getAsString();
    }
    return stringsArray;
  }

  /**
   * Reads an int-array from the given path using the provided {@link JsonObject} parameter.
   *
   * @param jsonData the {@link JsonObject} from which read the data.
   * @param path the path from where read the array.
   * @return the expected int-array if available, otherwise {@code null}.
   * @see #jsonArrayIfAvailable(JsonObject, String) Get a JSON-array object
   * @since 1.0.0
   */
  default int @Nullable [] readIntegerArray(final JsonObject jsonData, final String path) {
    final JsonArray array = this.jsonArrayIfAvailable(jsonData, path);
    if (array == null) {
      return null;
    }
    final int[] intsArray = new int[array.size()];
    int index = 0;
    array.forEach(element -> intsArray[index] = element.getAsInt());
    return intsArray;
  }

  /**
   * Reads a list from the given path using the provided {@link JsonObject} and
   * {@link JsonDeserializationContext} parameters.
   *
   * @param <R> the expected component-type for the list.
   * @param jsonData the {@link JsonObject} from where read the data.
   * @param context the context used for the list's values deserialization.
   * @param path the path from where read the list.
   * @return a new {@link List} of {@link R} values, or {@code null} if the path is not a {@link JsonArray}
   * instance.
   * @since 1.0.0
   */
  default <R> @Nullable List<R> readList(final JsonObject jsonData, final JsonDeserializationContext context, final String path) {
    final JsonArray array = this.jsonArrayIfAvailable(jsonData, path);
    if (array == null) {
      return null;
    }
    final List<R> values = new ObjectArrayList<>(array.size());
    array.forEach(value -> values.add(context.deserialize(value, value.getClass())));
    return values;
  }

  /**
   * Reads a list from the given path using the provided {@link JsonObject} and
   * {@link JsonDeserializationContext} parameters.
   * <p>
   * This function will return a new modifiable empty-list as possible result.
   *
   * @param <R> the expected component-type for the list.
   * @param jsonData the {@link JsonObject} from where read the data.
   * @param context the context used for the list's values deserialization.
   * @param path the path from where read the list.
   * @return the {@link List} read from the path if available, otherwise a {@link ObjectArrayList} is returned.
   * @see #readList(JsonObject, JsonDeserializationContext, String) List-reading from path
   * @since 1.0.0
   */
  default <R> List<R> readListOrEmpty(final JsonObject jsonData, final JsonDeserializationContext context, final String path) {
    final List<R> values = this.readList(jsonData, context, path);
    return values == null ? new ObjectArrayList<>() : values;
  }

  /**
   * Writes the given values-collection to a {@link JsonArray} object.
   *
   * @param jsonData the {@link JsonObject} to
   * @param values the values to write.
   * @param context the context used for the collection's values serialization.
   * @param path the path to where write the array.
   * @param <R> the expected component-type for the array.
   * @see #writeArray(JsonObject, Object[], JsonSerializationContext, String) JSON-array writing operation
   * @since 1.0.0
   */
  default <R> void writeMapValuesAsArray(
      final JsonObject jsonData,
      final Collection<R> values,
      final JsonSerializationContext context,
      final String path) {
    this.writeArray(jsonData, values.toArray(), context, path);
  }

  /**
   * Writes the given array's contents to a {@link JsonArray} object.
   * <p>
   * This function will write array's contents on the same order (index) they're provided by the
   * {@code source} array.
   * <p>
   * If the {@code source} parameter is null the function will return a {@link JsonNull}.
   *
   * @param <R> the expected component-type for the array.
   * @param jsonData the {@link JsonObject} to write the array.
   * @param source the array to read for the writing.
   * @param context the context for the array's elements serialization.
   * @param path the path to where write the array.
   * @since 1.0.0
   */
  @SuppressWarnings("unchecked")
  default <R> void writeArray(
      final JsonObject jsonData,
      final R @Nullable [] source,
      final JsonSerializationContext context,
      final String path) {
    if (source == null) {
      jsonData.add(path, null);
      return;
    }
    final Class<R> arrayType = (Class<R>) source.getClass().getComponentType();
    final JsonArray jsonArray = new JsonArray(source.length);
    for (int i = 0; i < source.length; i++) {
      // Pretty similar JSON requirement explained in [readArray].
      jsonArray.set(i, context.serialize(source[i], arrayType));
    }
    jsonData.add(path, jsonArray);
  }

  /**
   * Writes the given string-array's contents to the {@link JsonObject} object.
   *
   * @param jsonData the {@link JsonObject} to write the array.
   * @param source the array to write.
   * @param path the path to where write the array.
   * @see #writeObjectArray(JsonObject, Object[], Class, JsonSerializationContext, String) Custom-type array writing
   * @since 1.0.0
   */
  default void writeStringArray(final JsonObject jsonData, final String @Nullable [] source, final String path) {
    this.writeObjectArray(jsonData, source, String.class, null, path);
  }

  /**
   * Writes the given integers-array's contents to the {@link JsonObject} object.
   *
   * @param jsonData the {@link JsonObject} to write the array.
   * @param source the array to write.
   * @param path the path to where write the array.
   * @see #writeObjectArray(JsonObject, Object[], Class, JsonSerializationContext, String) Custom-type array writing
   * @since 1.0.0
   */
  default void writeIntegerArray(final JsonObject jsonData, final Integer @Nullable [] source, final String path) {
    this.writeObjectArray(jsonData, source, Integer.class, null, path);
  }

  /**
   * Writes the given string-list's contents to the {@link JsonObject} object.
   *
   * @param jsonData the {@link JsonObject} to write the list.
   * @param source the list to write.
   * @param path the path to where write the list.
   * @see #writeObjectArray(JsonObject, Object[], Class, JsonSerializationContext, String) Custom-type array writing
   * @since 1.0.0
   */
  default void writeStringList(final JsonObject jsonData, final @Nullable List<String> source, final String path) {
    this.writeObjectArray(jsonData, source == null ? null : source.toArray(String[]::new), String.class, null, path);
  }

  /**
   * Writes the given integer-list's contents to the {@link JsonObject} object.
   *
   * @param jsonData the {@link JsonObject} to write the list.
   * @param source the list to write.
   * @param path the path to where write the list.
   * @see #writeObjectArray(JsonObject, Object[], Class, JsonSerializationContext, String) Custom-type array writing
   * @since 1.0.0
   */
  default void writeIntegerList(final JsonObject jsonData, final @Nullable IntList source, final String path) {
    this.writeObjectArray(jsonData, source == null ? null : source.toArray(Integer[]::new), Integer.class, null, path);
  }

  /**
   * Writes the given array of objects to a {@link JsonObject} object.
   * <p>
   * If the {@code source} is not provided then the function will write a {@code null} value on the given path for
   * the JSON, as well, if the array is of a custom-type and no {@code context} is provided, the function will write
   * {@code null} on the given path too.
   *
   * @param jsonData the {@link JsonObject} to write the array.
   * @param source the array to write.
   * @param type the component-type of the array.
   * @param context the context used for the array's values serialization.
   * @param path the path to where write the array.
   * @param <R> the expected component-type of the array.
   * @since 1.0.0
   */
  default <R> void writeObjectArray(
      final JsonObject jsonData,
      final R @Nullable [] source,
      final Class<R> type,
      final @Nullable JsonSerializationContext context,
      final String path) {
    if (source == null) {
      jsonData.add(path, null);
      return;
    }
    final JsonArray jsonArray = new JsonArray(source.length);
    final boolean isCustomType = type != String.class && type != Integer.class;
    if (isCustomType && context == null) {
      jsonData.add(path, null);
      return;
    }
    for (final R element : source) {
      if (isCustomType) {
        jsonArray.add(context.serialize(element, type));
        continue;
      }
      if (type == String.class) {
        jsonArray.add((String) element);
        continue;
      }
      jsonArray.add((int) element);
    }
    jsonData.add(path, jsonArray);
  }

  /**
   * Writes the given list's contents to a {@link JsonArray} object with an expected capacity (if available),
   * and returns it as a {@link JsonElement} object.
   * <p>
   * If the {@code source} parameter is null a {@link JsonNull} will be returned instead.
   *
   * @param <R> the expected component-type for the list.
   * @param source the list to read for the writing.
   * @param context the context used for the list's values serialization.
   * @return a {@link JsonElement} that may be a {@link JsonArray} if the list was written correctly.
   * Otherwise, it'll be a {@link JsonNull}.
   * @see #writeIteratorWithoutKnownCapacity(Iterator, JsonSerializationContext) No expected-capacity List writing
   * @see #writeIteratorWithCapacity(Iterator, int, JsonSerializationContext) With expected-capacity List writing
   * @since 1.0.0
   */
  default <R> JsonElement writeList(final @Nullable List<R> source, final JsonSerializationContext context) {
    return (source == null)
        ? this.writeIteratorWithoutKnownCapacity(null, context)
        : this.writeIteratorWithCapacity(source.iterator(), source.size(), context);
  }

  /**
   * Writes the given {@link Iterator}'s contents to a {@link JsonArray} object without a known/specified
   * capacity, and returns it as a {@link JsonElement} object.
   * <p>
   * If the {@code source} parameter is null a {@link JsonNull} will be returned instead.
   *
   * @param <R> the expected component-type for the iterator.
   * @param source the iterator-object to read for the writing.
   * @param context the context used for the iterator's values serialization.
   * @return a {@link JsonElement} that may be a {@link JsonArray} if the iterator was written correctly.
   * Otherwise, it'll be a {@link JsonNull}.
   * @see #writeIteratorWithCapacity(Iterator, int, JsonSerializationContext) Iterator contents writing
   * @since 1.0.0
   */
  default <R> JsonElement writeIteratorWithoutKnownCapacity(final @Nullable Iterator<R> source, final JsonSerializationContext context) {
    return this.writeIteratorWithCapacity(source, -1, context);
  }

  /**
   * Writes the given {@link Iterator}'s contents to a {@link JsonArray} object with an expected capacity,
   * and returns it as a {@link JsonElement} object.
   * <p>
   * If the {@code source} parameter is null a {@link JsonNull} will be returned instead.
   *
   * @param <R> the expected component-type for the iterator.
   * @param source the iterator-object to read for the writing.
   * @param capacity the expected capacity for the written internal JSON array, or {@code -1} if no capacity
   *                 is known, or expected for the JSON-array.
   * @param context the context used for the iterator's values serialization.
   * @return a {@link JsonElement} that may be a {@link JsonArray} if the iterator was written correctly.
   * Otherwise, it'll be a {@link JsonNull}.
   * @since 1.0.0
   */
  default <R> JsonElement writeIteratorWithCapacity(
      final @Nullable Iterator<R> source,
      final int capacity,
      final JsonSerializationContext context) {
    if (source == null) return JsonNull.INSTANCE;

    final JsonArray jsonArray = capacity == -1 ? new JsonArray() : new JsonArray(capacity);
    R next;
    while (source.hasNext()) {
      next = source.next();
      jsonArray.add(context.serialize(next, next.getClass()));
    }
    return jsonArray;
  }
}
