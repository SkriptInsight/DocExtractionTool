package me.skriptinsight.extractiontool.cmd;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.Aliases;
import ch.njol.skript.lang.ExpressionInfo;
import ch.njol.skript.lang.function.Functions;
import ch.njol.skript.lang.function.JavaFunction;
import ch.njol.skript.lang.function.Parameter;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.util.Timespan;
import ch.njol.skript.util.Utils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import com.genymobile.mirror.Mirror;
import com.gilecode.yagson.YaGson;
import com.gilecode.yagson.YaGsonBuilder;
import com.gilecode.yagson.types.TypeInfoPolicy;
import me.skriptinsight.extractiontool.SkriptInsightDocExtractionTool;
import me.skriptinsight.extractiontool.mirror.AliasesProviderMirror;
import me.skriptinsight.extractiontool.mirror.ParameterMirror;
import me.skriptinsight.extractiontool.model.aliases.AliasesInfo;
import me.skriptinsight.extractiontool.model.documentation.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@CommandAlias("extractdoc|ed")
public class ExtractDocCommand extends BaseCommand {

    private static YaGson gson = new YaGsonBuilder()
            .disableHtmlEscaping()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .setTypeInfoPolicy(TypeInfoPolicy.DISABLED)
            .create();
    private static ParameterMirror paramMirror = Mirror.create(ParameterMirror.class);

    private String msg(String msg) {
        return ChatColor.GRAY + "SkriptInsight> " + msg;
    }

    //region Stubs

    @Subcommand("stubs")
    public void genStubs(CommandSender sender) {
        File dataFolder =
                SkriptInsightDocExtractionTool.getPlugin(SkriptInsightDocExtractionTool.class).getDataFolder();

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(new File(dataFolder, "stubs.sk")))) {
            writeStubsHeader(writer);

            Functions.getJavaFunctions().forEach(f -> writeStubFunction(f, writer));

            sender.sendMessage(msg(ChatColor.GREEN + "Stubs export finished."));
        } catch (IOException e) {
            sender.sendMessage(msg(e.toString()));
        }
    }

    private void writeStubFunction(JavaFunction<?> f, PrintWriter writer) {
        writeStubFunctionDocumentation(f, writer);

        writer.write("function "); //Keyword
        writer.write(f.getName()); //Name
        writer.write('('); //Start parameter list
        writer.write(Arrays.stream(f.getParameters()).map(this::parameterToString).collect(Collectors.joining(", ")));
        writer.write(')'); //End parameter list
        writer.write(" :: "); //Return type separator
        writer.write(f.getReturnType().toString()); //Return type separator
        writer.println(':'); //Return type separator
        writer.println("    # Auto-generated SkriptInsight Java function stub");
        writer.println();
    }

    private void writeStubFunctionDocumentation(JavaFunction<?> f, PrintWriter writer) {
        List<String> lines = new ArrayList<>();

        if (f.getDescription() != null && f.getDescription().length > 0) {
            lines.addAll(Arrays.stream(f.getDescription()).map(SkriptInsightDocExtractionTool::removeHtml)
                    .map(l -> "@doc " + l).collect(Collectors.toCollection(ArrayList::new)));
        } else {
            lines.add("@doc ");
        }

        lines.addAll(Arrays.stream(f.getParameters()).map(p -> String.format("@param %s ", p.getName())).collect(Collectors.toCollection(ArrayList::new)));

        if (f.getSince() != null) {
            lines.add("@since " + f.getSince());
        }
        lines.add("@returns ");


        lines.forEach(l -> writer.println("# " + l));
    }

    private String parameterToString(Parameter<?> p) {
        paramMirror.setInstance(p);

        boolean isNaN = (paramMirror.getDef() != null && paramMirror.getDef().toString(null, false).equalsIgnoreCase(
                "NaN"));

        return paramMirror.getName() + ": " + Utils.toEnglishPlural(paramMirror.getType().getCodeName(),
                !paramMirror.isSingle()) + (paramMirror.getDef() != null && !isNaN ?
                " = " + paramMirror.getDef().toString(null
                        , false) : "");
    }

    private void writeStubsHeader(PrintWriter writer) {
        String[] fileHeader = new String[]{
                "SkriptInsight - Skript Java function stubs",
                "",
                "This file is intended for SkriptInsight only but can be used for other engines",
                "that want to provide smart code suggestions for Skript functions"
        };

        for (String line : fileHeader) writer.println("# " + line);
        writer.println();
    }
    //endregion

    @Default
    public void extract(CommandSender sender, String addon) {
        long start = System.currentTimeMillis();
        sender.sendMessage(msg(ChatColor.DARK_GREEN + "Starting extraction."));

        SkriptDocumentation doc;
        try {
            doc = buildDocumentationFor(addon);
        } catch (RuntimeException e) {
            sender.sendMessage(msg(ChatColor.RED + e.getMessage()));
            return;
        }

        sender.sendMessage(msg(ChatColor.DARK_GREEN + String.format("Extraction finished in %s.",
                Timespan.toString(System.currentTimeMillis() - start))));

        try {
            File dataFolder =
                    SkriptInsightDocExtractionTool.getPlugin(SkriptInsightDocExtractionTool.class).getDataFolder();

            try (FileWriter writer =
                         new FileWriter(new File(dataFolder, doc.getAddon().getName() + ".json"))) {
                writer.append(gson.toJson(doc));
            }

            if (doc.getAliasesInfo() != null) {
                try (FileWriter writer = new FileWriter(new File(dataFolder, "aliases.json"))) {
                    writer.append(gson.toJson(doc.getAliasesInfo()));
                }
            }
        } catch (IOException e) {
            sender.sendMessage(msg(System.lineSeparator() + gson.toJson(doc)));
        }

    }

    @NotNull
    private SkriptDocumentation buildDocumentationFor(@Optional String addon) {
        SkriptDocumentation.getAddons().clear();

        SkriptDocumentation.getAddons().put(Skript.class.getPackage().getName(), new SkriptAddon("Skript"));

        //Get addons
        Skript.getAddons().forEach(a -> SkriptDocumentation.getAddons().put(a.plugin.getClass().getPackage().getName(),
                new SkriptAddon(a.getName())));

        String name = addon == null ? "" : addon;
        SkriptDocumentation doc = new SkriptDocumentation(SkriptDocumentation.getAddonByName(name));

        if (doc.getAddon() == null || doc.getAddon().getName() == null)
            throw new RuntimeException("Unable to find an addon with that name");

        if (doc.getAddon().getName().equals("Skript")) {
            //Get aliases
            AliasesProviderMirror providerMirror = Mirror.create(AliasesProviderMirror.class);
            providerMirror.setInstance(Aliases.getAddonProvider(Skript.getAddonInstance()));

            doc.setAliasesInfo(new AliasesInfo(providerMirror.getAliases().keySet().toArray(new String[]{})));
        }

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
        return doc;
    }

}




