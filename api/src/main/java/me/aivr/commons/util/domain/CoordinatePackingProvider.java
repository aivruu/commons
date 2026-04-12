package me.aivr.commons.util.domain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jspecify.annotations.Nullable;

/**
 * Utility-class to compress and decompress {@link Location} coordinates-values using bit-shifting.
 * <p>
 * Functions within this class uses {@link Location}'s {@code int} coordinates to handle packing/unpacking, using the
 * {@code double} coordinates may lead to inconsistent values or unexpected results.
 *
 * @since 2.1.0
 */
public final class CoordinatePackingProvider {
  // Required-bits related constants.
  /**
   * The amount of bits it takes {@code x} to be represented.
   *
   * @since 2.1.0
   */
  private static final int X_BITS = 26;
  /**
   * The amount of bits it takes {@code z} to be represented.
   *
   * @since 2.1.0
   */
  private static final int Z_BITS = 26;
  /**
   * The amount of bits it takes {@code y} to be represented.
   *
   * @since 2.1.0
   */
  private static final int Y_BITS = 9;

  // Bit-mask related operations.

  /**
   * A bit-mask used by {@link #pack(int, int, int)} for the {@code x} coordinate packing.
   *
   * @since 2.1.0
   */
  private static final long X_BIT_MASK = ((1L << X_BITS) - 1);
  /**
   * A bit-mask used by {@link #pack(int, int, int)} for the {@code z} coordinate packing.
   *
   * @since 2.1.0
   */
  private static final long Z_BIT_MASK = ((1L << Z_BITS) - 1);
  /**
   * A bit-mask used by {@link #pack(int, int, int)} for the {@code y} coordinate packing.
   *
   * @since 2.1.0
   */
  private static final long Y_BIT_MASK = ((1L << Y_BITS) - 1);

  // Min and max range-related constants.

  /**
   * The max-range that can support {@code x}.
   *
   * @since 2.1.0
   */
  private static final int X_MAX = (1 << (X_BITS - 1)) - 1;
  /**
   * The min-range that can support {@code x}.
   *
   * @since 2.1.0
   */
  private static final int X_MIN = -(1 << (X_BITS - 1));
  /**
   * The max-range that can support {@code y}.
   *
   * @since 2.1.0
   */
  private static final int Y_MAX = (1 << (Y_BITS - 1)) - 1;
  /**
   * The min-range that can support {@code y}.
   *
   * @since 2.1.0
   */
  private static final int Y_MIN = -(1 << (Y_BITS - 1));
  /**
   * The max-range that can support {@code z}.
   *
   * @since 2.1.0
   */
  private static final int Z_MAX = (1 << (Z_BITS - 1)) - 1;
  /**
   * The min-range that can support {@code z}.
   *
   * @since 2.1.0
   */
  private static final int Z_MIN = -(1 << (Z_BITS - 1));

  /**
   * Calls to the {@link #pack(int, int, int)} function providing the given location's values.
   *
   * @param location the location to which pack its values.
   * @return a {@code long} that represents the packed values, or {@code -1} if a coordinate is out of bound.
   * @see #pack(int, int, int) Internal coordinate-packing operation
   * @since 2.1.0
   */
  public static long packLocation(final Location location) {
    return CoordinatePackingProvider.pack(location.getBlockX(), location.getBlockY(), location.getBlockZ());
  }

  /**
   * Performs a bit-packing operation over the provided coordinates ensuring they remain within the bounds for the built-in
   * values provided in this class, if some coordinate is out of bound the function will terminate and no operation will be
   * performed at all.
   * <p>
   * The 'how' this function works is by fitting the bits of the three coordinates into a single {@code long} while still allowing
   * to have a quite-usable range for the coordinates, each coordinate's bits are shifted based on the bits they'll take.
   * <p>
   * {@code x} would take the most part of the bits (MSB) to represent the highest coordinate, {@code z} is also ensured to fit
   * within the range defined by the mask {@link #Z_BIT_MASK} and would be shifted before the bits of {@code y}. {@code y} is not
   * shifted at all and just takes the remaining bits of the {@code long} on the far-right (LSB).
   * <p>
   * After that process, the three shifted values are packed into a single {@code long} value, this value contains the three
   * main-coordinates so {@link Location}s can still be rebuilt from packed-values safely.
   *
   * @param x the location's X coordinate.
   * @param y the location's Y coordinate.
   * @param z the location's Z coordinate.
   * @return a {@code long} that contains the coordinates' information (packed), or {@code -1} if a coordinate is out of bounds.
   * @see #outOfBounds(int, int, int) Coordinate-range check operation
   * @since 2.1.0
   */
  public static long pack(int x, int y, int z) {
    if (!CoordinatePackingProvider.outOfBounds(x, X_MIN, X_MAX)) return -1;

    if (!CoordinatePackingProvider.outOfBounds(z, Z_MIN, Z_MAX)) {
      return -1;
    }
    if (!CoordinatePackingProvider.outOfBounds(y, Y_MIN, Y_MAX)) {
      return -1;
    }
    return (((x & X_BIT_MASK)) << (Y_BITS + Z_BITS)) | (((z & Z_BIT_MASK)) << Y_BITS) | ((y & Y_BIT_MASK));
  }

  private static boolean outOfBounds(final int value, final int min, final int max) {
    return value < min || value > max;
  }

  private static int restoreSignExtension(int value, final long bits) {
    // https://stackoverflow.com/questions/54675321/what-is-the-most-efficient-way-in-java-to-sign-extend-an-arbitrary-length-patter
    return ((value << (Integer.SIZE - bits)) >> (Integer.SIZE - bits));
  }

  /**
   * Retrieves the value for the {@code x} coordinate from the packed coordinates.
   *
   * @param packed the packed-coordinates from which retrieve the value.
   * @return the unpacked {@code x} value.
   * @see #restoreSignExtension(int, long) Sign-applying for unpacked-value
   * @since 2.1.0
   */
  public static int unpackX(final long packed) {
    // -> 9 + 26 = 35
    final int x = (int) ((packed >> (Y_BITS + Z_BITS)) & X_BIT_MASK);
    return CoordinatePackingProvider.restoreSignExtension(x, X_BITS);
  }

  /**
   * Retrieves the value for the {@code z} coordinate from the packed coordinates.
   *
   * @param packed the packed-coordinates from which retrieve the value.
   * @return the unpacked {@code z} value.
   * @see #restoreSignExtension(int, long) Sign-applying for unpacked-value
   * @since 2.1.0
   */
  public static int unpackZ(final long packed) {
    final int z = (int) ((packed >> Y_BITS) & Z_BIT_MASK);
    return CoordinatePackingProvider.restoreSignExtension(z, Z_BITS);
  }

  /**
   * Retrieves the value for the {@code y} coordinate from the packed coordinates.
   *
   * @param packed the packed-coordinates from which retrieve the value.
   * @return the unpacked {@code y} value.
   * @see #restoreSignExtension(int, long) Sign-applying for unpacked-value
   * @since 2.1.0
   */
  public static int unpackY(final long packed) {
    final int y = (int) (packed & Y_BIT_MASK);
    return CoordinatePackingProvider.restoreSignExtension(y, Y_BITS);
  }

  /**
   * Rebuilds the {@link Location} object from the given packed information.
   *
   * @param worldName the name of the location's world.
   * @param packed the packed-coordinates from which retrieve the actual coordinates.
   * @return a new {@link Location}.
   * @see #unpackX(long) X-unpacking operation
   * @see #unpackY(long) Y-unpacking operation
   * @see #unpackZ(long) Z-unpacking operation
   * @since 2.1.0
   */
  public static Location unpackToLocation(final @Nullable String worldName, final long packed) {
    final int x = CoordinatePackingProvider.unpackX(packed);
    final int y = CoordinatePackingProvider.unpackY(packed);
    final int z = CoordinatePackingProvider.unpackZ(packed);
    return worldName == null ? new Location(null, x, y, z) : new Location(Bukkit.getWorld(worldName), x, y, z);
  }
}
