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
    private String description;

    public Event(int id, String name, String date, int days, int cityId, ImageView photo, String field, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.photo = photo;
        this.field = field;
        this.description = description;
        this.cityId = cityId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
