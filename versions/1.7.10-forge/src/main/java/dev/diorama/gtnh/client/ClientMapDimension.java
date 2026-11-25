package dev.diorama.gtnh.client;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;

import org.jspecify.annotations.Nullable;

/** Only the vanilla dimension identities with the audited 16-section storage span. */
enum ClientMapDimension {

    OVERWORLD(0, "overworld"),
    NETHER(-1, "nether"),
    END(1, "end");

    private final int id;
    private final String suffix;

    ClientMapDimension(int id, String suffix) {
        this.id = id;
        this.suffix = suffix;
    }

    int id() {
        return id;
    }

    String suffix() {
        return suffix;
    }

    boolean surfaceTerrain() {
        // A top-down ray would show the Nether's roof, not the region the player explores.
        return this != NETHER;
    }

    static @Nullable ClientMapDimension of(@Nullable WorldClient world) {
        if (world == null || world.provider == null || world.getHeight() != 256) return null;
        Class<?> type = world.provider.getClass();
        int id = world.provider.dimensionId;
        boolean noSky = world.provider.hasNoSky;
        // Modded Overworld providers may not have the exact vanilla provider class.
        if (id == OVERWORLD.id && !noSky) return OVERWORLD;
        if (type == WorldProviderHell.class && id == NETHER.id && noSky) return NETHER;
        if (type == WorldProviderEnd.class && id == END.id && noSky) return END;
        return null;
    }
}
