package net.kaupenjoe.tutorialmod;

import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.PathfindingConstants;
import net.kaupenjoe.tutorialmod.config.SpawnConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import static net.kaupenjoe.tutorialmod.TutorialMod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {
    public void init() {}

    public void clientInit() {
    }

    public void initPathfinding() {
        //PathfindingConstants.isDebugMode = true;
        PathfindingConstants.pathfindingThreads = Math.max(PathfindingConstants.pathfindingThreads, SpawnConfig.pathfindingThreads);
    }
}

