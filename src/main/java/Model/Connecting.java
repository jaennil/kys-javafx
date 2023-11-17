package Model;

import Other.Database;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.example.App;

import java.io.IOException;

public class Connecting {
    private final StringProperty connectionResult = new SimpleStringProperty("Connecting to database...");
    public Property<String> connectionResultProperty() {
        return connectionResult;
    }

    public void setConnected(boolean isConnected) {
        if (isConnected)
            connectionResult.setValue("Successfully connected. Please wait");
        else
            connectionResult.setValue("Failed to connect. Please check your VPN and restart the app.");
    }

    public void connectToDatabase() {
        Database.getInstance();
        boolean isConnected = Database.connect();
        setConnected(isConnected);
        if (isConnected)
            authenticateView();
    }

    private void authenticateView() {
        Platform.runLater(() -> {
            try {
                App.setRoot("authorization");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
