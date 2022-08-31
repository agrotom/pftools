package com.at.pftools20.util.json;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JSONUtil
{
	private final File jsonFile;

	private static final Logger logger = Logger.getLogger(JSONUtil.class);

	private final String _filePath;
	private final String _badFormat;
	private final String _fileNotFound;

	/**
	 * It is just a simple tool for reading and writing JSON files.
	 * 
	 * @param jsonFile file, containing JSON.
	 */
	public JSONUtil(@NotNull File jsonFile)
	{
		this.jsonFile = jsonFile;
		this._filePath = "File \"" + jsonFile.getPath() + "\"";
		this._badFormat = _filePath + " has bad JSON format!";
		this._fileNotFound = _filePath + " not found!";
	}

	/**
	 *
	 * @return JSON string. If method catch exception, it will return empty string.
	 *
	 */
	public String read()
	{
		if (isValidateFileToRead())
		{
			final BufferedReader reader;

			try
			{
				reader = new BufferedReader(new FileReader(jsonFile));
			} catch (FileNotFoundException e)
			{
				logger.log(Level.ERROR, _fileNotFound + " Empty string will be returned!");

				return "";
			}

			if (!isValidateJSON())
			{
				logger.log(Level.ERROR, _badFormat + " Empty string will be returned!");

				close(reader);
				return "";
			}

			final JSONTokener jsonTokener = new JSONTokener(reader);
			final JSONObject root = new JSONObject(jsonTokener);

			try
			{
				reader.close();
			} catch (IOException e)
			{
				close(reader);
				return "";
			}

			return root.toString();
		} else
		{
			logger.log(Level.ERROR,
					_filePath + " doesn't exist/not file (a directory)/not JSON.\nEmpty string will be returned!");

			return "";
		}
	}

	/**
	 * If method catch error, it won't write file.
	 * 
	 * @param json JSON as string.
	 */
	public void write(@NotNull final String json)
	{
		if (isValidateFileToWrite())
		{
			final BufferedWriter writer;

			try
			{
				writer = new BufferedWriter(new FileWriter(jsonFile));
			} catch (IOException e)
			{
				logger.log(Level.ERROR, _fileNotFound + " File won't be written!");

				return;
			}

			if (!Files.exists(Path.of(jsonFile.getPath())))
			{
				try
				{
					logger.log(Level.INFO,
							_filePath + (jsonFile.createNewFile() ? " doesn't exist. Creating this file..."
									: " already exists!"));
				} catch (IOException e)
				{
					logger.log(Level.ERROR, _filePath + " was not created! File won't be written!");

					close(writer);
					
					return;
				} catch (SecurityException e)
				{
					logger.log(Level.ERROR,
							"Can't write file \"" + jsonFile.getPath() + "\"! Program have not permission!");

					close(writer);
					return;
				}
			}

			if (!isValidateJSON(json))
			{
				logger.log(Level.ERROR, _badFormat + " File won't be written!");

				close(writer);
				
				return;
			}

			try
			{
				writer.write(json);
				logger.log(Level.INFO, _filePath + " was written successfully!");
			} catch (IOException e)
			{
				logger.log(Level.ERROR, _filePath + " was not created! File won't be written!");
			}

			close(writer);
		} else
			logger.log(Level.ERROR, _filePath + " not file (a directory)/not JSON.\nFile won't be written!");
	}

	private boolean isValidateJSON()
	{
		final JSONTokener jsonTokener;

		try
		{
			jsonTokener = new JSONTokener(new BufferedReader(new FileReader(jsonFile)));
		} catch (FileNotFoundException e)
		{
			logger.log(Level.ERROR, _filePath + _fileNotFound);
			return false;
		}

		try
		{
			@SuppressWarnings("unused")
			final JSONObject root = new JSONObject(jsonTokener);
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "Couldn't parse JSON file: \"" + jsonFile.getPath() + "\". Message: " + e.getMessage());
			return false;
		}

		return true;
	}
	
	private boolean isValidateJSON(@NotNull String json)
	{
		final JSONTokener jsonTokener;

		jsonTokener = new JSONTokener(json);
		
		try
		{
			@SuppressWarnings("unused")
			final JSONObject root = new JSONObject(jsonTokener);
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "Couldn't parse JSON: \"" + json + "\". Message: " + e.getMessage());
			return false;
		}

		return true;
	}

	private boolean isValidateFileToRead()
	{
		final Path filePath = Path.of(jsonFile.getPath());

		return Files.exists(filePath) && !Files.isDirectory(filePath)
				&& FilenameUtils.getExtension(jsonFile.getName()).equals("json");
	}

	private boolean isValidateFileToWrite()
	{
		final Path filePath = Path.of(jsonFile.getPath());

		return !Files.isDirectory(filePath) && FilenameUtils.getExtension(jsonFile.getName()).equals("json");
	}

	private boolean close(Closeable closeable)
	{
		try
		{
			closeable.close();
		} catch (IOException e)
		{
			logger.log(Level.ERROR, "Can't close buffer!");
			return false;
		}

		return true;
	}
}
