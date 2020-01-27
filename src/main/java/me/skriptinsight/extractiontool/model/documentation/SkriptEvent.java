package me.skriptinsight.extractiontool.model.documentation;

import ch.njol.skript.lang.SkriptEventInfo;
import ch.njol.skript.registrations.Classes;
import com.genymobile.mirror.Mirror;
import me.skriptinsight.extractiontool.SkriptInsightDocExtractionTool;
import me.skriptinsight.extractiontool.mirror.EventValues;
import me.skriptinsight.extractiontool.mirror.EventValuesList;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class SkriptEvent {
    private transient static EventValueInfo[] allCurrentEventValues;
    private transient static EventValueInfo[] allPastEventValues;
    private transient static EventValueInfo[] allFutureEventValues;
    private transient static EventValues valuesMirror;
    private final String name;
    private final int id;
    private final String[] patterns;
    private EventValueInfo[] currentEventValues;
    private EventValueInfo[] pastEventValues;
    private EventValueInfo[] futureEventValues;
    @Nullable
    private String[] description;
    @Nullable
    private String[] examples;
    @Nullable
    private String since;
    @Nullable
    private String documentationID;
    @Nullable
    private String[] requiredPlugins;
    private transient SkriptAddon addon;
    private String addonName;
    private String[] classNames;
    private boolean cancellable;

    @SuppressWarnings("unchecked")
    public SkriptEvent(SkriptEventInfo eventInfo) {

        if (allPastEventValues == null)
            allPastEventValues = getValueInfosFor(-1);
        if (allCurrentEventValues == null)
            allCurrentEventValues = getValueInfosFor(0);
        if (allFutureEventValues == null)
            allFutureEventValues = getValueInfosFor(1);

        name = eventInfo.getName();
        patterns = Arrays.stream(eventInfo.patterns).sorted(String::compareTo).toArray(String[]::new);
        id = eventInfo.getId().hashCode();
        description = eventInfo.getDescription();
        if (description != null)
            description = Arrays.stream(description)
                    .map(SkriptInsightDocExtractionTool::removeHtml)
                    .toArray(String[]::new);
        examples = eventInfo.getExamples();
        since = eventInfo.getSince();
        documentationID = eventInfo.getDocumentationID();
        requiredPlugins = eventInfo.getRequiredPlugins();
        currentEventValues =
                Arrays.stream(eventInfo.events)
                        .map(c -> Arrays.stream(allCurrentEventValues)
                                .filter(ce -> ce.getEventClassObj().isAssignableFrom(c)))
                        .flatMap(Function.identity())
                        .toArray(EventValueInfo[]::new);

        pastEventValues =
                Arrays.stream(eventInfo.events)
                        .map(c -> Arrays.stream(allPastEventValues)
                                .filter(ce -> ce.getEventClassObj().isAssignableFrom(c)))
                        .flatMap(Function.identity())
                        .map(
                                c -> new EventValueInfo[]{

                                        c.withTime("past"),
                                        c.withTime("former")
                                })
                        .flatMap(Arrays::stream)
                        .toArray(EventValueInfo[]::new);

        futureEventValues =
                Arrays.stream(eventInfo.events)
                        .map(c -> Arrays.stream(allPastEventValues)
                                .filter(ce -> ce.getEventClassObj().isAssignableFrom(c)))
                        .flatMap(Function.identity())
                        .map(
                                c -> new EventValueInfo[]{

                                        c.withTime("future"),
                                        c.withTime("latter")
                                })
                        .flatMap(Arrays::stream)
                        .toArray(EventValueInfo[]::new);

        addon = SkriptDocumentation.getAddonFromClass(eventInfo.c);
        if (addon != null) {
            addonName = addon.getName();
        }
        cancellable = Arrays.stream(eventInfo.events).anyMatch(Cancellable.class::isAssignableFrom);
        classNames = Arrays.stream(eventInfo.events).map(Class::getName).toArray(String[]::new);
    }

    private static EventValueInfo[] getValueInfosFor(int time) {
        ArrayList<EventValues.EventValueInfo> wrapped = new ArrayList<>();
        EventValuesList list = getValuesMirror().getEventValuesList(time);
        int size = list.size();

        //It actually can't be replaced because we wouldn't be getting the wrapped value
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < size; i++) {
            wrapped.add(list.get(i));
        }

        return wrapped.stream().map(info -> {
            EventValueInfo val = new EventValueInfo(info);
            //noinspection unchecked
            val.setValueName("event-" + Classes.getSuperClassInfo(val.getValueClassObj()).getCodeName());

            return val;
        }).toArray(EventValueInfo[]::new);

    }

    private static EventValues getValuesMirror() {
        if (valuesMirror == null) {
            valuesMirror = Mirror.create(EventValues.class);
        }
        return valuesMirror;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public EventValueInfo[] getCurrentEventValues() {
        return currentEventValues;
    }

    public EventValueInfo[] getPastEventValues() {
        return pastEventValues;
    }

    public EventValueInfo[] getFutureEventValues() {
        return futureEventValues;
    }

    public SkriptAddon getAddon() {
        return addon;
    }

    public String getAddonName() {
        return addonName;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Nullable
    public String[] getDescription() {
        return description;
    }

    @Nullable
    public String[] getExamples() {
        return examples;
    }

    @Nullable
    public String getSince() {
        return since;
    }

    @Nullable
    public String getDocumentationID() {
        return documentationID;
    }

    @Nullable
    public String[] getRequiredPlugins() {
        return requiredPlugins;
    }

    public String[] getPatterns() {
        return patterns;
    }
}
