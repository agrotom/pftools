package com.at.pftools20.logic.statcard.main;

import java.util.List;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.props.main.IntegerProperty;
import com.at.pftools20.logic.props.main.Range;
import com.at.pftools20.logic.props.main.StringProperty;
import com.at.pftools20.logic.props.main.TypeProperty;

public class Hero extends StatCard
{

	public final StringProperty kind = StringProperty.newBuilder("kind", "No info").register(properties).build();
	public final TypeProperty idea = TypeProperty.newBuilder("idea", "No info", List.of(
				"lg", "ln", "le",
				"ng", "n", "ne",
				"cg", "cn", "ce"
			)).register(properties).build();
	
	public final IntegerProperty hp = IntegerProperty.newBuilder("hp", 1).setRange(Range.UNSIGNED_INT_ZERO)
			.register(properties).build();
	
	public final StringProperty perception = StringProperty.newBuilder("perception", "No info").register(properties).build();
	public final IntegerProperty care = IntegerProperty.newBuilder("care", 0).register(properties).build();
	
	public final IntegerProperty xp = IntegerProperty.newBuilder("xp", 0).setRange(Range.UNSIGNED_INT)
			.register(properties).build();
	
	public final IntegerProperty strength = IntegerProperty.newBuilder("strength", 0).register(properties).build();
	public final IntegerProperty agility = IntegerProperty.newBuilder("agility", 0).register(properties).build();
	public final IntegerProperty endurance = IntegerProperty.newBuilder("endurance", 0).register(properties).build();
	public final IntegerProperty intelligence = IntegerProperty.newBuilder("intelligence", 0).register(properties).build();
	public final IntegerProperty wisdom = IntegerProperty.newBuilder("wisdom", 0).register(properties).build();
	public final IntegerProperty charisma = IntegerProperty.newBuilder("charisma", 0).register(properties).build();
	
	@Override
	public CardType getType()
	{
		return CardType.HERO;
	}

}
