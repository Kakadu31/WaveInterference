public class PointSource {
	private float x;
	private float y;
	private float frequency;
	private float phase;
	private float amplitude;
	private double zerfallsK = 0;
	private String zerfallsType = "exponential";
	
	public PointSource(float x, float y) {
		this.x = x;
		this.y = y;
		this.frequency = 20;
		this.phase = 0;
		this.amplitude = (float) 0.5;
	}
	
	public PointSource(float amplitude, float frequency, float phase, float x, float y) {
		this.x = x;
		this.y = y;
		this.frequency = frequency;
		this.phase = phase;
		this.amplitude = amplitude;
	}
	
	public PointSource(double amplitude, double frequency, double phase, double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
		this.frequency = (float) frequency;
		this.phase = (float) phase;
		this.amplitude = (float) amplitude;
	}
	
	public PointSource(double amplitude, double frequency, double phase, double x, double y, double zerfallsK, String zerfallsType) {
		this.x = (float) x;
		this.y = (float) y;
		this.frequency = (float) frequency;
		this.phase = (float) phase;
		this.amplitude = (float) amplitude;
		this.zerfallsK = zerfallsK;
		this.zerfallsType = zerfallsType;
	}

	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getFrequency() {
		return frequency;
	}
	
	public void setFrequency(float frequency) {
		this.frequency = frequency;
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
	
	public String toString() {
		String str = amplitude + "," + frequency + "," + phase + ", " + x/10 + "," + y/10;
		return str;
	}

	public double getZerfallsK() {
		return zerfallsK;
	}

	public void setZerfallsK(double d) {
		this.zerfallsK = d;
	}

	public String getZerfallsType() {
		return zerfallsType;
	}

	public void setZerfallsType(String zerfallsType) {
		this.zerfallsType = zerfallsType;
	}
}
