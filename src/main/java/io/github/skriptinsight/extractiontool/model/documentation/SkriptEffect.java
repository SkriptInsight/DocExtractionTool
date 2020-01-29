package io.github.skriptinsight.extractiontool.model.documentation;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.SyntaxElementInfo;

import java.util.Arrays;

public class SkriptEffect {
    private transient SkriptAddon addon;
    private String[] patterns;
    private String addonName;
    private String className;

    public SkriptEffect(SyntaxElementInfo<? extends Effect> effect) {
        this.patterns = Arrays.stream(effect.patterns).sorted(String::compareTo).toArray(String[]::new);
        addon = SkriptDocumentation.getAddonFromClass(effect.c);
        if (addon != null) {
            addonName = addon.getName();
        }
        className = effect.c.getName();
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
