package io.github.skriptinsight.extractiontool.mirror.entitydata;

import com.genymobile.mirror.annotation.Class;
import com.genymobile.mirror.annotation.GetField;
import com.genymobile.mirror.annotation.SetInstance;

@Class("ch.njol.skript.lang.util.SimpleLiteral")
public interface SimpleLiteralMirror {

    @SetInstance
    void setInstance(Object obj);

    @GetField("data")
    SimpleEntityDataMirror[] getSimpleEntityData();


}
