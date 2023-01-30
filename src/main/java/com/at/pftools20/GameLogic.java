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
import com.at.pftools20.util.FileManager;

public class GameLogic
{
	private final List<LivingActor> actorList = new LinkedList<>();
	private final List<Hero> heroList = new LinkedList<>();
	
	public void addActor(final String... paths)
	{
		if(paths == null)
		{
			logger.log(Level.WARN, "Given args are null!");
			return;
		}
		
		for(final String path : paths)
		{
			final FileManager fileManager = new FileManager();
			final StatCard card = fileManager.openSilentStatCard(path);
			
			if(card.getType() == CardType.ACTOR)
				actorList.add(new LivingActor((Actor) card, path));
			else if(card.getType() == CardType.HERO)
				heroList.add((Hero) card);
		}
				
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
