package net.roxymc.brigadier.tree;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.roxymc.brigadier.builder.MultiLiteralArgumentBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * Shouldn't be directly registered.
 * {@link CommandNode#addChild(CommandNode)} should redirect a {@link MultiLiteralCommandNode} instance to {@link MultiLiteralCommandNode#buildLiteralNodes()}
 */
public class MultiLiteralCommandNode<S> extends CommandNode<S> {
    private final String[] literals;

    public MultiLiteralCommandNode(final String[] literals, final Command<S> command, final Predicate<S> requirement, final CommandNode<S> redirect, final RedirectModifier<S> modifier, final boolean forks) {
        super(command, requirement, redirect, modifier, forks);
        this.literals = literals;
    }

    public String[] getLiterals() {
        return literals;
    }

    @Override
    public String getName() {
        return Arrays.toString(literals);
    }

    @Override
    public void parse(final StringReader reader, final CommandContextBuilder<S> contextBuilder) throws CommandSyntaxException {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isValidInput(final String input) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof MultiLiteralCommandNode)) return false;

        final MultiLiteralCommandNode that = (MultiLiteralCommandNode) o;

        if (!Arrays.equals(literals, that.literals)) return false;
        return super.equals(o);
    }

    @Override
    public String getUsageText() {
        return Arrays.toString(literals);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(literals);
        result = 31 * result + super.hashCode();
        return result;
    }

    @Override
    public MultiLiteralArgumentBuilder<S> createBuilder() {
        final MultiLiteralArgumentBuilder<S> builder = MultiLiteralArgumentBuilder.multiLiteral(this.literals);
        builder.requires(getRequirement());
        builder.forward(getRedirect(), getRedirectModifier(), isFork());
        if (getCommand() != null) {
            builder.executes(getCommand());
        }
        return builder;
    }

    @Override
    protected String getSortedKey() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList(literals);
    }

    @Override
    public String toString() {
        return "<multiLiteral " + Arrays.toString(literals) + ">";
    }

    public Collection<LiteralCommandNode<S>> buildLiteralNodes() {
        List<LiteralCommandNode<S>> literalNodes = new ArrayList<>();

        for (String literal : literals) {
            LiteralCommandNode<S> literalNode = LiteralArgumentBuilder.<S>literal(literal)
                    .executes(getCommand())
                    .requires(getRequirement())
                    .forward(getRedirect(), getRedirectModifier(), isFork())
                    .build();

            getChildren().forEach(literalNode::addChild);

            literalNodes.add(literalNode);
        }

        return Collections.unmodifiableList(literalNodes);
    }
}
