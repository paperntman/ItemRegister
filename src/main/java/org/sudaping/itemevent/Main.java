package org.sudaping.itemevent;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.sudaping.itemevent.ci.CustomInventoryCommand;
import org.sudaping.itemevent.ci.CustomInventoryCommandMoveListener;
import org.sudaping.itemevent.ci.CustomInventoryListener;
import org.sudaping.itemevent.ci.CustomInventoryTab;
import org.sudaping.itemevent.cr.CustomRecipeCommand;
import org.sudaping.itemevent.cr.CustomRecipeTab;
import org.sudaping.itemevent.cr.DebugInventoryListener;
import org.sudaping.itemevent.cr.RecipeListener;
import org.sudaping.itemevent.cr.crv.RecipeViewCommand;
import org.sudaping.itemevent.cr.crv.RecipeViewTab;
import org.sudaping.itemevent.db.DebugDataCommand;
import org.sudaping.itemevent.eventListeners.*;
import org.sudaping.itemevent.help.HELPTab;
import org.sudaping.itemevent.pd.PersistentDataTab;
import org.sudaping.itemevent.pd.item.*;
import org.sudaping.itemevent.fly.FlyCommand;
import org.sudaping.itemevent.help.HelpCommand;
import org.sudaping.itemevent.pd.PersistentDataCommand;
import org.sudaping.itemevent.prefix.PrefixCommand;
import org.sudaping.itemevent.prefix.PrefixGetEvent;
import org.sudaping.itemevent.prefix.PrefixInventoryListener;
import org.sudaping.itemevent.prefix.PrefixTab;
import org.sudaping.itemevent.ra.ItemCraftListener;
import org.sudaping.itemevent.ra.RecipeAnnouncementCommand;
import org.sudaping.itemevent.ra.RecipeAnnouncementTab;
import org.sudaping.itemevent.fly.Fly;

import java.io.*;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class Main extends JavaPlugin{

    public static File dataFolder;
    public static Main plugin;
    public static Logger logger;

    public static void compressGzipFile(File file, ItemStack itemStack) {
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


    @Override
    public void onEnable() {
        dataFolder = getDataFolder();
        Archive.loadWaiting();
        logger = JavaPlugin.getPlugin(Main.class).getLogger();
        plugin = this;

        Objects.requireNonNull(getCommand("customrecipe")).setExecutor(new CustomRecipeCommand());
        Objects.requireNonNull(getCommand("customrecipe")).setTabCompleter(new CustomRecipeTab());
        Objects.requireNonNull(getCommand("debugdata")).setExecutor(new DebugDataCommand());
        Objects.requireNonNull(getCommand("recipeannouncement")).setExecutor(new RecipeAnnouncementCommand());
        Objects.requireNonNull(getCommand("recipeannouncement")).setTabCompleter(new RecipeAnnouncementTab());
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand());
        Objects.requireNonNull(getCommand("커스텀레시피")).setExecutor(new RecipeViewCommand());
        Objects.requireNonNull(getCommand("커스텀레시피")).setTabCompleter(new RecipeViewTab());
        Objects.requireNonNull(getCommand("persistentdata")).setExecutor(new PersistentDataCommand());
        Objects.requireNonNull(getCommand("persistentdata")).setTabCompleter(new PersistentDataTab());
        Objects.requireNonNull(getCommand("custominventory")).setExecutor(new CustomInventoryCommand());
        Objects.requireNonNull(getCommand("custominventory")).setTabCompleter(new CustomInventoryTab());
        Objects.requireNonNull(getCommand("help")).setExecutor(new HelpCommand());
        Objects.requireNonNull(getCommand("help")).setTabCompleter(new HELPTab());
        Objects.requireNonNull(getCommand("prefix")).setExecutor(new PrefixCommand());
        Objects.requireNonNull(getCommand("prefix")).setTabCompleter(new PrefixTab());



        getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        getServer().getPluginManager().registerEvents(new CommandItem(), this);
        getServer().getPluginManager().registerEvents(new CustomInventoryCommandMoveListener(), this);
        getServer().getPluginManager().registerEvents(new CustomInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new DebugInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new EMFAddon(), this);
        getServer().getPluginManager().registerEvents(new FlyItem(), this);
        getServer().getPluginManager().registerEvents(new ItemCraftListener(), this);
        getServer().getPluginManager().registerEvents(new LocateItem(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PrefixGetEvent(), this);
        getServer().getPluginManager().registerEvents(new PrefixInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PrefixItemListener(), this);
        getServer().getPluginManager().registerEvents(new RecipeListener(), this);



        Bukkit.getScheduler().runTaskTimerAsynchronously(this, Fly::save, 0, 20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Fly(), 0, 1);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
