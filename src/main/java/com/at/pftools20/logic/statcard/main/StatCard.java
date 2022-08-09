package com.at.pftools20.logic.statcard.main;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.props.main.BaseProperty;
import com.at.pftools20.logic.props.main.StringProperty;
import com.at.pftools20.util.json.JSONUtil;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class StatCard {
    protected final List<BaseProperty<?>> properties = new LinkedList<>();

    public final StringProperty name = StringProperty.newBuilder("name", "No Info").setDescription("Name of card").register(properties).build();
    public final StringProperty description = StringProperty.newBuilder("description", "No Info").setDescription("Description of card").register(properties).build();
    
    public final boolean loadCardFromFile(@NotNull File file)
    {
    	final JSONUtil jsonUtil = new JSONUtil(file);
    	
    	final String json = jsonUtil.read();
    	
    	if(json.equals(""))
    	{
    		logger.log(Level.WARN, "JSON parse exception!");
    		return false;
    	}
    	
    	final JSONTokener jsonTokener = new JSONTokener(json);
    	final JSONObject root = new JSONObject(jsonTokener);
    	
    	final JSONObject main = root.getJSONObject("main");
    	
    	if(main == null)
    	{
    		logger.log(Level.WARN, "There is no main key in root!");
    		return false;
    	}
    	
    	final String rawCardType = root.getString("type");
    	
    	if(rawCardType == null)
    	{
    		logger.log(Level.WARN, "There is no type key in root!");
    		return false;
    	}
    	
    	final CardType cardType;
    	
    	try
    	{
    		cardType = CardType.valueOf(rawCardType);
    	} catch(IllegalArgumentException e)
    	{
    		logger.log(Level.WARN, "Unknown type of card!");
    		return false;
    	}
    	
    	if(cardType != getType())
    	{
    		logger.log(Level.WARN, "Card type in file not match to class type!");
    		return false;
    	}
    	
    	properties.forEach(property -> {
    		final String propTag = property.getTag();
    		final String value = main.getString(propTag);
    		
    		if(value == null)
    			logger.log(Level.WARN, "Property with tag: \"" + propTag + "\" is not existing in file!");
    		else
				property.parseJSON(value);
    	});
    	
    	return true;
    }
    
    /**
     * Converts Card to JSON format. If JSON has bad format, method return empty string!
     * @return Card in JSON format.
     */
    public final String toJSON()
    {
    	final JSONObject root = new JSONObject();
    	final JSONObject main = new JSONObject();
    	
    	root.put("main", main);
    	
    	properties.forEach(property -> root.put(property.getTag(), property.toJSON()));
    	
    	final CardType cardType = getType();
    	
    	if(cardType == null)
    	{
    		logger.log(Level.ERROR, "Card Type is null!");
    		return "";
    	}
    	
    	root.put("type", cardType.toString());
    	
    	return root.toString();
    }
    
    /**
     * 
     * @return copy of properties list, because properties list is immutable!
     */
    public List<BaseProperty<?>> copyOfProps()
    {
    	return new LinkedList<>(properties);
    }
    
    public abstract CardType getType();
    
    private final Logger logger = Logger.getLogger(StatCard.class);
}
