package authoring_actionconditions;

import java.util.List;

public class ActionVBox<T> extends ActionConditionVBox<T> implements ActionVBoxI<T> {

	public ActionVBox(String selectorString) {
		super(selectorString);
	}
	
	public ActionVBox(String selectorString,List<T> rows) {
		super(selectorString,rows);
	}

	@Override
	public void addAction(String label) {
		ActionRow actionRow = new ActionRow(getRows().size() + 1, (ActionVBox<ActionRow>) this);
		addToRows(actionRow);
		BuildActionView bav = new BuildActionView(this, actionRow);
	}
	

}
