package authoring_data;

import java.util.ArrayList;
import java.util.List;

import authoring.GridManagers.SpriteObjectGridManager;
import authoring.Sprite.AbstractSpriteObject;
import authoring.Sprite.SpriteObject;
import authoring.Sprite.AnimationSequences.AnimationSequenceImage;
import authoring.Sprite.AnimationSequences.AuthoringAnimationSequence;
import authoring.Sprite.Parameters.SpriteParameter;
import authoring_UI.DraggableGrid;
import engine.Action;
import engine.GameLayer;
import engine.GameMaster;
import engine.GameObject;
import engine.GameWorld;
import engine.Holdable;
import engine.VariableContainer;
import engine.sprite.AnimationSequence;
import engine.sprite.BoundedImage;
import engine.sprite.Sprite;
import engine.utilities.data.GameDataHandler;

public class SpriteObjectGridToEngineController {
	private GameMaster myEC;
	private GameDataHandler myGDH;
	private GameWorld currentWorld;
	private static Integer CELL_WIDTH = 50;
	private static Integer CELL_HEIGHT = 50;

	public SpriteObjectGridToEngineController(GameDataHandler GDH){
		myGDH = GDH;
	}
	
	// called every time a grid is processed (new world is added to engine)
	public void createLayerAndAddToEngine(DraggableGrid currentGrid) { //SpriteObjectGridManagerI SOGMI
		List<SpriteObjectGridManager> allLayers = currentGrid.getGrids();
		createWorld(currentGrid.getName());
		for (SpriteObjectGridManager thisLayer : allLayers) {
			createEngineLayerAndAddToWorld(thisLayer, thisLayer.getName());
		}
//		addHUDToWorld()
		addWorldToEngine(currentWorld);
	}
	
	private void createWorld(String n) {
		currentWorld = new GameWorld(n); 
	}
	
	private void createWorld() {
		currentWorld = new GameWorld(); 
	}

	private void createEngineLayerAndAddToWorld(SpriteObjectGridManager thisLayer, String name) {
		List<GameObject> GO_LIST = convertSpriteObjectGridToListOfGameObjects(thisLayer);
		GameLayer engineLayer = createLayer(GO_LIST, name);
		addLayerToWorld(engineLayer);
	}

	private GameLayer createLayer(List<GameObject> gO_LIST, String name) {
		GameLayer thisLayer = new GameLayer(name);
		addAllGameObjectsToLayer(gO_LIST, thisLayer);
		return thisLayer;
	}

	public void saveEngine() {
		try{
		myGDH.saveGame(myEC);
		} catch (Exception e){
			throw new RuntimeException("Cant save game....");
		}
	}
	
	private GameObject convertToGameObject(SpriteObject SOI){
		//added null as input to rid error
		GameObject GE = new GameObject(SOI.getName());
		setTags(SOI, GE);
		setSpriteForGameObject(SOI, GE);

		setPositionAndSizeOfGameObject(SOI, GE);
		setInventory(SOI, GE);
		addParametersToVariableContainer(SOI, GE);
		addConditionsAndActionsToGameObject(SOI, GE);
		
		return GE;
	}
	
	private void setTags(AbstractSpriteObject ASO, GameObject GO){
		ASO.getTags().forEach((tag)->{
			GO.addTag(tag);
		});
		
	}
	
	private void setInventory(SpriteObject SO, GameObject GO){
		SO.getInventory().forEach((inventory)->{
			GO.getInventory().addObject(convertInventoryObjectToHoldable(inventory));
		});
	}
	
	private Holdable convertInventoryObjectToHoldable(AbstractSpriteObject ASO){
		Sprite sprite = getSprite(ASO);
		Holdable holdable = new Holdable(sprite);
		addParametersToVariableContainer(ASO, holdable);
		holdable.setSelectActions(getActions(ASO));
		return holdable;
	}
	
	private void setSpriteForGameObject(AbstractSpriteObject SOI, GameObject GO){
		Sprite s = getSprite(SOI);
		GO.setSprite(s);
	}
	
	private Sprite getSprite(AbstractSpriteObject SOI){
		Sprite sprite = new Sprite();
		SOI.getAnimationSequences().forEach((animation)->{
			AnimationSequence as = createSpriteAnimation(animation);
			sprite.addAnimationSequence(as);
		});
		
		return sprite;
	}
	
	private AnimationSequence createSpriteAnimation(AuthoringAnimationSequence AAS){
		List<BoundedImage> bimages = new ArrayList<BoundedImage>();
		AAS.getImages().forEach((ASI)->{
			BoundedImage converted = convertAnimationSequenceImageToBoundedImage(ASI);
			;
			bimages.add(converted);
		});
		AnimationSequence ret = new AnimationSequence(AAS.getName(), bimages);
	return ret;
	}
	
	private BoundedImage convertAnimationSequenceImageToBoundedImage(AnimationSequenceImage ASI){
		return new BoundedImage(ASI.getImage().getImagePath().replaceAll(".*\\", ""));
	}
	
	private void setPositionAndSizeOfGameObject(SpriteObject SOI, GameObject GO){
		GO.setLocation(SOI.getXCenterCoordinate()*CELL_WIDTH, SOI.getYCenterCoordinate()*CELL_HEIGHT);
		GO.setSize(SOI.getNumCellsWidth()*CELL_WIDTH, SOI.getNumCellsHeight()*CELL_HEIGHT);
		GO.setUniqueID(SOI.getUniqueID());
	}

	private void addParametersToVariableContainer(AbstractSpriteObject sOI, VariableContainer varCont) {
		for (List<SpriteParameter> SPI_LIST: sOI.getParameters().values()){
			for (SpriteParameter SPI: SPI_LIST){
				varCont.addParameter(SPI.getName(), SPI.getValue());
			}
		}
	}
	
	private void addConditionsAndActionsToGameObject(AbstractSpriteObject ASO, GameObject GE){
		ASO.conditionActionPairings().forEach((condition, actionList)->{
			GE.addConditionAction(condition, actionList);
		});		
	}
	
	private List<Action> getActions(AbstractSpriteObject ASO){
		return null;
	}
	
	private List<GameObject> convertSpriteObjectGridToListOfGameObjects(SpriteObjectGridManager SOGM_IN) {
		List<GameObject> GO_LIST = new ArrayList<GameObject>();
		for (AbstractSpriteObject SOI: SOGM_IN.getEntireListOfSpriteObjects()) {
			GameObject convertedToGameObject = convertToGameObject((SpriteObject) SOI);
			GO_LIST.add(convertedToGameObject);
		}
		return GO_LIST;
	}
	
	private void addLayerToWorld(GameLayer layerToAdd) {
		currentWorld.addLayer(layerToAdd);
	}
	
	private void addAllGameObjectsToLayer(List<GameObject> GO_LIST, GameLayer layer) {
		for (GameObject GO: GO_LIST) {
			layer.addElement(GO);
			layer.addGameObject(GO);
		}
	}
	
	private void createEngine() {
		myEC = new GameMaster();
		myEC.setNextWorld(currentWorld.getName());

		System.out.print(currentWorld.getName());
	}
	
	private void addWorldToEngine(GameWorld newWorld) {
		if (myEC==null){
			createEngine();
		}
		myEC.addWorld(newWorld);
	}
}
