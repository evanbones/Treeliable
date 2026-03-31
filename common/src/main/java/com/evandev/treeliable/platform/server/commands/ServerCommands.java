package com.evandev.treeliable.platform.server.commands;

import com.evandev.treeliable.TreeliableException;
import com.evandev.treeliable.common.chop.ChopUtil;
import com.evandev.treeliable.common.util.LevelUtil;
import com.mojang.brigadier.CommandDispatcher;
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

        builder.then(Commands.literal("fell")
                .then(Commands.argument("chopPos", BlockPosArgument.blockPos())
                        .executes(ServerCommands::fell)));

        dispatcher.register(builder);
    }

    private static int fell(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        BlockPos pos = context.getArgument("chopPos", Coordinates.class).getBlockPos(source);

        try {
            boolean felled = !ChopUtil.chop(
                    source.getPlayer(),
                    source.getLevel(),
                    pos,
                    source.getLevel().getBlockState(pos),
                    ItemStack.EMPTY,
                    context,
                    10000,
                    false
            );

            if (felled) {
                LevelUtil.harvestBlock(source.getPlayer(), source.getLevel(), pos, ItemStack.EMPTY, true);
            }
        } catch (TreeliableException e) {
            source.sendFailure(Component.literal("Failed to fell tree: " + e.getMessage()));
        }

        return 1;
    }
}