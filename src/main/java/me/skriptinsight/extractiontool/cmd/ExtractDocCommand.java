package me.skriptinsight.extractiontool.cmd;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionInfo;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.types.TypeInfoPolicy;
import me.skriptinsight.extractiontool.SkriptInsightDocExtractionTool;
import me.skriptinsight.extractiontool.model.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ExtractDocCommand extends BaseCommand {

    private static YaGson gson = new YaGsonBuilder()
            //.setPrettyPrinting()
            .disableHtmlEscaping()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .setTypeInfoPolicy(TypeInfoPolicy.DISABLED)
            .create();

    private String msg(String msg) {
        return ChatColor.GRAY + "SkriptInsight> " + msg;
    }

    @CommandAlias("extractdoc|ed")
    public void extract(CommandSender sender, @Optional String addon) {
        long start = System.currentTimeMillis();
        sender.sendMessage(msg(ChatColor.DARK_GREEN + "Starting extraction."));

        SkriptDocumentation.getAddons().clear();

        SkriptDocumentation.getAddons().put(Skript.class.getPackage().getName(), new SkriptAddon("Skript"));

        String name = addon == null ? "" : addon;
        SkriptDocumentation doc = new SkriptDocumentation(SkriptDocumentation.getAddon(name));
        //Get addons

        Skript.getAddons().forEach(a -> SkriptDocumentation.getAddons().put(a.plugin.getClass().getPackage().getName(),
                new SkriptAddon(a.getName())));

        doc.getTypes()
                .addAll(
                        Classes.getClassInfos().stream()
                                .map(SkriptType::new)
                                .filter(c -> doc.getAddon() == null || doc.getAddon().equals(c.getAddon()))
                                .collect(Collectors.toList())
                );

        doc.getConditions()
                .addAll(
                        Skript.getConditions().stream()
                                .map(SkriptCondition::new)
                                .filter(c -> doc.getAddon() == null || doc.getAddon().equals(c.getAddon()))
                                .collect(Collectors.toList())
                );


        Iterable<ExpressionInfo> iterable = () -> ((Iterator) Skript.getExpressions());
        Stream<ExpressionInfo> targetStream = StreamSupport.stream(iterable.spliterator(), false);

        doc.getExpressions()
                .addAll(
                        targetStream
                                .map(SkriptExpression::new)
                                .filter(c -> doc.getAddon() == null || doc.getAddon().equals(c.getAddon()))
                                .collect(Collectors.toList())
                );

        doc.getEvents()
                .addAll(
                        Skript.getEvents().stream()
                                .map(SkriptEvent::new)
                                .filter(c -> doc.getAddon() == null || doc.getAddon().equals(c.getAddon()))
                                .collect(Collectors.toList())
                );

        doc.getEffects()
                .addAll(
                        Skript.getEffects().stream()
                                .map(SkriptEffect::new)
                                .filter(c -> doc.getAddon() == null || doc.getAddon().equals(c.getAddon()))
                                .collect(Collectors.toList())
                );


        sender.sendMessage(msg(ChatColor.DARK_GREEN + String.format("Extraction finished in %s.",
                Timespan.toString(System.currentTimeMillis() - start))));

        try {
            try (FileWriter writer =
                         new FileWriter(new File(SkriptInsightDocExtractionTool.getPlugin(SkriptInsightDocExtractionTool.class).getDataFolder(), "output.json"))) {
                writer.append(gson.toJson(doc));
            }
        } catch (IOException e) {
            sender.sendMessage(msg(System.lineSeparator() + gson.toJson(doc)));
        }

    }

}




