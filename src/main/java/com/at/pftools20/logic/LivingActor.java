package com.at.pftools20.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import com.at.pftools20.logic.statcard.main.Actor;

public class LivingActor
{

	public final Actor template;
	private int hp;
	private List<Consumer<Integer>> updateHandler = new LinkedList<>();
	
	//TODO: EFFECTLIST
	
	public LivingActor(final Actor actor)
	{
		template = actor;
		hp = actor.hp.getValue();
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
	
	/**
	 * Uses for widget update (e.g. changing HP for actor).
	 * @see com.at.pftools20.util.WidgetBuilder
	 */
	public void update()
	{
		updateHandler.forEach(consumer -> consumer.accept(hp));
	}
	
	public int getHP()
	{
		return hp;
	}
	
	public List<Consumer<Integer>> getUpdateHandler()
	{
		return updateHandler;
	}
}
