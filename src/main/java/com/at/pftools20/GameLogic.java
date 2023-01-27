package com.at.pftools20;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.at.pftools20.logic.LivingActor;
import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.statcard.main.Actor;
import com.at.pftools20.logic.statcard.main.Hero;
import com.at.pftools20.logic.statcard.main.StatCard;

public class GameLogic
{
	private final List<LivingActor> actorList = new LinkedList<>();
	private final List<Hero> heroList = new LinkedList<>();
	
	public void addActor(final StatCard... cards)
	{
		if(cards == null)
		{
			logger.log(Level.WARN, "Can't add actor card, because given args are null!");
			return;
		}
		
		for(final StatCard actor : cards)
			if(actor.getType() == CardType.ACTOR)
				actorList.add(new LivingActor((Actor) actor));
			else if(actor.getType() == CardType.HERO)
				heroList.add((Hero) actor);
				
	}
	
	public void removeActor(final int index)
	{
		actorList.remove(index);
	}
	
	@SuppressWarnings("exports")
	public Collection<LivingActor> getActorList()
	{
		return Collections.unmodifiableCollection(actorList);
	}
	
	public Collection<Hero> getHeroList()
	{
		return Collections.unmodifiableCollection(heroList);
	}
	
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	//TODO: SAVING
}
