package com.at.pftools20.logic.props.main;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import com.at.pftools20.logic.props.main.BaseProperty.Builder;
import com.at.pftools20.logic.props.main.BaseProperty.PropertyStruct;
import com.at.pftools20.logic.props.main.CritHitProperty.CritHitPropBuilder;
import com.at.pftools20.logic.statcard.main.StatCard;

import javafx.scene.layout.Region;

public class ListProperty extends BaseProperty<List<ListProperty.CardHolder>>
{
	protected ListProperty(@NotNull String tag, @NotNull List<ListProperty.CardHolder> value)
	{
		super(tag);
		
		setPropertyStruct(new ListPropertyStruct());
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		final List<CardHolder> value = getValue();
		
		value.forEach(item -> {
			builder.append(getDescription());
		});
		
		return null;
	}

	@Override
	public void parse(@NotNull String string)
	{
		
	}

	@Override
	public void parseJSON(@NotNull String jsonValue)
	{
		
	}

	@Override
	public String toJSON()
	{
		return null;
	}
	

	@Override
	protected List<CardHolder> correctValue(@NotNull List<CardHolder> newValue)
	{
		return new LinkedList<>(newValue);
	}
	
	
	private class ListPropertyStruct implements PropertyStruct
	{

		@Override
		public Region getMainNode()
		{
			return null;
		}
		
	}
	
	
	public class ListPropBuilder extends Builder<ListPropBuilder, ListProperty>
	{

		@Override
		protected ListProperty.ListPropBuilder getThis()
		{
			return this;
		}

		@Override
		public ListProperty build()
		{
			return ListProperty.this;
		}
		
	}

	public static ListPropBuilder newBuilder(@NotNull String tag, @NotNull List<CardHolder> value) { return new ListProperty(tag, value).new ListPropBuilder(); }

	public record CardHolder(int count, @NotNull String name, @NotNull String cardPath)
	{
		private static int d;
	}
}
