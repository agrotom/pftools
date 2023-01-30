package com.at.pftools20.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.at.pftools20.App;

public class TranslatableText
{
	private static final Logger logger = Logger.getLogger(TranslatableText.class);
	
	private static final JSONObject defaultLangJSON = getLangJSON("/lang/en.json");
	private static final JSONObject langJSON = getLangJSON("/lang/"
			+ App.getAppCore().getPreferences().language.toString() + ".json");
	
	/**
	 * 
	 * @param rawString Unlocalized string.
	 * @return Formatted string based on current application language. Returns {@link rawString}
	 * if method faced with unexpected problems.
	 */
	public static String formatString(@NotNull String rawString)
	{
		String formattedText = null;
		
		if(langJSON != null)
		{
			try
			{
				formattedText = langJSON.getString(rawString);
			} catch (Exception e)
			{
				logger.log(Level.WARN, "There is no localization for: \"" + rawString + "\" in \""
						+ App.getAppCore().getPreferences().language.toString() + ".json\" language file! "
						+ "Try to find it in default language file!");
			}
			
			
			if(formattedText == null && defaultLangJSON != null)
			{
				try
				{
					formattedText = defaultLangJSON.getString(rawString);
				} catch (Exception e)
				{
					logger.log(Level.WARN, "There is no localization for: \"" + rawString + "\" in "
							+ "default language file! Return unlocalized string!");
				}
				
				if(formattedText == null)
					formattedText = rawString;
			}
		}
		
		return formattedText;
	}
	

	private static JSONObject getLangJSON(@NotNull String resourcePath)
	{
		/*final String langRawFilePath = "/lang/"
				+ HelloApplication.getAppCore().getPreferences().language.toString() + ".json";
		final URL langFile = TranslatableText.class.getResource(resourcePath);
		
		if(langFile == null)
		{
			logger.log(Level.ERROR, "Resource \"" + langRawFilePath + "\" not found! Set language string to empty!");
			return null;
		}*/
		
		/*final String langFilePath = /*langFile.getPath(); Helper.getResourcePath(resourcePath);
		
		logger.log(Level.INFO, langFilePath);
	
		if(langFilePath.isEmpty())
		{
			logger.log(Level.ERROR, "Resource \"" + resourcePath + "\" not found! Set language string to empty!");
			return null;
		}
		
		final JSONUtil jsonUtil = new JSONUtil(new File(langFilePath));
		
		final String rawJSON = jsonUtil.read();
		
		if(rawJSON.isEmpty())
		{
			logger.log(Level.ERROR, "JSON is empty! Check syntax! Set language string to empty!");
			return null;
		}
		
		return new JSONObject(new JSONTokener(rawJSON));*/
		
		final InputStream langFileStream = ResourceHelper.getResourceAsStream(resourcePath);
		
		if(langFileStream == null)
		{
			logger.log(Level.ERROR, "Resource \"" + resourcePath + "\" not found! Set language string to empty!");
			return null;
		}
		
		final JSONTokener jsonTokener = new JSONTokener(new InputStreamReader(langFileStream, StandardCharsets.UTF_8));
		
		JSONObject root = null;
		
		try
		{
			root = new JSONObject(jsonTokener);
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "Check syntax! Set language string to empty!");
			return null;
		}
		
		try
		{
			langFileStream.close();
		} catch (IOException e)
		{
			logger.log(Level.ERROR, "Can't close input stream!");
		}
		
		return root;
	}
}
