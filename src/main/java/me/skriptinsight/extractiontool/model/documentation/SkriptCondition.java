package me.skriptinsight.extractiontool.model.documentation;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.SyntaxElementInfo;

import java.util.Arrays;

public class SkriptCondition {
    private String[] patterns;
    private transient SkriptAddon addon;
    private String addonName;
    private String className;

    public SkriptCondition(SyntaxElementInfo<? extends Condition> c) {
        if (c.patterns != null)
            this.patterns = Arrays.stream(c.patterns).sorted(String::compareTo).toArray(String[]::new);
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
