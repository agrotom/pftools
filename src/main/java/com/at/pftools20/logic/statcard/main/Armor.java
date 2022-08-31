package com.at.pftools20.logic.statcard.main;

import java.util.List;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.props.main.IntegerProperty;
import com.at.pftools20.logic.props.main.TypeProperty;
import com.at.pftools20.logic.props.main.IntegerProperty.Range;

public class Armor extends Item
{
	public final IntegerProperty quality = IntegerProperty.newBuilder("quality", 1).setRange(Range.UNSIGNED_INT_ZERO).register(properties).build();
	public final TypeProperty type = TypeProperty.newBuilder("type", "none", List.of("none", "light", "medium", "heavy", "super"))
			.register(properties).setCustomDescriptionFormatID("property.armor.type.description").build();

	@Override
	public CardType getType()
	{
		// TODO Auto-generated method stub
		return CardType.ARMOR;
	}

}
