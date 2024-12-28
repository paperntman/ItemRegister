package org.sudaping.itemevent;
import com.oheers.fish.api.reward.EMFRewardsLoadEvent;
import com.oheers.fish.api.reward.RewardType;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EMFAddon implements RewardType, Listener {


    /**
     * The reward itself is handled here.
     * @param player The player this reward is meant for.
     * @param key The key of this reward, should match the identifier.
     * @param value The value of this reward. For example EXP:5000, 5000 is the value.
     * @param location The location of the player's fishing hook.
     */

    @Override
    public void doReward(@NotNull Player player, @NotNull String key, @NotNull String value, Location location) {
        boolean given = Prefix.givePrefix(player.getUniqueId(), value);
        Main.logger.info("gave prefix "+value+" to "+player.getUniqueId());
        if (given) player.sendMessage(Component.text("칭호 ")
                .append(Prefix.getPrefixMap().get(value))
                .append(Component.text("를 획득했습니다!")));
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

    @EventHandler
    public void onLoad(EMFRewardsLoadEvent event){
        this.register();
    }
}
