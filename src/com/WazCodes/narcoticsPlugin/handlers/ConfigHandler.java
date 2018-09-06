package com.WazCodes.narcoticsPlugin.handlers;

import com.WazCodes.narcoticsPlugin.Narcotic;
import com.WazCodes.narcoticsPlugin.Updater;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ConfigHandler {

    private Narcotic plugin;
    private FileConfiguration config;
    private ArrayList<World> worlds = new ArrayList<>();
    private File matList = new File("plugins/Narcotic/materialList.txt");
    private File oldDir = new File("plugins/Narcotic/Old_Configs/");
    private Updater updater;
    private boolean hasCheckedUpdate = false;
    private boolean hasDownloadedUpdate = false;

    public ConfigHandler(Narcotic plugin) {
        this.plugin = plugin;
        this.config = plugin.config;
        this.updater = new Updater(plugin, 35506, plugin.file, Updater.UpdateType.NO_DOWNLOAD, true);
    }

    /**
     * Generate a list of all current Materials in MineCraft
     */
    public void createMaterialList() {
        try {
            if (!matList.exists()) {
                matList.createNewFile();
                FileWriter fw = new FileWriter(matList);

                fw.write("---- All Materials ----" + System.lineSeparator());
                for (Material m : Material.values()) {
                    fw.write(m.name() + System.lineSeparator());
                }
                fw.close();

                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.RED + "" + ChatColor.BOLD + "[Narcotic] Material File Generated " + ChatColor.RESET);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check for config update, if so; update;
     *
     * @return If update was needed / successful
     */
    public boolean configUpdate() {
        try {
            File file = new File(plugin.getDataFolder(), "config.yml");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            // Only change this if you need to regenerate the config.
            String check = "DO_NOT_TOUCH: 0.9.3";
            boolean needUpdate = false;
            boolean saveOld = true;

            while ((line = br.readLine()) != null) {
                if (line.equalsIgnoreCase(check)) {
                    needUpdate = true;
                }
                if (line.replaceAll(" ", "").equalsIgnoreCase(
                        "SaveOldConfigs:false")) {
                    saveOld = false;
                }
            }
            br.close();

            if (needUpdate) {
                String string;
                if (saveOld) {
                    string = ChatColor.RED
                            + "[Narcotic] Config Saved & Regenerated! Update new one to your liking."
                            + ChatColor.RESET;
                    oldDir.mkdir();
                    DateFormat dateFormat = new SimpleDateFormat(
                            "MM-dd-yyyy_HH-mm-ss");
                    Date date = new Date();
                    file.renameTo(new File(oldDir + "\\" + dateFormat.format(date)
                            + ".yml"));
                    plugin.saveDefaultConfig();
                    Bukkit.getConsoleSender().sendMessage(string);
                    return true;
                } else {
                    string = ChatColor.RED + "" + ChatColor.BOLD
                            + "[Narcotic] Config Regenerated! Update new one to your liking."
                            + ChatColor.RESET;
                    config = plugin.getConfig();
                    config.set("Options.SaveOldConfigs", false);
                    plugin.saveDefaultConfig();
                    Bukkit.getConsoleSender().sendMessage(string);
                    return true;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Check for plugin update
     *
     * @return If there's a checkable update
     */
    public boolean isUpdateCheck() {
        if (config.getBoolean("Options.AutoUpdateDownload")) {
            isUpdateDownload();
            return true;
        }
        if (config.getBoolean("Options.AutoUpdateChecker")) {
            if (!updater.getLatestName().equalsIgnoreCase("narcotic v" + plugin.getDescription().getVersion())) {
                if (!hasCheckedUpdate) {
                    Bukkit.getConsoleSender()
                            .sendMessage(ChatColor.RED
                                    + System.lineSeparator()
                                    + System.lineSeparator()
                                    + "[Narcotics] Update Available! "
                                    + System.lineSeparator()
                                    + "Download it at: dev.bukkit.org/server-mods/narcotics"
                                    + System.lineSeparator() + ChatColor.RESET);
                    hasCheckedUpdate = true;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Check for downloadable update
     *
     * @return If there's a downloadable update
     */
    public boolean isUpdateDownload() {
        if (config.getBoolean("Options.AutoUpdateDownload")) {
            if (!updater.getLatestName().equalsIgnoreCase("narcotic v" + plugin.getDescription().getVersion())) {
                if (!hasDownloadedUpdate) {
                    updater = new Updater(plugin, 35506, plugin.file, Updater.UpdateType.DEFAULT, true);
                    hasDownloadedUpdate = true;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * If plugin is running specific multiple worlds
     *
     * @return If plugin is using specific multiple worlds
     */
    public boolean isMultiworld() {
        return config.getString("Options.Worlds").split(",").length > 1;
    }

    /**
     * @return A Collection of worlds that the plugin is active in
     */
    public Collection<World> getWorlds() {
        return worlds;
    }

    /**
     * Gather the worlds the plugin is using
     */
    public void gatherWorlds() {
        String[] inConfig = config.getString("Options.Worlds").split(",");
        for (String s : inConfig) {
            worlds.add(Bukkit.getWorld(s));
        }
    }

    /**
     * Clear world collection
     */
    public void clearWorlds() {
        worlds.clear();
    }
}
