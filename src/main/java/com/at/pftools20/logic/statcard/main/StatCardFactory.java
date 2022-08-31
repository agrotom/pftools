package com.at.pftools20.logic.statcard.main;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.at.pftools20.logic.enums.CardType;

public class StatCardFactory
{
	private static final Logger logger = Logger.getLogger(StatCardFactory.class);
	
	public @Nullable StatCard createFromJSON(@NotNull String json)
	{
		final JSONObject root;
		
		try
		{
			root = new JSONObject(new JSONTokener(json));
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "There is syntax error in JSON argument!");
			return null;
		}
		
		final String rawCardType;
		
		try
		{
			rawCardType = root.getString("type");
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "There is no \"type\" tag in JSON argument!");
			return null;
		}
		
		final CardType cardType;
		
		try
		{
			cardType = CardType.valueOf(rawCardType);
		} catch (IllegalArgumentException e)
		{
			logger.log(Level.ERROR, "Unknown type \"" + rawCardType + "\"!");
			return null;
		}
		
		final JSONObject main;
		
		try
		{
			main = root.getJSONObject("main");
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "There is no \"main\" tag in JSON argument!");
			return null;
		}
		
		final StatCard statCard = createPure(cardType);
		
		if(statCard != null)
			statCard.loadCardFromFile(main);
		
		return statCard;
	}

	public @Nullable StatCard createPure(@NotNull CardType cardType)
	{
		switch(cardType)
		{
		case ACTOR:
			return new Actor();
		case ARMOR:
			return new Armor();
		case HERO:
			return new Item();
		case ITEM:
			return new Item();
		case SPELL:
			return new Spell();
		case WEAPON:
			return new Weapon();
		default:
			return null;
		
		}
	}
}
