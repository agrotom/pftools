package com.at.pftools20.logic.props.main;

import javafx.scene.layout.Region;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.at.pftools20.util.TranslatableText;

import java.util.List;

/**
 * BaseProperty contains base for other properties. If any class inherits from
 * {@link BaseProperty} it should represent this structure: {@link JSONAble}
 * implemented methods, {@link BaseProperty#correctValue(T newValue)}
 * implementation, PropertyStruct and its own builder. Properties uses for
 * StatCard specification. It implements {@link JSONAble} for converting
 * properties to JSON format.
 * 
 * @see com.at.pftools20.util.json.JSONAble
 */
public abstract non-sealed class BaseProperty<T> implements ImmutableProperty<T>
{
	private final String tag;
	private T value;

	private String tagNameFormatID;
	private String descriptionFormatID;

	protected BaseProperty(@NotNull String tag)
	{
		this.tag = tag;
		tagNameFormatID = "property." + tag;
		descriptionFormatID = tagNameFormatID + ".description";
	}

	
	/**
	 * Generic must correctly implement toString method. If generic type has
	 * specific stringify method, this method should be overridden.
	 * 
	 * @return Value as string.
	 */
	@Override
	public String toString()
	{
		return value.toString();
	}

	/**
	 * Parses value from string. If string is not parsable, inherit class method
	 * will set property value to base value of its generic type.
	 * 
	 * @param string String to parse.
	 */
	public abstract void parse(@NotNull String string);

	/**
	 * Converts JSON value to property value. Most of primitive properties, like
	 * Integer, String etc. have same behavior as "parse".
	 * 
	 * @param jsonValue JSON value.
	 */
	public abstract void parseJSON(@NotNull String jsonValue);

	/**
	 * Converts property to JSON format. Most of primitive properties, like Integer,
	 * String etc. returns same value as "toString".
	 * 
	 * @return JSON as string.
	 */
	@Override
	public abstract String toJSON();

	
	public final void setValue(@NotNull T newValue)
	{
		value = correctValue(newValue);
	}

	public final String getTag()
	{
		return tag;
	}
	
	public final String getTagNameID()
	{
		return tagNameFormatID;
	}
	
	public final String getFormattedTag()
	{
		return TranslatableText.formatString(tagNameFormatID);
	}
	
	@Override
	public final T getValue()
	{
		return value;
	}
	
	public final String getDescription()
	{
		final String formattedDescription = TranslatableText.formatString(descriptionFormatID);
		
		return formattedDescription.equals(descriptionFormatID) ? "" : formattedDescription;
	}

	protected abstract T correctValue(@NotNull T newValue);
	

	/**
	 *
	 * @return PropertyStruct of this property.
	 */
	public abstract PropertyStruct getPropertyStruct();

	/**
	 * PropertyStruct contains control node of this property. Example: see
	 * {@link IntegerProperty}.
	 */
	public interface PropertyStruct
	{
		@SuppressWarnings("exports")
		Region getMainNode();
	}

	
	/**
	 * Builder with inheritance behavior.
	 * 
	 * @param <S> Builder of inherited class. Should extend this builder class.
	 * @param <K> Property extending {@link BaseProperty<T>}.
	 */
	protected abstract class Builder<S extends Builder<S, K>, K extends BaseProperty<T>>
	{
		public final S register(@NotNull List<BaseProperty<?>> registerList)
		{
			registerList.add(BaseProperty.this);

			return getThis();
		}

		public final S setCustomDescriptionFormatID(@NotNull String formatID)
		{
			descriptionFormatID = formatID;
			
			return getThis();
		}
		
		/**
		 * Default format is "property.[PROPERTY_TAG]". The method changes it to desired id.
		 * @param formatID Unlocalized string.
		 */
		public final S setCustomTagFormatID(@NotNull String formatID)
		{
			tagNameFormatID = formatID;
			
			return getThis();
		}

		protected abstract S getThis();

		public abstract K build();
	}

	protected final Logger logger = Logger.getLogger(this.getClass());
}
