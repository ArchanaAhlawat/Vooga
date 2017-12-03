package authoring_UI;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

import authoring.AbstractSpriteObject;
import authoring.InventoryObject;
import authoring.SpriteObject;
import authoring.SpriteParameterI;
import javafx.geometry.Point2D;

public class SpriteDataConverter {

	private static final XStream SERIALIZER = setupXStream();

	private static XStream setupXStream() {
		XStream xstream = new XStream(new DomDriver());
		// xstream.addPermission(NoTypePermission.NONE);
		xstream.addPermission(NullPermission.NULL);
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xstream.allowTypes(new Class[] { Point2D.class });
		xstream.allowTypesByWildcard(new String[] { "engine.**", "java.**" });
		return xstream;
	}

	HashMap<String, ArrayList<SpriteParameterI>> catmap;
	ArrayList<SpriteDataConverter> inventory;
	Integer[] gridPos;
	String name;
	Integer width;
	Integer height;
	String UUID;
	String imageURL;
	String mySavePath;
	String spriteType;

	public SpriteDataConverter(AbstractSpriteObject ASO) {
		convertSprite(ASO);
	}

	public String getName() {
		return name;
	}

	public SpriteObject getSprite(File file) {

		return null;
	}

	public SpriteDataConverter getToSerialize() {
		return this;
	}

	public void setPath(String path) {
		mySavePath = path;
	}

	public void convertSprite(AbstractSpriteObject ASO) {
		catmap = ASO.getParameters();
		gridPos = ASO.getPositionOnGrid();
		name = ASO.getName();
		width = ASO.getNumCellsWidth();
		height = ASO.getNumCellsHeight();
		UUID = ASO.getUniqueID();
		imageURL = ASO.getImageURL();
		mySavePath = ASO.getSavePath();
		inventory = new ArrayList<SpriteDataConverter>();
		ASO.getInventory().forEach(sprite -> {
			inventory.add(new SpriteDataConverter(sprite));
		});

		if (ASO instanceof SpriteObject) {
			spriteType = "SpriteObject";
		} else if (ASO instanceof InventoryObject) {
			spriteType = "InventoryObject";
		}
	}

	public AbstractSpriteObject createSprite() {
		// SpriteObject ret = new SpriteObject(true);
		AbstractSpriteObject ret;
		if (spriteType.equals("SpriteObject")) {
			ret = new SpriteObject(true);
		} else if (spriteType.equals("InventoryObject")) {
			ret = new InventoryObject(true);
		} else {
			// Default
			ret = new SpriteObject(true);
		}

		ret.setImageURL(imageURL);
		ret.setParameterMap(catmap);
		ret.setPositionOnGrid(gridPos);
		ret.setNumCellsHeight(height);
		ret.setNumCellsWidth(width);
		ret.setUniqueID(UUID);
		ret.setName(name);
		ret.setSavePath(mySavePath);
		ArrayList<AbstractSpriteObject> newInventory = new ArrayList<AbstractSpriteObject>();
		inventory.forEach(SDC ->{
			newInventory.add(SDC.createSprite());
		});
		ret.setInventory(newInventory);
		System.out.println("spriteInventoryinSDC: "+ret.getInventory());
		return ret;

	}
}
