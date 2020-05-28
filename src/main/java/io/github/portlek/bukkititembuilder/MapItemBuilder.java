package io.github.portlek.bukkititembuilder;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MapItemBuilder extends Builder<MapItemBuilder, MapMeta> {

    public MapItemBuilder(@NotNull final ItemStack item, @NotNull final MapMeta meta) {
        super(item, meta);
    }

    @NotNull
    @Deprecated
    public MapItemBuilder mapId(final int id) {
        return this.update(meta ->
            meta.setMapId(id));
    }

    @NotNull
    public MapItemBuilder mapView(@NotNull final MapView mapView) {
        return this.update(meta ->
            meta.setMapView(mapView));
    }

    @NotNull
    public MapItemBuilder scaling(final boolean scaling) {
        return this.update(meta ->
            meta.setScaling(scaling));
    }

    @NotNull
    public MapItemBuilder locationName(@Nullable final String name) {
        return this.update(meta ->
            meta.setLocationName(name));
    }

    @NotNull
    public MapItemBuilder color(@Nullable final Color color) {
        return this.update(meta ->
            meta.setColor(color));
    }

    @NotNull
    @Override
    public MapItemBuilder get() {
        return this;
    }

}
