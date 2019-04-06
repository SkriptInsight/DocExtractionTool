package me.skriptinsight.extractiontool.model;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.SyntaxElementInfo;

public class SkriptCondition {
    private String[] patterns;
    private transient SkriptAddon addon;
    private String addonName;
    private String className;

    public SkriptCondition(SyntaxElementInfo<? extends Condition> c) {
        this.patterns = c.patterns;
        addon = SkriptDocumentation.getAddonFromClass(c.c);
        if (addon != null) {
            addonName = addon.getName();
        }
        className = c.c.getSimpleName();

    }

    public String getClassName() {
        return className;
    }

    public SkriptAddon getAddon() {
        return addon;
    }

    public String getAddonName() {
        return addonName;
    }

    public String[] getPatterns() {
        return patterns;
    }
}
