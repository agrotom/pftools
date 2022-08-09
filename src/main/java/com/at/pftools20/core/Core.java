package com.at.pftools20.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.at.pftools20.core.preferences.Preferences;
import com.at.pftools20.util.json.JSONUtil;

import javafx.stage.Stage;

/**
 * 
 * Core of the program. If it can't create some core files, program will be terminated!
 *
 */
public final class Core
{
	private final String jarPath;
	private final String prefPath;
	private final String recentlyPath;
	
	private final Stage mainStage;
	
	private final PreferenceController prefCtrl;
	
	
	public Core(@NotNull Stage mainStage)
	{
		this.mainStage = mainStage;
		
		final URL jarURL = this.getClass().getProtectionDomain().getCodeSource().getLocation();
		
		if(jarURL == null)
		{
			logger.log(Level.FATAL, "Can't get access to JAR location! Terminating the program!");
			System.exit(-1);
		}
		
		jarPath = jarURL.getPath().substring(1).replace("/", "\\\\");
		
		logger.log(Level.INFO, "Current JAR directory: " + jarPath);
		
		prefPath = jarPath + "/pref.json";
		recentlyPath = jarPath + "/recently.json";
		
		prefCtrl = new PreferenceController();
	}
	
	public Stage getMainStage()
	{
		return mainStage;
	}
	
	public Preferences getPreferences()
	{
		return prefCtrl.preferences;
	}
	
	public void saveCoreData()
	{
		prefCtrl.save();
	}
	
	private class PreferenceController
	{
		private final File prefFile = new File(prefPath);
		private final Preferences preferences;
		
		private PreferenceController()
		{
			if(!isCreated())
				create();
			
			preferences = load();
			
			save();
		}
		
		private void save()
		{
			final JSONUtil jsonUtil = new JSONUtil(prefFile);
			logger.log(Level.INFO, preferences.toJSON());
			jsonUtil.write(preferences.toJSON());
		}
		
		private Preferences load()
		{
			logger.log(Level.INFO, "Preferences already created!");
			
			final JSONUtil jsonUtil = new JSONUtil(prefFile);
			final String json = jsonUtil.read();
			
			return new Preferences(json);
		}
		
		private void create()
		{
			logger.log(Level.INFO, "Preferences aren't created! Creating now...");
			
			try
			{
				prefFile.createNewFile();
			} catch (IOException e)
			{
				logger.log(Level.FATAL, "Something blocked creating new preferences file. Terminating the program!");
				System.exit(-1);
			}
		}
		
		private boolean isCreated()
		{
			return Files.exists(Path.of(prefPath));
		}
	}
	
	private final Logger logger = Logger.getLogger(Core.class);
}
