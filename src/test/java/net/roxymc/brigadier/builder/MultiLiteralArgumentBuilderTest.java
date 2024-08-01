package net.roxymc.brigadier.builder;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import org.junit.Before;
import org.junit.Test;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static net.roxymc.brigadier.builder.MultiLiteralArgumentBuilder.multiLiteral;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

public class MultiLiteralArgumentBuilderTest {
    private LiteralArgumentBuilder<Object> rootBuilder;
    private MultiLiteralArgumentBuilder<Object> builder;

    @Before
    public void setUp() throws Exception {
        rootBuilder = literal("root");
        builder = multiLiteral("test", "roxymc", "brigadier");
    }

    @Test
    public void test() {
        rootBuilder.then(builder);

        assertThat(rootBuilder.build().getChildren(), hasSize(3));
    }
}
