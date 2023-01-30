package com.at.pftools20.logic.enums;

import org.jetbrains.annotations.NotNull;

public enum CardType {
	ACTOR,
	ITEM,
	WEAPON(ITEM),
	ARMOR(ITEM),
	SPELL,
	HERO;
	
	private final CardType parent;
	
	private CardType()
	{
		this.parent = null;
	}
	
	private CardType(@NotNull CardType parent)
	{
		this.parent = parent;
	}
	
	public boolean isChild(@NotNull CardType type)
	{
		if(parent == null)
			return false;
		else if(parent == type)
			return true;
		else
			parent.isChild(type);
		
		return false;
	}
}
