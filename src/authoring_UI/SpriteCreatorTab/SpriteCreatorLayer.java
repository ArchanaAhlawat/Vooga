package authoring_UI.SpriteCreatorTab;

import java.util.function.Supplier;

import authoring.Sprite.AbstractSpriteObject;
import authoring.Sprite.SpriteObject;
import authoring_UI.AuthoringMapStackPane;
import authoring_UI.SpriteGridHandler;
import authoring_UI.Map.MapLayer;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SpriteCreatorLayer extends MapLayer {

	public SpriteCreatorLayer(int rows, int columns, SpriteGridHandler SGH){
		this(rows, columns, SGH, Color.TRANSPARENT);
	}

		public SpriteCreatorLayer(int rows, int columns, SpriteGridHandler SGH, Color c) {
		super(rows, columns, SGH, c);
		setDefaultColor(Color.TRANSPARENT);
		setName("SpriteCreator View");
	}
		
		@Override 
		public AbstractSpriteObject setBackgroundImage(Supplier<AbstractSpriteObject> givenSprite){
			AuthoringMapStackPane AMSP = this.getChildAtPosition(0, 0);
			if (AMSP.hasChild()){
				AMSP.removeChild();
			}
			this.getChildren().forEach(cell->{
				((AuthoringMapStackPane) cell).setInactiveBackground(Color.TRANSPARENT);
			});

			AbstractSpriteObject ASO = givenSprite.get();
//					new SpriteObject(image, path);
			
			ASO.setNumCellsHeightNoException(this.numRowsProperty.get());
			ASO.setNumCellsWidthNoException(this.numColumnsProperty.get());
			AMSP.addChild(ASO);
//			AMSP.setRowSpan(this.numRowsProperty.get());
//			AMSP.setColSpan(this.numColumnsProperty.get());

			this.mySGH.addSpriteMouseClick(ASO);
			numColumnsProperty.addListener((observable, oldNumColumns, newNumColumns)->{
				AMSP.setColSpan(newNumColumns);
				ASO.setNumCellsWidthNoException(newNumColumns);
			});
			numRowsProperty.addListener((observable, oldNumRows, newNumRows)->{
				AMSP.setRowSpan(newNumRows);
				ASO.setNumCellsHeightNoException(newNumRows);
			});
//			ASO.getHeightObjectProperty().addListener((change, oldVal, newVal)->{
//				numRowsProperty.set(newVal);
//			});
//			
//			ASO.getWidthObjectProperty().addListener((change, oldVal, newVal)->{
//				numColumnsProperty.set(newVal);
//			});
			return ASO;
		}
		
}

