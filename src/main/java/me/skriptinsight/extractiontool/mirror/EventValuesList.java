package me.skriptinsight.extractiontool.mirror;

import com.genymobile.mirror.annotation.Class;
import com.genymobile.mirror.annotation.GetInstance;
import com.genymobile.mirror.annotation.SetInstance;

import java.util.List;

@Class("java.util.List")
public interface EventValuesList extends List<EventValues.EventValueInfo> {
    @GetInstance
    Object getInstance();

    @SetInstance
    void setInstance(Object obj);

    EventValues.EventValueInfo get(int index);


}
