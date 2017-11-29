package authoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteObject extends ImageView implements SpriteObjectI{
	
	private HashMap<String, ArrayList<SpriteParameterI>> categoryMap = new HashMap<String, ArrayList<SpriteParameterI>>();
	private ImageView myImageView;
	private String myImageURL;
	private Integer[] myPositionOnGrid;
	private String myName;
	private double myNumCellsWidth;
	private double myNumCellsHeight;
	private int myUniqueID;
	
	public SpriteObject() {
		super();
		setUniqueID();
	}

	public SpriteObject(String fileURL){
		this();
		setupImageURLAndView(fileURL);
		System.out.println(fileURL);
		myName = fileURL.split("\\.")[0];
//		myName = fileURL.split(".")[0];
	}
	
	public SpriteObject(HashMap<String, ArrayList<SpriteParameterI>> inCategoryMap) {
		this();
		categoryMap = new HashMap<String, ArrayList<SpriteParameterI>>(inCategoryMap);
	}
	
	SpriteObject(HashMap<String, ArrayList<SpriteParameterI>> inCategoryMap, String fileURL) {
		this();
		categoryMap = new HashMap<String, ArrayList<SpriteParameterI>>(inCategoryMap);
		setupImageURLAndView(fileURL);
	}
	
	private void setupImageURLAndView(String fileURL){
		myImageURL = fileURL;
		this.setImage(new Image(myImageURL));
		this.setFitWidth(45);
		this.setFitHeight(45);
	}
	
	private void setUniqueID() {
		if (myUniqueID <= 0) {
			myUniqueID = SpriteIDGenerator.getInstance().getUniqueID();
		}
	}
	
	private double getNumCellsWidth() {
		return myNumCellsWidth;
	}

	private void setNumCellsWidth(double in) {
		myNumCellsWidth = in;
	}

	private double getNumCellsHeight() {
		return myNumCellsHeight;
	}

	private void setNumCellsHeight(double in) {
		myNumCellsHeight = in;
	}

	private double[] getCenterCoordinates() {
		return new double[] { getXCenterCoordinate(), getYCenterCoordinate() };
	}


	public double getYCenterCoordinate() {
		double height = getNumCellsHeight();
		double ypos = getRowOnGrid();
		double centery = ypos + height / 2;
		return centery;
	}


	public double getXCenterCoordinate() {
		double width = getNumCellsWidth();
		double xpos = getColumnOnGrid();
		double centerx = xpos + width / 2;
		return centerx;
	}
	
	
	@Override
	public ImageView getImageView(){
		return myImageView;
	}
	
	@Override 
	public Integer[] getPositionOnGrid(){
		return myPositionOnGrid;
	}
	
	@Override 
	public void setPositionOnGrid(Integer[] pos){
		myPositionOnGrid = pos;
	}
	
	public int getRowOnGrid() {
		return getPositionOnGrid()[0];
	}

	public int getColumnOnGrid() {
		return getPositionOnGrid()[1];
	}
	
	@Override 
	public void setImageURL(String fileLocation){
		setupImageURLAndView(fileLocation);
	}
	
	@Override
	public void setName(String name){
		myName = name;
	}
	
	@Override
	public HashMap<String, ArrayList<SpriteParameterI>> getParameters() {	
		return categoryMap;
	}

	@Override
	public void addParameter(SpriteParameterI SP) {
		addParameter("General", SP);
		
	}
	
	public void addParameter(String category,SpriteParameterI SP){
		if (!categoryMap.containsKey(category)){
			categoryMap.put(category, new ArrayList<SpriteParameterI>());
		}
		
		ArrayList<SpriteParameterI> val = categoryMap.get(category);
		val.add(SP);
		categoryMap.put(category, val);
	}
	

	@Override
	public void applyParameterUpdate(HashMap<String, ArrayList<SpriteParameterI>> newParams) {
		categoryMap = new HashMap<String, ArrayList<SpriteParameterI>>(newParams);
	}

	@Override
	public boolean isSame(SpriteObject other){
		if (!(other instanceof SpriteObject)) {
	        return false;
	    }
		SpriteObject otherSO = (SpriteObject) other;
		System.out.println("Using custom equals method for Sprite Object");
		HashMap<String, ArrayList<SpriteParameterI>> otherMap = otherSO.getParameters();
		HashMap<String, ArrayList<SpriteParameterI>> thisMap = this.getParameters();
		for (String category: otherMap.keySet()){
			if (!thisMap.keySet().contains(category)) { return false;}
			ArrayList<SpriteParameterI> otherParamList = otherMap.get(category);
			ArrayList<SpriteParameterI> thisParamList = new ArrayList<SpriteParameterI>(thisMap.get(category));
			if (otherParamList.size() != thisParamList.size()){
				return false;
			}
			Iterator<SpriteParameterI> otherIt = otherParamList.iterator();
			while (otherIt.hasNext()){
				SpriteParameterI otherSPI = otherIt.next();
				Iterator<SpriteParameterI> thisIt = thisParamList.iterator();
				while (thisIt.hasNext()){
					SpriteParameterI thisSPI = thisIt.next();
					if (thisSPI.isSame(otherSPI)) {
						thisIt.remove();
						break;
					}
				}
			}
			if (thisParamList.size()>0){
				return false;
			}
		}
		return true;
	}
	
	private void replaceCategoryMap(HashMap<String, ArrayList<SpriteParameterI>> newParams) {
		categoryMap = new HashMap<String, ArrayList<SpriteParameterI>>(newParams);
	}
	
	@Override
	public SpriteObject newCopy(){
//		System.out.println("Making copy");
		SpriteObject ret = new SpriteObject();
		ret.setName(this.getName());
		ret.replaceCategoryMap(this.categoryMap);
		if (this.myImageURL != null) {
			ret.setupImageURLAndView(this.myImageURL);
		}
//		if(this.myImageURL!=null) {
//		return new SpriteObject(this.categoryMap, this.myImageURL);
//		} else {
//			return new SpriteObject(this.categoryMap);
//		}
		return ret;
	}
	
	private ArrayList<SpriteParameterI> getSpriteParametersMatching(String type) {
		ArrayList<SpriteParameterI> ret = new ArrayList<SpriteParameterI>();
		Class desiredClass;
		switch (type){
		case "Boolean":
			desiredClass = BooleanSpriteParameter.class;
			break;
		case "Double":
			desiredClass = DoubleSpriteParameter.class;
			break;
		case "String":
			desiredClass = StringSpriteParameter.class;
			break;
		default:
			desiredClass = SpriteParameter.class;
			break;
		}
		
		for (SpriteParameterI SPI: getAllParameters()){
			if (SPI.getClass().equals(desiredClass)){
				ret.add(SPI);
			}
		}
		return ret;
	}
	
	public ArrayList<String> getParameterNamesMatching(String type) {
		ArrayList<SpriteParameterI> concreteParameters = getSpriteParametersMatching(type); 
		ArrayList<String> ret = new ArrayList<String>();
		concreteParameters.forEach((item) -> {ret.add(item.getName());});
		return ret;
	}
	
	private ArrayList<SpriteParameterI> getAllParameters() {
		ArrayList<SpriteParameterI> ret = new ArrayList<SpriteParameterI>();
		for (ArrayList<SpriteParameterI> SPI_LIST: getParameters().values()){
			for(SpriteParameterI SPI: SPI_LIST){
				ret.add(SPI);
			}
		}
		return ret;
	}

	@Override
	public String getName() {
		return myName;
	}
	
	@Override
	public void changeCategoryName(String prev, String next) {
		getParameters().put(next, getParameters().remove(prev));
	}

	public String getImageURL() {
		return myImageURL;
	}
	
	
	
}
