package com.at.pftools20;

import com.at.pftools20.logic.enums.Dice;
import com.at.pftools20.logic.props.main.DiceProperty;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

public class MainMenuController
{
	@FXML
	private MenuItem newMenuItem;

	@FXML
	private MenuItem openMenuItem;

	@FXML
	private MenuItem openRecentlyMenuItem;

	@FXML
	private MenuItem saveMenuItem;

	@FXML
	private MenuItem saveAsMenuItem;

	@FXML
	private MenuItem closeMenuItem;

	@FXML
	private VBox propertyDataVBox;

	@FXML
	private void initialize()
	{
		saveMenuItem.setOnAction(this::saveAction);
	}

	@FXML
	private void onHelloButtonClick()
	{
		newMenuItem.setText("dsadsd");
	}

	private void saveAction(ActionEvent event)
	{
		
	}
}