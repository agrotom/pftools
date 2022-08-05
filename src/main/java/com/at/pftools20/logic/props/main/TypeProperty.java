package com.at.pftools20.logic.props.main;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TypeProperty extends BaseProperty<String>
{
	private final List<String> availableTypes;

	private final PropertyStruct propertyStruct;

	private TypeProperty(String tag, String value, List<String> availableTypes)
	{
		super(tag);

		this.availableTypes = Objects.requireNonNullElse(availableTypes, new ArrayList<>());

		setValue(value);

		propertyStruct = new TypePropertyStruct();
	}

	/**
	 * Parses value from string. If string is not parsable, inherit class method
	 * will set property value to base value of its generic type.
	 *
	 * @param newValue String to parse.
	 */
	@Override
	public void parse(String newValue)
	{
		setValue(newValue);
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
	public void parseJSON(String jsonValue)
	{
		parse(jsonValue);
	}

	@Override
	protected String correctValue(String newValue)
	{
		if (newValue == null || !availableTypes.contains(newValue))
		{
			if (!availableTypes.isEmpty())
				return availableTypes.get(0);
			else
				return "";
		}

		return newValue;
	}

	private class TypePropertyStruct implements PropertyStruct
	{
		private final ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableList(availableTypes));

		private TypePropertyStruct()
		{
			init();
		}

		private void init()
		{
			// ChoiceBox initialize
			choiceBox.setValue(getValue());
			choiceBox.setOnAction(this::applyChanges);
		}

		@Override
		public Region getMainNode()
		{
			return choiceBox;
		}

		private void applyChanges(ActionEvent event)
		{
			setValue(choiceBox.getValue());
		}
	}

	/**
	 * @return PropertyStruct of this property.
	 */
	@Override
	public PropertyStruct getPropertyStruct()
	{
		return propertyStruct;
	}

	public class TypePropBuilder extends BaseProperty<String>.Builder<TypePropBuilder, TypeProperty>
	{

		@Override
		protected TypePropBuilder getThis()
		{
			return null;
		}

		@Override
		public TypeProperty build()
		{
			return null;
		}
	}
}
