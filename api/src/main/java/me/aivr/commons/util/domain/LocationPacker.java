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
package me.aivr.commons.util.domain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jspecify.annotations.Nullable;

/**
 * Utility-class to provide functions to perform bit-shifting over {@link Location} values to store compressed-coordinates
 * for home-points.
 *
 * @deprecated use {@link CoordinatePackingProvider} instead.
 * @since 1.0.0
 */
@Deprecated
public final class LocationPacker {
  /**
   * The maximum-value allowed to be packed for both {@code x} and {@code z} coordinates.
   * <p>
   * This value is represented in decimal as {@code 67108863}.
   *
   * @since 1.0.0
   */
  public static final long MAX_XZ_RANGE = 0x3FFFFFF;
  /**
   * The maximum-value allowed to be packed for the {@code y} coordinate.
   * <p>
   * This value is represented in decimal as {@code 4095}.
   *
   * @since 1.0.0
   */
  public static final long MAX_Y_RANGE = 0xFFF;
  /**
   * The amount of bits it requires {@code x} to be shifted over to accommodate it in a {@code long} value . This value depends on the
   * range desired for the coordinate -> {@link #MAX_XZ_RANGE}.
   *
   * @since 1.0.0
   */
  public static final int X_SHIFT_BITS = 38;
  /**
   * The amount of bits it requires {@code z} to be shifted over to accommodate it in a {@code long} value. This value depends on the
   * range desired for the coordinate -> {@link #MAX_XZ_RANGE}.
   *
   * @since 1.0.0
   */
  public static final int Z_SHIFT_BITS = 12;

  private LocationPacker() {
    throw new UnsupportedOperationException("This class is for utility and cannot be instantiated.");
  }

  /**
   * Performs a bit-packing operation over the coordinates (rotation not included) of this {@link Location}.
   *
   * @param location the location to which pack its values.
   * @return a {@code long} that represents the packed values.
   * @see #packCoordinates(int, int, int) Actual coordinate-packing operation
   * @since 1.0.0
   */
  public static long packLocation(final Location location) {
    return LocationPacker.packCoordinates(location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }

  /**
   * Performs a bit-packing operation over the provided coordinates ensuring they remain within limits for
   * the built-in range-values provided in this class.
   * <p>
   * The 'how' this function works is by fitting the bits of the three coordinates into a single {@code long} while still
   * allowing to have a quite-usable range for the coordinates, bit-shifting is performed over the amount of bits each coordinate
   * would need to represent in a specific range (check {@link #MAX_XZ_RANGE} and {@link #MAX_Y_RANGE}), but this amount is not
   * fixed for all three.
   * <p>
   * {@code x} would take the most part of the bits (MSB) to represent the highest coordinate and define the sign for the coordinate
   * (whether it must be positive or negative), {@code z} is also ensured to fit within the range defined by {@link #MAX_XZ_RANGE}
   * but it will use fewer bits than {@code x} as mostly part of them is already used for that coordinate, though {@code z} would
   * use fewer bits, it still can represent pretty-high values for the coordinate. {@code y} is not shifted at all and just takes the
   * remaining bits of the {@code long}, so it would be on the far right of the value (LSB) to accommodate the highest-possible value
   * for the location's height,
   * <p>
   * After that process, the three shifted values are packed into a single {@code long} value, this value contains all the
   * information related (but the world, obviously) and needed to re-build the location again.
   *)} instead.
   * @param x the location's X coordinate.
   * @param y the location's Y coordinate.
   * @param z the location's Z coordinate.
   * @return a {@code long} that contains the coordinates' information (packed).
   * @since 1.0.0
   */
  public static long packCoordinates(final int x, final int y, final int z) {
    return ((x & MAX_XZ_RANGE) << X_SHIFT_BITS) | ((z & MAX_XZ_RANGE) << Z_SHIFT_BITS) | (y & MAX_Y_RANGE);
  }

  /**
   * Retrieves the value for the {@code x} coordinate from the given packed value.
   * <p>
   * This function follows a similar operation to {@link #packCoordinates(int, int, int)}, it shifts the bits of the value
   * {@link #X_SHIFT_BITS} positions to retrieve the original value of {@code x}, and it ensures the value can fit within the
   * {@link #MAX_XZ_RANGE} value.
   *
   * @param packed the packed-coordinates from which retrieve the value.
   * @return the unpacked {@code x} value.
   * @since 1.0.0
   */
  public static int unpackX(final long packed) {
    return (int) ((packed >> X_SHIFT_BITS) & MAX_XZ_RANGE);
  }

  /**
   * Retrieves the value for the {@code y} coordinate from the given packed value.
   * <p>
   * This function follows a similar operation to {@link #packCoordinates(int, int, int)} for {@code y}, as this value is packed on
   * the far-right of the {@code long} (LSB) we only require to ensure the value can fit within the {@link #MAX_Y_RANGE} value, so
   * we can get the original Y value.
   *
   * @param packed the packed-coordinates from which retrieve the value.
   * @return the unpacked {@code y} value.
   * @since 1.0.0
   */
  public static int unpackY(final long packed) {
    return (int) (packed & MAX_Y_RANGE);
  }

  /**
   * Retrieves the value for the {@code z} coordinate from the given packed value.
   * <p>
   * This function follows a similar operation to {@link #packCoordinates(int, int, int)} for {@code z}, it shifts the bits of the
   * value {@link #Z_SHIFT_BITS} positions to retrieve the original value of {@code x}, and it ensures the value can fit within the
   * {@link #MAX_XZ_RANGE} value.
   *
   * @param packed the packed-coordinates from which retrieve the value.
   * @return the unpacked {@code z} value.
   * @since 1.0.0
   */
  public static int unpackZ(final long packed) {
    return (int) ((packed >> Z_SHIFT_BITS) & MAX_XZ_RANGE);
  }

  /**
   * Rebuilds the {@link Location} object from the given information.
   * <p>
   * This function won't specify a world nor head-rotation ({@code yaw} and {@code pitch}) values for the location.
   *
   * @param worldName the name of the location's world.
   * @param packed the packed-coordinates from which retrieve the actual coordinates.
   * @return a new {@link Location}.
   * @since 1.0.0
   */
  public static Location unpackToLocation(final @Nullable String worldName, final long packed) {
    final int x = LocationPacker.unpackX(packed);
    final int y = LocationPacker.unpackY(packed);
    final int z = LocationPacker.unpackZ(packed);
    return worldName == null ? new Location(null, x, y, z) : new Location(Bukkit.getWorld(worldName), x, y, z);
  }
}
