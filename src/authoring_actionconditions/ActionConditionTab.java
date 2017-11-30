package authoring_actionconditions;

import java.util.ResourceBundle;

import authoring_UI.Menu;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class ActionConditionTab extends Tab implements ActionConditionTabI {
	
	private static final double SPACING = 10;
	
	private ResourceBundle actionTabResources;
	private ScrollPane actionConditionManager;
	private TopToolBar buttons;
	private ActionConditionVBox actionConditionVBox;
	private boolean isConditionTab;
	
	public ActionConditionTab(String title) {
		super(title);
		determineTabType(title);
		String resourcePath = "TextResources/" + title.substring(0,title.length() - 1) + "TabResources";
		actionTabResources = ResourceBundle.getBundle(resourcePath); 
		actionConditionManager = new ScrollPane();
		setContent(actionConditionManager);
		setUpActionConditionManager();
		//ControllerTopToolBar controllerTopToolBar = new ControllerTopToolBar(buttons,actionConditionVBox);
	}

	private void setUpActionConditionManager() {
		buttons = new TopToolBar(actionTabResources,"AddButtonLabel","Options","SelectorLabel","RemoveButtonLabel");
		actionConditionVBox = new ActionConditionVBox(actionTabResources.getString("SelectorLabel"),isConditionTab);
		VBox mainVBox = new VBox(SPACING);
		mainVBox.getChildren().addAll(buttons,actionConditionVBox);
		actionConditionManager.setContent(mainVBox);
	}
	
	protected void addTopToolBarListChangeListener(ListChangeListener<Integer> listChangeListener) {
		buttons.addRemoveRowVBoxListener(listChangeListener);
	}
	
	protected ObservableList<Integer> getCurrentActions() {
		return buttons.getRemoveRowVBoxOptions();
	}
	
	protected void setNewActionOptions(ObservableList<Integer> newActionOptions) {
		actionConditionVBox.setNewActionOptions(newActionOptions);
	}
	
	protected String getActionCondition() {
		return buttons.getOptionsValue();
	}
	
	protected void addCondition(String label,ObservableList<Integer> currentActions) {
		actionConditionVBox.addConditionAction(label,currentActions);
	}
	
	protected void addAction(String label) {
		actionConditionVBox.addAction(label);
	}
	
	protected void addRemoveOption() {
		buttons.addRemoveOption();
	}
	
	protected void removeActionCondtion(Integer row) {
		actionConditionVBox.removeActionCondition(row);
	}
	
	protected void removeRowOption(Integer row) {
		buttons.removeRemoveOption(row);
	}
	
	private void determineTabType(String title) {
		if(title.equals(Menu.conditionActionTitles.getString("ConditionsTabTitle"))) isConditionTab = true;
		else isConditionTab = false;
	}

	@Override
	public Integer getRemoveValue() {
		return buttons.getRemoveValue();
	}

	@Override
	public void addButtonListener(EventHandler<ActionEvent> e) {
		buttons.addButtonListener(e);
	}

	@Override
	public void addRemoveListener(EventHandler<ActionEvent> e) {
		buttons.addRemoveListener(e);
	}

	@Override
	public void addActionOption() {
		actionConditionVBox.addActionOption();
	}

	@Override
	public void removeActionOption(Integer action) {
		actionConditionVBox.removeActionOption(action);
	}
	
}