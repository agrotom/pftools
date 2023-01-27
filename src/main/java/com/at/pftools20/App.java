package com.at.pftools20;

import com.at.pftools20.core.Core;
import com.at.pftools20.util.ResourceHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class App extends Application
{
	private static Core core;
	
	@SuppressWarnings("exports")
	@Override
	public void start(@NotNull Stage stage) throws IOException
	{
		core = new Core(stage);
		
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main-menu.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1280, 720);

		final String iconPath = ResourceHelper.getResource("/images/icon.png");
		
		Logger.getLogger(App.class).log(Level.INFO, iconPath);

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