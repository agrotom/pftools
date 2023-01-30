package com.at.pftools20.logic.props.main;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CardHolder
{
	private final SimpleIntegerProperty count;
	private final SimpleStringProperty name;
	private final SimpleStringProperty path;
	
	public CardHolder(int count, @NotNull String name, @NotNull String path)
	{
		this.count = new javafx.beans.property.SimpleIntegerProperty(count);
		this.name = new javafx.beans.property.SimpleStringProperty(name);
		this.path = new javafx.beans.property.SimpleStringProperty(path);
	}
	
	@SuppressWarnings("exports")
	public JSONObject createJSONArray()
	{
		final JSONObject root = new JSONObject();
		
		root.put("count", count.get());
		root.put("name", name.get());
		root.put("cardPath", path.get());
		
		return root;
	}

	public int getCount()
	{
		return count.get();
	}
	
	public String getName()
	{
		return name.get();
	}

	public String getPath()
	{
		return path.get();
	}
	
	@Override
	public String toString()
	{
		return name.get() + " x" + count.get();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		
		if(!(obj instanceof CardHolder))
			return false;
		
		final CardHolder toCompare = (CardHolder) obj;
		
		return this.count == toCompare.count && this.name == toCompare.name && this.path == toCompare.path;
	}
}
