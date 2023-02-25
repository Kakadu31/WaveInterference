import java.awt.geom.Line2D;
import java.awt.Color;

@SuppressWarnings("serial")
public class MyLine2D extends Line2D.Float {
	
	private double speed;
	private Color color;
	private boolean isSelected;
	private float amplitude = 1;
	private double frequency = 0.11814;
	private float phase = 0;
	private double zerfallsK = 0.0005;
	private String zerfallsType = "exponential";
	
	public MyLine2D(float x1, float y1, float x2, float y2){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.speed = -1;
	    this.isSelected = true;
	  }
	
	public MyLine2D(float x1, float y1, float x2, float y2, float amplitude, double frequency, float phase){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.amplitude = amplitude;
	    this.frequency = frequency;
	    this.phase = phase;
	    this.speed = -1;
	    this.isSelected = true;
	  }
	
	public MyLine2D(float x1, float y1, float x2, float y2, float amplitude, double frequency, float phase, double zerfallsK, String zerfallsType){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.amplitude = amplitude;
	    this.frequency = frequency;
	    this.phase = phase;
	    this.speed = -1;
	    this.isSelected = true;
	    this.zerfallsK = zerfallsK;
	    this.zerfallsType = zerfallsType;
	  }
	
	public MyLine2D(float x1, float y1, float x2, float y2, int speed){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.speed = speed;
	    this.isSelected = true;
	  }  
	

	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public Color getColor() {
		return color;
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
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void move(int x, int y) {
		this.x1 = this.x1 + x;
		this.x2 = this.x2 + x;
		this.y1 = this.y1 + y;
		this.y2 = this.y2 + y;
	}
	
	public void moveTo(int x, int y) {
		float xdiff = this.x2-this.x1;
		float ydiff = this.y2-this.y1;
		this.x2 = xdiff + x;
		this.y2 = ydiff + y;
		this.x1 = x;
		this.y1 = y;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public float getPhase() {
		return phase;
	}

	public void setPhase(float phase) {
		this.phase = phase;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(float frequency) {
		this.frequency = frequency;
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