package authoring.Sprite;

import javafx.scene.image.Image;

public class InventoryObject extends AbstractSpriteObject {
	
	public InventoryObject(){
		
	}
	
	public InventoryObject(Image im, String file){
		super(im, file);
	}
	
	public InventoryObject(String fileURL){
		super(fileURL);
	}

	public InventoryObject(boolean b) {
		super(b);
	}

	@Override
	public AbstractSpriteObject newCopy() {
		InventoryObject ret = new InventoryObject();
		if (this.myImageURL != null) {
			ret.setupImageURLAndView(this.myImageURL);
		}
		ret.setName(this.getName());
		ret.replaceCategoryMap(this.categoryMap);
		return ret;
	}

//	@Override
//	public void writeExternal(ObjectOutput out) throws IOException {
//		out.writeObject(categoryMap);
//	    out.writeObject(mySpriteObjectInventory);
//	    out.writeObject(myInventoryObjectInventory);
//	    out.writeObject(myImageURL);
//	    out.writeObject(myPositionOnGrid);
//	    out.writeObject(myName);
//	    out.writeObject(myNumCellsWidth);
//	    out.writeObject(myNumCellsHeight);
//	    out.writeObject(myUniqueID);
//	    out.writeObject(mySavePath);
//	    out.writeObject(myAnimationSequences);
//		
//	}
//
//	@Override
//	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//		categoryMap = (HashMap<String, ArrayList<SpriteParameterI>>)in.readObject();
//		mySpriteObjectInventory = (ArrayList<SpriteObject>)in.readObject();
//		myInventoryObjectInventory = (ArrayList<InventoryObject>)in.readObject();
//		myImageURL =(String)in.readObject();
//		myPositionOnGrid = (Integer[])in.readObject();
//		myName = (String) in.readObject();
//		myNumCellsWidth = (Integer) in.readObject();
//		myNumCellsHeight = (Integer) in.readObject();
//		myUniqueID = (String) in.readObject();
//		mySavePath = (String) in.readObject();
//		myAnimationSequences = (ArrayList<AnimationSequence>) in.readObject();	
//	}
//	


}
