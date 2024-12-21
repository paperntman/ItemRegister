package org.sudaping.itemevent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DiscordIntegration extends ListenerAdapter implements Listener{

    public static final Long channelID = 1319990370611761254L;
    public static JDA jda;
    public DiscordIntegration() {
        jda = JDABuilder
                .createDefault("MTIwMzY5NjE3ODg1MjUzMjI1NA.G9KW7l.ulj8poIEImZ6J1GcVYJ35l-a8Q52_EBfezs64w")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new DiscordListener())
                .build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        String message = ((TextComponent) event.message()).content();
        // 디스코드 채널에 메시지 전송
        TextChannel channel = jda.getTextChannelById(channelID);
        if (channel != null) {
            channel.sendMessage("<"+event.getPlayer().getName()+"> "+message).queue();
        }
    }
}

// 디스코드 메시지 리스너
class DiscordListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if (event.getChannel().getIdLong()==(DiscordIntegration.channelID)) {
            // 마인크래프트 서버에 메시지 전송
            Bukkit.getServer().broadcast(Component.text("[Discord] <" + event.getAuthor().getName() +"> "+ event.getMessage().getContentDisplay()));
        }
    }
}
