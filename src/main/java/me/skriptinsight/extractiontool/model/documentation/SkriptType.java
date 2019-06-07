package me.skriptinsight.extractiontool.model.documentation;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import com.genymobile.mirror.Mirror;
import me.skriptinsight.extractiontool.SkriptInsightDocExtractionTool;
import me.skriptinsight.extractiontool.mirror.entitydata.NounMirror;
import me.skriptinsight.extractiontool.mirror.entitydata.SimpleLiteralMirror;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SkriptType {
    private int id;
    @Nullable
    private String[] description;
    @Nullable
    private String[] usage;
    @Nullable
    private String[] examples;
    @Nullable
    private String since;
    @NotNull
    private String typeName;
    private String className;
    private transient SkriptAddon addon;
    private String addonName;
    private String[] possibleValues;
    private List<String> patterns;

    public SkriptType(ClassInfo<?> info) {
        this.typeName = info.getCodeName();
        this.id = typeName.hashCode();
        if (info.getDescription() != null)
            this.description =
                    Arrays.stream(info.getDescription()).map(SkriptInsightDocExtractionTool::removeHtml).toArray(String[]::new);
        if (info.getUsage() != null)
            this.usage =
                    Arrays.stream(info.getUsage()).map(SkriptInsightDocExtractionTool::removeHtml).toArray(String[]::new);
        this.examples = info.getExamples();
        this.since = info.getSince();
        if (info.getUserInputPatterns() != null)
            this.patterns =
                    Arrays.stream(info.getUserInputPatterns()).map(Pattern::pattern).sorted(String::compareTo).collect(Collectors.toList());

        addon = SkriptDocumentation.getAddonFromClassInfo(info);
        if (addon != null) {
            addonName = addon.getName();
        }
        className = info.getC().getSimpleName();

        try {
            fetchManualValues(info, className);
        } catch (Exception e) {
            Skript.warning("Unable to fetch manual values from " + className);
            e.printStackTrace();
        }
    }

    public List<String> getPatterns() {
        return patterns;
    }

    public String getClassName() {
        return className;
    }

    public String[] getPossibleValues() {
        return possibleValues;
    }

    private void fetchManualValues(ClassInfo<?> info, String clazz) throws IllegalAccessException {
        if (Objects.equals(clazz, "EntityData")) {
            SimpleLiteralMirror mirror = Mirror.create(SimpleLiteralMirror.class);
            mirror.setInstance(info.getDefaultExpression());
            NounMirror[] names = mirror.getSimpleEntityData()[0].getEntityDataInfo().getNames();

            possibleValues = Arrays.stream(names).map(NounMirror::getValue).toArray(String[]::new);
        }
    }

    public SkriptAddon getAddon() {
        return addon;
    }

    public String getAddonName() {
        return addonName;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getTypeName() {
        return typeName;
    }

    @Nullable
    public String[] getDescription() {
        return this.description;
    }

    @Nullable
    public String[] getUsage() {
        return this.usage;
    }

    @Nullable
    public String[] getExamples() {
        return this.examples;
    }

    @Nullable
    public String getSince() {
        return this.since;
    }

}
