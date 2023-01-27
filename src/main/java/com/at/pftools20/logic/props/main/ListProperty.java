package com.at.pftools20.logic.props.main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.at.pftools20.App;
import com.at.pftools20.logic.enums.CardType;
import com.at.pftools20.logic.statcard.main.StatCard;
import com.at.pftools20.logic.statcard.main.StatCardFactory;
import com.at.pftools20.util.FileManager;
import com.at.pftools20.util.ResourceHelper;
import com.at.pftools20.util.TranslatableText;
import com.at.pftools20.util.json.JSONUtil;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListProperty extends BaseProperty<List<CardHolder>>
{
	private CardType type = null;
	
	protected ListProperty(@NotNull String tag, @NotNull List<CardHolder> value)
	{
		super(tag);
		
		setValue(value);
	}
	
	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		final List<CardHolder> value = getValue();
		
		value.forEach(item -> {
			builder.append(item.getName() + ", ");
		});
		
		if(value.size() > 0)
			builder.delete(builder.length() - 2, builder.length() - 1);
		
		return builder.toString();
	}

	@Override
	public void parse(@NotNull String string)
	{
		logger.log(Level.WARN, "Trying to call parse method in ListProperty! The method not uses in that type of property!");
	}

	@Override
	public void parseJSON(@NotNull String jsonValue)
	{
		final JSONTokener jsonTokener = new JSONTokener(jsonValue);
		final JSONArray root;
		
		try
		{
			root = new JSONArray(jsonTokener);
		} catch (JSONException e)
		{
			logger.log(Level.ERROR, "Can't parse JSON value!");
			return;
		}
		
		final List<CardHolder> value = getValue();
		
		root.forEach(rawHolder -> {
			final JSONObject holder;
			
			try
			{
				holder = (JSONObject) rawHolder;
			} catch (ClassCastException e)
			{
				logger.log(Level.ERROR, "Can't cast one of items from List Property array to JSONObject! Skipping the item!");
				return;
			}
			
			final int count;
			final String name, path;
			
			try
			{
				count = holder.getInt("count");
				name = holder.getString("name");
				path = holder.getString("cardPath");
			} catch (JSONException e)
			{
				logger.log(Level.ERROR, "Can't get one of key of Card Holder from JSON value! Skipping the item!");
				return;
			}
			
			final FileManager manager = new FileManager();
			
			final StatCard statCard = manager.openSilentStatCard(path);
			
			if(statCard == null)
			{
				logger.log(Level.WARN, "StatCard with name: \"" + name + "\" not exists!");
				return;
			}
			
			if(type != null && statCard.getType().isChild(type))
			{
				logger.log(Level.WARN, "StatCard with name: \"" + name + "\" hasn't valid type!");
				return;
			}
			
			final CardHolder newHolder = new CardHolder(count, name, path);
			
			value.add(newHolder);
		});
	}

	@Override
	public String toJSON()
	{
		final JSONArray root = new JSONArray();
		
		final List<CardHolder> value = getValue();
		
		value.forEach(holder -> {
			root.put(holder.createJSONArray());
		});
		
		//logger.log(Level.INFO, root.toString());
		
		return root.toString();
	}
	

	@Override
	protected List<CardHolder> correctValue(@NotNull List<CardHolder> newValue)
	{
		return new LinkedList<>(newValue);
	}
	
	
	@Override
	public PropertyStruct getPropertyStruct()
	{
		return new ListPropertyStruct();
	}
	
	
	private class ListPropertyStruct implements PropertyStruct
	{
		private final Button openEditor = new Button(TranslatableText.formatString("propertylist.editor.button"));
		
		private final Stage stage = new Stage();
		
		private final TableColumn<CardHolder, Integer> countC = new TableColumn<>(TranslatableText.formatString("propertylist.editor.table.count"));
		
		private final TableColumn<CardHolder, String> nameC = new TableColumn<>(TranslatableText.formatString("propertylist.editor.table.name"));
		
		private final TableColumn<CardHolder, String> pathC = new TableColumn<>(TranslatableText.formatString("propertylist.editor.table.path"));
		
		private final TableView<CardHolder> allItems = new TableView<>();
		
		private final FlowPane assetMenu = new FlowPane();
		
		private final ScrollPane assetScroll = new ScrollPane(assetMenu);
		
		private final TextField countTextField = new TextField();
		
		private final Button addButton = new Button(TranslatableText.formatString("propertylist.editor.open"));
		
		private final SplitPane editPanel = new SplitPane(allItems, assetScroll);
		
		private final HBox bottomPanel = new HBox(countTextField, addButton);
		
		private final SplitPane root = new SplitPane(editPanel, bottomPanel);
		
		private final Scene scene = new Scene(root);
		
		private ListPropertyStruct()
		{
			init();
		}
		
		private void init()
		{
			// Stage initialize
			stage.getIcons().setAll(App.getAppCore().getMainStage().getIcons());
			
			// Scene initialize
			scene.getStylesheets().add(ResourceHelper.getResource("/com/at/pftools20/CSS/light_mode.css"));
			
			// Root initialize
			root.setOrientation(Orientation.VERTICAL);
			root.setDividerPositions(0.85);
			
			// Edit panel initialize
			editPanel.setOrientation(Orientation.HORIZONTAL);
			
			// Asset panel initialize
			//assetScroll.setHbarPolicy(ScrollBarPolicy.NEVER);
			
			assetMenu.setHgap(10.0);
			assetMenu.setVgap(10.0);
			
			// Stage initialize
			stage.setScene(scene);
			
			// Open Editor initialize
			openEditor.setOnAction(this::onOpenEditor);
			
			// Table initialize
			allItems.getColumns().add(countC);
			allItems.getColumns().add(nameC);
			allItems.getColumns().add(pathC);
			
			allItems.setOnMouseClicked(this::onRightClicked);
			
			countC.setCellValueFactory(new PropertyValueFactory<>("count"));
			nameC.setCellValueFactory(new PropertyValueFactory<>("name"));
			pathC.setCellValueFactory(new PropertyValueFactory<>("path"));
			
			sync();
			
			// Edit pane initialize
			editPanel.setDividerPositions(0.37);
			
			// Bottom pane initialize
			bottomPanel.setAlignment(Pos.CENTER_RIGHT);
			bottomPanel.setSpacing(10.0);
			HBox.setMargin(countTextField, new Insets(5.0));
			HBox.setMargin(addButton, new Insets(5.0));
			
			// Text field counter initialize
			countTextField.setTextFormatter(new TextFormatter<String>((UnaryOperator<Change>) change -> {
				if(change.isDeleted())
					return change;
				
				final String newText = change.getControlNewText();
				
				try
				{
					@SuppressWarnings("unused")
					final int n = Integer.parseInt(newText);
					
					return change;
				} catch (NumberFormatException ignore) {}
			    
			    return null;
			}));
			countTextField.setPromptText(TranslatableText.formatString("propertylist.textfield.prompt"));
			
			// Add button initialize
			addButton.setOnAction(this::onAddButtonClick);
		}
		
		private void updateAssetMenu()
		{
			assetMenu.getChildren().clear();
			
			final String assetPath = App.getAppCore().getPreferences().assetPath.getValue();
			
			if(assetPath != null && Files.exists(Path.of(assetPath)) && Files.isDirectory(Path.of(assetPath)))
			{
				//logger.log(Level.INFO, "Step into first");
				
				final File assetDir = new File(assetPath);
				final List<File> assetFiles = Arrays.asList(assetDir.listFiles(file -> {
					return FilenameUtils.isExtension(file.getName(), "json");
				}));
				
				//logger.log(Level.INFO, "Cards passed by extension: " + assetFiles.size());
				
				assetFiles.forEach(file -> {
					final JSONUtil jsonUtil = new JSONUtil(file);
					final StatCard card = new StatCardFactory().createFromJSON(jsonUtil.read());
					
					if(card == null)
					{
						//logger.log(Level.INFO, "Can't parse card!");
						return;
					}
					
					if(type != null && !card.getType().isChild(type) && type != card.getType())
					{
						//logger.log(Level.INFO, "Card has bad type!");
						return;
					}
					
					for(final CardHolder i : getValue())
						if(i.getName().equals(card.name.getValue()))
							return;
					
					final Label assetName = new Label(card.name.getValue());
					
					final Image assetImage = card.getImage();
					final Image plugImage = new Image(ResourceHelper.getResource("/images/imagenotfound.png"));
					
					final ImageView assetImageView = new ImageView(
							assetImage != null ? assetImage : plugImage
									);
					assetImageView.setPreserveRatio(true);
					assetImageView.setFitWidth(128.0);
					assetImageView.setFitHeight(128.0);
					
					
					final VBox asset = new VBox(assetName, assetImageView);
					asset.setAlignment(Pos.TOP_CENTER);
					asset.setSpacing(5.0);
					
					asset.setOnMouseClicked(event -> {
						addItem(card.name.getValue(), file.getPath());
					});
					
					assetMenu.getChildren().add(asset);
					//logger.log(Level.INFO, card.name.getValue() + " passed!");
				});
			}
		}
		
		private void onRightClicked(@NotNull MouseEvent event)
		{
			
			if(event.getButton().equals(MouseButton.SECONDARY))
			{
				final CardHolder selectedCard = allItems.getSelectionModel().getSelectedItem();
				
				if(selectedCard != null)
					removeItem(selectedCard);
				
				sync();
				updateAssetMenu();
			}
		}
		
		private void removeItem(@NotNull CardHolder card)
		{
			final List<CardHolder> value = getValue();

			value.remove(card);
			sync();
			
			updateAssetMenu();
		}
		
		private void onAddButtonClick(@NotNull ActionEvent event)
		{
			final FileManager manager = new FileManager();
			
			final StatCard card = manager.openSilentStatCard(App.getAppCore().getMainStage());
			
			if(card != null)
				addItem(card.name.getValue(), FileManager.getLastPath());
		}
		
		private void addItem(@NotNull String name, @NotNull String path)
		{
			final List<CardHolder> value = getValue();
			
			int count = 1;
			
			try {
				count = Integer.parseInt(countTextField.getText());
			} catch (NumberFormatException ignore) {}
			
			value.add(new CardHolder(count, name, path));
			sync();
			
			updateAssetMenu();
		}
		
		private void sync()
		{
			allItems.getItems().setAll(getValue());
		}
		
		private void onOpenEditor(@NotNull ActionEvent event)
		{
			stage.show();
			updateAssetMenu();
		}
		
		@Override
		public Region getMainNode()
		{
			return openEditor;
		}
		
	}
	
	
	public class ListPropBuilder extends Builder<ListPropBuilder, ListProperty>
	{

		public ListProperty.ListPropBuilder setType(@NotNull CardType newType)
		{
			type = newType;
			
			return this;
		}
		
		@Override
		protected ListProperty.ListPropBuilder getThis()
		{
			return this;
		}

		@Override
		public ListProperty build()
		{
			return ListProperty.this;
		}
		
	}

	public static ListPropBuilder newBuilder(@NotNull String tag, @NotNull List<CardHolder> value) { return new ListProperty(tag, value).new ListPropBuilder(); }

	
}
