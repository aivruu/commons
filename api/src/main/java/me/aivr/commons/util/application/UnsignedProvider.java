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
package me.aivr.commons.util.application;

/**
 * Utility-class that provides functionality to "represent" and use "unsigned" {@code byte} values.
 * <p>
 * Built-in functions for this class works by ensuring the given value (take {@code int} as example) is in within the range on which
 * values can be represented using the {@code byte}'s 8 bits, so higher positive-values can be "represented" within the same data-type.
 *
 * @since 1.0.0
 */
public final class UnsignedProvider {
  /**
   * The maximum-value that can be represented as "unsigned" for a {@code byte}, also represented as decimal {@code 255}.
   *
   * @since 1.0.0
   */
  public static final int UNSIGNED_MASK = 0xFF;
  /**
   * An exception that's thrown if the value passed for the {@link #unsignedFromInt(int)} function is negative or exceeds
   * {@link #UNSIGNED_MASK} value.
   *
   * @since 1.0.0
   */
  private static final IllegalArgumentException OUT_OF_BOUNDS_EXCEPTION
      = new IllegalArgumentException("Provided value for unsigned conversion is out of bounds (0 to 255).");

  private UnsignedProvider() {
    throw new UnsupportedOperationException("This class is for utility and cannot be instantiated.");
  }

  /**
   * Returns an {@code int} representation of the given "unsigned" value.
   * <p>
   * This function performs a bit {@code AND} operation over the given value using the bit-mask {@link #UNSIGNED_MASK} to
   * ensure the value fits within the defined range and shows the actual value.
   *
   * @param value the value passed as "unsigned".
   * @return a signed {@code int} that represents the actual value.
   * @see #UNSIGNED_MASK
   * @since 1.0.0
   */
  public static int signedFromByte(final byte value) {
    // e.g. 00000011 & 11111111
    // -------- AND -------
    // = 00000011
    return value & UNSIGNED_MASK;
  }

  /**
   * Returns an "unsigned" byte to allow higher and non-negative capacity within the same data-type ({@code (-128 - 127) -> (0 - 255)}.
   * <p>
   * This function performs a bit {@code AND} operation over the given {@code value} using the max-range allowed for the
   * value -> {@link #UNSIGNED_MASK}, the value results in a {@code byte} that still represents values normally as negative
   * and positive, but that when is parsed to an {@code int} and ensuring the mask is applied, it shows the actual value.
   * <p>
   * This is useful for when needs to represent smaller positive-values with a {@code byte}, typically {@code 0} to {@code 255},
   * without having to use bigger types, like {@code short} or {@code int}.
   *
   * @throws IllegalArgumentException if given {@code value} parameter is negative or exceeds range for {@link #UNSIGNED_MASK} value.
   * @param value the value to use.
   * @return a {@code byte} that can "represent" values between from {@code 0} up to {@code 255}.
   * @see #UNSIGNED_MASK
   * @see #OUT_OF_BOUNDS_EXCEPTION
   * @since 1.0.0
   */
  public static byte unsignedFromInt(final int value) {
    if (value < 0 || value > UNSIGNED_MASK) throw OUT_OF_BOUNDS_EXCEPTION;
    return (byte) (value & UNSIGNED_MASK);
  }
}
