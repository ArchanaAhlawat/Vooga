package authoring_UI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import controller.welcomeScreen.SceneController;
import gui.welcomescreen.FileSelector;
import gui.welcomescreen.WelcomeScreen;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tools.DisplayLanguage;

/**
 * Acts as the uppermost ToolBar that is accessible from any authoring view. This contains the options for loading and saving any authoring configuration,
 * as well as importing or exporting any VOOGA projects. It also allows the user to exit back to the main menu. Finally, it has two viewers:
 * the element viewer and map viewer. These allow game builders to see a bigger picture of all their sprites and existing maps in a separate window.
 * 
 * @author Samarth Desai
 *
 */
public class Toolbar extends ToolBar {
	
	public static final String FILE_SELECTOR = "File Selector";
	private static final String FILE_STRING = "File";
	private static final String LOAD_STRING = "Load";
	private static final String SAVE_STRING = "Save";
	private static final String IMPORT_STRING = "Import";
	private static final String EXPORT_STRING = "Export";
	private static final String EXIT_STRING = "Exit";
	private static final String VIEWS_STRING = "Viewers";
	private static final String ELEMENT_VIEWER_STRING = "ElementViewer";
	private static final String MAP_VIEWER_STRING = "MapViewer";
	private static final String SETTINGS_STRING = "Settings";
	private static final String GAMES_PATH = "data/UserCreatedGames";
	
	private MenuButton fileOptions;
	private MenuButton settings;
	private SceneController sceneController;
	private MenuButton views;
	private Stage myStage;

	/**
	 * Creates the ToolBar and adds the file, viewers, and settings options.
	 * 
	 * @param stage
	 * @param currentSceneController
	 */
	public Toolbar(Stage stage, SceneController currentSceneController) {
		myStage = stage;
		sceneController = currentSceneController;
		createFileOptions();
		createViewers();
		createSettings();
		this.getItems().addAll(
				fileOptions,
				views,
				settings
				);
	}
	
	/**
	 * Creates the File dropdown button and its 5 options: Load, Save, Import, Export, and Exit. 
	 */
	private void createFileOptions() {
		
		MenuItem load = new MenuItem();
		load.textProperty().bind(DisplayLanguage.createStringBinding(LOAD_STRING));
		load.setOnAction(e -> this.loadNewGame());
		
		MenuItem save = new MenuItem();
		save.textProperty().bind(DisplayLanguage.createStringBinding(SAVE_STRING));
		save.setOnAction(e -> sceneController.saveWorlds());
		
		Menu importOption = new Menu();
		importOption.textProperty().bind(DisplayLanguage.createStringBinding(IMPORT_STRING));	
		List<MenuItem> importItems = createImportOptions();
		for (MenuItem item : importItems) {
			importOption.getItems().add(item);
		}
		importOption.textProperty().bind(DisplayLanguage.createStringBinding(IMPORT_STRING));
		
		MenuItem export = new MenuItem();
		export.textProperty().bind(DisplayLanguage.createStringBinding(EXPORT_STRING));
		export.setOnAction(e -> sceneController.exportToEngine());
		
		MenuItem exit = new MenuItem();
		exit.textProperty().bind(DisplayLanguage.createStringBinding(EXIT_STRING));
		exit.setOnAction(e -> sceneController.switchScene(SceneController.WELCOME_SCREEN_KEY));
		
		fileOptions = new MenuButton(FILE_STRING, null, load, save, importOption, export, exit);
		fileOptions.textProperty().bind(DisplayLanguage.createStringBinding(FILE_STRING));
	}
	
	/**
	 * Action that sends the user back to the File Selector so they can load a different game into the authoring environment.
	 */
	private void loadNewGame() {
		myStage.close();
		SceneController newScene = new SceneController(new Stage());
		newScene.switchScene(FILE_SELECTOR);
	}
	
	/**
	 * Creates the list of all current games to import to add onto the current project. 
	 * @return the list of games the user can import.
	 */
	private List<MenuItem> createImportOptions() {
		List<MenuItem> importItems = new ArrayList<MenuItem>();
		File f = new File(GAMES_PATH);
		File[] listOfFiles = f.listFiles();
		for (File file: listOfFiles) {
			MenuItem tempItem = new MenuItem(file.getName());
			tempItem.setOnAction(e -> sceneController.importWorlds(file.getName()));
			importItems.add(tempItem);
		}
		return importItems;
	}
	
	/**
	 * Creates the different viewers that the user can access. These currently consist of the element viewer and the map viewer, which allow
	 * the user to see all their sprites and maps for the current project in a bigger display than what is offered in the Map Editor.
	 */
	private void createViewers() {
		MenuItem elementViewer = new MenuItem();
		elementViewer.textProperty().bind(DisplayLanguage.createStringBinding(ELEMENT_VIEWER_STRING));
		elementViewer.setOnAction(e -> new ElementViewer());
		
		MenuItem mapViewer = new MenuItem();
		mapViewer.textProperty().bind(DisplayLanguage.createStringBinding(MAP_VIEWER_STRING));
		//TODO language.setOnAction(e -> ());

		views = new MenuButton (VIEWS_STRING, null, elementViewer, mapViewer);
		views.textProperty().bind(DisplayLanguage.createStringBinding(VIEWS_STRING));
	}
	
	/**
	 * Creates the settings option.
	 */
	private void createSettings() {
		settings = new MenuButton (SETTINGS_STRING, null);
		settings.textProperty().bind(DisplayLanguage.createStringBinding(SETTINGS_STRING));
	}
}