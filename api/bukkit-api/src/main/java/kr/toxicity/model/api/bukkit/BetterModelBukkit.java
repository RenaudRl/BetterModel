/*
 * This source file is part of BetterModel.
 * Copyright (c) 2026 toxicity188
 * Licensed under the MIT License.
 * See LICENSE.md file for full license text.
 */

package kr.toxicity.model.api.bukkit;

import kr.toxicity.model.api.BetterModel;
import kr.toxicity.model.api.BetterModelPlatform;
import kr.toxicity.model.api.bukkit.platform.BukkitAdapter;
import kr.toxicity.model.api.bukkit.scheduler.BukkitModelScheduler;
import org.jetbrains.annotations.NotNull;

import static kr.toxicity.model.api.util.ReflectionUtil.classExists;
import static kr.toxicity.model.api.util.ReflectionUtil.methodExists;

/**
 * Represents the Bukkit-specific platform interface for BetterModel.
 * <p>
 * This interface extends {@link BetterModelPlatform} to provide Bukkit-specific implementations
 * for scheduling and entity adaptation.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * BetterModelBukkit platform = BetterModelBukkit.platform();
 * BukkitModelScheduler scheduler = platform.scheduler();
 * if (BetterModelBukkit.IS_FOLIA) {
 *     // Folia region-aware scheduling
 * }
 * }</pre>
 *
 * @since 2.0.0
 */
public interface BetterModelBukkit extends BetterModelPlatform {

    /**
     * Checks if the server is running on the Folia platform.
     * @since 2.0.0
     */
    boolean IS_FOLIA = classExists("io.papermc.paper.threadedregions.RegionizedServer");
    /**
     * Checks if the server is running on the Purpur platform.
     * <p>
     * The presence of {@code PurpurConfig} alone is not enough: BTC Core ships a stub of that class
     * in Purpur's own package so its ported Purpur feature patches keep compiling, while providing
     * none of {@code purpur-api}. Detecting on the class alone made BTC Core report itself as Purpur
     * and start {@code PurpurHook}, whose view filters call {@code Player#isAfk()} — a method that
     * does not exist there, so every filtered viewer would hit a {@link NoSuchMethodError}. The
     * second check is on the API this flag actually gates.
     * @since 2.0.0
     */
    boolean IS_PURPUR = classExists("org.purpurmc.purpur.PurpurConfig")
        && methodExists("org.bukkit.entity.Player", "isAfk");
    /**
     * Checks if the server is running on the BTC Core platform.
     * @since 2.0.0
     */
    boolean IS_BTC_CORE = classExists("com.infernalsuite.asp.api.BTCCoreAPI");
    /**
     * Checks if the server is running on the Paper platform (or a fork like Purpur/Folia/BTC Core).
     * @since 2.0.0
     */
    boolean IS_PAPER = IS_PURPUR || IS_FOLIA || IS_BTC_CORE || classExists("io.papermc.paper.configuration.PaperConfigurations");

    /**
     * Returns the current {@link BetterModelBukkit} instance.
     *
     * @return the current platform instance
     * @since 2.0.0
     */
    static @NotNull BetterModelBukkit platform() {
        return (BetterModelBukkit) BetterModel.platform();
    }

    /**
     * Returns the Bukkit-specific scheduler.
     *
     * @return the scheduler
     * @since 2.0.0
     */
    @Override
    @NotNull BukkitModelScheduler scheduler();

    /**
     * Returns the Bukkit-specific adapter.
     *
     * @return the adapter
     * @since 2.0.0
     */
    @Override
    @NotNull BukkitAdapter adapter();

    /**
     * Returns the Bukkit-specific event bus.
     *
     * @return the event bus
     * @since 2.0.0
     */
    @Override
    @NotNull BukkitModelEventBus eventBus();
}
