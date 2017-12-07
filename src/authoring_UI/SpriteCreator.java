package authoring_UI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ResourceBundle;

import authoring.AbstractSpriteObject;
import authoring.AuthoringEnvironmentManager;
import authoring.SpriteNameManager;
import authoring.SpriteObject;
import authoring.SpriteParameterI;
import gui.welcomescreen.WelcomeScreen;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Class for creating new sprites
 * 
 * @author taekwhunchung
 *
 */

public class SpriteCreator extends TabPane {

	private static final String PATH = "resources/";
	private static final String SPRITECREATORRESOURCES_PATH = "TextResources/SpriteCreatorResources";
	private static final int PANE_WIDTH = MainAuthoringGUI.AUTHORING_WIDTH - ViewSideBar.VIEW_MENU_HIDDEN_WIDTH;

	private ResourceBundle spriteCreatorResources;
	private AuthoringEnvironmentManager myAEM;
	private SpriteNameManager mySNM;
	private Pane myPane;
	private VBox myStatePanel;
	private StackPane myImageStack;
	private int spriteCount = 1;

	public SpriteCreator(AuthoringEnvironmentManager AEM) {
		spriteCreatorResources = ResourceBundle.getBundle(SPRITECREATORRESOURCES_PATH);
		myAEM = AEM;
		mySNM = new SpriteNameManager();
		myPane = new Pane();
		myPane.getChildren().add(this);
		this.setWidth(PANE_WIDTH);
		this.setLayoutX(ViewSideBar.VIEW_MENU_HIDDEN_WIDTH);
		Tab tab = makeTab();
		this.getTabs().add(tab);

	}

	private Tab makeTab() {
		Tab tab = new Tab();
		tab.setText(spriteCreatorResources.getString("SpriteTab") + spriteCount);
		spriteCount++;

		HBox hb = addParentHBox(tab);

		addStatePanel(hb);
		addImageStackPane(hb);
		addToolBox(hb);

		return tab;

	}

	private void addToolBox(HBox parentBox) {
		VBox toolBox = new VBox();
		toolBox.setPrefSize(200, WelcomeScreen.HEIGHT);
		Button b1 = new Button(spriteCreatorResources.getString("TestTool1"));
		Button b2 = new Button(spriteCreatorResources.getString("TestTool2"));
		toolBox.getChildren().addAll(b1, b2);
		parentBox.getChildren().add(toolBox);
	}

	private void addImageStackPane(HBox parentBox) {
		myImageStack = new StackPane();
		// myImageStack.setPrefSize(PANE_WIDTH/2-100, WelcomeScreen.HEIGHT);
		myImageStack.setMinWidth(PANE_WIDTH / 2 - 100);
		myImageStack.setMaxSize(PANE_WIDTH / 2 - 100, WelcomeScreen.HEIGHT);
		myImageStack.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		BorderStroke border = new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY,
				BorderWidths.DEFAULT);
		myImageStack.setBorder(new Border(border));
		ImageView test = new ImageView("pikachu.png");
		HBox nameBox = makeNameBox();

		myImageStack.getChildren().addAll(test, nameBox);
		parentBox.getChildren().add(myImageStack);
		// parentBox.getChildren().add(new Text("test"));
	}

	private HBox makeNameBox() {
		HBox nameBox = new HBox(10);
		Text enterName = new Text(spriteCreatorResources.getString("NameField"));
		TextField nameField = new TextField();
		Button confirm = new Button(spriteCreatorResources.getString("ConfirmButton"));

		nameBox.getChildren().addAll(enterName, nameField, confirm);

		nameField.setOnAction(e -> {
			System.out.println("name entered");
			if (mySNM.isNameValidTemplate(nameField.getText())) {
				// add to mySNM
				mySNM.addTemplateName(nameField.getText());
				System.out.println("name is valid");
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Input Error");
				alert.setHeaderText("Name already exists");
				alert.setContentText("Change to a unique name");
				alert.showAndWait();
			}
		});

		confirm.setOnAction(e -> {
			System.out.println("name entered");
			if (mySNM.isNameValidTemplate(nameField.getText())) {
				// add to mySNM
				System.out.println("name is valid");
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Name already exists");
				alert.setContentText("Change to a unique name");
				alert.showAndWait();
			}
		});

		return nameBox;
	}

	private void addStatePanel(HBox parentBox) {
		myStatePanel = new VBox();
		myStatePanel.setPrefWidth(PANE_WIDTH / 2);
		myStatePanel.setMaxWidth(PANE_WIDTH / 2);
		myStatePanel.setPrefHeight(WelcomeScreen.HEIGHT);
		myStatePanel.setMaxWidth(PANE_WIDTH);
		myStatePanel.setStyle("-fx-background-color: #ffaadd;");

		HBox buttonBox = new HBox(10);

		addButtons(buttonBox);
		VBox stateBox = createStateBox();
		ScrollPane sp = createGrid();

		myStatePanel.getChildren().addAll(buttonBox, stateBox, sp);

		parentBox.getChildren().add(myStatePanel);
	}

	private void addButtons(HBox buttonBox) {
		Button loadImageButton = new Button(spriteCreatorResources.getString("LoadImageButton"));
		loadImageButton.setOnAction(e -> {
			try {
				openImage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		Button createSpriteButton = new Button("Create Sprite Template");

		buttonBox.getChildren().addAll(loadImageButton, createSpriteButton);

	}

	private void openImage() throws IOException {
		FileChooser imageChooser = new FileChooser();
		imageChooser.setInitialDirectory(new File("resources/"));
		imageChooser.setTitle(spriteCreatorResources.getString("ImageChooser"));
		File file = imageChooser.showOpenDialog(new Stage());

		if (file != null) {
			Files.copy(file.toPath(), Paths.get(PATH + file.getName()), StandardCopyOption.REPLACE_EXISTING);
			Image image = new Image(file.getName());
			ImageView imageView = new ImageView(image);
			
			AbstractSpriteObject newSprite = new SpriteObject();
			newSprite.setImageURL(file.getName());
			
			//add params
			ArrayList<SpriteParameterI> myParams = new ArrayList<SpriteParameterI>();
			
			newSprite.setNumCellsWidthNoException(1);
			newSprite.setNumCellsHeightNoException(1);
			
			// imageView.setFitHeight(WelcomeScreen.HEIGHT);
			// imageView.setFitWidth(PANE_WIDTH/2-100);
			
			myImageStack.getChildren().remove(1);
			
//			myImageStack.getChildren().add(imageView);
			myImageStack.getChildren().add(newSprite);

			// myStatePanel.getChildren().add(imageView);
			System.out.println("image loaded");
			// System.out.println(file.getName());
			// mySpriteObject.setImageURL(file.getName());
			// setChanged();
			// System.out.print(file.getName());
			// notifyObservers(file.getName());
			// System.out.println("image chosen");
		}
	}

	private ScrollPane createGrid() {
		GridPane gp = new GridPane();

		int counter = 0;
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 10; j++) {
				StackPane sp = new StackPane();
				sp.setPrefHeight(50);
				sp.setPrefWidth(50);
				sp.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				BorderStroke border = new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.DOTTED, CornerRadii.EMPTY,
						BorderWidths.DEFAULT);
				sp.setBorder(new Border(border));

				ArrayList<AbstractSpriteObject> sprites = myAEM.getDefaultSpriteController().getAllSprites();
//				if (counter < sprites.size()) {
//					AbstractSpriteObject toLoad = sprites.get(counter);
//					sp.getChildren().add(toLoad);
//				}
				counter++;

				gp.add(sp, j, i);
			}
		}

		ScrollPane sp = new ScrollPane(gp);
		sp.setMaxHeight(WelcomeScreen.HEIGHT / 2);
		return sp;
	}

	private VBox createStateBox() {
		VBox stateBox = new VBox();
		stateBox.setMaxHeight(WelcomeScreen.HEIGHT / 2);
		stateBox.setPrefHeight(WelcomeScreen.HEIGHT / 2);
		stateBox.setPrefWidth(PANE_WIDTH / 2);
		stateBox.setStyle("-fx-background-color: #ffaadd;");
		return stateBox;
	}

	private HBox addParentHBox(Tab tab) {
		HBox hb = new HBox();
		hb.setStyle("-fx-background-color: #FFFFFF;");
		hb.setPrefWidth(PANE_WIDTH);
		hb.setPrefHeight(WelcomeScreen.HEIGHT);
		tab.setContent(hb);
		return hb;
	}

	public Pane getPane() {
		return myPane;
	}
}
