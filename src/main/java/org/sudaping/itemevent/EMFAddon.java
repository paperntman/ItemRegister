package org.sudaping.itemevent;
import com.oheers.fish.api.reward.RewardType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EMFAddon implements RewardType{


    /**
     * The reward itself is handled here.
     * @param player The player this reward is meant for.
     * @param key The key of this reward, should match the identifier.
     * @param value The value of this reward. For example EXP:5000, 5000 is the value.
     * @param location The location of the player's fishing hook.
     */

    @Override
    public void doReward(@NotNull Player player, @NotNull String key, @NotNull String value, Location location) {

    }

    @Override
    public @NotNull String getIdentifier() {
        return "PREFIX";
    }

    @Override
    public @NotNull String getAuthor() {
        return "topen0330";
    }

    @Override
    public @NotNull JavaPlugin getPlugin() {
        return Main.plugin;
    }
}
