package com.at.pftools20;

import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.at.pftools20.logic.props.main.BaseProperty.PropertyStruct;
import com.at.pftools20.logic.props.main.IntegerProperty;
import com.at.pftools20.logic.statcard.main.Item;
import com.at.pftools20.logic.statcard.main.StatCard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
	private VBox allPropsPane;
	
	private VBox secondVBox = new VBox();

	@FXML
	private void initialize()
	{
		
		final StatCard card = new Item();
		
		loadCard(card);
	}

	@FXML
	private void onHelloButtonClick()
	{
		newMenuItem.setText("dsadsd");
	}

	@FXML
	private void saveAction(ActionEvent event)
	{
		
	}
	
	@FXML
	private void onSettingsAction()
	{
		try
		{
			OptionsController.start(HelloApplication.getAppCore().getMainStage());
		} catch (IOException e)
		{
			logger.log(Level.ERROR, "Can't load settings menu!");
		}
	}
	
	public void loadCard(StatCard card)
	{
		card.copyOfProps().forEach(property -> {
			final Label label = new Label(property.getFormattedTag());
			
			final HBox propRow = new HBox(label);
			propRow.setPrefHeight(21.0);
			propRow.setOnMouseClicked(event -> {
				final Text tagName = new Text(property.getFormattedTag());
				final Text description =  new Text(property.getDescription());
				
				propertyDataVBox.getChildren().setAll(tagName, property.getPropertyStruct().getMainNode(), description);
			});
			propRow.setOnMouseEntered(this::onRowEntered);
			propRow.setOnMouseExited(this::onRowExited);
			
			allPropsPane.getChildren().add(propRow);
		});
	}
	
	private void onRowEntered(MouseEvent event)
	{
		final HBox propRow = (HBox) event.getSource();
		
		propRow.setBackground(new Background(new BackgroundFill(Color.rgb(25, 144, 223), CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
	private void onRowExited(MouseEvent event)
	{
		final HBox propRow = (HBox) event.getSource();
		
		propRow.setBackground(null);
	}
	
	private final Logger logger = Logger.getLogger(MainMenuController.class);
}