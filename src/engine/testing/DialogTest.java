package engine.testing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import engine.Action;
import engine.Condition;
import engine.DialogSequence;
import engine.GameLayer;
import engine.GameMaster;
import engine.GameObject;
import engine.GameObjectFactory;
import engine.GameWorld;
import engine.Actions.changeObject.RemoveFromWorld;
import engine.Actions.dialog.ClearTyped;
import engine.Actions.dialog.DisplayDialog;
import engine.Actions.dialog.PlaceTextOn;
import engine.operations.booleanops.BooleanValue;
import engine.operations.booleanops.KeyHeld;
import engine.operations.booleanops.KeyReleased;
import engine.operations.doubleops.Value;
import engine.operations.gameobjectops.Get;
import engine.operations.gameobjectops.Self;
import engine.operations.stringops.SelfString;
import engine.operations.stringops.TypedString;
import engine.operations.vectorops.BasicVector;
import engine.operations.vectorops.LocationOf;
import engine.sprite.AnimationSequence;
import engine.sprite.BoundedImage;
import engine.sprite.DisplayableText;
import engine.sprite.Positionable;
import engine.sprite.PositionableObject;
import engine.sprite.Sprite;
import engine.utilities.data.GameDataHandler;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class DialogTest extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage s) throws IOException {
		generateGame(s, "Dialog test");
	}

	public void generateGame(Stage stage, String name) throws IOException {		
		DisplayableText text = new DisplayableText(0,
				"A string of text",
				"Comic Sans", 12, "#008000");
		Positionable p = new PositionableObject();
		p.setPosition(-.5, 0);
		p.setSize(.2, .2);
		text.setRelativePosition(p);

		DisplayableText text2 = new DisplayableText(0,
				"another string of text",
				"Comic Sans", 12, "#008000");
		p = new PositionableObject();
		p.setPosition(.3, .3);
		p.setSize(.2, .2);
		text2.setRelativePosition(p);
		
		List<DisplayableText> texts = new ArrayList<>();
		texts.add(text);
		texts.add(text2);
		
		text = new DisplayableText(
				"Text 3",
				"Comic Sans", 12, "#008000");
		p = new PositionableObject();
		p.setPosition(0, -.5);
		p.setSize(.5, .2);
		text.setRelativePosition(p);

		text2 = new DisplayableText(
				"another string of text 4",
				"Comic Sans", 12, "#008000");
		p = new PositionableObject();
		p.setPosition(.4, .5);
		p.setSize(.5, .2);
		text2.setRelativePosition(p);
		
		List<DisplayableText> texts2 = new ArrayList<>();
		texts2.add(text);
		texts2.add(text2);
		
		GameObjectFactory fact = new GameObjectFactory();
		
		GameDataHandler gdh = new GameDataHandler(name);
		File f = gdh.chooseFile(stage);
		gdh.addFileToProject(f);
		GameObject object = makeObject("Box", new BoundedImage(f.getName()),
				500, 500, this::condAct);
		object.setSize(400, 400);
		
		DialogSequence s = new DialogSequence("Sequence1", f.getName(), texts);
		s.setSize(400, 400);
		DialogSequence s2 = new DialogSequence("Sequence2", f.getName(), texts2);
		s2.setSize(400, 400);
		s.setNextDialog(s2);
		fact.addBlueprint(s);
		fact.addBlueprint(s2);
		fact.addBlueprint(object);

		//object.setDialogue(texts);
		
		GameLayer l = new GameLayer("Layer");
		l.addGameObject(object);

		GameWorld w = new GameWorld("World");
		w.addLayer(l);

		GameMaster master = new GameMaster();
		master.addWorld(w);
		master.setNextWorld("World");
		master.addBlueprints(fact);
		gdh.saveGame(master);
	}

	private GameObject makeObject(String name, BoundedImage i, double x, double y, Consumer<GameObject> condActGen) {
		GameObject obj = new GameObject(name);
		obj.setLocation(x, y);
		condActGen.accept(obj);
		Sprite sprite = new Sprite();
		List<BoundedImage> images = new ArrayList<>();
		images.add(i);
		AnimationSequence animation = new AnimationSequence("Animation", images);
		sprite.addAnimationSequence(animation);
		sprite.setAnimation("Animation");
		obj.setSprite(sprite);
		return obj;
	}

	private void deletable(GameObject object) {
		ArrayList<Action> actions1 = new ArrayList<Action>();
		actions1.add(new RemoveFromWorld(new Get(object)));
		object.addConditionAction(new Condition(100, new KeyHeld(new SelfString("L"))), actions1);
		
		actions1 = new ArrayList<Action>();
		actions1.add(new ClearTyped());
		object.addConditionAction(new Condition(3, new KeyHeld(new SelfString(KeyCode.ESCAPE.getName()))), actions1);
		
		actions1 = new ArrayList<Action>();
		actions1.add(new PlaceTextOn(new Self(), new TypedString()));
		object.addConditionAction(new Condition(4, new BooleanValue(true)), actions1);
		
	}
	
	private void condAct(GameObject object) {
//		List<Action> actions1 = new ArrayList<Action>();
//		actions1.add(new SetAcceleration(new Self(), new UnitVector(new VectorDifference(new MouseLocation(),new LocationOf(new Self())))));
//		object.addConditionAction(new Condition(1, new BooleanValue(true)), actions1);
//		
		ArrayList<Action> actions1 = new ArrayList<Action>();
		actions1.add(new DisplayDialog(new SelfString("Sequence1"), new BasicVector(new Value(200), new Value(200))));
		object.addConditionAction(new Condition(2, new KeyReleased(new SelfString("D"))), actions1);
	}
}
