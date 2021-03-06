package authoring_UI;

import java.util.ArrayList;
import java.util.List;

import authoring.AuthoringEnvironmentManager;
import authoring.SpriteCreatorSpriteManager;
import gui.welcomescreen.WelcomeScreen;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SpriteCreatorManager extends HBox {
	public static final int VIEW_WIDTH = MainAuthoringGUI.AUTHORING_WIDTH - ViewSideBar.VIEW_MENU_HIDDEN_WIDTH;
	public static final int VIEW_HEIGHT = WelcomeScreen.HEIGHT - 35;

	private AuthoringEnvironmentManager myAEM;
	private SpriteCreatorSpritePanel mySpritePanel;
	private SpriteCreatorSpriteManager mySM;
	private SpriteCreatorGridHandler myGridHandler;
	private int myTabCount = 1;

	public SpriteCreatorManager(AuthoringEnvironmentManager AEM, SpriteCreatorImageGrid imageGrid, SpriteCreatorGridHandler SCG, SpriteCreatorSpriteManager SM) {
		myAEM = AEM;
		mySM = SM;
		myGridHandler = SCG;
		this.setPrefWidth(VIEW_WIDTH);
		this.setPrefHeight(VIEW_HEIGHT);
		this.setLayoutX(ViewSideBar.VIEW_MENU_HIDDEN_WIDTH);

		mySpritePanel = setupScene(imageGrid);
	}

	private SpriteCreatorSpritePanel setupScene(SpriteCreatorImageGrid imageGrid) {
		SpriteCreatorSpritePanel spritePanels = new SpriteCreatorSpritePanel(myGridHandler, myAEM, mySM);
		myGridHandler.setDisplayPanel(spritePanels);
		return spritePanels;
	}

	public SpriteCreatorDisplayPanel getDisplayPanel() {
		return mySpritePanel.getDisplayPanel();
	}

	public SpriteCreatorSpriteSelector getSpriteSelector() {
		return mySpritePanel.getSpriteSelector();
	}
	// public Tab getDialoguesTab() {
	// return spritePanels.getDialoguesTab();
	// }
	//
}
