package com.at.pftools20.logic.props.main;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.apache.log4j.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class IntegerProperty extends BaseProperty<Integer>
{
	private Range range = Range.DEFAULT_INT;
	
	private IntegerProperty(@NotNull String tag, int value)
	{
		super(tag);

		setValue(value);
	}

	
	@Override
	public void parse(@NotNull String string)
	{
		try
		{
			setValue(Integer.parseInt(string));
		} catch (NumberFormatException e)
		{
			logger.log(Level.WARN,
					"Parse argument doesn't contain a parsable string. Setting value to range minimum value!");
			setValue(range.getMin());
		}
	}

	@Override
	public String toJSON()
	{
		return toString();
	}

	@Override
	public void parseJSON(@NotNull String jsonValue)
	{
		parse(jsonValue);
	}
	

	@Override
	protected Integer correctValue(@NotNull Integer newValue)
	{
		if (range.forward(newValue))
			return newValue;
		else
			return range.getMin();
	}
	
	
	@Override
	public PropertyStruct getPropertyStruct()
	{
		return new IntegerPropertyStruct();
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
			root.setAlignment(Pos.TOP_CENTER);

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

		private void onButtonIncrease(@NotNull MouseEvent event)
		{
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

		private void onButtonDecrease(@NotNull MouseEvent event)
		{
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

		private void onDragCounter(MouseEvent event)
		{
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

		public IntegerPropBuilder setRange(@NotNull Range newRange)
		{
			range = newRange;
			return this;
		}
	}

	@Contract("_, _ -> new")
	public static @NotNull IntegerProperty.IntegerPropBuilder newBuilder(@NotNull String tag, int value)
	{
		return new IntegerProperty(tag, value).new IntegerPropBuilder();
	}
}
