package io.github.skriptinsight.extractiontool.model.documentation;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import com.genymobile.mirror.Mirror;
import io.github.skriptinsight.extractiontool.SkriptInsightDocExtractionTool;
import io.github.skriptinsight.extractiontool.mirror.entitydata.EntityDataInfoMirror;
import io.github.skriptinsight.extractiontool.mirror.entitydata.EntityDataMirror;
import io.github.skriptinsight.extractiontool.mirror.entitydata.NounMirror;
import io.github.skriptinsight.extractiontool.mirror.entitydata.SimpleLiteralMirror;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        className = info.getC().getName();

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

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    private void fetchManualValues(ClassInfo<?> info, String clazz) throws IllegalAccessException {
        if (Objects.equals(clazz, "ch.njol.skript.entity.EntityData")) {
            Stream<NounMirror> infos = Mirror.create(EntityDataMirror.class).getInfos()
                    .stream().map(c -> {
                        EntityDataInfoMirror mirror = Mirror.create(EntityDataInfoMirror.class);
                        mirror.setInstance(c);
                        return mirror;
                    })
                    .flatMap(c -> Arrays.stream(c.getNames()));


            SimpleLiteralMirror mirror = Mirror.create(SimpleLiteralMirror.class);
            mirror.setInstance(info.getDefaultExpression());
            Stream<NounMirror> names = Stream.concat(infos,
                    Arrays.stream(mirror.getSimpleEntityData()[0].getEntityDataInfo().getNames()))
                    .filter(distinctByKey(NounMirror::getValue));

            possibleValues = names.map(NounMirror::getValue).toArray(String[]::new);
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
