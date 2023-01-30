package com.at.pftools20.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import com.at.pftools20.logic.statcard.main.Actor;

public class LivingActor
{

	public final Actor template;
	private final String templatePath;
	
	private int hp;
	private final List<String> effects = new LinkedList<>();
	
	private List<Consumer<Integer>> updateHandler = new LinkedList<>();
	
	//TODO: EFFECTLIST
	
	public LivingActor(final Actor actor, final String path)
	{
		template = actor;
		templatePath = path;
		
		hp = actor.hp.getValue();
	}
	
	public LivingActor(@NotNull Actor actor, @NotNull String path, final int hp, @NotNull final List<String> effects)
	{
		template = actor;
		templatePath = path;
		this.hp = hp;
		this.effects.addAll(effects);
		
	}
	
	/**
	 * Heals actor for given points.
	 * @param points
	 * @return true if healed more than max health points. False is default.
	 */
	public boolean heal(final int points)
	{
		hp += points;
		
		update();
		
		return false;
	}
	
	/**
	 * Damage actor for given points.
	 * @param points
	 * @return true if actor was damaged to death. False is default.
	 */
	public boolean damage(final int points)
	{
		if(hp - points <= 0)
		{
			hp = 0;
			update();
			return true;
		}
		
		hp -= points;
		
		update();
		
		return false;
	}
	
	public void addEffect(final String effect)
	{
		effects.add(effect);
	}
	
	public void removeEffect(final int index)
	{
		effects.remove(index);
	}
	
	/**
	 * Uses for widget update (e.g. changing HP for actor).
	 * @see com.at.pftools20.util.WidgetBuilder
	 */
	public void update()
	{
		updateHandler.forEach(consumer -> consumer.accept(hp));
	}
	
	public String getPath()
	{
		return templatePath;
	}
	
	public int getHP()
	{
		return hp;
	}
	
	public List<String> getEffects()
	{
		return effects;
	}
	
	public List<Consumer<Integer>> getUpdateHandler()
	{
		return updateHandler;
	}
}
