/*
 * This source file is part of BetterModel.
 * Copyright (c) 2025 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.model.api.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Reflection util
 */
@ApiStatus.Internal
public final class ReflectionUtil {
    /**
     * No initializer
     */
    private ReflectionUtil() {
        throw new RuntimeException();
    }

    /**
     * Checks some class is existing.
     * @param clazz class path
     * @return exists
     */
    public static boolean classExists(@NotNull String clazz) {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Checks some method is existing.
     * <p>
     * Use this instead of {@link #classExists(String)} when the point is to know whether an API can
     * actually be called: a fork may ship a class of another platform as a stub without any of the
     * API surface that goes with it.
     *
     * @param clazz class path
     * @param method method name
     * @param parameters method parameter types
     * @return exists
     */
    public static boolean methodExists(@NotNull String clazz, @NotNull String method, @NotNull Class<?>... parameters) {
        try {
            Class.forName(clazz).getMethod(method, parameters);
            return true;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }
    }
}
