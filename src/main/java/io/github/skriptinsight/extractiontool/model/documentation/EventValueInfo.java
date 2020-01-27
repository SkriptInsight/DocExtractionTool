package io.github.skriptinsight.extractiontool.model.documentation;

import io.github.skriptinsight.extractiontool.mirror.EventValues;

@SuppressWarnings("rawtypes")
public class EventValueInfo {
    private transient Class valueClassObj;
    private transient Class eventClassObj;
    private transient String eventClass;
    private String valueName;
    private String valueClass;

    private EventValueInfo(Class valueClassObj, Class eventClassObj, String valueName, String eventClass,
                           String valueClass) {
        this.valueClassObj = valueClassObj;
        this.eventClassObj = eventClassObj;
        this.valueName = valueName;
        this.eventClass = eventClass;
        this.valueClass = valueClass;
    }

    public EventValueInfo(EventValues.EventValueInfo info) {
        valueClassObj = info.getValueClass();
        eventClassObj = info.getEventClass();
        valueClass = info.getValueClass().getName();
        eventClass = info.getEventClass().getName();
    }

    public String getEventClass() {
        return eventClass;
    }

    public String getValueClass() {
        return valueClass;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public Class getValueClassObj() {
        return valueClassObj;
    }

    public Class getEventClassObj() {
        return eventClassObj;
    }

    public EventValueInfo withTime(String time) {
        if (time == null || time.isEmpty()) return this;

        return new EventValueInfo(valueClassObj, eventClassObj, String.format("%s %s", time, valueName), eventClass,
                valueClass);
    }
}
