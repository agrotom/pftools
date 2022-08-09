package com.at.pftools20;

import com.at.pftools20.core.Core;
import com.at.pftools20.util.Helper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HelloApplication extends Application
{
	private static Core core;
	
	@SuppressWarnings("exports")
	@Override
	public void start(@NotNull Stage stage) throws IOException
	{
		core = new Core(stage);
		
		FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-menu.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1280, 720);

		final String iconPath = Helper.getResource("/images/icon.png");

		if (!iconPath.equals(""))
			stage.getIcons().add(new Image(iconPath));

		stage.setTitle("Pathfinder Tools");
		stage.setScene(scene);
		stage.show();
	}
	
	@SuppressWarnings("exports")
	public static Core getAppCore()
	{
		return core;
	}

	public static void main(String[] args)
	{
		launch();
	}
}