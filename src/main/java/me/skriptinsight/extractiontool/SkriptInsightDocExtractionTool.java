package me.skriptinsight.extractiontool;

import co.aikar.commands.PaperCommandManager;
import me.skriptinsight.extractiontool.cmd.ExtractDocCommand;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class  SkriptInsightDocExtractionTool extends JavaPlugin {

    private static Pattern htmlPattern = Pattern.compile("<.+?>(.+?)</.+?>");

    public static String removeHtml(String val) {
        if (val == null)
            return null;
        Matcher matcher = htmlPattern.matcher(val);

        return StringEscapeUtils.unescapeHtml(matcher.replaceAll("$1"));
    }

    @Override
    public void onEnable() {
        getDataFolder().mkdir();

        PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new ExtractDocCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
