package com.at.pftools20.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.at.pftools20.App;
import com.at.pftools20.logic.statcard.main.StatCard;
import com.at.pftools20.logic.statcard.main.StatCardFactory;
import com.at.pftools20.util.json.JSONUtil;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class FileManager
{
	public static final Image NOT_FOUND_IMAGE = new Image(ResourceHelper.getResource("/images/imagenotfound.png"));
	
	private static String currentPath = null;
	private static String lastPath = null;
	
	/**
	 * 
	 * @param statCard StatCard to save
	 * @param ownerWindow Uses to properly call file chooser.
	 * @param saveAs False - save by current path, True - save with file chooser. If current path is null file chooser calls anyway.
	 */
	public void save(@NotNull StatCard statCard, @NotNull Window ownerWindow, boolean saveAs)
	{
		final File file;
		final JSONUtil jsonUtil;
		
		if(saveAs || currentPath == null)
		{
			final FileChooser fileChooser = createNewChooser();
			
			file = fileChooser.showSaveDialog(ownerWindow);
			
			if(file == null)
				return;
			
			lastPath = currentPath = file.getPath();
			App.getAppCore().pushRecently(currentPath);
		}
		else
		{
			file = new File(currentPath);
		}
		
		jsonUtil = new JSONUtil(file);
		
		jsonUtil.write(statCard.toJSON());
	}
	
	/**
	 * Shows window to pick stat card file.
	 * @param ownerWindow Uses to properly call file chooser.
	 * @return Baked statCard. Returns null if method can't open StatCard by given path.
	 */
	public @Nullable StatCard openStatCard(@NotNull Window ownerWindow)
	{
		final FileChooser fileChooser = createNewChooser();
		
		final File file = fileChooser.showOpenDialog(ownerWindow);
		
		if(file == null)
			return null;
		
		final JSONUtil jsonUtil = new JSONUtil(file);
		
		final String json = jsonUtil.read();
		
		if(json.isEmpty())
		{
			final Alert alert = new Alert(AlertType.ERROR, TranslatableText.formatString("filemanager.alert.emptyjson"));
			
			logger.log(Level.ERROR, "There is a syntax error in JSON file!");
			
			alert.showAndWait();
			
			return null;
		}
		
		final StatCardFactory factory = new StatCardFactory();
		
		final StatCard statCard = factory.createFromJSON(json);
		
		if(statCard != null)
		{
			lastPath = currentPath = file.getPath();
			App.getAppCore().pushRecently(currentPath);
		}
		
		return statCard;
	}
	
	/**
	 * Shows window to pick stat card file.
	 * @param path File's path
	 */
	public @Nullable StatCard openStatCard(@NotNull String path)
	{
		final File file = new File(path);
		
		final JSONUtil jsonUtil = new JSONUtil(file);
		
		final String json = jsonUtil.read();
		
		if(json.isEmpty())
		{
			final Alert alert = new Alert(AlertType.ERROR, TranslatableText.formatString("filemanager.alert.emptyjson"));
			
			logger.log(Level.ERROR, "There is a syntax error in JSON file!");
			
			alert.showAndWait();
			
			return null;
		}
		
		final StatCardFactory factory = new StatCardFactory();
		
		final StatCard statCard = factory.createFromJSON(json);
		
		if(statCard != null)
		{
			lastPath = currentPath = file.getPath();
			App.getAppCore().pushRecently(currentPath);
		}
		
		return statCard;
	}
	
	/**
	 * Opens StatCard file without changing application logic.
	 * @param path Path of StatCard.
	 * @return StatCard. Returns null if method can't open StatCard by given path.
	 */
	public @Nullable StatCard openSilentStatCard(@NotNull Window ownerWindow)
	{
		final FileChooser fileChooser = createNewChooser();
		
		final File file = fileChooser.showOpenDialog(ownerWindow);
		
		final JSONUtil jsonUtil = new JSONUtil(file);
		
		final String json = jsonUtil.read();
		
		if(json.isEmpty())
		{
			final Alert alert = new Alert(AlertType.ERROR, TranslatableText.formatString("filemanager.alert.emptyjson"));
			
			logger.log(Level.ERROR, "There is a syntax error in JSON file!");
			
			alert.showAndWait();
			
			return null;
		}
		
		final StatCardFactory factory = new StatCardFactory();
		
		final StatCard statCard = factory.createFromJSON(json);
		
		lastPath = file.getPath();
		
		return statCard;
	}
	
	/**
	 * Opens StatCard file without changing application logic and file chooser.
	 * @param path Path of StatCard.
	 * @return StatCard. Returns null if method can't open StatCard by given path.
	 */
	public @Nullable StatCard openSilentStatCard(@NotNull String path)
	{
		final File file = new File(path);
		
		final JSONUtil jsonUtil = new JSONUtil(file);
		
		final String json = jsonUtil.read();
		
		if(json.isEmpty())
		{
			final Alert alert = new Alert(AlertType.ERROR, TranslatableText.formatString("filemanager.alert.emptyjson"));
			
			logger.log(Level.ERROR, "There is a syntax error in JSON file!");
			
			alert.showAndWait();
			
			return null;
		}
		
		final StatCardFactory factory = new StatCardFactory();
		
		final StatCard statCard = factory.createFromJSON(json);
		
		lastPath = file.getPath();
		
		return statCard;
	}
	
	public static void close()
	{
		currentPath = null;
	}
	
	public static @Nullable String getCurrentPath()
	{
		return currentPath;
	}
	
	public static @Nullable String getLastPath()
	{
		return lastPath;
	}
	
	private FileChooser createNewChooser()
	{
		final FileChooser fileChooser = new FileChooser();
		final Path assetPath = Path.of(App.getAppCore().getPreferences().assetPath.getValue());
		
		fileChooser.getExtensionFilters().add(new ExtensionFilter("JSON", "*.json"));
		
		if(currentPath != null)
			fileChooser.setInitialDirectory(new File(currentPath).getParentFile());
		else if(Files.exists(assetPath) && Files.isDirectory(assetPath))
			fileChooser.setInitialDirectory(new File(assetPath.toUri().getPath()));
		
		return fileChooser;
	}
	
	public @Nullable Image openImage()
	{
		final FileChooser fileChooser = createNewImageChooser();
		
		final File imageFile = fileChooser.showOpenDialog(App.getAppCore().getMainStage());
		
		if(imageFile == null)
			return null;
		
		Image image = null;
		
		try
		{
			image = new Image(new FileInputStream(imageFile));
		} catch (FileNotFoundException e)
		{
			logger.log(Level.ERROR, "File was choosed, but application not found it!");
		} catch (SecurityException e)
		{
			logger.log(Level.ERROR, "File has specific permissions. Can't open it!");
		}
		
		return image;
	}
	
	private FileChooser createNewImageChooser()
	{
		final FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Available image extensions", "*.png", "*.jpg"));
		
		return fileChooser;
	}
	
	private static final Logger logger = Logger.getLogger(FileManager.class);
}
