package io.github.skriptinsight.extractiontool.mirror;

import com.genymobile.mirror.annotation.Class;
import com.genymobile.mirror.annotation.GetField;
import com.genymobile.mirror.annotation.GetInstance;
import com.genymobile.mirror.annotation.SetInstance;

@Class("ch.njol.skript.registrations.EventValues")
public interface EventValues {

    @Class("ch.njol.skript.registrations.EventValues$EventValueInfo")
    interface EventValueInfo {
        @GetField("event")
        java.lang.Class getEventClass();

        @GetField("c")
        java.lang.Class getValueClass();


        @GetInstance
        Object getInstance();

        @SetInstance
        void setInstance(Object obj);
    }


    EventValuesList getEventValuesList(int time);
}
