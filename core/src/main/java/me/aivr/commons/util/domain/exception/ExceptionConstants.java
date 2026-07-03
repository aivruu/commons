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
package me.aivr.commons.util.domain.exception;

import org.jetbrains.annotations.ApiStatus;

/**
 * Provides cached-exceptions for reuse on generic-cases where exceptions could be used repeatedly.
 *
 * @since 3.0.0-rc1
 */
@ApiStatus.Experimental
public final class ExceptionConstants {
  /**
   * Cached {@link IllegalStateException} for when not-initialized fields are tried to be accessed.
   *
   * @since 3.0.0-rc1
   */
  public static final IllegalStateException NOT_INITIALIZED_FIELD_EXCEPTION
      = new IllegalStateException("The expected field couldn't be accessed due to not being initialized.");
  /**
   * Cached {@link IllegalArgumentException} for when incorrect-values are passed either to a constructor or function.
   *
   * @since 3.0.0-rc1
   */
  public static final IllegalArgumentException WRONG_VALUE_EXCEPTION
      = new IllegalArgumentException("The provided value is not valid.");
  public static final UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION
      = new UnsupportedOperationException("This operation is not supported by this class.");
  public static final UnsupportedOperationException NOT_INSTANTIABLE_EXCEPTION
      = new UnsupportedOperationException("This class is for utility and cannot be instantiated.");

  private ExceptionConstants() {
    throw NOT_INSTANTIABLE_EXCEPTION;
  }
}
