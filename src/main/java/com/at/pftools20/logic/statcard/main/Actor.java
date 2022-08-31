package com.at.pftools20.logic.statcard.main;

import java.util.LinkedList;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.props.main.ListProperty;

public class Actor extends StatCard
{
	public final ListProperty inventory = ListProperty.newBuilder("inventory", new LinkedList<>()).register(properties).setType(CardType.ITEM).build();

	@Override
	public CardType getType()
	{
		return CardType.ACTOR;
	}

}
