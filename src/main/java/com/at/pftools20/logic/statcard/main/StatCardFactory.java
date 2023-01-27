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
		
		try // Getting a card as JSON representation
		{
			root = new JSONObject(new JSONTokener(json));
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "There is syntax error in JSON argument!");
			return null;
		}
		
		final String rawCardType;
		
		try // Get type of a parsed card
		{
			rawCardType = root.getString("type");
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "There is no \"type\" tag in JSON argument!");
			return null;
		}
		
		final CardType cardType;
		
		try // Validating a card type
		{
			cardType = CardType.valueOf(rawCardType);
		} catch (IllegalArgumentException e)
		{
			logger.log(Level.ERROR, "Unknown type \"" + rawCardType + "\"!");
			return null;
		}
		
		final JSONObject main;
		
		try // Getting a main info about card as JSON representation
		{
			main = root.getJSONObject("main");
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "There is no \"main\" tag in JSON argument!");
			return null;
		}
		
		final StatCard statCard = createPure(cardType); // Creating a pure card
		
		if(statCard != null)
			statCard.parseCardFromJSON(main); // Parsing JSON if it's valid
		
		return statCard;
	}

	/**
	 * Creates pure card by given type.
	 * @param cardType
	 * @return Baked StatCard. If given type is null, method will return null!
	 */
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
