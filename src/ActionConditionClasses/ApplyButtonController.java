package ActionConditionClasses;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import authoring.Sprite.AbstractSpriteObject;
import authoring_actionconditions.ActionConditionHBox;
import authoring_actionconditions.ActionRow;
import authoring_actionconditions.ActionTab;
import authoring_actionconditions.ActionTreeView;
import authoring_actionconditions.ActionVBox;
import authoring_actionconditions.ConditionRow;
import authoring_actionconditions.ConditionTab;
import authoring_actionconditions.ConditionTreeView;
import authoring_actionconditions.ConditionVBox;
import engine.Action;
import engine.Condition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ApplyButtonController {
	
	public static AbstractSpriteObject selectedSpriteObject;

	public void updateActionConditionTabs(ConditionTab<ConditionRow> conditionTab, ActionTab<ActionRow> actionTab,
			AbstractSpriteObject selectedSpriteObject) {
		
		ApplyButtonController.selectedSpriteObject = selectedSpriteObject;
		
		HashMap<ConditionTreeView, List<Integer>> conditions = selectedSpriteObject.getConditionTreeviews();
		List<ActionTreeView> actions = selectedSpriteObject.getActionTreeViews();
		ObservableList<Integer> allConditions = selectedSpriteObject.getAllConditions();
		ObservableList<Integer> allActions = selectedSpriteObject.getAllActions();
		List<String> selectedConditionOperations = selectedSpriteObject.getSelectedConditionOperations();
		List<List<String>> selectedActionOperations = selectedSpriteObject.getSelectedActionOperations();
		Map<Condition, List<Integer>> spriteConditions = selectedSpriteObject.getConditionRows();
		List<Action> spriteActions = selectedSpriteObject.getActionRows();
		ActionConditionHBox topToolBarConditions = new ActionConditionHBox(
				ResourceBundleUtil.getTabTitle("ConditionsTabTitle"),
				createObservableIntegerList(selectedConditionOperations.size()));
		ActionConditionHBox topToolBarActions = new ActionConditionHBox(
				ResourceBundleUtil.getTabTitle("ActionsTabTitle"),
				createObservableIntegerList(selectedActionOperations.size()));
		int rowCond = 1;
		List<ConditionRow> conditionRows = new LinkedList<ConditionRow>();
		ConditionVBox<ConditionRow> conditionVBox = new ConditionVBox<ConditionRow>();
		if (conditions == null) {
			Iterator<Condition> it = spriteConditions.keySet().iterator();
			ObservableList<Integer> actionOperations = createObservableIntegerList(spriteActions.size());
			while (it.hasNext()) {
				ConditionRow conditionRow = new ConditionRow(rowCond, actionOperations, spriteConditions.get(it.next()),
						conditionVBox, selectedConditionOperations.get(rowCond - 1), it.next());
				conditionRows.add(conditionRow);
				rowCond++;
			}
		} else {
			for (ConditionTreeView conditionTreeView : conditions.keySet()) {
				ConditionRow conditionRow = new ConditionRow(rowCond,
						createObservableIntegerList(selectedActionOperations.size()), conditions.get(conditionTreeView),
						conditionVBox, conditionTreeView);
				conditionRows.add(conditionRow);
				rowCond++;
			}
		}
		conditionVBox = new ConditionVBox<ConditionRow>(conditionRows);
		List<ActionRow> actionRows = new LinkedList<ActionRow>();
		ActionVBox<ActionRow> actionVBox = new ActionVBox<ActionRow>();
		int rowAct = 1;
		if (actions == null) {
			ActionRow actionRow = new ActionRow(rowAct, actionVBox, selectedActionOperations.get(rowAct - 1),
					spriteActions.get(rowAct - 1));
			actionRows.add(actionRow);
			rowAct++;
		} else {
			for (ActionTreeView actionTreeView : actions) {
				ActionRow actionRow = new ActionRow(rowAct, actionVBox, actionTreeView);
				actionRows.add(actionRow);
				rowAct++;
			}
		}
		actionVBox = new ActionVBox<ActionRow>(actionRows);
		conditionTab.setTopToolBar(topToolBarConditions);
		conditionTab.setNoReturnActionConditionVBox(conditionVBox);
		actionTab.setTopToolBar(topToolBarActions);
		actionTab.setNoReturnActionConditionVBox(actionVBox);
	}

	public void updateSpriteObject(ConditionTab<ConditionRow> conditionTab, ActionTab<ActionRow> actionTab,
			AbstractSpriteObject spriteObject) {
		try {
			HashMap<ConditionTreeView, List<Integer>> conditions = new HashMap<ConditionTreeView, List<Integer>>();
			List<String> selectedConditionOperations = new LinkedList<String>();
			conditionTab.getActionConditionVBox().getRows().forEach(row -> {

				conditions.put(row.getTreeView(), row.getSelectedActions());
				selectedConditionOperations.add(row.getTreeView().getSelectedOperation());

			});
			List<ActionTreeView> actions = new LinkedList<ActionTreeView>();
			List<List<String>> selectedActionOperations = new LinkedList<List<String>>();
			actionTab.getActionConditionVBox().getRows().forEach(row -> {
				actions.add(row.getTreeView());
				List<String> treeViewParams = new LinkedList<String>();
				treeViewParams.add(row.getTreeView().getCategoryName());
				treeViewParams.add(row.getTreeView().getActionName());
				selectedActionOperations.add(treeViewParams);
			});
			spriteObject.setAllConditions(conditionTab.getTopToolBar().getRemoveRowVBoxOptions());
			spriteObject.setAllActions(actionTab.getTopToolBar().getRemoveRowVBoxOptions());
			spriteObject.setSelectedConditionOperations(selectedConditionOperations);
			spriteObject.setSelectedConditionOperations(selectedConditionOperations);
			spriteObject.setSelectedActionOperations(selectedActionOperations);
			spriteObject.setConditions(conditions);
			spriteObject.setActions(actions);
		} catch (NullPointerException | NumberFormatException e) {
			conditionTab.displayRowExceptionMessage(e.getMessage());
		}
	}

	private ObservableList<Integer> createObservableIntegerList(int size) {
		ObservableList<Integer> ret = FXCollections.observableArrayList();
		for (int i = 1; i <= size; i++) {
			ret.add(i);
		}
		return ret;
	}
	
	public static AbstractSpriteObject getSelectedSpriteObject() {
		return selectedSpriteObject;
	}

}
