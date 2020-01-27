package io.github.skriptinsight.extractiontool;

import co.aikar.commands.PaperCommandManager;
import io.github.skriptinsight.extractiontool.cmd.ExtractDocCommand;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SkriptInsightDocExtractionTool extends JavaPlugin {
    private static SkriptInsightDocExtractionTool instance;
    private static Pattern htmlPattern = Pattern.compile("<.+?>(.+?)</.+?>");
    private static boolean isUsingOurCustomSkript = false;

    public static SkriptInsightDocExtractionTool getInstance() {
        return instance;
    }

    public static Logger getPluginLogger() {
        return instance.getLogger();
    }

    public static String removeHtml(String val) {
        if (val == null)
            return null;
        Matcher matcher = htmlPattern.matcher(val);

        return StringEscapeUtils.unescapeHtml(matcher.replaceAll("$1"));
    }

    @Override
    public void onLoad() {
        instance = this;
        getDataFolder().mkdir();
    }


    public static boolean isIsUsingOurCustomSkript() {
        return isUsingOurCustomSkript;
    }

    @Override
    public void onEnable() {
        try {
            Class.forName("me.skriptinsight.SkriptInsightRocks");
            isUsingOurCustomSkript = true;
        } catch (ClassNotFoundException ignored) {}

        PaperCommandManager manager = new PaperCommandManager(this);

        manager.registerCommand(new ExtractDocCommand());
    }

    @Override
    public void onDisable() {
        instance = null;
        // Plugin shutdown logic
    }
}
