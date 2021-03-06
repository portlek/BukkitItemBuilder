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

package io.github.portlek.bukkititembuilder.util;

import com.cryptomorin.xseries.SkullUtils;
import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import io.github.portlek.bukkitversion.BukkitVersion;
import java.util.*;
import java.util.stream.Collectors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemStackUtil {

  private static final String[] AMOUNT_KEYS = {"amount", "quantity", "miktar"};

  private static final String[] DAMAGE_KEYS = {"damage", "durability"};

  private static final String[] DATA_KEYS = {"data"};

  private static final String[] DISPLAY_NAME_KEYS = {"name", "display", "display-name", "isim", "ad"};

  private static final String[] ENCHANTMENT_KEYS = {"enchants", "enchantments", "enchant", "enchantment", "büyü", "büyüler"};

  private static final String[] FLAG_KEYS = {"flags", "flag"};

  private static final String[] LORE_KEYS = {"lore", "açıklama"};

  private static final String[] MATERIAL_KEYS = {"material", "mat", "esya", "eşya", "id"};

  private static final String[] SKULL_TEXTURE_KEYS = {"skull", "skull-texture", "texture", "skin"};

  private static final int VERSION = new BukkitVersion()
    .minor();

  private ItemStackUtil() {
  }

  @NotNull
  public static Optional<ItemStack> from(@NotNull final Map<String, Object> map) {
    final Optional<String> materialStringOptional =
      ItemStackUtil.getOrDefault(map, String.class, ItemStackUtil.MATERIAL_KEYS);
    if (!materialStringOptional.isPresent()) {
      return Optional.empty();
    }
    final String materialString = materialStringOptional.get();
    @Nullable final Material material;
    if (ItemStackUtil.VERSION > 7) {
      final Optional<XMaterial> xmaterialoptional = XMaterial.matchXMaterial(materialString);
      if (!xmaterialoptional.isPresent()) {
        return Optional.empty();
      }
      final Optional<Material> mtrloptnl = Optional.ofNullable(xmaterialoptional.get().parseMaterial());
      if (!mtrloptnl.isPresent()) {
        return Optional.empty();
      }
      material = mtrloptnl.get();
    } else {
      material = Material.getMaterial(materialString);
    }
    if (material == null) {
      return Optional.empty();
    }
    final int amount = ItemStackUtil.getOrDefault(map, Number.class, ItemStackUtil.AMOUNT_KEYS)
      .map(Number::intValue)
      .orElse(1);
    final ItemStack itemStack;
    if (ItemStackUtil.VERSION < 13) {
      itemStack = new ItemStack(material, amount);
      ItemStackUtil.getOrDefault(map, Number.class, ItemStackUtil.DAMAGE_KEYS)
        .map(Number::shortValue)
        .ifPresent(itemStack::setDurability);
      ItemStackUtil.getOrDefault(map, Number.class, ItemStackUtil.DATA_KEYS)
        .map(Number::byteValue)
        .map(material::getNewData)
        .ifPresent(itemStack::setData);
    } else {
      itemStack = new ItemStack(material, amount);
      ItemStackUtil.getOrDefault(map, Number.class, ItemStackUtil.DAMAGE_KEYS).ifPresent(integer ->
        itemStack.setDurability(integer.shortValue()));
    }
    Optional.ofNullable(itemStack.getItemMeta()).ifPresent(itemMeta -> {
      if (itemMeta instanceof SkullMeta) {
        ItemStackUtil.getOrDefault(map, String.class, ItemStackUtil.SKULL_TEXTURE_KEYS).ifPresent(s ->
          SkullUtils.applySkin(itemMeta, s));
      }
      ItemStackUtil.getOrDefault(map, String.class, ItemStackUtil.DISPLAY_NAME_KEYS).ifPresent(s ->
        itemMeta.setDisplayName(ColorUtil.colored(s)));
      ItemStackUtil.getOrDefault(map, Collection.class, ItemStackUtil.LORE_KEYS)
        .map(list -> (Collection<String>) list)
        .map(ColorUtil::colored)
        .ifPresent(itemMeta::setLore);
      ItemStackUtil.getOrDefault(map, Map.class, ItemStackUtil.ENCHANTMENT_KEYS)
        .map(mp -> (Map<String, Integer>) mp)
        .ifPresent(mp ->
          mp.forEach((key, value) ->
            XEnchantment.matchXEnchantment(String.valueOf(key)).flatMap(xEnchantment ->
              Optional.ofNullable(xEnchantment.parseEnchantment())
            ).ifPresent(enchantment ->
              itemMeta.addEnchant(enchantment, value, true))));
      ItemStackUtil.getOrDefault(map, Collection.class, ItemStackUtil.FLAG_KEYS)
        .map(flags -> (Collection<String>) flags)
        .ifPresent(flags ->
          flags.stream()
            .map(ItemFlag::valueOf)
            .forEach(itemMeta::addItemFlags));
      itemStack.setItemMeta(itemMeta);
    });
    return Optional.of(itemStack);
  }

  @NotNull
  public static Map<String, Object> to(@NotNull final ItemStack itemStack) {
    final Map<String, Object> map = new HashMap<>();
    final String materialKey = ItemStackUtil.MATERIAL_KEYS[0];
    final String amountKey = ItemStackUtil.AMOUNT_KEYS[0];
    final String damageKey = ItemStackUtil.DAMAGE_KEYS[0];
    final String dataKey = ItemStackUtil.DATA_KEYS[0];
    final String displayNameKey = ItemStackUtil.DISPLAY_NAME_KEYS[0];
    final String loreKey = ItemStackUtil.LORE_KEYS[0];
    final String enchantmentKey = ItemStackUtil.ENCHANTMENT_KEYS[0];
    final String flagKey = ItemStackUtil.FLAG_KEYS[0];
    map.put(materialKey, itemStack.getType().toString());
    map.put(amountKey, itemStack.getAmount());
    if ((int) itemStack.getDurability() != 0) {
      map.put(damageKey, (int) itemStack.getDurability());
    }
    if (ItemStackUtil.VERSION < 13) {
      Optional.ofNullable(itemStack.getData())
        .filter(materialData -> (int) materialData.getData() != 0)
        .ifPresent(materialData ->
          map.put(dataKey, (int) materialData.getData()));
    }
    final String skullTextureKey = ItemStackUtil.SKULL_TEXTURE_KEYS[0];
    Optional.ofNullable(itemStack.getItemMeta()).ifPresent(itemMeta -> {
      if (itemMeta instanceof SkullMeta) {
        Optional.ofNullable(SkullUtils.getSkinValue(itemMeta)).ifPresent(s ->
          map.put(skullTextureKey, s));
      }
      if (itemMeta.hasDisplayName()) {
        map.put(displayNameKey, itemMeta.getDisplayName().replace("§", "&"));
      }
      Optional.ofNullable(itemMeta.getLore()).ifPresent(lore ->
        map.put(loreKey,
          lore.stream()
            .map(s -> s.replace("§", "&"))
            .collect(Collectors.toList())));
      final Set<ItemFlag> flags = itemMeta.getItemFlags();
      if (!flags.isEmpty()) {
        map.put(flagKey, flags.stream()
          .map(Enum::name)
          .collect(Collectors.toList()));
      }
    });
    final Map<String, Integer> enchantments = new HashMap<>();
    itemStack.getEnchantments().forEach((enchantment, integer) ->
      enchantments.put(enchantment.getName(), integer));
    if (!enchantments.isEmpty()) {
      map.put(enchantmentKey, enchantments);
    }
    return map;
  }

  @NotNull
  private static <T> Optional<T> getOrDefault(@NotNull final Map<String, Object> map, @NotNull final Class<T> tClass,
                                       @NotNull final String... keys) {
    return ItemStackUtil.getOrDefault(map, tClass, new LinkedList<>(Arrays.asList(keys)));
  }

  @NotNull
  private static  <T> Optional<T> getOrDefault(@NotNull final Map<String, Object> map, @NotNull final Class<T> tClass,
                                       @NotNull final LinkedList<String> keys) {
    final String key = keys.pollFirst();
    if (key == null) {
      return Optional.empty();
    }
    if (!map.containsKey(key)) {
      return ItemStackUtil.getOrDefault(map, tClass, keys);
    }
    final Object object = map.get(key);
    if (tClass.isAssignableFrom(object.getClass())) {
      // noinspection unchecked
      return Optional.of((T) object);
    }
    return ItemStackUtil.getOrDefault(map, tClass, keys);
  }
}
