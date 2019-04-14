package me.skriptinsight.extractiontool.model;

import ch.njol.skript.lang.ExpressionInfo;

import java.util.Arrays;

public class SkriptExpression {
    private final String[] patterns;
    private transient SkriptAddon addon;
    private String addonName;
    private String className;

    public SkriptExpression(ExpressionInfo info) {
        addon = SkriptDocumentation.getAddonFromClass(info.c);
        addonName = addon.getName();
        if (info.patterns != null)
            patterns = Arrays.stream(info.patterns).sorted(String::compareTo).toArray(String[]::new);
        else
            patterns = new String[0];
        className = info.c.getSimpleName();
    }

    public String[] getPatterns() {
        return patterns;
    }

    public SkriptAddon getAddon() {
        return addon;
    }

    public String getAddonName() {
        return addonName;
    }

    public String getClassName() {
        return className;
    }
}
