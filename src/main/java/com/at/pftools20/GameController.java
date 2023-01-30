package com.at.pftools20;

import java.io.IOException;


import org.jetbrains.annotations.NotNull;

import com.at.pftools20.logic.LivingActor;
import com.at.pftools20.logic.statcard.main.Actor;
import com.at.pftools20.util.FileManager;
import com.at.pftools20.util.SceneTranslator;
import com.at.pftools20.util.TranslatableText;
import com.at.pftools20.util.WidgetBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameController
{
	
	private final GameLogic gameLogic = new GameLogic();

	@FXML
	private SplitPane root;
	
	@FXML
	private TitledPane paramsTitle;
	
	@FXML
	private TitledPane actorsTitle;
	
	@FXML
	private VBox cardColumn;
	
	@FXML
	private VBox actorPanel;
	
	@FXML
	private Button addButton;
	
	@FXML
	private void initialize()
	{
		// Elements translating
		
		/*paramsTitle.setText(TranslatableText.formatString("game.paramstitle"));
		actorsTitle.setText(TranslatableText.formatString("game.actorstitle"));
		
		addButton.setText(TranslatableText.formatString("game.addbutton"));*/
	}
	
	@FXML
	private void addCard(@NotNull final ActionEvent event)
	{
		final FileManager fileManager = new FileManager();
		final FileChooser fileChooser = fileManager.createNewChooser();
		
		final String path = fileChooser.showOpenDialog(App.getAppCore().getMainStage()).getAbsolutePath();
		
		if(path != null)
			gameLogic.addActor(path);
		
		updateCardColumn();
	}
	
	private void updateCardColumn()
	{
		cardColumn.getChildren().clear();
		gameLogic.getActorList().forEach(actor -> {
			final HBox widget = WidgetBuilder.createActorWidget(actor);
			
			widget.setOnMouseClicked(event -> {
				if(event.getClickCount() == 3)
				{
					deleteAllCardData(cardColumn.getChildren().indexOf(widget));
				}
				else
				{
					displayActorPanel(actor);
				}
			});
			
			cardColumn.getChildren().add(widget);
		});
	}
	
	/**
	 * Displays controls and info for given actor.
	 * @param actor
	 */
	private void displayActorPanel(final LivingActor actor)
	{
		actorPanel.getChildren().clear();
		
		final Actor template = actor.template;
		
		final Spinner<Integer> spinner = new Spinner<>();
		spinner.getEditor().setDisable(true);
		spinner.setEditable(true);
		
		spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
		        0, 10000, Integer.parseInt("0")));

		final Button damage = new Button(TranslatableText.formatString("game.actorpanel.damage"));
		damage.setOnAction(event -> {
			actor.damage(spinner.getValue());
		});
		
		final Button heal = new Button(TranslatableText.formatString("game.actorpanel.heal"));
		heal.setOnAction(event -> {
			actor.heal(spinner.getValue());
		});
		
		final HBox controlRow = new HBox(damage, spinner, heal);
		controlRow.setSpacing(3.0);
		
		final ListView<String> effectList = new ListView<>();
		final TextField input = new TextField();
		input.setPromptText(TranslatableText.formatString("game.actorpanel.effectprompt"));
		
		final Button add = new Button(TranslatableText.formatString("generic.add"));
		add.setOnAction(event -> {
			effectList.getItems().add(input.getText());
			actor.addEffect(input.getText());
		});
		
		final Button remove = new Button(TranslatableText.formatString("generic.remove"));
		remove.setOnAction(event -> {
			if(effectList.getSelectionModel().getSelectedIndex() != -1)
			{
				final int index = effectList.getSelectionModel().getSelectedIndex();
				effectList.getItems().remove(index);
				actor.removeEffect(index);
			}
		});
		
		final HBox effectControls = new HBox(input, add, remove);
		effectControls.setSpacing(3.0);
		
		
		effectList.setMinHeight(256);
		
		final Text controlDivider = new Text(TranslatableText.formatString("game.actorpanel.dividor.control"));
		controlDivider.setFont(Font.font(16));
		
		final Text infoDivider = new Text(TranslatableText.formatString("game.actorpanel.dividor.info"));
		infoDivider.setFont(Font.font(16));
		
		actorPanel.getChildren().addAll(controlDivider, controlRow, effectControls, effectList, infoDivider);
		
		template.copyOfProps().forEach(prop -> {
			final StringBuilder builder = new StringBuilder();
			builder.append(prop.getFormattedTag() + ": " + prop.getValue() + "\n");
			
			final Text actorInfo = new Text(builder.toString());
			actorInfo.setWrappingWidth(actorPanel.getWidth());
			
			actorPanel.getChildren().add(actorInfo);
		});
	}
	
	private void deleteAllCardData(final int index)
	{
		gameLogic.removeActor(index);
		updateCardColumn();
	}
	
	@SuppressWarnings("exports")
	public static void start(@NotNull final Stage primaryStage) throws IOException
	{
		final Stage stage = new Stage();
		
		final FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("game.fxml"));
		final Scene scene = new Scene(fxmlLoader.load(), 600, 400);
		
		stage.setTitle(TranslatableText.formatString("game.title"));
		stage.setScene(scene);
		stage.getIcons().setAll(primaryStage.getIcons());
		
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(primaryStage.getOwner());
		
		SceneTranslator.translateScene(scene.getRoot());
		
		stage.show();
	}

}
