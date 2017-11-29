package authoring_UI;

import authoring.SpriteParameterI;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class FEBooleanParameter extends FEParameter {
	SpriteParameterI myParam;
	FEParameterName myName;	
	CheckBox myCheckBox;
	Boolean myValue;
	
	protected FEBooleanParameter(SpriteParameterI BEParam) {
		myParam = BEParam;
		BorderStroke border = new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT);
		myName = new FEParameterName(myParam.getName());
		myName.setBorder(new Border(border));
		myCheckBox = new CheckBox();
		myValue = (Boolean) myParam.getValue();
		myCheckBox.setSelected(myValue);
		this.getChildren().addAll(myName, myCheckBox);
		this.setSpacing(3);

		handleValueChange();
	}
	
	private void handleCheckBox() {
		myCheckBox.setOnAction((event) -> {
			boolean isSelected = myCheckBox.isSelected();
			myCheckBox.setSelected(isSelected);
			myValue = isSelected;
		});
	}
	
	protected void handleValueChange() {
		handleCheckBox();
	}
	
	protected void updateParameter() {
		myParam.update(myName.getText(), myValue);
	}
	
	

}

