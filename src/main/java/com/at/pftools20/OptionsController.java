package com.at.pftools20;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import com.at.pftools20.util.TranslatableText;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OptionsController
{
	@FXML
	private VBox prefList;
	
	@FXML
	private VBox prefData;
	
	@FXML
	private Button applyButton;
	
	@FXML
	private void initialize()
	{
		applyButton.setText(TranslatableText.formatString("options.apply"));
		
		App.getAppCore().getPreferences().getNewList().forEach(pref -> {
			final Label label = new Label(pref.getFormattedTag());
			
			final HBox row = new HBox(label);
			HBox.setMargin(label, new Insets(5.0));

			row.setOnMouseClicked(event -> {
				final Text name = new Text(pref.getFormattedTag());
				final Text description = new Text(pref.getDescription());
				
				prefData.getChildren().setAll(name, pref.getPropertyStruct().getMainNode(), description);
			});
			
			row.setOnMouseEntered(event -> row.setBackground(new Background(
						new BackgroundFill(Color.rgb(25, 144, 223), CornerRadii.EMPTY, Insets.EMPTY))
					));
			
			row.setOnMouseExited(event -> row.setBackground(null));
			
			prefList.getChildren().add(row);
		});;
	}
	
	@FXML
	private void applyChanges()
	{
		App.getAppCore().saveCoreData();
	}

	@SuppressWarnings("exports")
	public static void start(@NotNull Stage primaryStage) throws IOException
	{
		final Stage stage = new Stage();
		
		final FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("options.fxml"));
		final Scene scene = new Scene(fxmlLoader.load(), 600, 400);
		
		stage.setTitle(TranslatableText.formatString("settings.title"));
		stage.setScene(scene);
		stage.getIcons().setAll(primaryStage.getIcons());
		
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(primaryStage.getOwner());
		stage.show();
	}

}
