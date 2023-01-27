package com.at.pftools20.logic.statcard.main;

import java.util.LinkedList;
import java.util.List;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.props.main.IntegerProperty;
import com.at.pftools20.logic.props.main.Range;
import com.at.pftools20.logic.props.main.ListProperty;
import com.at.pftools20.logic.props.main.StringProperty;
import com.at.pftools20.logic.props.main.TypeProperty;

public class Actor extends StatCard
{
	public final StringProperty kind = StringProperty.newBuilder("kind", "No info").register(properties).build();
	public final TypeProperty idea = TypeProperty.newBuilder("idea", "No info", List.of(
				"lg", "ln", "le",
				"ng", "n", "ne",
				"cg", "cn", "ce"
			)).register(properties).build();
	
	public final IntegerProperty initiative = IntegerProperty.newBuilder("initiative", 0).register(properties).build();
	public final IntegerProperty speed = IntegerProperty.newBuilder("speed", 0).register(properties).build();
	public final IntegerProperty dangerClass = IntegerProperty.newBuilder("dangerClass", 1).setRange(Range.UNSIGNED_INT_ZERO)
			.register(properties).build();
	public final IntegerProperty hp = IntegerProperty.newBuilder("hp", 1).setRange(Range.UNSIGNED_INT_ZERO)
			.register(properties).build();
	
	public final StringProperty perception = StringProperty.newBuilder("perception", "No info").register(properties).build();
	public final IntegerProperty care = IntegerProperty.newBuilder("care", 0).register(properties).build();
	
	public final IntegerProperty xp = IntegerProperty.newBuilder("xp", 0).setRange(Range.UNSIGNED_INT)
			.register(properties).build();
	
	public final StringProperty defence = StringProperty.newBuilder("defence", "No info").register(properties).build();
	public final StringProperty attack = StringProperty.newBuilder("attack", "No info").register(properties).build();
	public final StringProperty params = StringProperty.newBuilder("params", "No info").register(properties).build();
	
	public final IntegerProperty strength = IntegerProperty.newBuilder("strength", 0).register(properties).build();
	public final IntegerProperty agility = IntegerProperty.newBuilder("agility", 0).register(properties).build();
	public final IntegerProperty endurance = IntegerProperty.newBuilder("endurance", 0).register(properties).build();
	public final IntegerProperty intelligence = IntegerProperty.newBuilder("intelligence", 0).register(properties).build();
	public final IntegerProperty wisdom = IntegerProperty.newBuilder("wisdom", 0).register(properties).build();
	public final IntegerProperty charisma = IntegerProperty.newBuilder("charisma", 0).register(properties).build();
	
	public final ListProperty inventory = ListProperty.newBuilder("inventory", new LinkedList<>()).register(properties).setType(CardType.ITEM).build();
	public final ListProperty treasure = ListProperty.newBuilder("treasure", new LinkedList<>()).register(properties).setType(CardType.ITEM).build();
	
	public final StringProperty specs = StringProperty.newBuilder("specs", "No info").register(properties).build();
	
	@Override
	public CardType getType()
	{
		return CardType.ACTOR;
	}

}
