/*
 * MIT License
 *
 * Copyright (c) 2020 Hasan Demirtaş
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
  public MapItemBuilder color(@Nullable final Color color) {
    return this.update(meta ->
      meta.setColor(color));
  }

  @NotNull
  @Override
  public MapItemBuilder get() {
    return this;
  }

  @NotNull
  public MapItemBuilder locationName(@Nullable final String name) {
    return this.update(meta ->
      meta.setLocationName(name));
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
}
