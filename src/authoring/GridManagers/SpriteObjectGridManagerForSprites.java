package authoring.GridManagers;

import java.util.List;

import authoring.Sprite.AbstractSpriteObject;
import authoring_UI.SpriteGridHandler;
import authoring_UI.Map.SpriteLayer;
import authoring_UI.Map.TerrainLayer;
import javafx.scene.paint.Color;

public class SpriteObjectGridManagerForSprites extends SpriteObjectGridManager{

	public SpriteObjectGridManagerForSprites(int rows, int columns, SpriteGridHandler SGH) {
		super(rows, columns, SGH);
		myLayerNum = 2;
	}
	
	public SpriteObjectGridManagerForSprites(int rows, int columns) {
		super(rows, columns);
		myLayerNum = 2;
	}

	public SpriteObjectGridManagerForSprites(int myNumRows, int myNumCols, Color myColor) {
		super(myNumRows, myNumCols, myColor);
		myLayerNum = 2;
	}

	@Override
	public void createMapLayer() {
		if (hasStoredSprites()){
			loadedFromData = true;
			myMapLayer = new SpriteLayer(defaultRows, defaultColumns,mySpriteGridHandler,getStoredSpriteList());
		} else{
		myMapLayer = new SpriteLayer(getNumRows(), getNumCols(), mySpriteGridHandler);
		
		}
	}
	
	

}
