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
package me.aivr.commons.aggregate.infrastructure.repository.mongo.codec;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.Decoder;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.Encoder;
import org.bson.codecs.EncoderContext;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface MongoCodec<T> extends Codec<T> {
  @SuppressWarnings("unchecked")
  default <R> R @Nullable [] readArray(
      final BsonReader reader,
      final int expectedCapacity,
      final DecoderContext context,
      final Decoder<R> dedicatedDecoder) {
    if (reader.readBsonType() != BsonType.ARRAY) return null;

    final R[] array = expectedCapacity == -1 ? (R[]) new Object[]{} : (R[]) new Object[expectedCapacity];
    reader.readStartArray();
    byte index = 0;
    while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
      array[index] = context.decodeWithChildContext(dedicatedDecoder, reader);
    }
    reader.readEndArray();
    return array;
  }

  default String @Nullable [] readStringArray(final BsonReader reader, final int expectedCapacity) {
    if (reader.readBsonType() != BsonType.ARRAY) return null;

    final String[] array = new String[expectedCapacity];
    reader.readStartArray();
    byte index = 0;
    while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
      array[index] = reader.readString();
    }
    reader.readEndArray();
    return array;
  }

  /**
   * Reads an array, if available, from the {@link BsonReader} and converts it into a {@link List} of
   * {@link String}.
   *
   * @param reader the {@link BsonReader} from where read the array for the list.
   * @return a new {@link List} of {@link String}s if found, otherwise {@code null} if no array exist.
   * @since 0.0.1
   */
  default @Nullable List<String> readStringList(final BsonReader reader) {
    if (reader.readBsonType() != BsonType.ARRAY) return null;

    final List<String> values = new ObjectArrayList<>();
    reader.readStartArray();
    while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
      values.add(reader.readString());
    }
    reader.readEndArray();
    return values;
  }

  /**
   * Reads an array, if available, from the {@link BsonReader} and converts it into a {@link IntList}.
   *
   * @param reader the {@link BsonReader} from where read the array for the list.
   * @return the read {@link IntList} if found, otherwise {@code null} if no array exist.
   * @since 0.0.1
   */
  default @Nullable IntList readIntegerList(final BsonReader reader) {
    if (reader.readBsonType() != BsonType.ARRAY) return null;

    final IntList values = new IntArrayList();
    reader.readStartArray();
    while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
      values.add(reader.readInt32());
    }
    reader.readEndArray();
    return values;
  }

  /**
   * Reads an array, if available, from the {@link BsonReader} and converts it into a {@link List}.
   *
   * @param <R> the expected component-type for the list.
   * @param reader the {@link BsonReader} from where read the array for the list.
   * @param context the context used for the list's values deserialization.
   * @param dedicatedDecoder the decoder used for specific-deserialization for the array's objects.
   * @return a new {@link List} of {@link R} values, or {@code null} if the current reader's position is not
   * of {@link BsonType#ARRAY} type.
   * @since 0.0.1
   */
  default <R> @Nullable List<R> readList(final BsonReader reader, final DecoderContext context, final Decoder<R> dedicatedDecoder) {
    if (reader.readBsonType() != BsonType.ARRAY) return null;

    final List<R> values = new ObjectArrayList<>();
    // Mongo requires to indicate it we're gonna read an array, so we need to call the function before
    // read any array.
    reader.readStartArray();
    while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
      // Delegate object-decoding so we can use any other custom decoder if needed.
      values.add(context.decodeWithChildContext(dedicatedDecoder, reader));
    }
    // And we need to tell it when we stop reading the array.
    reader.readEndArray();
    return values;
  }

  /**
   * Reads an array, from the {@link BsonReader} and converts it into a {@link List}.
   * <p>
   * This function will return a new modifiable empty-list as possible result.
   *
   * @param <R> the expected component-type for the list.
   * @param reader the {@link BsonReader} from where read the array for the list.
   * @param context the context used for the list's values deserialization.
   * @param dedicatedDecoder the decoder used for specific-deserialization for the array's objects.
   * @return the {@link List} read from the array, otherwise a {@link ObjectArrayList} is returned instead.
   * @see #readList(BsonReader, DecoderContext, Decoder) List-reading from document
   * @since 0.0.1
   */
  default <R> List<R> readListOrEmpty(final BsonReader reader, final DecoderContext context, final Decoder<R> dedicatedDecoder) {
    final List<R> values = this.readList(reader, context, dedicatedDecoder);
    return values == null ? new ObjectArrayList<>() : values;
  }

  default <R> void writeCollectionAsArray(
      final BsonWriter writer,
      final String name,
      final Collection<R> values,
      final EncoderContext context,
      final Encoder<R> dedicatedEncoder) {
    writer.writeStartArray(name);
    for (final R element : values) {
      context.encodeWithChildContext(dedicatedEncoder, writer, element);
    }
    writer.writeEndArray();
  }

  /**
   * Writes the given string-list's contents to a document as an array, using the provided {@link BsonWriter}
   * parameter.
   * <p>
   * If the {@code source} is null then the {@code writer} will write a {@link BsonType#NULL} for the given
   * {@code path} for this function.
   *
   * @param writer the {@link BsonWriter} to write the list's contents.
   * @param source the list to read for the writing.
   * @param name the path where write the list's contents to in the document.
   * @see #writeStringIterator(BsonWriter, Iterator, String) Iterator-string writing
   * @since 0.0.1
   */
  default void writeStringList(final BsonWriter writer, final @Nullable List<String> source, final String name) {
    this.writeStringIterator(writer, source == null ? null : source.iterator(), name);
  }

  /**
   * Writes the given string-iterator's contents to a document as an array, using the provided
   * {@link BsonWriter} parameter.
   * <p>
   * If the {@code source} is null then the {@code writer} will write a {@link BsonType#NULL} for the given
   * {@code path} for this function.
   *
   * @param writer the {@link BsonWriter} to write the iterator's contents.
   * @param source the iterator to read for the writing.
   * @param name the path where write the iterator's contents to in the document.
   * @since 0.0.1
   */
  default void writeStringIterator(final BsonWriter writer, final @Nullable Iterator<String> source, final String name) {
    if (source == null) {
      writer.writeNull(name);
      return;
    }
    writer.writeStartArray(name);
    while (source.hasNext()) {
      writer.writeString(source.next());
    }
    writer.writeEndArray();
  }

  /**
   * Writes the given int-list's contents to a document as an array, using the provided {@link BsonWriter}
   * parameter.
   * <p>
   * If the {@code source} is null then the {@code writer} will write a {@link BsonType#NULL} for the given
   * {@code path} for this function.
   *
   * @param writer the {@link BsonWriter} to write the iterator's contents.
   * @param source the list to read for the writing.
   * @param name the path where write the list's contents to in the document.
   * @see #writeIntegerIterator(BsonWriter, IntListIterator, String) Integer-iterator writing
   * @since 0.0.1
   */
  default void writeIntegerList(final BsonWriter writer, final @Nullable IntList source, final String name) {
    this.writeIntegerIterator(writer, source == null ? null : source.iterator(), name);
  }

  /**
   * Writes the given int-iterator's contents to a document as an array, using the provided {@link BsonWriter}
   * parameter.
   * <p>
   * If the {@code source} is null then the {@code writer} will write a {@link BsonType#NULL} for the given
   * {@code path} for this function.
   *
   * @param writer the {@link BsonWriter} to write the iterator's contents.
   * @param source the iterator to read for the writing.
   * @param name the path where write the iterator's contents to in the document.
   * @since 0.0.1
   */
  default void writeIntegerIterator(final BsonWriter writer, final @Nullable IntListIterator source, final String name) {
    if (source == null) {
      writer.writeNull(name);
      return;
    }
    // Required by Mongo before actual writing.
    writer.writeStartArray(name);
    while (source.hasNext()) {
      writer.writeInt32(source.nextInt());
    }
    writer.writeEndArray();
  }

  /**
   * Writes the given list's contents to a document as an array, using the provided {@link BsonWriter} object.
   * <p>
   * If the {@code source} parameter is {@code null}, the {@link BsonWriter} will write {@code null} instead.
   *
   * @param <R> the expected component-type for the list.
   * @param writer the {@link BsonWriter} to write the list's data.
   * @param source the list to read for the writing.
   * @param context the context used for the list's values serialization.
   * @param dedicatedEncoder the encoder to use for specific-serialization of list's objects.
   * @param name the name to use by the {@code writer} for the array.
   * @see #writeIterator(BsonWriter, Iterator, EncoderContext, Encoder, String) Iterator contents writing
   * @since 0.0.1
   */
  default <R> void writeList(
      final BsonWriter writer,
      final @Nullable List<R> source,
      final EncoderContext context,
      final Encoder<R> dedicatedEncoder,
      final String name) {
    this.writeIterator(writer, source == null ? null : source.iterator(), context, dedicatedEncoder, name);
  }

  /**
   * Writes the given {@link Iterator}'s contents to a document as an array, using the provided
   * {@link BsonWriter} object.
   * <p>
   * If the {@code source} parameter is {@code null}, the {@link BsonWriter} will write {@code null} instead.
   *
   * @param <R> the expected component-type for the list.
   * @param source the iterator to read for the writing.
   * @param context the context used for the list's values serialization.
   * @param dedicatedEncoder the encoder to use for specific-serialization of list's objects.
   * @param name the name to use by the {@code writer} for the array.
   * @since 0.0.1
   */
  default <R> void writeIterator(
      final BsonWriter writer,
      final @Nullable Iterator<R> source,
      final EncoderContext context,
      final Encoder<R> dedicatedEncoder,
      final String name) {
    if (source == null) {
      writer.writeNull(name);
      return;
    }
    // Mongo requires to indicate it we're gonna read an array, so we need to call the function before
    // read any array.
    writer.writeStartArray(name);
    while (source.hasNext()) {
      // Pretty the same BSON requirement explained in [readList] function.
      context.encodeWithChildContext(dedicatedEncoder, writer, source.next());
    }
    writer.writeEndArray();
  }
}
