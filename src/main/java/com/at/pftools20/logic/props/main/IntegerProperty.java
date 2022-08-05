package com.at.pftools20.logic.props.main;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.apache.log4j.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class IntegerProperty extends BaseProperty<Integer>
{
	private Range range = Range.DEFAULT_INT;

	private final PropertyStruct propertyStruct;

	private IntegerProperty(String tag, int value)
	{
		super(tag);

		setValue(value);

		propertyStruct = new IntegerPropertyStruct();
	}

	@Override
	public void parse(String string)
	{
		try
		{
			setValue(Integer.parseInt(string));
		} catch (NumberFormatException e)
		{
			logger.log(Level.WARN,
					"Parse argument doesn't contain a parsable string. Setting value to range minimum value!");
			setValue(range.min);
		}
	}

	@Override
	public String toJSON()
	{
		return toString();
	}

	@Override
	public void parseJSON(String jsonValue)
	{
		parse(jsonValue);
	}

	@Override
	protected Integer correctValue(Integer newValue)
	{
		if (range == null)
			return 0;

		if (range.forward(newValue))
			return newValue;
		else
			return range.min;
	}

	/**
	 * It is a simple class, that provide Range functional. Range model: [min; max].
	 */
	public static class Range
	{
		private final int max;
		private final int min;

		public static final Range DEFAULT_INT = new Range(Integer.MIN_VALUE, Integer.MAX_VALUE);
		public static final Range UNSIGNED_INT = new Range(0, Integer.MAX_VALUE);
		public static final Range UNSIGNED_INT_ZERO = new Range(1, Integer.MAX_VALUE);

		/**
		 * @param min Min value
		 * @param max Max value
		 */
		public Range(int min, int max)
		{
			this.max = max;
			this.min = min;
		}

		public boolean forward(int num)
		{
			return num >= min && num <= max;
		}

		public int getMax()
		{
			return max;
		}

		public int getMin()
		{
			return min;
		}
	}

	private class IntegerPropertyStruct implements PropertyStruct
	{

		private final Button buttonDecrease = new Button("-");
		private final Button buttonIncrease = new Button("+");

		private final Label counter = new Label();

		private final HBox root = new HBox(buttonDecrease, counter, buttonIncrease);

		private IntegerPropertyStruct()
		{
			init();
		}

		/**
		 * Initialize method. It initializes all nodes of this property struct.
		 */
		private void init()
		{
			// Root node initialize
			root.setSpacing(10.0);

			// Label initialize
			counter.setText(String.valueOf(getValue()));
			counter.setOnMouseDragged(this::onDragCounter);

			// Buttons initialize
			final double size = 27.0;

			buttonDecrease.setPrefWidth(size);
			buttonDecrease.setPrefHeight(size);
			buttonDecrease.setOnMousePressed(this::onButtonDecrease);

			buttonIncrease.setPrefWidth(size);
			buttonIncrease.setPrefHeight(size);
			buttonIncrease.setOnMousePressed(this::onButtonIncrease);
		}

		private void onButtonIncrease(final MouseEvent event)
		{
			if (event == null)
			{
				logger.log(Level.INFO, "Event is null!");
				return;
			}

			int increaseValue = 1;
			final int lastValue = getValue();

			if (event.isShiftDown() && !event.isControlDown())
				increaseValue = 1000;
			else if (event.isControlDown() && !event.isShiftDown())
				increaseValue = 1000000;

			if (event.isControlDown() && event.isShiftDown())
				increaseValue = 1000000000;

			if (range.forward(lastValue + increaseValue))
				setValue(lastValue + increaseValue);
			else
				setValue(range.getMin());

			counter.setText(String.valueOf(getValue()));
		}

		private void onButtonDecrease(final MouseEvent event)
		{
			if (event == null)
			{
				logger.log(Level.INFO, "Event is null!");
				return;
			}

			int increaseValue = 1;
			final int lastValue = getValue();

			if (event.isShiftDown() && !event.isControlDown())
				increaseValue = 1000;
			else if (event.isControlDown() && !event.isShiftDown())
				increaseValue = 1000000;

			if (event.isControlDown() && event.isShiftDown())
				increaseValue = 1000000000;

			if (range.forward(lastValue - increaseValue))
				setValue(lastValue - increaseValue);
			else
				setValue(range.getMax());

			counter.setText(String.valueOf(getValue()));
		}

		private void onDragCounter(final MouseEvent event)
		{
			if (event == null)
			{
				logger.log(Level.INFO, "Event is null!");
				return;
			}

			int bias = Math.abs((int) (event.getY() / 3));
			int result = (int) (getValue() - event.getY() * bias);

			if (range.forward(result))
				setValue(result);
			else if (result < range.getMin())
				setValue(range.getMin());
			else if (result < range.getMax())
				setValue(range.getMax());

			counter.setText(String.valueOf(getValue()));
		}

		@Override
		public Region getMainNode()
		{
			return root;
		}
	}

	@Override
	public PropertyStruct getPropertyStruct()
	{
		return propertyStruct;
	}

	public class IntegerPropBuilder extends BaseProperty<Integer>.Builder<IntegerPropBuilder, IntegerProperty>
	{
		@Override
		protected IntegerPropBuilder getThis()
		{
			return this;
		}

		@Override
		public IntegerProperty build()
		{
			return IntegerProperty.this;
		}

		public IntegerPropBuilder setRange(Range newRange)
		{
			if (newRange == null)
				logger.log(Level.WARN, "New range is null!");

			range = Objects.requireNonNullElse(newRange, Range.DEFAULT_INT);
			return this;
		}
	}

	@Contract("_, _ -> new")
	public static @NotNull IntegerProperty.IntegerPropBuilder newBuilder(String tag, int value)
	{
		return new IntegerProperty(tag, value).new IntegerPropBuilder();
	}
}
