package com.at.pftools20;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Core
{
	private String currentPath;
	
	public void setCurrentPath(String newPath)
	{
		assert newPath != null;
		
		if(newPath == null)
		{
			logger.log(Level.WARN, "New current path argument is null!");
			return;
		}
	}
	
	private static Logger logger = Logger.getLogger(Core.class);
}
