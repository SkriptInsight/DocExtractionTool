package me.skriptinsight.extractiontool.model;

import ch.njol.skript.classes.ClassInfo;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkriptDocumentation {
    private transient static Map<String, SkriptAddon> addons = new HashMap<>();
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
    public static SkriptAddon getAddon(String name) {
        return addons.getOrDefault(name, null);
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
