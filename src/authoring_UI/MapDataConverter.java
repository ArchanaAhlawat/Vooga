package authoring_UI;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;
import authoring.SpriteObjectGridManager;
import javafx.geometry.Point2D;

public class MapDataConverter {
	private final XStream SERIALIZER = setupXStream();
	private String myName;
	private String layerPath;
	
	public XStream setupXStream() {
		XStream xstream = new XStream(new DomDriver());
		xstream.addPermission(NullPermission.NULL);
		xstream.addPermission(PrimitiveTypePermission.PRIMITIVES);
		xstream.allowTypes(new Class[] { Point2D.class });
		xstream.allowTypesByWildcard(new String[] { "engine.**", "java.**" });
		return xstream;
	}
	
	public String getName(){
		return myName;
	}
	
	public MapDataConverter getToSerialize(){
		return this;
	}
	
	public MapDataConverter(DraggableGrid grids) {
		//convertLayer(grids);
	}
	
	public String getLayerPath() {
		return layerPath;
	}
	
	public void setLayerPath(String path) {
		layerPath = path;
	}
	
	public DraggableGrid createMap() {
		DraggableGrid newMap = new DraggableGrid();
		return newMap;
	}
}