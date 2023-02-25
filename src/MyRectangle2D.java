import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MyRectangle2D extends Rectangle2D.Float {
	
	private boolean isSelected;
	private boolean isFilled;
	private Color color;
	private int hatch = 0;
	private float amplitude = 1;
	private double frequency = 0.11814;
	private float phase = 0;
	private double zerfallsK = 0.0005;
	private String zerfallsType = "exponential";
	
	public MyRectangle2D(int x1, int y1, int x2, int y2){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.isSelected = true;
	    this.isFilled = false;
	  }
	
	public MyRectangle2D(double x1, double y1, double x2, double y2){
	    super((float) x1, (float) y1, (float) x2, (float) y2); //calls the constructor in the parent class to initialize the name
	    this.isSelected = true;
	    this.isFilled = false;
	  }
	
	public MyRectangle2D(double x1, double y1, double x2, double y2, float amplitude, float frequency, float phase){
	    super((float) x1, (float) y1, (float) x2, (float) y2); //calls the constructor in the parent class to initialize the name
	    this.amplitude = amplitude;
	    this.frequency = frequency;
	    this.phase = phase;
	    this.isSelected = true;
	    this.isFilled = false;
	  }
	
	public MyRectangle2D(double x1, double y1, double x2, double y2, float amplitude, double frequency, float phase, double zerfallsK, String zerfallsType){
	    super((float) x1, (float) y1, (float) x2, (float) y2); //calls the constructor in the parent class to initialize the name
	    this.amplitude = amplitude;
	    this.frequency = frequency;
	    this.phase = phase;
	    this.isSelected = true;
	    this.isFilled = false;
	    this.zerfallsK = zerfallsK;
	    this.zerfallsType = zerfallsType;
	  }
	
	
	public MyRectangle2D(int x1, int y1, int x2, int y2, boolean isFilled){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.isSelected = true;
	    this.isFilled = isFilled;
	  }
	
	public ArrayList<MyLine2D> toLines() {
		ArrayList<MyLine2D> lines = new ArrayList<MyLine2D>();
		if ((height == 0)||(width ==0)) {
			return lines;
		}
       	MyLine2D l1 = new MyLine2D(this.x, this.y, this.x+width, this.y, this.amplitude, this.frequency, this.phase);
       	MyLine2D l2 = new MyLine2D(this.x, this.y, this.x, this.y+height, this.amplitude, this.frequency, this.phase);
       	MyLine2D l3 = new MyLine2D(this.x+width, this.y+height, this.x+width, y, this.amplitude, this.frequency, this.phase);
       	MyLine2D l4 = new MyLine2D(this.x+width, this.y+height, this.x, y+height, this.amplitude, this.frequency, this.phase);
       	lines.add(l1);
       	lines.add(l2);
       	lines.add(l3);
       	lines.add(l4);
        return lines;
      	}
	
	public boolean isFilled() {
		return isFilled;
	}
	
	public void fill() {
		isFilled = true;
	}
	
	public void unFill() {
		isFilled = false;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void select() {
		isSelected = true;
	}
	
	public void unSelect() {
		isSelected = false;
	}

	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void move(float x, float y) {
		this.x = this.x + x;
		this.y = this.y + y;
	}
	
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public int getHatch() {
		return hatch;
	}

	public void setHatch(int hatch) {
		this.hatch = hatch;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}

	public float getPhase() {
		return phase;
	}

	public void setPhase(float phase) {
		this.phase = phase;
	}

	public double getZerfallsK() {
		return zerfallsK;
	}

	public void setZerfallsK(float zerfallsK) {
		this.zerfallsK = zerfallsK;
	}

	public String getZerfallsType() {
		return zerfallsType;
	}

	public void setZerfallsType(String zerfallsType) {
		this.zerfallsType = zerfallsType;
	}
	
}