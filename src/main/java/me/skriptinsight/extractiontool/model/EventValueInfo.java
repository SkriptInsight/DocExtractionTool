package me.skriptinsight.extractiontool.model;

import me.skriptinsight.extractiontool.mirror.EventValues;

public class EventValueInfo {
    private transient Class valueClassObj;
    private transient Class eventClassObj;
    private String valueName;
    private String eventClass;
    private String valueClass;

    public String getEventClass() {
        return eventClass;
    }

    public String getValueClass() {
        return valueClass;
    }

    public EventValueInfo(EventValues.EventValueInfo info) {
        valueClassObj = info.getValueClass();
        eventClassObj = info.getEventClass();
        valueClass = info.getValueClass().getSimpleName();
        eventClass = info.getEventClass().getSimpleName();
    }

    public String getValueName() {
        return valueName;
    }

    public Class getValueClassObj() {
        return valueClassObj;
    }

    public Class getEventClassObj() {
        return eventClassObj;
    }
}
