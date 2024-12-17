package org.sudaping.flying;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Registration implements @Nullable CommandExecutor {

    public static Map<Component, String> componentMap = new HashMap<>();
    public static Logger logger;

    public Registration(){
        logger = JavaPlugin.getPlugin(Main.class).getLogger();
    }

    //itemevent command
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage(Component.text("권한이 없습니다!").color(NamedTextColor.RED));
            return true;
        }
        if (!sender.isOp()){
            sender.sendMessage(Component.text("권한이 없습니다!").color(NamedTextColor.RED));
            return true;
        }
        if (args.length == 0){
            sender.sendMessage(Component.text("태그를 지정해주세요!").color(NamedTextColor.RED));
            return true;
        }
        ItemStack targetItemStack = player.getInventory().getItemInMainHand();
        if (targetItemStack.getItemMeta() == null){
            sender.sendMessage(Component.text("아이템에 커스텀 이름이 없습니다!").color(NamedTextColor.RED));
            return true;
        }
        Component key = targetItemStack.getItemMeta().displayName();
        componentMap.put(key, args[0]);

        return true;
    }


    private static void compressGzipFile(File file, ItemStack itemStack) {
        try (FileOutputStream fos = new FileOutputStream(file);
             GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
            gzos.write(itemStack.serializeAsBytes());
        } catch (FileNotFoundException e) {
            logger.severe("File not found: " + e.getMessage());
        } catch (IOException e) {
            logger.severe("IO error: " + e.getMessage());
        }
    }

    public static byte[] decompressGzipFile(String filePath){
        // GZIP 파일을 읽기 위한 FileInputStream
        try (FileInputStream fis = new FileInputStream(filePath);
             GZIPInputStream gzis = new GZIPInputStream(fis);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;

            // GZIP 파일에서 데이터를 읽고 압축 해제
            while ((len = gzis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            return baos.toByteArray(); // 압축 해제된 바이트 배열 반환
        } catch (FileNotFoundException e) {
            logger.severe("File not found: " + e.getMessage());
        } catch (IOException e) {
            logger.severe("IO error: " + e.getMessage());
        }
        return null;
    }
}
