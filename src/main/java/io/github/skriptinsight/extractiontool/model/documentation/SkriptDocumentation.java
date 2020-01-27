package io.github.skriptinsight.extractiontool.model.documentation;

import ch.njol.skript.classes.ClassInfo;
import io.github.skriptinsight.extractiontool.model.aliases.AliasesInfo;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkriptDocumentation {
    private transient static Map<String, SkriptAddon> addons = new HashMap<>();
    private transient AliasesInfo aliasesInfo;
    private SkriptAddon addon;
    private List<SkriptType> types = new ArrayList<>();
    private List<SkriptCondition> conditions = new ArrayList<>();
    private List<SkriptEvent> events = new ArrayList<>();
    private List<SkriptEffect> effects = new ArrayList<>();
    private List<SkriptExpression> expressions = new ArrayList<>();

    public SkriptDocumentation(SkriptAddon addon) {
        this.addon = addon;
    }

    @Nullable
    public static SkriptAddon getAddonByName(String name) {
        return addons.values().stream().filter((d) -> d.getName().equals(name)).findFirst().orElse(null);
    }

    public static Map<String, SkriptAddon> getAddons() {
        return addons;
    }

    @Nullable
    static SkriptAddon getAddonFromClassInfo(ClassInfo info) {
        if (info.getParser() != null)
            return getAddonFromClass(info.getParser().getClass());

        return null;
    }

    static SkriptAddon getAddonFromClass(Class clazz) {
        String name = clazz.getPackage().getName();
        return addons.entrySet().stream()
                .filter(entry -> name.startsWith(entry.getKey()))
                .findFirst()
                .map(Map.Entry::getValue).orElse(null);
    }

    public AliasesInfo getAliasesInfo() {
        return aliasesInfo;
    }

    public void setAliasesInfo(AliasesInfo aliasesInfo) {
        this.aliasesInfo = aliasesInfo;
    }

    public List<SkriptExpression> getExpressions() {
        return expressions;
    }

    public List<SkriptEvent> getEvents() {
        return events;
    }

    public List<SkriptType> getTypes() {
        return types;
    }

    public List<SkriptCondition> getConditions() {
        return conditions;
    }

    public List<SkriptEffect> getEffects() {
        return effects;
    }

    public SkriptAddon getAddon() {
        return addon;
    }
}
