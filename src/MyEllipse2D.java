import java.awt.geom.Ellipse2D;
import java.awt.Color;

@SuppressWarnings("serial")
public class MyEllipse2D extends Ellipse2D.Float {
	
	private double speed;
	private boolean isFilled;
	private Color color;
	private boolean isSelected;
	private float amplitude = 1;
	private double frequency = 0.11814;
	private float phase = 0;
	private double zerfallsK = 0.0005;
	private String zerfallsType = "exponential";
	
	public MyEllipse2D(float x1, float y1, float x2, float y2){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.isFilled = true;
	    this.isSelected = true;
	  }
	
	public MyEllipse2D(float x1, float y1, float x2, float y2, float amplitude, double randomFreq, float phase){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.amplitude = amplitude;
	    this.frequency = randomFreq;
	    this.phase = phase;
	    this.isFilled = true;
	    this.isSelected = true;
	  }
	
	public MyEllipse2D(float x1, float y1, float x2, float y2, float amplitude, double freq, float phase, double zerfallsK, String zerfallsType){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.amplitude = amplitude;
	    this.frequency = freq;
	    this.phase = phase;
	    this.isFilled = true;
	    this.isSelected = true;
	    this.zerfallsK = zerfallsK;
	    this.zerfallsType = zerfallsType;
	  }
	
	public MyEllipse2D(float x1, float y1, float x2, float y2, boolean isFilled){
	    super(x1,y1,x2,y2); //calls the constructor in the parent class to initialize the name
	    this.isFilled = isFilled;
	    this.isSelected = true;
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

	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
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
	
	public void move(float x, float y) {
		this.x = this.x + x;
		this.y = this.y + y;
	}
	
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
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

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
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