package com.at.pftools20.logic.props.main;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import java.util.function.UnaryOperator;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StringProperty extends BaseProperty<String>
{
	private int maxLength = -1;

	
	private StringProperty(@NotNull String tag, @NotNull String value)
	{
		super(tag);

		setValue(value);
	}

	
	@Override
	public void parse(@NotNull String string)
	{
		setValue(string);
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

	
	/**
	 * @return corrected string. Returns empty string if new value equals null
	 */
	@Override
	protected String correctValue(String newValue)
	{
		if (maxLength > 0 && newValue.length() > maxLength)
			return newValue.substring(0, maxLength - 1);

		return newValue;
	}
	
	
	@Override
	public PropertyStruct getPropertyStruct()
	{
		return new StringPropertyStruct();
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
			expandedTextField.setWrapText(true);

			// Expand button
			expandButton.setOnAction(this::expandTextField);
		}

		@Contract(" -> new")
		private @NotNull TextFormatter<String> newTextFormatter()
		{
			return new TextFormatter<String>((UnaryOperator<Change>)(change -> {
				final String newText = change.getControlNewText();

				if (!change.isDeleted() && maxLength > 0 && newText.length() > maxLength)
					return null;

				StringProperty.this.setValue(newText);
				return change;
			}));
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
	public static @NotNull StringPropBuilder newBuilder(@NotNull String tag, @NotNull String value)
	{
		return new StringProperty(tag, value).new StringPropBuilder();
	}
}
