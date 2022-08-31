package com.at.pftools20;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.statcard.main.StatCard;
import com.at.pftools20.logic.statcard.main.StatCardFactory;
import com.at.pftools20.util.FileManager;
import com.at.pftools20.util.Helper;
import com.at.pftools20.util.TranslatableText;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class MainMenuController
{
	@FXML
	private Menu fileMenu;
	
	@FXML
	private Menu newMenuItem;
	
	@FXML
	private MenuItem openMenuItem;
	
	@FXML
	private Menu openRecentlyMenu;
	
	@FXML
	private MenuItem saveMenuItem;
	
	@FXML
	private MenuItem saveAsMenuItem;
	
	@FXML
	private MenuItem closeMenuItem;
	
	@FXML
	private Menu gameMenu;
	
	@FXML
	private MenuItem newGameMenuItem;
	
	@FXML
	private MenuItem openGameMenuItem;
	
	@FXML
	private Menu optionsMenu;
	
	@FXML
	private MenuItem openSettingsMenuItem;
	
	@FXML
	private Menu helpMenu;
	
	@FXML
	private MenuItem helpMenuItem;

	@FXML
	private TitledPane titledPane1;
	
	@FXML
	private TitledPane titledPane2;
	
	@FXML
	private TitledPane titledPane3;
	
	@FXML
	private VBox propertyDataVBox;
	
	@FXML
	private VBox allPropsPane;
	
	@FXML
	private VBox infoVBox;
	
	@FXML
	private BorderPane mainPane;
	
	@FXML
	private Label statCardName;
	
	@FXML
	private ImageView statCardImage;
	
	
	private StatCard statCard = null;

	@FXML
	private void initialize()
	{
		translateAll();
		
		panesInit();
		
		updateRecently();
		
		resetPanes();
		
		final List<CardType> cardTypes = Arrays.asList(CardType.values());
		
		cardTypes.forEach(type -> {
			final MenuItem menuItem = new MenuItem(TranslatableText.formatString("cardtype." + type.toString()));
			
			menuItem.setOnAction(event -> {
				statCard = new StatCardFactory().createPure(type);
				loadCard(statCard);
				
				updateRecently();
			});
			
			newMenuItem.getItems().add(menuItem);
		});
		
		final BackgroundFill backgroundFill = new BackgroundFill(
				new ImagePattern(new Image(Helper.getResource("/images/background.png"))), CornerRadii.EMPTY, Insets.EMPTY);
		
		mainPane.setBackground(new Background(backgroundFill));
	}
	
	private void translateAll()
	{
		fileMenu.setText(TranslatableText.formatString("menu.file"));
		newMenuItem.setText(TranslatableText.formatString("menu.file.new"));
		openMenuItem.setText(TranslatableText.formatString("menu.file.open"));
		openRecentlyMenu.setText(TranslatableText.formatString("menu.file.recently"));
		saveMenuItem.setText(TranslatableText.formatString("menu.file.save"));
		saveAsMenuItem.setText(TranslatableText.formatString("menu.file.saveas"));
		closeMenuItem.setText(TranslatableText.formatString("menu.file.close"));
		gameMenu.setText(TranslatableText.formatString("menu.game"));
		newGameMenuItem.setText(TranslatableText.formatString("menu.file.new"));
		openGameMenuItem.setText(TranslatableText.formatString("menu.file.open"));
		optionsMenu.setText(TranslatableText.formatString("menu.pref"));
		openSettingsMenuItem.setText(TranslatableText.formatString("menu.pref.open"));
		helpMenu.setText(TranslatableText.formatString("menu.help"));
		helpMenuItem.setText(TranslatableText.formatString("menu.help.open"));
		
		titledPane1.setText(TranslatableText.formatString("titledpane1.title"));
		titledPane2.setText(TranslatableText.formatString("titledpane2.title"));
		titledPane3.setText(TranslatableText.formatString("titledpane3.title"));
	}
	
	private void updateRecently()
	{
		openRecentlyMenu.getItems().clear();
		
		HelloApplication.getAppCore().getRecentlies().forEach(recently -> {
			final MenuItem menuItem = new MenuItem(FilenameUtils.getName(recently));
			
			menuItem.setOnAction(event -> {
				final FileManager manager = new FileManager();
				
				statCard = manager.openStatCard(recently);
				
				if(statCard != null)
				{
					loadCard(statCard);
					
					updateRecently();
				}
			});
			
			openRecentlyMenu.getItems().add(menuItem);
		});
	}
	
	private void panesInit()
	{
		final List<FadeTransition> effects = new ArrayList<>();
		
		effects.add(new FadeTransition(Duration.seconds(0.1), titledPane1));
		effects.add(new FadeTransition(Duration.seconds(0.1), titledPane2));
		effects.add(new FadeTransition(Duration.seconds(0.1), titledPane3));
		
		titledPane1.setOnMouseEntered(this::onPaneEntered);
		titledPane1.setOnMouseExited(this::onPaneExited);
		
		titledPane2.setOnMouseEntered(this::onPaneEntered);
		titledPane2.setOnMouseExited(this::onPaneExited);
		
		titledPane3.setOnMouseEntered(this::onPaneEntered);
		titledPane3.setOnMouseExited(this::onPaneExited);
		
		effects.forEach(effect -> {
			effect.setFromValue(1.0);
			effect.setToValue(0.3);
			
			effect.play();
		});
	}

	@FXML
	private void closeAction(ActionEvent event)
	{
		statCard = null;
		FileManager.close();
		resetPanes();
	}

	@FXML
	private void saveAction(ActionEvent event)
	{
		final FileManager manager = new FileManager();
		
		if(statCard != null)
		{
			manager.save(statCard, HelloApplication.getAppCore().getMainStage(), false);
			
			updateRecently();
		}
	}
	
	@FXML
	private void saveAsAction(ActionEvent event)
	{
		final FileManager manager = new FileManager();
		
		if(statCard != null)
		{
			manager.save(statCard, HelloApplication.getAppCore().getMainStage(), true);
			saveMenuItem.setDisable(false);
			
			updateRecently();
		}
	}
	
	@FXML
	private void openAction(ActionEvent event)
	{
		final FileManager manager = new FileManager();
		
		statCard = manager.openStatCard(HelloApplication.getAppCore().getMainStage());
		
		if(statCard != null)
		{
			loadCard(statCard);
			
			updateRecently();
		}
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
	
	public void loadCard(@NotNull StatCard card)
	{
		resetPanes();
		
		card.copyOfProps().forEach(property -> {
			final Label label = new Label(property.getFormattedTag());
			
			final HBox propRow = new HBox(label);
			HBox.setMargin(label, new Insets(5.0));
			propRow.setPrefHeight(21.0);
			propRow.setOnMouseClicked(event -> {
				final Text tagName = new Text(property.getFormattedTag());
				final Text description =  new Text(property.getDescription());
				
				description.setWrappingWidth(propertyDataVBox.getPrefWidth());
				description.setTextAlignment(TextAlignment.CENTER);
				
				propertyDataVBox.getChildren().setAll(tagName, property.getPropertyStruct().getMainNode(), description);
			});
			propRow.setOnMouseEntered(this::onRowEntered);
			propRow.setOnMouseExited(this::onRowExited);
			
			allPropsPane.getChildren().add(propRow);
		});
		
		statCardName.setText(card.name.toString());
		
		final Image currentCardImage = card.getImage();
		
		statCardImage.setImage(currentCardImage == null ? new Image(Helper.getResource("/images/imagenotfound.png")) : currentCardImage);
	}
	
	private void resetPanes()
	{
		allPropsPane.getChildren().clear();
		propertyDataVBox.getChildren().clear();
		
		saveMenuItem.setDisable(FileManager.getCurrentPath() == null);
		saveAsMenuItem.setDisable(statCard == null);
		
		closeMenuItem.setDisable(statCard == null);
		
		if(statCard == null)
		{
			statCardName.setText("");
			statCardImage.setImage(null);
		}
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
	
	private void onPaneEntered(MouseEvent event)
	{
		final Node node = (Node) event.getSource();
		
		final FadeTransition effect = new FadeTransition(Duration.seconds(0.1), node);
		
		effect.setFromValue(0.3);
		effect.setToValue(1.0);
		
		effect.play();
	}
	
	private void onPaneExited(MouseEvent event)
	{
		final Node node = (Node) event.getSource();
		
		final FadeTransition effect = new FadeTransition(Duration.seconds(0.1), node);
		
		effect.setFromValue(1.0);
		effect.setToValue(0.3);
		
		effect.play();
	}
	
	@FXML
	private void uploadImage()
	{
		if(statCard != null)
		{
			final FileManager manager = new FileManager();
			
			final Image newImage = manager.openImage();
			
			if(newImage != null)
			{
				statCard.setImage(newImage);
				statCardImage.setImage(newImage);
			}
		}
	}
	
	private final Logger logger = Logger.getLogger(MainMenuController.class);
}