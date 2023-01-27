package com.at.pftools20.logic.statcard.main;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.props.main.IntegerProperty;
import com.at.pftools20.logic.props.main.StringProperty;
import com.at.pftools20.logic.props.main.Range;

public class Spell extends StatCard
{
	public final IntegerProperty distance = IntegerProperty.newBuilder("distance", 1).setRange(Range.UNSIGNED_INT_ZERO)
			.setCustomDescriptionFormatID("property.spell.distance.description").register(properties).build();
	
	public final StringProperty time = StringProperty.newBuilder("time", "No info").register(properties).build();

	@Override
	public CardType getType()
	{
		// TODO Auto-generated method stub
		return CardType.SPELL;
	}

}
