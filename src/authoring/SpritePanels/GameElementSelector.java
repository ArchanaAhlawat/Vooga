package authoring.SpritePanels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import authoring.GridManagers.*;
import authoring.Sprite.*;
import authoring.SpriteManagers.*;
import authoring.SpritePanels.*;
import authoring.util.*;
import authoring_UI.Map.*;
import authoring_UI.*;
import authoring.*;
import authoring_UI.Inventory.*;
import engine.utilities.data.GameDataHandler;
import gui.welcomescreen.MenuOptionsTemplate;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import tools.DisplayLanguage;

public class GameElementSelector extends TabPane implements Observer {
	
	protected static final String SPRITES = "Sprites";
	protected static final String DIALOGUES = "Dialogues";
	protected static final String DEFAULT = "Default";
	protected static final String USER = "User";
	protected static final String IMPORTED = "Imported";
	protected static final String IMPORTEDINVENTORY = "Imported Inventory";
	protected static final String INVENTORY = "Inventory";
	
	protected DraggableGrid myGrid;
//	private SpriteSelectPanel mySprites;
//	private SpriteSelectPanel myUserSprites;
	private final int NUM_COLUMNS = 10;

	protected AuthoringEnvironmentManager myAEM;
//	private SpriteParameterFactory mySPF;
//	private List<AbstractSpriteObject> mySpriteObjs = new ArrayList<AbstractSpriteObject>();
//	private List<SpriteObject> myUserSpriteObjs = new ArrayList<SpriteObject>();
//	private GameDataHandler myGDH;
//	private SpriteObjectGridManagerI mySOGM;
	protected SpriteGridHandler mySpriteGridHandler;
	private Tab dialoguesTab;
	protected String myType;
	
	protected GameElementSelector(SpriteGridHandler spriteGridHandler, AuthoringEnvironmentManager AEM){
		this(spriteGridHandler, AEM, "");	}

	protected GameElementSelector(SpriteGridHandler spriteGridHandler, AuthoringEnvironmentManager AEM, String type) {
		myType = type;
		myAEM = AEM;
		mySpriteGridHandler = spriteGridHandler;
		this.setPrefHeight(280);
		createSpriteTabs();
	}

	/**
	 * creates new user sprite
	 * @author Samuel
	 * @param sp
	 */

	public void createUserSprite(Object spObj) {
		if (!(spObj instanceof SpriteObject)) {
			throw new RuntimeException("Its not a sprite");
		}
		SpriteObject sp = (SpriteObject) spObj;
		try {
			this.myAEM.addUserSprite(sp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void createSpriteTabs() {
		TabPane spritesTabPane = new TabPane();
		TabPane dialoguesTabPane = new TabPane();
		TabPane inventoryTabPane = new TabPane();
		;
		Tab defaultSpriteTab = createSubTab(DEFAULT, myAEM.getDefaultSpriteController());
		Tab userSpriteTab = createSubTab(USER, myAEM.getCustomSpriteController());
		Tab importedSpriteTab = createSubTab(IMPORTED, myAEM.getImportedSpriteController());
		Tab inventorySpriteTab = createSubTab(INVENTORY, myAEM.getInventoryController());
		Tab importedInventorySpriteTab = createSubTab(IMPORTEDINVENTORY, myAEM.getImportedInventorySpriteController());
//		Tab defaultDialogueTab = createSubTab(DEFAULT);
//		Tab userDialogueTab = createSubTab(USER);
//		Tab importedDialogueTab = createSubTab(IMPORTED);
		spritesTabPane.getTabs().addAll(defaultSpriteTab, userSpriteTab, importedSpriteTab);
		spritesTabPane.setSide(Side.RIGHT);
//		dialoguesTabPane.getTabs().addAll(defaultDialogueTab, userDialogueTab, importedDialogueTab);
		dialoguesTabPane.setSide(Side.RIGHT);
		
		inventoryTabPane.setSide(Side.RIGHT);
		inventoryTabPane.getTabs().addAll(inventorySpriteTab, importedInventorySpriteTab);
		
		Tab spritesTab = createElementTab(SPRITES, spritesTabPane);
		spritesTab.setClosable(false);
		dialoguesTab = createElementTab(DIALOGUES, dialoguesTabPane);
		dialoguesTab.setClosable(false);
		Tab inventoryTab = createElementTab(INVENTORY, inventoryTabPane);
		inventoryTab.setClosable(false);
		this.getTabs().addAll(spritesTab, dialoguesTab, inventoryTab);
		
		this.setSide(Side.TOP);
	}
	
	protected Tab createSubTab(String tabName, SpriteSet controller) {
		Tab subTab = new Tab();
		subTab.setText(tabName);
//		subTab.textProperty().bind(DisplayLanguage.createStringBinding(tabName));
//		defaultSpriteTab.setContent(mySprites);
		subTab.setContent(makeCategoryTabPane(controller));
		subTab.setOnSelectionChanged((event)->{
			if (subTab.isSelected()){
				subTab.setContent(makeCategoryTabPane(controller));
			}
		});
		subTab.setClosable(false);
		return subTab;
	}
	
	private TabPane makeCategoryTabPane(SpriteSet controller){
		TabPane categoryTabPane = new TabPane();
		categoryTabPane.setSide(Side.LEFT);
		for (Entry<String, List<AbstractSpriteObject>> cat: controller.getAllSpritesAsMap().entrySet()){
			Tab catTab = new Tab();
			catTab.setClosable(false);
			catTab.setText(cat.getKey());
			catTab.setContent(makeGrid(cat.getValue()));
			catTab.setOnSelectionChanged(event->{
				if (catTab.isSelected()){
					catTab.setContent(makeGrid(cat.getValue()));
				}
			});
			categoryTabPane.getTabs().add(catTab);
		}
		return categoryTabPane;
	}
	
	private Tab createElementTab(String tabName, TabPane tabPane) {
		Tab elementTab = new Tab();
		elementTab.setText(tabName);
//		elementTab.textProperty().bind(DisplayLanguage.createStringBinding(tabName));
		elementTab.setContent(tabPane);
		elementTab.setClosable(false);
		return elementTab;
	}

	private ScrollPane makeGrid(List<AbstractSpriteObject> sprites) {
		GridPane gp = new GridPane();
		int totalRows = (int) Math.ceil(sprites.size()/10);
		int DEFAULT_MIN_ROWS = 15;
		totalRows = (totalRows<DEFAULT_MIN_ROWS) ? DEFAULT_MIN_ROWS : totalRows;
		int counter =0;
		for (int i = 0; i < totalRows; i++) {
			for (int j = 0; j < 10; j++) {				
				StackPane sp = new StackPane();
				sp.setPrefHeight(50);
				sp.setPrefWidth(50);
				sp.setBackground(
						new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				BorderStroke border = new BorderStroke(Color.LIGHTGREY, BorderStrokeStyle.DOTTED,
						CornerRadii.EMPTY, BorderWidths.DEFAULT);
				sp.setBorder(new Border(border));
				if (counter<sprites.size()) {
					AbstractSpriteObject toPopulate = sprites.get(counter);
					System.out.println("Adding " + toPopulate.getImageURL());
					addSpriteGridHandlerFunctionality(toPopulate);
					sp.getChildren().add(toPopulate);
				counter++;
				gp.add(sp, j, i);
			}
		}
	}

		ScrollPane SP = new ScrollPane(gp);
		return SP;
	}
	
	protected void addSpriteGridHandlerFunctionality(AbstractSpriteObject ASO){
		mySpriteGridHandler.addSpriteDrag(ASO);
		mySpriteGridHandler.addSpriteMouseClick(ASO);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		;
		createUserSprite(arg);
	}
	
	public Tab getDialoguesTab() {
		return dialoguesTab;
	}
}