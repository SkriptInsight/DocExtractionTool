package io.github.skriptinsight.extractiontool.mirror;

import com.genymobile.mirror.annotation.Class;
import com.genymobile.mirror.annotation.GetField;
import com.genymobile.mirror.annotation.GetInstance;
import com.genymobile.mirror.annotation.SetInstance;

import java.util.HashMap;

@Class("ch.njol.skript.localization.Language")
public interface LanguageMirror {

    @SetInstance
    void setInstance(Object obj);

    @GetInstance
    Object getInstance();

    @GetField("english")
    HashMap<String, String> getEnglish();
}
