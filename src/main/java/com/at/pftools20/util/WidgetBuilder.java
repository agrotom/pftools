package com.at.pftools20.util;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.at.pftools20.logic.LivingActor;
import com.at.pftools20.logic.statcard.main.Actor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class WidgetBuilder
{

	/**
	 * Creates short info box about living actor.
	 * @param actor
	 * @return Baked widget.
	 */
	public static HBox createActorWidget(@NotNull final LivingActor actor)
	{
		final Actor card = actor.template;
		final String cardName = card.name.getValue();
		Image cardImage = card.getImage();
		
		if(cardImage == null)
			cardImage = FileManager.NOT_FOUND_IMAGE;
		
		final ImageView imageView = new ImageView(cardImage);
		imageView.setFitHeight(128.0D);
		imageView.setFitWidth(128.0D);
		
		final Text textName = new Text(cardName);
		final Text healths = new Text("HP: " + actor.getHP());
		healths.setFill(Color.RED);
		healths.setFont(Font.font(16));
		
		final Text maxHealths = new Text("Max HP: " + card.hp.getValue());
		maxHealths.setStyle("-fx-font-style: italic;");
		
		
		
		actor.getUpdateHandler().add(new Consumer<Integer>() {

			@Override
			public void accept(Integer hp)
			{
				healths.setText("HP: " + hp);
			}
			
		});
		
		final VBox infoColumn = new VBox(textName, healths, maxHealths);
		
		final HBox widget = new HBox(imageView, infoColumn, createChart(card));
		
		widget.setOnMouseEntered(event -> {
			widget.setStyle("-fx-background-color: -windows-control-hover-color;");
		});
		
		widget.setOnMouseExited(event -> {
			widget.setStyle("");
		});
		
		HBox.setHgrow(widget, Priority.ALWAYS);
		widget.setAlignment(Pos.CENTER);
		widget.setSpacing(10.0D);
		widget.setPadding(new Insets(5.0D));
		
		return widget;
	}
	
	/**
	 * Service method to create chart for widget.
	 */
	private static PieChart createChart(@NotNull final Actor actorStats)
	{
		final ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
					new PieChart.Data("S", actorStats.strength.getValue() < 0 ? 0 : actorStats.strength.getValue()),
					new PieChart.Data("A", actorStats.agility.getValue() < 0 ? 0 : actorStats.agility.getValue()),
					new PieChart.Data("E", actorStats.endurance.getValue() < 0 ? 0 : actorStats.endurance.getValue()),
					new PieChart.Data("I", actorStats.intelligence.getValue() < 0 ? 0 : actorStats.intelligence.getValue()),
					new PieChart.Data("W", actorStats.wisdom.getValue() < 0 ? 0 : actorStats.wisdom.getValue()),
					new PieChart.Data("C", actorStats.charisma.getValue() < 0 ? 0 : actorStats.charisma.getValue())
				);
		final PieChart stats = new PieChart(data);

		final Tooltip tooltip = new Tooltip("Strength: " + actorStats.strength.getValue() +
				   "\nAgility: " + actorStats.agility.getValue() +
				   "\nEndurance: " + actorStats.endurance.getValue() +
				   "\nIntelligence: " + actorStats.intelligence.getValue() +
				   "\nWisdom: " + actorStats.wisdom.getValue() +
				   "\nCharisma: " + actorStats.charisma.getValue());
		
		Tooltip.install(stats, tooltip);
		
		stats.setMaxSize(50, 50);
		stats.resize(20, 20);
		stats.setLabelLineLength(0);
		
		return stats;
	}

}
