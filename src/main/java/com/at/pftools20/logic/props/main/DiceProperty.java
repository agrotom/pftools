package com.at.pftools20.logic.props.main;

import com.at.pftools20.logic.enums.Dice;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DiceProperty extends BaseProperty<DiceProperty.DiceMultiplier>
{
	private DiceProperty(@NotNull String tag, @NotNull Dice dice, int multiplier)
	{
		super(tag);

		final DiceMultiplier value = new DiceMultiplier(dice, multiplier);

		setValue(value);
	}

	
	/**
	 * Parses value from string. If string can't be parsed, inherit class method
	 * will set property value to base value of its generic type.
	 *
	 * @param string String to parse.
	 */
	@Override
	public void parse(@NotNull String string)
	{
		getValue().parse(string);
	}

	/**
	 * Converts property to JSON format. Most of primitive properties, like Integer,
	 * String etc. returns same value as "toString".
	 *
	 * @return JSON as string.
	 */
	@Override
	public String toJSON()
	{
		return toString();
	}

	/**
	 * Converts JSON value to property value. Most of primitive properties, like
	 * Integer, String etc. have same behavior as "parse".
	 *
	 * @param jsonValue JSON value.
	 */
	@Override
	public void parseJSON(@NotNull String jsonValue)
	{
		parse(jsonValue);
	}

	
	@Override
	protected DiceMultiplier correctValue(@NotNull DiceProperty.DiceMultiplier newValue)
	{
		return newValue;
	}

	
	@Override
	public PropertyStruct getPropertyStruct()
	{
		return new DicePropertyStruct();
	}
	
	private class DicePropertyStruct implements PropertyStruct
	{
		private final Spinner<Integer> spinner = new Spinner<>(1, Integer.MAX_VALUE, 1);
		private final Label multLabel = new Label("*");
		private final ChoiceBox<Dice> choiceBox = new ChoiceBox<>(
				FXCollections.observableList(List.of(Dice.D4, Dice.D6, Dice.D8, Dice.D10, Dice.D12, Dice.D20)));

		private final HBox root = new HBox(spinner, multLabel, choiceBox);

		private DicePropertyStruct()
		{
			init();
		}

		private void init()
		{
			final DiceMultiplier thisValue = getValue();

			// Root initialize
			root.setSpacing(10.0);

			// Spinner initialize
			spinner.getValueFactory().setValue(thisValue.multiplier);
			spinner.setOnMouseClicked(this::onSpinnerChanged);

			// ChoiceBox initialize
			choiceBox.setValue(thisValue.dice);
			choiceBox.setOnAction(this::onDiceChanged);
		}

		private void onSpinnerChanged(@NotNull MouseEvent event)
		{
			DiceProperty.this.getValue().multiplier = spinner.getValue();
		}

		private void onDiceChanged(@NotNull ActionEvent event)
		{
			DiceProperty.this.getValue().dice = choiceBox.getValue();
		}

		@Override
		public Region getMainNode()
		{
			return root;
		}
	}


	public class DicePropBuilder extends Builder<DicePropBuilder, DiceProperty>
	{

		@Override
		protected DicePropBuilder getThis()
		{
			return this;
		}

		@Override
		public DiceProperty build()
		{
			return DiceProperty.this;
		}
	}

	@Contract("_, _, _ -> new")
	public static @NotNull DicePropBuilder newBuilder(@NotNull String tag, @NotNull Dice dice, int multiplier)
	{
		return new DiceProperty(tag, dice, multiplier).new DicePropBuilder();
	}

	protected static class DiceMultiplier
	{
		private Dice dice;
		private int multiplier;

		public DiceMultiplier(@NotNull Dice dice, int multiplier)
		{
			this.dice = dice != null ? dice : Dice.NONE;
			this.multiplier = multiplier > 0 ? multiplier : 1;
		}

		public void parse(@NotNull String string)
		{
			if (string.isEmpty())
			{
				logger.log(Level.WARN, "String is empty! String won't be parsed!");
				return;
			}

			final String[] construct = string.split(":"); // Template: MULTIPLIER:DICE

			if (construct.length != 2)
			{
				logger.log(Level.WARN, "Bad format \"MULTIPLIER:DICE\"!");
				return;
			}

			final Dice rawDice;

			try
			{
				rawDice = Dice.valueOf(construct[0]);
			} catch (IllegalArgumentException e)
			{
				logger.log(Level.WARN, "It seems, that string contains bad enumeration!");
				return;
			}

			final int rawMultiplier;

			try
			{
				rawMultiplier = Integer.parseInt(construct[1]);
			} catch (NumberFormatException e)
			{
				logger.log(Level.WARN, "It seems, that string contains no number!");
				return;
			}

			this.dice = rawDice;
			this.multiplier = rawMultiplier;
		}

		@Contract(pure = true)
		@Override
		public @NotNull String toString()
		{
			return multiplier + ":" + dice;
		}

		private static final Logger logger = Logger.getLogger(DiceMultiplier.class);
	}
}
