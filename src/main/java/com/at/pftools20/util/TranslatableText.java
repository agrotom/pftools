package com.at.pftools20.util;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.at.pftools20.HelloApplication;
import com.at.pftools20.util.json.JSONUtil;

public class TranslatableText
{
	/**
	 * 
	 * @param rawString Unlocalized string.
	 * @return Formatted string based on current application language. Returns {@link rawString}
	 * if method faced with unexpected problems.
	 */
	public static String formatString(@NotNull String rawString)
	{
		final String langRawFilePath = "/lang/"
				+ HelloApplication.getAppCore().getPreferences().language.toString() + ".json";
		final URL langFile = TranslatableText.class.getResource(langRawFilePath);
		
		if(langFile == null)
		{
			logger.log(Level.ERROR, "Resource \"" + langRawFilePath + "\" not found!");
			return rawString;
		}
		
		final String langFilePath = langFile.getPath();
		
		final JSONUtil jsonUtil = new JSONUtil(new File(langFilePath));
		
		final String rawJSON = jsonUtil.read();
		
		if(rawJSON.isEmpty())
		{
			logger.log(Level.ERROR, "JSON is empty! Check syntax!");
			return rawString;
		}
		
		final JSONTokener jsonTokener = new JSONTokener(rawJSON);
		final JSONObject root = new JSONObject(jsonTokener);
		
		
		
		return rawJSON;
	}
	
	private static final Logger logger = Logger.getLogger(TranslatableText.class);
}
