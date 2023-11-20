package Other;

import javafx.scene.image.ImageView;

public class Event {
    private int id;
    private String name;
    private String date;
    private int days;
    private int cityId;
    private String field;
    private ImageView photo;

    public Event(int id, String name, String date, ImageView photo, String field) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.photo = photo;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
