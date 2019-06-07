package me.skriptinsight.extractiontool.mirror;

import ch.njol.skript.aliases.ItemType;
import com.genymobile.mirror.annotation.Class;
import com.genymobile.mirror.annotation.GetField;
import com.genymobile.mirror.annotation.GetInstance;
import com.genymobile.mirror.annotation.SetInstance;

import java.util.Map;

@Class("ch.njol.skript.aliases.AliasesProvider")
public interface AliasesProviderMirror {

    @SetInstance
    void setInstance(Object obj);

    @GetInstance
    Object getInstance();

    @GetField("aliases")
    Map<String, ItemType> getAliases();
}
