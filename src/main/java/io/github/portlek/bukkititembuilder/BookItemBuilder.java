package io.github.portlek.bukkititembuilder;

import java.util.Arrays;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BookItemBuilder {

    @NotNull
    private final ItemStackBuilder builder;

    @NotNull
    private final BookMeta bookMeta;

    public BookItemBuilder(@NotNull final ItemStackBuilder builder, @NotNull final BookMeta bookMeta) {
        this.builder = builder;
        this.bookMeta = bookMeta;
    }

    @NotNull
    public BookItemBuilder title(@Nullable final String title) {
        return this.change(() ->
            this.bookMeta.setTitle(title));
    }

    @NotNull
    private BookItemBuilder change(@NotNull final Runnable runnable) {
        runnable.run();
        this.builder.setItemMeta(this.bookMeta);
        return this;
    }

    @NotNull
    public BookItemBuilder generation(@Nullable final BookMeta.Generation generation) {
        return this.change(() ->
            this.bookMeta.setGeneration(generation));
    }

    @NotNull
    public BookItemBuilder setPage(final int page, @NotNull final String text) {
        return this.change(() ->
            this.bookMeta.setPage(page, text));

    }

    @NotNull
    public BookItemBuilder addPages(@NotNull final String... list) {
        return this.change(() ->
            this.bookMeta.addPage(list));
    }

    @NotNull
    public BookItemBuilder setPages(@NotNull final String... list) {
        return this.setPages(Arrays.asList(list));
    }

    @NotNull
    public BookItemBuilder setPages(@NotNull final List<String> list) {
        return this.change(() ->
            this.bookMeta.setPages(list));
    }

    @NotNull
    public BookItemBuilder author(@Nullable final String author) {
        return this.change(() ->
            this.bookMeta.setAuthor(author));
    }

    @NotNull
    public ItemStack build() {
        return this.builder.build();
    }

}
