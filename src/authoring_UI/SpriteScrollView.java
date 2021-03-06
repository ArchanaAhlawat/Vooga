package authoring_UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import authoring.Sprite.AbstractSpriteObject;
import javafx.geometry.Side;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class SpriteScrollView extends ScrollPane {

	private VBox containerVBox;
	private Consumer<Pane> childOnClickAction;
	private List<AbstractSpriteObject> spriteList;

	public SpriteScrollView() {
		spriteList = new ArrayList<AbstractSpriteObject>();
		createContainerVBox();
		putContainerVBoxIntoScrollPane();
		this.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		this.setChildOnClickAction(a -> {
			// Nothing by default
		});
	}

	SpriteScrollView(List<Pane> panes) {

	}

	public void addToVBox(List<Pane> panes) {
		panes.forEach(pane -> {
			addToVBox(pane);
		});
	}

	public void addToVBox(Map<String, List<Pane>> panes) {
		TabPane tp = new TabPane();
		tp.setSide(Side.TOP);
		tp.setId("InventoryTabPane");

		panes.forEach((key, value) -> {
			Tab tab = new Tab(key);
			SpriteScrollView SSV = new SpriteScrollView();
			SSV.addToVBox(value);
			SSV.setChildOnClickAction(this.childOnClickAction);
			tab.setContent(SSV);
			tp.getTabs().add(tab);
			// ScrollPane SP = new ScrollPane();
			// VBox VB = new VBox(5);
			// value.forEach(pane->);
		});
		addToVBox(new VBox(tp));
		// panes.forEach(pane->{
		// addToVBox(pane);
		// });
	}

	public void addToSpriteList(AbstractSpriteObject ASO) {
		spriteList.add(ASO);
	}

	public void removeFromSpriteList(AbstractSpriteObject ASO) {
		spriteList.remove(ASO);
	}

	public List<AbstractSpriteObject> getSpriteList() {
		return spriteList;
	}

	private Separator getSeparator() {
		Separator ret = new Separator();
		return ret;
	}
	
	public void removeFromVBox(Pane pane){
		containerVBox.getChildren().remove(pane);
		
	}

	public void addToVBox(Pane pane) {
		pane.setOnMouseClicked(click -> {
			childOnClickAction.accept(pane);
		});
		containerVBox.getChildren().add(pane);
		if (containerVBox.getChildren().size() > 0) {
			containerVBox.getChildren().add(getSeparator());
		}		
	}
	
	public void addToVBoxNoSeparator(VBox vbox) {
		vbox.setOnMouseClicked(click -> {
			childOnClickAction.accept(vbox);
		});
		containerVBox.getChildren().add(vbox);
	}

	private void createContainerVBox() {
		containerVBox = new VBox();
		containerVBox.setPrefWidth(525);
	}

	private void putContainerVBoxIntoScrollPane() {
		this.setContent(containerVBox);
	}

	public void clearVBox() {
		containerVBox.getChildren().clear();
	}

	public void setChildOnClickAction(Consumer<Pane> consumer) {
		childOnClickAction = consumer;
	}
}