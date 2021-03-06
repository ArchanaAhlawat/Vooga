package authoring_UI.cutscene;

import javafx.scene.control.Button;
import javafx.scene.control.TabPane;

public class CutsceneTabPane extends TabPane {

	private static final String DEFAULT = "DefaultDialogues";
	private static final String USER = "UserCreatedDialogues";
	private DefaultCutsceneTab defaultTab;
	private UserCutsceneTab userTab;

	public CutsceneTabPane() {
		
		defaultTab = new DefaultCutsceneTab(DEFAULT);
		userTab = new UserCutsceneTab(USER);
		this.getTabs().addAll(defaultTab, userTab);
	}
	
	public void addDefaultCutsceneButton(int number, Button btn) {
		defaultTab.addDisplayable(number, btn);
	}
	
	public void addUserCutsceneButton(int number, Button btn) {
		userTab.addDisplayable(number, btn);
	}

	public void removeUserCutsceneButton(int id) {
		userTab.deleteDisplayable(id);
	}
	
	public int getButtonIndex(Button btn) {
		return userTab.getButtonIndex(btn);
	}

}
