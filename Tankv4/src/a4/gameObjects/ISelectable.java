package a4.gameObjects;

import java.awt.Graphics;
import java.awt.Point;

public interface ISelectable {
	
	public void setSelected(boolean selected);
	
	public boolean isSelected();
	
	public boolean contains(Point p);
	
	public void draw(Graphics g);

}
