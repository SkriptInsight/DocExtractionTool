package me.skriptinsight.extractiontool.model.documentation;

import ch.njol.skript.lang.ExpressionInfo;
import ch.njol.skript.lang.ExpressionType;
import me.skriptinsight.extractiontool.SkriptInsightDocExtractionTool;

import java.util.Arrays;

public class SkriptExpression {
    private final String[] patterns;
    private transient SkriptAddon addon;
    private String addonName;
    private String className;
    private String returnType;
    private ExpressionType expressionType = ExpressionType.SIMPLE;

    public SkriptExpression(ExpressionInfo info) {
        addon = SkriptDocumentation.getAddonFromClass(info.c);
        addonName = addon.getName();
        if (info.patterns != null)
            patterns = Arrays.stream(info.patterns).sorted(String::compareTo).toArray(String[]::new);
        else
            patterns = new String[0];
        className = info.c.getSimpleName();
        returnType = info.returnType.getName();

        if (SkriptInsightDocExtractionTool.isIsUsingOurCustomSkript()) {
            //We are using our custom Skript version that stores the expression types
            expressionType = info.expressionType;
        }
    }

    public ExpressionType getExpressionType() {
        return expressionType;
    }

    public String getReturnType() {
        return returnType;
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
