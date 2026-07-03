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
package me.aivr.commons.valueobject.domain;

import org.jspecify.annotations.Nullable;

/**
 * Represents a status-container that encapsulates a status-code with a result for a performed operation.
 *
 * @param status the status-code of the operation.
 * @param result the result the operation returned, can be {@code null} if the operation didn't return a result.
 * @param <T> a result of any type.
 * @since 1.0.0
 */
public record ValueObjectMutation<T>(byte status, @Nullable T result) {
  /**
   * The operation terminated on an unchanged-state of the object.
   *
   * @since 1.0.0
   */
  public static final byte UNCHANGED_STATUS = 0;
  /**
   * The operation terminated with a changed-state of the object.
   *
   * @since 1.0.0
   */
  public static final byte CHANGED_STATUS = 1;
  /**
   * The operation terminated with an error and no state was changed for the object.
   *
   * @since 1.0.0
   */
  public static final byte ERROR_STATUS = 2;

  /**
   * Creates and returns a {@link ValueObjectMutation} object with the {@link #UNCHANGED_STATUS} and with no result.
   *
   * @param <T> a result of any type, ignored for this function.
   * @return a {@link ValueObjectMutation} with the {@link #UNCHANGED_STATUS} and no result.
   * @since 1.0.0
   */
  public static <T> ValueObjectMutation<@Nullable T> unchanged() {
    return new ValueObjectMutation<>(UNCHANGED_STATUS, null);
  }

  /**
   * Creates and returns a {@link ValueObjectMutation} object with the {@link #CHANGED_STATUS} and given result.
   *
   * @param <T> a result of any type.
   * @return a {@link ValueObjectMutation} with the {@link #CHANGED_STATUS} and with a result.
   * @since 1.0.0
   */
  public static <T> ValueObjectMutation<T> changed(final T result) {
    return new ValueObjectMutation<>(CHANGED_STATUS, result);
  }

  /**
   * Creates and returns a {@link ValueObjectMutation} object with the {@link #ERROR_STATUS} and with no result.
   *
   * @param <T> a result of any type, ignored for this function.
   * @return a {@link ValueObjectMutation} with the {@link #ERROR_STATUS} and no result.
   * @since 1.0.0
   */
  public static <T> ValueObjectMutation<@Nullable T> error() {
    return new ValueObjectMutation<>(UNCHANGED_STATUS, null);
  }

  /**
   * Creates and returns a {@link ValueObjectMutation} object with the given status and result.
   *
   * @param <T> a result of any type.
   * @param status the status-code of the operation.
   * @param result the result of the operation, {@code null} if no result was provided.
   * @return a {@link ValueObjectMutation} with the given status-code and result.
   * @since 1.0.0
   */
  public static <T> ValueObjectMutation<@Nullable T> custom(final byte status, final @Nullable T result) {
    return new ValueObjectMutation<>(status, result);
  }

  /**
   * Checks whether the status-code for the operation was {@link #UNCHANGED_STATUS}.
   *
   * @return {@code true} if the operation didn't change the state of the object, {@code false} otherwise.
   * @since 1.0.0
   */
  public boolean remainedUnchanged() {
    return this.status == UNCHANGED_STATUS;
  }

  /**
   * Checks whether the status-code for the operation was {@link #CHANGED_STATUS}.
   *
   * @return {@code true} if the operation terminated correctly, {@code false} otherwise.
   * @since 1.0.0
   */
  public boolean success() {
    return this.status == CHANGED_STATUS;
  }

  /**
   * Checks whether the status-code for the operation was {@link #ERROR_STATUS}.
   *
   * @return {@code true} if the operation failed, {@code false} otherwise.
   * @since 1.0.0
   */
  public boolean failed() {
    return this.status == ERROR_STATUS;
  }

  /**
   * Checks whether the status-code provided was none of the built-in status-codes for this class.
   *
   * @param status the status-code to check.
   * @return {@code true} if the status-code was custom, {@code false} otherwise.
   * @since 1.0.0
   */
  public boolean isCustom(final byte status) {
    return (this.status != CHANGED_STATUS) && (this.status != UNCHANGED_STATUS) && (this.status != ERROR_STATUS);
  }
}
