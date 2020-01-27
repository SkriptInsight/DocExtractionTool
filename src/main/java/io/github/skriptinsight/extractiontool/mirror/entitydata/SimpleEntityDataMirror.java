package io.github.skriptinsight.extractiontool.mirror.entitydata;

import com.genymobile.mirror.annotation.Class;
import com.genymobile.mirror.annotation.GetField;
import com.genymobile.mirror.annotation.SetInstance;

@Class("ch.njol.skript.entity.EntityData")
public interface SimpleEntityDataMirror {

    @SetInstance
    void setInstance(Object obj);

    @GetField("info")
    EntityDataInfoMirror getEntityDataInfo();

}
