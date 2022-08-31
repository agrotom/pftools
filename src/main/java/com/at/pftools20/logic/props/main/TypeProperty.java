package com.at.pftools20.logic.props.main;

import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Region;

import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

import com.at.pftools20.util.TranslatableText;

public class TypeProperty extends BaseProperty<String>
{
	private final List<String> availableTypes;

	
	private TypeProperty(@NotNull String tag, @NotNull String value, @NotNull List<String> availableTypes)
	{
		super(tag);

		this.availableTypes = availableTypes;

		setValue(value);
	}

	
	/**
	 * Parses value from string. If string is not parsable, inherit class method
	 * will set property value to base value of its generic type.
	 *
	 * @param newValue String to parse.
	 */
	@Override
	public void parse(@NotNull String newValue)
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
	public void parseJSON(@NotNull String jsonValue)
	{
		parse(jsonValue);
	}

	
	@Override
	protected String correctValue(@NotNull String newValue)
	{
		if (!availableTypes.contains(newValue))
		{
			if (!availableTypes.isEmpty())
				return availableTypes.get(0);
			else
				return "";
		}

		return newValue;
	}

	
	@Override
	public PropertyStruct getPropertyStruct()
	{
		return new TypePropertyStruct();
	}
	
	
	private class TypePropertyStruct implements PropertyStruct
	{
		private final ChoiceBox<String> choiceBox = new ChoiceBox<>();

		private TypePropertyStruct()
		{
			init();
		}

		private void init()
		{
			// ChoiceBox initialize
			final List<String> formattedTypes = new LinkedList<>();
			
			availableTypes.forEach(type -> formattedTypes.add(TranslatableText.formatString(getTagNameID() + "." + type)));
			
			choiceBox.getItems().setAll(formattedTypes);
			
			choiceBox.setValue(formattedTypes.get(availableTypes.indexOf(getValue())));
			choiceBox.setOnAction(this::applyChanges);
		}

		@Override
		public Region getMainNode()
		{
			return choiceBox;
		}

		private void applyChanges(@NotNull ActionEvent event)
		{
			setValue(availableTypes.get(choiceBox.getSelectionModel().getSelectedIndex()));
		}
	}

	
	public class TypePropBuilder extends BaseProperty<String>.Builder<TypePropBuilder, TypeProperty>
	{

		@Override
		protected TypePropBuilder getThis()
		{
			return this;
		}

		@Override
		public TypeProperty build()
		{
			return TypeProperty.this;
		}
	}

	public static TypePropBuilder newBuilder(@NotNull String tag, @NotNull String value, @NotNull List<String> availableTypes)
	{
		return new TypeProperty(tag, value, availableTypes).new TypePropBuilder();
	}
}
