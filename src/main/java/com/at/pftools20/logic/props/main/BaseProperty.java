package com.at.pftools20.logic.props.main;

import javafx.scene.layout.Region;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Objects;

/**
 * BaseProperty contains base for other properties. If any class inherits from
 * {@link BaseProperty} it should represent this structure: {@link JSONAble}
 * implemented methods, {@link BaseProperty#correctValue(T newValue)}
 * implementation, PropertyStruct and its own builder. Properties using for
 * StatCard specification. It implements {@link JSONAble} for converting
 * properties to JSON format.
 * 
 * @see com.at.pftools20.util.json.JSONAble
 */
public abstract class BaseProperty<T>
{
	private final String tag;
	private T value;

	private String description = "";

	protected BaseProperty(String tag)
	{
		this.tag = Objects.requireNonNullElse(tag, "null");
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
	public abstract void parse(String string);

	/**
	 * Converts JSON value to property value. Most of primitive properties, like
	 * Integer, String etc. have same behavior as "parse".
	 * 
	 * @param jsonValue JSON value.
	 */
	public abstract void parseJSON(String jsonValue);

	/**
	 * Converts property to JSON format. Most of primitive properties, like Integer,
	 * String etc. returns same value as "toString".
	 * 
	 * @return JSON as string.
	 */
	public abstract String toJSON();

	public final String getTag()
	{
		return tag;
	}

	public final void setValue(T newValue)
	{
		value = correctValue(newValue);
	}

	public final T getValue()
	{
		return value;
	}

	protected abstract T correctValue(T newValue);

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
		public final S register(List<BaseProperty<?>> registerList)
		{
			if (registerList == null)
				logger.log(Level.WARN, "Register list argument is null!");
			else
				registerList.add(BaseProperty.this);

			return getThis();
		}

		public final S setDescription(String newDescription)
		{
			if (newDescription == null)
				logger.log(Level.WARN, "New description is null!");

			description = Objects.requireNonNullElse(description, "");
			return getThis();
		}

		protected abstract S getThis();

		public abstract K build();
	}

	protected final Logger logger = Logger.getLogger(BaseProperty.class);
}
