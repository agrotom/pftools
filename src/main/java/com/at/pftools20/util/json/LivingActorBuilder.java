package com.at.pftools20.util.json;

import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.at.pftools20.logic.LivingActor;
import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.statcard.main.Actor;
import com.at.pftools20.logic.statcard.main.StatCard;
import com.at.pftools20.util.FileManager;

public class LivingActorBuilder {
	public JSONObject saveToJSON(@NotNull final LivingActor actor)
	{
		final JSONObject root = new JSONObject();
		
		root.append("path", actor.getPath());
		root.append("hp", actor.getHP());
		root.append("effects", new JSONArray(actor.getEffects()));
		
		return root;
	}
	
	public @Nullable LivingActor loadFromJSON(@NotNull final JSONObject root)
	{
		final Actor actor;
		final String templatePath;
		int hp;
		List<String> effects = new LinkedList<>();
		
		try {
			templatePath = root.getString("path");
			
			final FileManager fileManager = new FileManager();
			final StatCard card = fileManager.openSilentStatCard(templatePath);
			
			if(card == null || card.getType() != CardType.ACTOR)
				return null;
			
			actor = (Actor) card;
			
		} catch (JSONException e) {
			return null;
		}
		
		try {
			hp = root.getInt("hp");
		} catch (JSONException e) {
			hp = actor.hp.getValue();
		}
		
		try {
			final JSONArray jsonArray = root.getJSONArray("effects");
			jsonArray.forEach(obj -> {
				if(obj instanceof String)
					effects.add((String) obj);
			});
			
		} catch (JSONException ignore) {}
		
		return new LivingActor(actor, templatePath, hp, effects);
	}
}
