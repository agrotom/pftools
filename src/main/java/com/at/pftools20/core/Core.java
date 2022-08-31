package com.at.pftools20.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
	private final String jarLocation;
	private final String prefPath;
	private final String recentlyPath;
	
	private final Stage mainStage;
	
	private final PreferenceController prefCtrl;
	private final RecentlyController recentlyCtrl;
	
	public String currentPath;
	
	
	public Core(@NotNull Stage mainStage)
	{
		this.mainStage = mainStage;
		
		final URL jarURL = this.getClass().getProtectionDomain().getCodeSource().getLocation();
		
		if(jarURL == null)
		{
			logger.log(Level.FATAL, "Can't get access to JAR location! Terminating the program!");
			System.exit(-1);
		}
		
		final String jarPath = new File(jarURL.getPath()).getParentFile().getAbsolutePath();
		
		jarLocation = jarPath.replace("/", "\\\\");
		
		logger.log(Level.INFO, "Current JAR directory: " + jarLocation);
		
		prefPath = jarLocation + "\\\\pref.json";
		recentlyPath = jarLocation + "\\\\recently.json";
		
		prefCtrl = new PreferenceController();
		recentlyCtrl = new RecentlyController();
	}
	
	
	public Stage getMainStage()
	{
		return mainStage;
	}
	
	
	public Preferences getPreferences()
	{
		return prefCtrl.preferences;
	}
	
	public void pushRecently(@NotNull String path)
	{
		switch(recentlyCtrl.validatePath(path, true))
		{
		case TRUE:
			if(recentlyCtrl.recently.size() >= 5)
				recentlyCtrl.recently.remove(recentlyCtrl.recently.size() - 1);
			
			recentlyCtrl.recently.add(0, path);
			
			recentlyCtrl.save();
			
			break;
		case FIRST:
			
			recentlyCtrl.recently.remove(path);
			
			pushRecently(path);
			
			break;
		default:
			break;
		}
	}
	
	public Collection<String> getRecentlies()
	{
		return Collections.unmodifiableCollection(recentlyCtrl.recently);
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
				e.printStackTrace();
				logger.log(Level.FATAL, "There was catched IOException. Terminating the program! Path: " + prefPath);
				System.exit(-1);
			} catch (SecurityException e)
			{
				e.printStackTrace();
				logger.log(Level.FATAL, "Something blocked creating new preferences file. Terminating the program! Path: " + prefPath);
				System.exit(-1);
			}
		}
		
		private boolean isCreated()
		{
			return Files.exists(Path.of(prefPath));
		}
	}
	
	private class RecentlyController
	{
		private final File recentlyFile = new File(recentlyPath);
		private final List<String> recently;
		
		private RecentlyController()
		{
			if(!isCreated())
				create();
			
			recently = load();
			
			save();
		}
		
		private void save()
		{
			final JSONUtil jsonUtil = new JSONUtil(recentlyFile);
			final JSONObject root = new JSONObject();
			
			root.put("recently", new JSONArray(recently));
			
			jsonUtil.write(root.toString());
		}
		
		private List<String> load()
		{
			logger.log(Level.INFO, "Recentlies already created!");
			
			final JSONUtil jsonUtil = new JSONUtil(recentlyFile);
			final String json = jsonUtil.read();
			
			final JSONTokener jsonTokener = new JSONTokener(json);
			final JSONObject root;
			final JSONArray array;
			
			try
			{
				root = new JSONObject(jsonTokener);
			} catch (JSONException e)
			{
				logger.log(Level.ERROR, "There is syntax error in JSON file.");
				return new LinkedList<>();
			}
			
			try
			{
				array = root.getJSONArray("recently");
			} catch (JSONException e)
			{
				logger.log(Level.ERROR, "Can't access to recently array. Tag of array may be not exists or value is not JSONArray!");
				return new LinkedList<>();
			}
			
			final List<String> newList = new LinkedList<>();
			
			array.toList().forEach(str -> {
				final String path;
				
				try
				{
					path = (String) str;
					
					if(validatePath(path, false) == VALIDATE_RESULT.TRUE)
						newList.add(path);
				} catch (ClassCastException e)
				{
					logger.log(Level.ERROR, "Can't cast object of array to string!");
				}
			});
			
			if(newList.size() > 5)
				for(int i = 5; i < newList.size(); i++)
					newList.remove(i);
			
			return newList;
		}
		
		private void create()
		{
			logger.log(Level.INFO, "Recentlies aren't created! Creating now...");
			
			try
			{
				recentlyFile.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
				logger.log(Level.FATAL, "There was catched IOException. Terminating the program! Path: " + prefPath);
				System.exit(-1);
			} catch (SecurityException e)
			{
				e.printStackTrace();
				logger.log(Level.FATAL, "Something blocked creating new recentlies file. Terminating the program! Path: " + prefPath);
				System.exit(-1);
			}
		}
		
		private VALIDATE_RESULT validatePath(@NotNull String path, boolean onContains)
		{
			boolean properlyExtension = false;
			
			try
			{
				final String extension = FilenameUtils.getExtension(path);
				
				properlyExtension = "json".equals(extension);
			} catch (IllegalArgumentException e)
			{
				logger.log(Level.WARN, "Path \"" + path + "\" has bad extension or isn't path!");
				return VALIDATE_RESULT.FALSE;
			}
			
			if(onContains)
			{
				if(!recentlyCtrl.recently.isEmpty() && !recentlyCtrl.recently.get(0).equals(path) && recentlyCtrl.recently.contains(path))
				{
					logger.log(Level.INFO, "Path \"" + path + "\" already in recentlies, but not first!");
					return VALIDATE_RESULT.FIRST;
				}
				
				if(Files.exists(Path.of(path)) && properlyExtension && !recentlyCtrl.recently.contains(path))
				{
					logger.log(Level.INFO, "Path \"" + path + "\" was validated!");
					return VALIDATE_RESULT.TRUE;
				}
			}
			else
			{
				if(Files.exists(Path.of(path)) && properlyExtension)
				{
					logger.log(Level.INFO, "Path \"" + path + "\" was validated!");
					return VALIDATE_RESULT.TRUE;
				}
			}
			
			logger.log(Level.WARN, "Path \"" + path + "\" has bad extension or it is already in recentlies!");
			return VALIDATE_RESULT.FALSE;
		}
		
		private boolean isCreated()
		{
			return Files.exists(Path.of(recentlyPath));
		}
		
		private enum VALIDATE_RESULT
		{
			TRUE,
			FIRST,
			FALSE
		}
	}
	
	private final Logger logger = Logger.getLogger(Core.class);
}
