package io.github.skriptinsight.extractiontool.mirror.entitydata;

import com.genymobile.mirror.annotation.Class;
import com.genymobile.mirror.annotation.GetInstance;
import com.genymobile.mirror.annotation.SetInstance;

@Class("ch.njol.skript.localization.Message")
public interface NounMirror {
    @GetInstance
    Object getInstance();

    @SetInstance
    void setInstance(Object obj);

    String getValue();
}
