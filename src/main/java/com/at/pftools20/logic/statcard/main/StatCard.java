package com.at.pftools20.logic.statcard.main;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.props.main.BaseProperty;
import com.at.pftools20.logic.props.main.StringProperty;

import jakarta.xml.bind.DatatypeConverter;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class StatCard {
    protected final List<BaseProperty<?>> properties = new LinkedList<>();

    public final StringProperty name = StringProperty.newBuilder("name", "No Info").register(properties).build();
    public final StringProperty description = StringProperty.newBuilder("description", "No Info").register(properties).build();
   
    
    private Image image = null;
    
    
    @SuppressWarnings("exports")
	public final void loadCardFromFile(@NotNull JSONObject main)
    {
    	properties.forEach(prop -> {
    		final String value;
    		
    		try
			{
				value = main.getString(prop.getTag());
			} catch (JSONException e)
			{
				logger.log(Level.WARN, "There is no tag: \"" + prop.getTag() + "\"! Setting default value!");
				return;
			}
    		
    		prop.parseJSON(value);
    	});
    	
    	try
		{
    		final JSONObject imageRoot = (JSONObject) main.get("image");
    		
    		final int w = imageRoot.getInt("width");
    		final int h = imageRoot.getInt("height");
			final String imageBase64 = imageRoot.getString("raw");
			
			final byte[] buffer = DatatypeConverter.parseBase64Binary(imageBase64);
			
			final WritableImage wi = new WritableImage(w, h);
			wi.getPixelWriter().setPixels(0, 0, w, h, PixelFormat.getByteBgraInstance(), buffer, 0, w * 4);
			
			image = wi;
		} catch (JSONException e)
		{
			logger.log(Level.WARN, "There is not tag: \"image\"! Setting default value!");
		} catch(ClassCastException e)
    	{
			logger.log(Level.WARN, "There was an error while casting classes!");
    	} catch(IllegalArgumentException e)
    	{
    		logger.log(Level.WARN, "Parse method can't properly decode Base64 to byte array!");
    	}
    }
    
    /**
     * Converts Card to JSON format. If JSON has bad format, method return empty string!
     * @return Card in JSON format.
     */
    public final String toJSON()
    {
    	final JSONObject root = new JSONObject();
    	final JSONObject main = new JSONObject();
    	
    	root.put("main", main);
    	
    	properties.forEach(property -> main.put(property.getTag(), property.toJSON()));
    	
    	final CardType cardType = getType();
    	
    	if(cardType == null)
    	{
    		logger.log(Level.ERROR, "Card Type is null!");
    		return "";
    	}
    	
    	root.put("type", cardType.toString());
    	
    	if(image != null)
    	{
    		final JSONObject imageRoot = new JSONObject();
    		
    		final int w = (int) image.getWidth();
    		final int h = (int) image.getHeight();
    		
    		imageRoot.put("width", w);
    		imageRoot.put("height", h);
    		
    		byte[] buffer = new byte[w * h * 4];
    		
    		image.getPixelReader().getPixels(0, 0, w, h, PixelFormat.getByteBgraInstance(), buffer, 0, w * 4);
    		
    		final String imageBase64 = DatatypeConverter.printBase64Binary(buffer);
    		
    		imageRoot.put("raw", imageBase64);
    		
    		main.put("image", imageRoot);
    	}
    	
    	return root.toString();
    }
    
    /**
     * 
     * @return copy of properties list, because properties list is immutable!
     */
    public List<BaseProperty<?>> copyOfProps()
    {
    	return new LinkedList<>(properties);
    }
    
    public abstract CardType getType();
    
    private final Logger logger = Logger.getLogger(StatCard.class);


    /**
     * 
     * @return image of that StatCard. If there is no image for StatCard it will return null!
     */
	@SuppressWarnings("exports")
	public @Nullable Image getImage()
	{
		return image;
	}

	@SuppressWarnings("exports")
	public void setImage(Image image)
	{
		this.image = image;
	}
}
