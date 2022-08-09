package com.at.pftools20.core.preferences;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.at.pftools20.logic.props.main.BaseProperty;
import com.at.pftools20.logic.props.main.ImmutableProperty;
import com.at.pftools20.logic.props.main.StringProperty;
import com.at.pftools20.logic.props.main.TypeProperty;

public class Preferences
{
	private final List<BaseProperty<?>> preferenceList = new ArrayList<>();
	
	public Preferences() {}
	
	public Preferences(@NotNull String json)
	{
		if(json.isEmpty())
		{
			logger.log(Level.WARN, "JSON argument is empty string!");
			return;
		}
		
		final JSONTokener jsonTokener = new JSONTokener(json);
		final JSONObject root;
		
		try
		{
			root = new JSONObject(jsonTokener);
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "JSON parse exception! Bad syntax very well.");
			return;
		}
		
		preferenceList.forEach(pref -> {
			final String prefTag = pref.getTag();
			final String value = root.getString(prefTag);
			
			if(value != null)
				pref.parseJSON(value);
			else
				logger.log(Level.WARN, "There is no value for property \"" + prefTag + "\" in JSON file!");
		});
	}
	
	public String toJSON()
	{
		final JSONObject root = new JSONObject();
		
		preferenceList.forEach(pref -> {
			root.put(pref.getTag(), pref.toJSON());
		});
		
		return root.toString();
	}
	
	public List<BaseProperty<?>> getNewList()
	{
		return new ArrayList<>(preferenceList);
	}
	
	public final ImmutableProperty<String> assetPath = StringProperty.newBuilder("assetPath", "-").register(preferenceList).build();
	public final ImmutableProperty<String> language = TypeProperty.newBuilder("language", "en", List.of("en", "ru"))
			.register(preferenceList).build();
	
	private final Logger logger = Logger.getLogger(Preferences.class);
}
