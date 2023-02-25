import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;



@SuppressWarnings("serial")
public class SelectingBox2D extends Rectangle2D.Float {
	
	private ArrayList<Shape> selectedShapes = new ArrayList<Shape>();
	int offset = 5;
	boolean menuPopped = false;
	
	public SelectingBox2D(int x1, int y1, int x2, int y2){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	  }
	
	public SelectingBox2D() {
		super(0,0,0,0);
	}

	public void remove(Shape s) {
		this.selectedShapes.remove(s);
	}
	
	public void addShape(Shape s) {
		this.selectedShapes.add(s);
	}
	
	public void clear() {
		this.selectedShapes.clear();
	}
	
	public ArrayList<Shape> getSelectedShapes(){
		return this.selectedShapes;
	}
	
	public boolean isEmptyArray(){
		return this.selectedShapes.isEmpty();
	}
	
	public void popMenu(){
		menuPopped = true;
	}
	
	public void unpopMenu(){
		menuPopped = false;
	}
	
	public boolean menuPopped(){
		return menuPopped;
	}
	
	
	public void resizeToFit() {
		if (!this.selectedShapes.isEmpty()) {
			int minX = 10000;
			int minY = 10000;
			int maxX = 0;
			int maxY = 0;
			int minWidth = 10000;
			int minHeight = 10000;
			for (Shape s : this.selectedShapes) {
				if ((s instanceof MyRectangle2D)) {
					minX = (int) Math.min(minX,  ((MyRectangle2D) s).getX());
					minY = (int) Math.min(minY,  ((MyRectangle2D) s).getY());
					maxX = Math.max(maxX,  ((int)((MyRectangle2D) s).getX())+(int)((MyRectangle2D) s).getWidth());
					maxY = Math.max(maxY,  ((int)((MyRectangle2D) s).getY())+(int)((MyRectangle2D) s).getHeight());
					minWidth = Math.abs(maxX - minX);
					minHeight = Math.abs(maxY - minY);
				}
				else if ((s instanceof MyEllipse2D)) {
					minX = (int) Math.min(minX,  ((MyEllipse2D) s).getX());
					minY = (int) Math.min(minY,  ((MyEllipse2D) s).getY());
					maxX = Math.max(maxX,  ((int)((MyEllipse2D) s).getX())+(int)((MyEllipse2D) s).getWidth());
					maxY = Math.max(maxY,  ((int)((MyEllipse2D) s).getY())+(int)((MyEllipse2D) s).getHeight());
					minWidth = Math.abs(maxX - minX);
					minHeight = Math.abs(maxY - minY);
				}
				else if ((s instanceof MyLine2D)) {
					minX = (int) Math.min(minX,  ((MyLine2D) s).getX1());
					minY = (int) Math.min(minY,  ((MyLine2D) s).getY1());
					minX = (int) Math.min(minX,  ((MyLine2D) s).getX2());
					minY = (int) Math.min(minY,  ((MyLine2D) s).getY2());
					maxX = Math.max(maxX,  ((int)((MyLine2D) s).getX1()));
					maxY = Math.max(maxY,  ((int)((MyLine2D) s).getY1()));
					maxX = Math.max(maxX,  ((int)((MyLine2D) s).getX2()));
					maxY = Math.max(maxY,  ((int)((MyLine2D) s).getY2()));
					minWidth = Math.abs(maxX - minX);
					minHeight = Math.abs(maxY - minY);
				}
			}
			this.x = minX-offset;
			this.y = minY-offset;
			this.width = minWidth+2*offset;
			this.height = minHeight+2*offset;
		}
	}
	
	public void move(float x, float y) {
		this.x = this.x + x;
		this.y = this.y + y;
	}

	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
}