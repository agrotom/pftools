package com.at.pftools20.logic.props.main;

import javafx.scene.control.Label;

import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;

import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class CritHitProperty extends BaseProperty<CritHitProperty.CriticalHit>
{
	

	private CritHitProperty(@NotNull String tag, @NotNull CriticalHit value)
	{
		super(tag);
		
		setValue(value);
	}

	
	@Override
	public void parse(String string)
	{
		final CriticalHit value = getValue();
		
		if (string.isEmpty())
		{
			logger.log(Level.ERROR, "String is empty! String won't be parsed!");
			return;
		}
			
		final String[] construct = string.split("x");
		
		if (construct.length != 2)
		{
			logger.log(Level.WARN, "Bad format \"MIND20VALUExMULTIPLIER\"!");
			return;
		}
		
		final int rawMinD20Value;
		final int rawMultiplier;
		
		try
		{
			rawMinD20Value = Integer.parseInt(construct[0]);
			rawMultiplier = Integer.parseInt(construct[1]);
		} catch(NumberFormatException e)
		{
			logger.log(Level.WARN, "It seems, that string contains no number!");
			return;
		}
		
		if(rawMinD20Value > 20)
			value.minD20Value = 20;
		else if(rawMinD20Value < 1)
			value.minD20Value = 1;
		else
			value.minD20Value = rawMinD20Value;
		
		if(rawMultiplier < 1)
			value.multiplier = 1;
		else
			value.multiplier = rawMultiplier;
	}
	
	@Override
	public String toString()
	{
		final CriticalHit value = getValue();
		
		return value.minD20Value + "x" + value.multiplier;
	}

	@Override
	public void parseJSON(String jsonValue)
	{
		parse(jsonValue);
	}

	@Override
	public String toJSON()
	{
		return toString();
	}

	
	@Override
	protected CriticalHit correctValue(CriticalHit newValue)
	{
		return newValue;
	}
	
	
	@Override
	public PropertyStruct getPropertyStruct()
	{
		return new CritHitPropertyStruct();
	}

	
	private class CritHitPropertyStruct implements PropertyStruct
	{
		private final Spinner<Integer> d20Spin = new Spinner<>(1, 20, 0);
		private final Label xSymbol = new Label("x");
		private final Spinner<Integer> multSpin = new Spinner<>(1, Integer.MAX_VALUE, 0);
		
		private final HBox root = new HBox(d20Spin, xSymbol, multSpin);
		
		private CritHitPropertyStruct()
		{
			init();
		}
		
		private void init()
		{
			// Root initialize
			root.setSpacing(10.0);
			
			// Spinners initialize
			final CriticalHit value = getValue();
			
			d20Spin.getValueFactory().setValue(value.minD20Value);
			multSpin.getValueFactory().setValue(value.multiplier);
		}
		
		@Override
		public Region getMainNode()
		{
			return root;
		}
		
	}

	
	public class CritHitPropBuilder extends Builder<CritHitPropBuilder, CritHitProperty>
	{

		@Override
		protected CritHitPropBuilder getThis()
		{
			return this;
		}

		@Override
		public CritHitProperty build()
		{
			return CritHitProperty.this;
		}
		
	}
	
	public static CritHitPropBuilder newBuilder(@NotNull String tag, @NotNull CriticalHit value) { return new CritHitProperty(tag, value)
			.new CritHitPropBuilder(); }
	
	/**
	 * 
	 * Simple class, containing minimum D20 value and its multiplier. Model of this class is: MinD20 x Multiplier
	 *
	 */
	public static class CriticalHit
	{
		private int minD20Value = 20;
		private int multiplier = 1;
		
		public CriticalHit(int multiplier)
		{
			assert multiplier > 0;
			
			if(multiplier > 0)
				this.multiplier = multiplier;
		}
		
		public CriticalHit(int multiplier, int minD20Value)
		{
			this(multiplier);
			
			assert minD20Value >= 1 && minD20Value <= 20;
			
			if(minD20Value >= 1 && minD20Value <= 20)
				this.minD20Value = minD20Value;
		}
	}
}
