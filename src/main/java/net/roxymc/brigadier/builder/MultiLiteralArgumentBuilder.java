package net.roxymc.brigadier.builder;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.roxymc.brigadier.tree.MultiLiteralCommandNode;

public class MultiLiteralArgumentBuilder<S> extends ArgumentBuilder<S, MultiLiteralArgumentBuilder<S>> {
    private final String[] literals;

    protected MultiLiteralArgumentBuilder(final String[] literals) {
        this.literals = literals;
    }

    public static <S> MultiLiteralArgumentBuilder<S> multiLiteral(final String... literals) {
        return new MultiLiteralArgumentBuilder<>(literals);
    }

    @Override
    protected MultiLiteralArgumentBuilder<S> getThis() {
        return this;
    }

    public String[] getLiterals() {
        return literals;
    }

    @Override
    public MultiLiteralCommandNode<S> build() {
        final MultiLiteralCommandNode<S> result = new MultiLiteralCommandNode<>(getLiterals(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());

        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
