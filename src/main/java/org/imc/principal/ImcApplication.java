package org.imc.principal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.imc.utils.PathFxml;

import java.io.FileInputStream;
import java.io.IOException;

public class ImcApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(new FileInputStream(PathFxml.patBase() + "/imc-view.fxml"));

        Scene scene = new Scene(root, 700, 500);
        stage.setTitle("Sistema-Imc");
        stage.setScene(scene);
        stage.show();
    }
}
