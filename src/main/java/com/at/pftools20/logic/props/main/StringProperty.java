package com.at.pftools20.logic.props.main;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StringProperty extends BaseProperty<String>
{
	private int maxLength = -1;

	private final PropertyStruct propertyStruct;

	private StringProperty(String tag, String value)
	{
		super(tag);

		setValue(value);

		propertyStruct = new StringPropertyStruct();
	}

	@Override
	public void parse(String string)
	{
		setValue(string);
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

	/**
	 * @return corrected string. Returns empty string if new value equals null
	 */
	@Override
	protected String correctValue(String newValue)
	{
		if (newValue == null)
		{
			logger.log(Level.WARN, "String argument is null! Value will be set to empty string!");
			return "";
		}

		if (maxLength > 0 && newValue.length() > maxLength)
			return newValue.substring(0, maxLength - 1);

		return newValue;
	}

	private class StringPropertyStruct implements PropertyStruct
	{
		private final TextField textField = new TextField(getValue());
		private final TextArea expandedTextField = new TextArea(getValue());

		private final Button expandButton = new Button("Expand");

		private final HBox root = new HBox(textField, expandButton);

		private boolean isExpanded = false;

		private StringPropertyStruct()
		{
			init();
		}

		public void init()
		{
			// Root initialize
			root.setSpacing(10.0);
			root.setAlignment(Pos.CENTER);

			// TextField initialize
			textField.setTextFormatter(newTextFormatter());

			// Expanded TextField (TextArea) initialize
			expandedTextField.setTextFormatter(newTextFormatter());

			// Expand button
			expandButton.setOnAction(this::expandTextField);
		}

		@Contract(" -> new")
		private @NotNull TextFormatter<TextFormatter.Change> newTextFormatter()
		{
			return new TextFormatter<>(change ->
			{
				final String newText = change.getControlNewText();

				if (!change.isDeleted() && maxLength > 0 && newText.length() > maxLength)
					return null;

				StringProperty.this.setValue(newText);
				return change;
			});
		}

		private void expandTextField(ActionEvent event)
		{
			root.getChildren().set(0, !(isExpanded = !isExpanded) ? textField : expandedTextField);
			updateAll();
		}

		private void updateAll()
		{
			textField.setText(getValue());
			expandedTextField.setText(getValue());
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

	public class StringPropBuilder extends BaseProperty<String>.Builder<StringPropBuilder, StringProperty>
	{

		@Override
		protected StringPropBuilder getThis()
		{
			return this;
		}

		@Override
		public StringProperty build()
		{
			return StringProperty.this;
		}

		public StringPropBuilder setMaxLength(int newMaxLength)
		{
			maxLength = newMaxLength;
			return this;
		}
	}

	@Contract("_, _ -> new")
	public static @NotNull StringPropBuilder newBuilder(String tag, String value)
	{
		return new StringProperty(tag, value).new StringPropBuilder();
	}

	private final Logger logger = Logger.getLogger(StringProperty.class);
}
