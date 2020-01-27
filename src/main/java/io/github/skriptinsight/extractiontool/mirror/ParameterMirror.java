package io.github.skriptinsight.extractiontool.mirror;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.Expression;
import com.genymobile.mirror.annotation.Class;
import com.genymobile.mirror.annotation.GetField;
import com.genymobile.mirror.annotation.GetInstance;
import com.genymobile.mirror.annotation.SetInstance;

@Class("ch.njol.skript.lang.function.Parameter")
public interface ParameterMirror {
    @SetInstance
    void setInstance(Object obj);

    @GetInstance
    Object getInstance();

    @GetField("name")
    String getName();

    @GetField("type")
    ClassInfo<?> getType();

    @GetField("def")
    Expression<?> getDef();

    @GetField("single")
    boolean isSingle();
}
