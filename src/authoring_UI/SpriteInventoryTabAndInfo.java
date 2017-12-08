package authoring_UI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import authoring.AbstractSpriteObject;
import authoring.AuthoringEnvironmentManager;
import authoring.SpriteSetHelper;
import authoring.SpriteThumbnail;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SpriteInventoryTabAndInfo {

	private VBox containerVBox;
	private SpriteScrollView myTabPane;
//	private ArrayList<AbstractSpriteObject> myInventory;
	private AbstractSpriteObject myASO;
	private final static double MENU_WIDTH = 400;
	private final static double MENU_HEIGHT = 500;
	private Consumer<Pane> itemOnClickAction;
	private Consumer buttonAction;
	private String buttonText;
	private SpriteSetHelper mySSH;

	private Set<AbstractSpriteObject> temporaryInventory;

	SpriteInventoryTabAndInfo(SpriteSetHelper SSH) {
		mySSH = SSH;
		createBoundingScrollPane();
		initialize();
	}

	SpriteInventoryTabAndInfo(AbstractSpriteObject ASO, SpriteSetHelper SSH) {
		this(SSH);
		setSpriteObject(ASO);
		remakeContainingVBoxFromNewInventory();
	}

	private void initialize() {
		containerVBox = new VBox(5);

//		myInventory = new ArrayList<AbstractSpriteObject>();
		temporaryInventory = new HashSet<AbstractSpriteObject>();
//		removedInventory = new ArrayList<AbstractSpriteObject>();
		this.setClickEvent(click -> {
			// Nothing by default
		});
		this.setButton("Add to inventory", action -> {
			List<AbstractSpriteObject> newInventory = triggerPopUpOfInventoryToChoose();
			temporaryInventory.addAll(newInventory);
			newInventory.forEach(sprite -> {
				addInventory(sprite);
			});
		});
		containerVBox.getChildren().addAll(myTabPane, makeAddInventoryButton());
	}

	public void setSpriteObjectAndUpdate(AbstractSpriteObject ASO) {
		setSpriteObject(ASO);
		remakeContainingVBoxFromNewInventory();
	}

	public void setSpriteObject(AbstractSpriteObject ASO) {
		myASO = ASO;
		setInventory(ASO.getInventory());
	}

	public void reset() {
		myASO = null;
		resetScrollPane();
	}

	public void addInventory(AbstractSpriteObject ASO) {
		SpriteThumbnail ST = new SpriteThumbnail(ASO, true);
		ST.addSideButton("Remove");
		ST.setSideButtonRunnable(()->{
			removeFromInventory(ST);
			
		});
		ST.setOnMouseClicked(event -> {
			itemOnClickAction.accept(ST);
		});
		myTabPane.addToVBox(ST);
	}
	

	private void remakeContainingVBoxFromNewInventory() {
		resetScrollPane();
		temporaryInventory.forEach(sprite -> {
			addInventory(sprite);
		});
	}

	private void setInventory(List<AbstractSpriteObject> newInventory) {
		temporaryInventory.clear();
		temporaryInventory.addAll(newInventory);
	}
	
	private void removeFromInventory(SpriteThumbnail ST){
		myTabPane.removeFromVBox(ST);
		temporaryInventory.remove(ST.getSprite());
	}

	private void createBoundingScrollPane() {
		myTabPane = new SpriteScrollView();
	}

	public VBox getContainingVBox() {
		return containerVBox;
	}

	public void resetScrollPane() {
		myTabPane.clearVBox();
	}

	private Button makeAddInventoryButton() {
		Button button = new Button(buttonText);
		button.setOnAction(e -> {
			buttonAction.accept(e);
		});
		return button;
	}

	private List<AbstractSpriteObject> triggerPopUpOfInventoryToChoose() {
		// SpriteInventoryTabAndInfo dummy = new SpriteInventoryTabAndInfo();
		SpriteScrollView SSV = new SpriteScrollView();
		SSV.setChildOnClickAction(pane -> {
			if (pane instanceof SpriteThumbnail) {
				SpriteThumbnail ST = (SpriteThumbnail) pane;
				ST.isClicked(!ST.isClicked());
				if (ST.isClicked()) {
					SSV.addToSpriteList(ST.getSprite());
				} else {
					SSV.removeFromSpriteList(ST.getSprite());
				}
			}
		});
		SSV.addToVBox(mySSH.getThumbnailSprites());

		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		Node parent = myTabPane.getParent();
		Scene s = parent.getScene();
		while (s == null) {
			parent = parent.getParent();
			s = parent.getScene();
		}
		dialog.initOwner(s.getWindow());
		// VBox dialogVbox = new VBox(20);
		// dialogVbox.getChildren().add(new Text("This is a Dialog"));
		Scene dialogScene = new Scene(SSV, 600, 400);
		dialog.setScene(dialogScene);
		
		// dialog.show();
		dialog.showAndWait();
		// dialog.setOnCloseRequest
		return SSV.getSpriteList();

	}

	public void setClickEvent(Consumer<Pane> consumer) {
		itemOnClickAction = consumer;
	}

	public void setButton(String description, Consumer consumer) {
		buttonText = description;
		buttonAction = consumer;
	}
	
	public void apply(){
//		myInventory.addAll(temporaryInventory);
		System.out.println("myASO: "+myASO);
		myASO.setInventory(temporaryInventory);
	}
}