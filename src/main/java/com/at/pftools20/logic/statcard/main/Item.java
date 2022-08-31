package com.at.pftools20.logic.statcard.main;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.props.main.IntegerProperty;
import com.at.pftools20.logic.props.main.IntegerProperty.Range;

public class Item extends StatCard
{
	public final IntegerProperty price = IntegerProperty.newBuilder("price", 1).setRange(Range.UNSIGNED_INT_ZERO)
			.register(properties).build();
	
	@Override
	public CardType getType() { return CardType.ITEM; }
	
}
