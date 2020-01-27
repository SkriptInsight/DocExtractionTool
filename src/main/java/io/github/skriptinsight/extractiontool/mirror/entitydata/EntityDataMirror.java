package io.github.skriptinsight.extractiontool.mirror.entitydata;

import com.genymobile.mirror.annotation.Class;
import com.genymobile.mirror.annotation.GetField;

import java.util.List;

@Class("ch.njol.skript.entity.EntityData")
public interface EntityDataMirror
{
    @GetField("infos")
    List<Object> getInfos();
}
