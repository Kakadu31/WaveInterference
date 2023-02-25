public class LineSource {
	private float x1;
	private float y1;
	private float x2;
	private float y2;
	private float frequency;
	private float phase;
	private float amplitude;
	private double zerfallsK = 0;
	private String zerfallsType = "exponential";
	
	public LineSource(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.frequency = 20;
		this.phase = 0;
		this.amplitude = (float) 0.5;
	}
	
	public LineSource(float amplitude, float frequency, float phase, float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.frequency = frequency;
		this.phase = phase;
		this.amplitude = amplitude;
	}
	
	public LineSource(float amplitude, float frequency, float phase, float x1, float y1, float x2, float y2, double zerfallsK, String zerfallsType) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.frequency = frequency;
		this.phase = phase;
		this.amplitude = amplitude;
		this.zerfallsK = zerfallsK;
		this.zerfallsType = zerfallsType;
	}
	
	public LineSource(double amplitude, double frequency, double phase, double x1, double y1, double x2, double y2, double zerfallsK, String zerfallsType) {
		this.x1 = (float) x1;
		this.y1 = (float) y1;
		this.x2 = (float) x2;
		this.y2 = (float) y2;
		this.frequency = (float) frequency;
		this.phase = (float) phase;
		this.amplitude = (float) amplitude;
		this.zerfallsK = zerfallsK;
		this.zerfallsType = zerfallsType;
	}
	
	public float getX1() {
		return x1;
	}
	
	public void setX1(float x) {
		this.x1 = x;
	}
	
	public float getY1() {
		return y1;
	}
	
	public void setY1(float y) {
		this.y1 = y;
	}
	
	public float getX2() {
		return x2;
	}
	
	public void setX2(float x) {
		this.x2 = x;
	}
	
	public float getY2() {
		return y2;
	}
	
	public void setY2(float y) {
		this.y2 = y;
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
		String str = amplitude + "," + frequency + "," + phase + ", " + x1/10 + "," + y1/10 + x2/10 + "," + y2/10;
		return str;
	}

	public String getZerfallsType() {
		return zerfallsType;
	}

	public void setZerfallsType(String zerfallsType) {
		this.zerfallsType = zerfallsType;
	}

	public double getZerfallsK() {
		return zerfallsK;
	}

	public void setZerfallsK(float zerfallsK) {
		this.zerfallsK = zerfallsK;
	}
}
