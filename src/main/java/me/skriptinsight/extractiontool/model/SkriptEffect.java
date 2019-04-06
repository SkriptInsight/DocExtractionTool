package me.skriptinsight.extractiontool.model;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.SyntaxElementInfo;

public class SkriptEffect {
    private transient SkriptAddon addon;
    private String[] patterns;
    private String addonName;
    private String className;

    public SkriptEffect(SyntaxElementInfo<? extends Effect> effect) {
        this.patterns = effect.patterns;
        addon = SkriptDocumentation.getAddonFromClass(effect.c);
        if (addon != null) {
            addonName = addon.getName();
        }
        className = effect.c.getSimpleName();
    }

    public String getClassName() {
        return className;
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
}
