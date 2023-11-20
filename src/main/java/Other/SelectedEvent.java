package Other;

public class SelectedEvent {
    private static SelectedEvent instance;
    private Event event;

    private SelectedEvent() {}

    public static SelectedEvent getInstance() {
        if (instance == null) {
            instance = new SelectedEvent();
        }
        return instance;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
