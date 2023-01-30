package com.at.pftools20.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.at.pftools20.GameLogic;
import com.at.pftools20.util.json.LivingActorBuilder;

public class GameSave {
	public static JSONObject saveGame(final GameLogic logic)
	{
		final JSONObject root = new JSONObject();
		final JSONArray array = new JSONArray();
		
		final LivingActorBuilder builder = new LivingActorBuilder();
		
		logic.getActorList().forEach(actor -> {
			array.put(builder.saveToJSON(actor));
		});
		
		root.append("actorList", array);
		
		return root;
	}
	
	public static GameLogic loadGame(final JSONObject json)
	{
		return new GameLogic();
	}
}
