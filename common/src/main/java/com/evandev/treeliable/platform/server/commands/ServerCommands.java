package com.evandev.treeliable.platform.server.commands;

import com.evandev.treeliable.TreeliableException;
import com.evandev.treeliable.common.chop.ChopUtil;
import com.evandev.treeliable.common.util.LevelUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ServerCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("treeliable")
                .requires(source -> source.hasPermission(2));

        builder.then(Commands.literal("chop")
                .then(Commands.argument("chopPos", BlockPosArgument.blockPos())
                        .then(Commands.argument("chopCount", IntegerArgumentType.integer(0))
                                .executes(ServerCommands::chop))));

        builder.then(Commands.literal("fell")
                .then(Commands.argument("chopPos", BlockPosArgument.blockPos())
                        .executes(ServerCommands::fell)));

        dispatcher.register(builder);
    }

    private static int chop(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        BlockPos pos = context.getArgument("chopPos", Coordinates.class).getBlockPos(source);
        int numChops = context.getArgument("chopCount", Integer.class);

        chop(context, source, pos, numChops);

        return 1;
    }

    private static void chop(CommandContext<CommandSourceStack> context, CommandSourceStack source, BlockPos pos, int numChops) {
        try {
            boolean felled = !ChopUtil.chop(
                    source.getPlayer(),
                    source.getLevel(),
                    pos,
                    source.getLevel().getBlockState(pos),
                    ItemStack.EMPTY,
                    context,
                    numChops,
                    false
            );

            if (felled) {
                LevelUtil.harvestBlock(source.getPlayer(), source.getLevel(), pos, ItemStack.EMPTY, true);
            }
        } catch (TreeliableException e) {
            source.sendFailure(Component.literal("Failed to chop block: " + e.getMessage()));
        }
    }

    private static int fell(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        BlockPos pos = context.getArgument("chopPos", Coordinates.class).getBlockPos(source);

        chop(context, source, pos, 10000);

        return 1;
    }
}
