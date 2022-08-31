package com.at.pftools20.logic.statcard.main;

import java.util.List;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.enums.Dice;
import com.at.pftools20.logic.props.main.CritHitProperty;
import com.at.pftools20.logic.props.main.CritHitProperty.CriticalHit;
import com.at.pftools20.logic.props.main.DiceProperty;
import com.at.pftools20.logic.props.main.IntegerProperty;
import com.at.pftools20.logic.props.main.IntegerProperty.Range;
import com.at.pftools20.logic.props.main.TypeProperty;

public class Weapon extends Item
{
	public final IntegerProperty distance = IntegerProperty.newBuilder("distance", 0).setRange(Range.UNSIGNED_INT).register(properties).build();
	public final TypeProperty grip = TypeProperty.newBuilder("grip", "one-handed", List.of(
				"one-handed",
				"bastard",
				"two-handed"
			)).register(properties).build();
	
	public final DiceProperty damage = DiceProperty.newBuilder("damage", Dice.D4, 1).register(properties).build();
	public final CritHitProperty critical = CritHitProperty.newBuilder("critical", new CriticalHit(1)).register(properties).build();

	@Override
	public CardType getType()
	{
		// TODO Auto-generated method stub
		return CardType.WEAPON;
	}

}
