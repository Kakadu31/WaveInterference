import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class SimulationPanel extends JPanel {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  Double[][] colorMap = {{1.00, 0.00, 0.00},{1.00, 0.05, 0.00},{1.00, 0.10, 0.00},{1.00, 0.15, 0.00},{1.00, 0.20, 0.00},{1.00, 0.25, 0.00},{1.00, 0.30, 0.00},{1.00, 0.35, 0.00},{1.00, 0.40, 0.00},{1.00, 0.45, 0.00},{1.00, 0.50, 0.00},{1.00, 0.55, 0.00},{1.00, 0.60, 0.00},{1.00, 0.65, 0.00},{1.00, 0.70, 0.00},{1.00, 0.75, 0.00},{1.00, 0.80, 0.00},{1.00, 0.85, 0.00},{1.00, 0.90, 0.00},{1.00, 0.95, 0.00},
            {1.00, 1.00, 0.00},{0.95, 1.00, 0.00},{0.90, 1.00, 0.00},{0.85, 1.00, 0.00},{0.80, 1.00, 0.00},{0.75, 1.00, 0.00},{0.70, 1.00, 0.00},{0.65, 1.00, 0.00},{0.60, 1.00, 0.00},{0.55, 1.00, 0.00},{0.50, 1.00, 0.00},{0.45, 1.00, 0.00},{0.40, 1.00, 0.00},{0.35, 1.00, 0.00},{0.30, 1.00, 0.00},{0.25, 1.00, 0.00},{0.20, 1.00, 0.00},{0.15, 1.00, 0.00},{0.10, 1.00, 0.00},{0.05, 1.00, 0.00},
            {0.00, 1.00, 0.00},{0.00, 1.00, 0.05},{0.00, 1.00, 0.10},{0.00, 1.00, 0.15},{0.00, 1.00, 0.20},{0.00, 1.00, 0.25},{0.00, 1.00, 0.30},{0.00, 1.00, 0.35},{0.00, 1.00, 0.40},{0.00, 1.00, 0.45},{0.00, 1.00, 0.50},{0.00, 1.00, 0.55},{0.00, 1.00, 0.60},{0.00, 1.00, 0.65},{0.00, 1.00, 0.70},{0.00, 1.00, 0.75},{0.00, 1.00, 0.80},{0.00, 1.00, 0.85},{0.00, 1.00, 0.90},{0.00, 1.00, 0.95},
            {0.00, 1.00, 1.00}};
    
    public double[][] matrixData;
    
    private double lineFactor = 0.50;
    private double linePointFactor = 1-lineFactor;
    private double oldFactor = 0;
    private double rMaxGauss = 0;
    private double minAmplitude = 0.2;
    private int filterBlocksize = 51;
    public Timer myTimer;
    public Timer iterationTimer;
    public int fps = 10;
	double waveSpeed = 0.0628505;
    public static boolean isRunning = false;
    public boolean colored = false;
    public boolean normiert = false;
	boolean decayErrorFlag = false;
    public String [] iterationsData = new String[7];
    public int iterationsDone = 0;
    public double maxAmplitude = 0.0;
    public static int areaSize = 10000;  //10 um x 10 um
    public static int resolution = 10; //10 nm pro pixel
    public int angle = 0;
    public int anglePlus = 10;
    int rectWidth = getWidth() / areaSize;
    int rectHeight = getHeight() / areaSize;
    public boolean saveImage = false;
    public boolean saveFrames = false;
    public boolean iterate = false;
    public String[] saveFramesInfo = {"","",""};
    public int framecounter = 0;
    BufferedImage[] frames = new BufferedImage[360/anglePlus];
    ArrayList<PointSource> pointsPassed = new ArrayList<PointSource>();
    ArrayList<LineSource> linesPassed = new ArrayList<LineSource>();
    ArrayList<PointSource> pointsIlluminated = new ArrayList<PointSource>();
    ArrayList<LineSource> linesIlluminated = new ArrayList<LineSource>();

    public SimulationPanel(){
        this.matrixData = new double[SimulationPanel.areaSize/resolution][SimulationPanel.areaSize/resolution];
        // InitializeMatrix with zeros
        for (int i = 0; i < (areaSize/resolution); i++) {
            for (int j = 0; j < (areaSize/resolution); j++) {
                this.matrixData[i][j] = 0;
            }
        }
        int preferredWidth = 1000;
        int preferredHeight = 1000;
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        
        final SimulationPanel simulationPanel = this;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	simulationPanel.updateA(simulationPanel.getGraphics());
            }
          };
          ActionListener taskPerformerIteration = new ActionListener() {
              public void actionPerformed(ActionEvent evt) {
              	simulationPanel.updateIteration(simulationPanel.getGraphics());
              }
            };
          
          this.myTimer = new Timer(1000/fps, taskPerformer);
          this.myTimer.start();
          this.iterationTimer = new Timer(10000, taskPerformerIteration);
    }

    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);
        // Clear the board
        g.clearRect(0, 0, getWidth(), getHeight());
        // Draw the grid
        rectWidth = getWidth() / (areaSize/resolution);
        rectHeight = getHeight() / (areaSize/resolution);
        
        Color currentColor = Color.black;
        
        
        for (int i = 0; i < (areaSize/resolution); i++) {
            for (int j = 0; j < (areaSize/resolution); j++) {
                // Upper left corner of this terrain rect
                int x = i * rectWidth;
                int y = j * rectHeight;
                if(iterationsDone != 0) {
                	double height = matrixData[i][j];
                    currentColor = getColorA(height);
                    g.setColor(currentColor);
                    g.fillRect(x, y, rectWidth, rectHeight);
                }
                else {
                    double height = 0.0;
                    for (PointSource ps : pointsPassed) {
                    	height = height  + getHeight(i,j,ps);
                    }
                    for (LineSource ls : linesPassed) {
                    	height = height  + getHeight(i,j,ls);
                    }
                    currentColor = getColorA(height);
                    g.setColor(currentColor);
                    g.fillRect(x, y, rectWidth, rectHeight);
                    if (normiert) {
                    	matrixData[i][j] = height;
                }
                }
        }
        
        if (saveImage){
        	saveImage = false;
        	saveImage(saveFramesInfo[0], saveFramesInfo[1],saveFramesInfo[2]);
        }
        else if (saveFrames){
        	saveFrames = false;
        	saveFrames(saveFramesInfo[0], saveFramesInfo[1],saveFramesInfo[2]);
        	saveFrames = true;
        }
        }
    }
    
    private void upDateSources(org.opencv.core.Point[] newPointSources, Line2D[] newLineSources, org.opencv.core.Point[] newLinePointSources, int maxSources) {
    	System.out.println("Im on updatesources()");
		pointsPassed.clear();
		linesPassed.clear();
		Random random = new Random();
		double wavelength = 0.513;								//Berechnet aus Luft + geschmolzenes Si
		double wavelengthDev = wavelength*0.01;  				//1% Fehler
		double phase = 0;										//Korrigiert auf Höchste Amplitude am Ursprung wäre 90
		double phaseDev =  0;									//Gehen wir davon ausalle Wellen entstehen gleichzeitig
		double freq = waveSpeed/wavelength;
		double freqDev = (waveSpeed*wavelengthDev)/(wavelength*wavelength);
		double zerfallsK = 1/(503);							//Zerfall entspricht gerechnetem Wert für SPPs  19.3 um
		String zerfallsType = "exponential";
    	double randomFreq;
		double randomPhase;
		double amplitude;
		for (org.opencv.core.Point source : newPointSources) {
			//Wird die Quelle überhaupt bestrahlt?
		   		if (freqDev != 0) {
					randomFreq = freq + random.nextFloat() * (2*freqDev) - freqDev;
		   			//randomWavelength = wavelength + random.nextDouble() * (2*wavelengthDev) - wavelengthDev;
		   			//randomFreq = waveSpeed*randomWavelength;
				}
				else {
					randomFreq = freq;
				}
				if (phaseDev != 0) {
					randomPhase = phase + random.nextFloat() * (2*phaseDev) - phaseDev; 
				}
				else {
					randomPhase = phase;
				}
				amplitude = meanFilter((int) source.x, (int) source.y, matrixData, filterBlocksize)/maxAmplitude;
				if(amplitude < minAmplitude) {
					amplitude = minAmplitude;
				}
				//amplitude = (Math.abs(matrixData[(int) source.x][(int) source.y])/maxAmplitude);
				if (amplitude > 0) {
					double offset = 0;
					if(Boolean.valueOf(iterationsData[6])){
						double nmPerIteration = (areaSize + Double.parseDouble(iterationsData[5]))/Double.parseDouble(iterationsData[0]);
						offset = ((areaSize) + (Double.parseDouble(iterationsData[5])/2) - (iterationsDone*nmPerIteration))/resolution;
					}
			    	double xDifference = ((areaSize/(2*resolution) + offset) - source.x);
			    	double yDifference = (areaSize/(2*resolution) - source.y);
					amplitude = amplitudeGauss(amplitude,Math.sqrt(Math.pow(xDifference,2) + Math.pow(yDifference,2))*resolution);
					PointSource newSource = new PointSource(amplitude, randomFreq, randomPhase, source.x, source.y, zerfallsK, zerfallsType);
					pointsPassed.add(newSource);
				}
		}
		for (org.opencv.core.Point source : newLinePointSources) {
		   		if (freqDev != 0) {
					randomFreq = freq + random.nextFloat() * (2*freqDev) - freqDev;
		   			//randomWavelength = wavelength + random.nextDouble() * (2*wavelengthDev) - wavelengthDev;
		   			//randomFreq = waveSpeed*randomWavelength;
				}
				else {
					randomFreq = freq;
				}
				if (phaseDev != 0) {
					randomPhase = phase + random.nextFloat() * (2*phaseDev) - phaseDev; 
				}
				else {
					randomPhase = phase;
				}
				amplitude = linePointFactor*(meanFilter((int) source.x, (int) source.y, matrixData, filterBlocksize)/maxAmplitude);
				if(amplitude < minAmplitude) {
					amplitude = minAmplitude;
				}
				if (amplitude > 0) {
					double offset = 0;
					if(Boolean.valueOf(iterationsData[6])){
						double nmPerIteration = (areaSize + Double.parseDouble(iterationsData[5]))/Double.parseDouble(iterationsData[0]);
						offset = ((areaSize) + (Double.parseDouble(iterationsData[5])/2) - (iterationsDone*nmPerIteration))/resolution;
					}
			    	double xDifference = ((areaSize/(2*resolution)+ offset) - source.x);
			    	double yDifference = (areaSize/(2*resolution) - source.y);
					amplitude = amplitudeGauss(amplitude,Math.sqrt(Math.pow(xDifference,2) + Math.pow(yDifference,2))*resolution);
					PointSource newSource = new PointSource(amplitude, randomFreq, randomPhase, source.x, source.y, zerfallsK, zerfallsType);
					pointsPassed.add(newSource);
				}
		}
		for (Line2D source : newLineSources) {
		   		if (freqDev != 0) {
					randomFreq = freq + random.nextFloat() * (2*freqDev) - freqDev;
		   			//randomWavelength = wavelength + random.nextDouble() * (2*wavelengthDev) - wavelengthDev;
		   			//randomFreq = waveSpeed*randomWavelength;
				}
				else {
					randomFreq = freq;
				}
				if (phaseDev != 0) {
					randomPhase = phase + random.nextFloat() * (2*phaseDev) - phaseDev; 
				}
				else {
					randomPhase = phase;
				}
				amplitude = lineFactor*meanFilter((int) (source.getP1().getX()+source.getP2().getX())/2, (int) (source.getP1().getY()+source.getP2().getY())/2, matrixData, filterBlocksize)/maxAmplitude;
				//amplitude = linienabschwaechung*((Math.abs(matrixData[(int) (source.getP1().getX()+source.getP2().getX())/2][(int) (source.getP1().getY()+source.getP2().getY())/2]))/maxAmplitude);
				if(amplitude < minAmplitude) {
					amplitude = minAmplitude;
				}
				if (amplitude > 0) {
					double offset = 0;
					if(Boolean.valueOf(iterationsData[6])){
						double nmPerIteration = (areaSize + Double.parseDouble(iterationsData[5]))/Double.parseDouble(iterationsData[0]);
						offset = ((areaSize) + (Double.parseDouble(iterationsData[5])/2) - (iterationsDone*nmPerIteration))/resolution;
					}
			    	double xDifference = ((areaSize/(2*resolution) + offset) - (source.getP1().getX()+source.getP2().getX())/2);
			    	double yDifference = (areaSize/(2*resolution) - (source.getP1().getY()+source.getP2().getY())/2);
					amplitude = amplitudeGauss(amplitude,Math.sqrt(Math.pow(xDifference,2) + Math.pow(yDifference,2))*resolution);
					LineSource newSource = new LineSource(amplitude, randomFreq, randomPhase, source.getP1().getX(), source.getP1().getY(), source.getP2().getX(), source.getP2().getY(),zerfallsK, zerfallsType);
					linesPassed.add(newSource);
				}
		}
		truncateSources(maxSources);
		maxAmplitude = 0;
    	for (PointSource ps : pointsPassed) {
            maxAmplitude += ps.getAmplitude();
        }
    	for (LineSource ls : linesPassed) {
            maxAmplitude += ls.getAmplitude();
        }
		System.out.println("");
		for(PointSource ps: pointsPassed) {
			System.out.print(ps);
			System.out.print(" / ");
		}
		System.out.println("");
		for(LineSource ls: linesPassed) {
			System.out.print(ls);
			System.out.print(" / ");
		}
	}
    
    public double amplitudeGauss(double amp, double distance) {
    	rMaxGauss = Double.parseDouble(iterationsData[5])/2;   // durch zwei weil diameter zu radius
    	amp = amp * Math.exp(-Math.pow(distance, 2)/Math.pow(rMaxGauss, 2));
    	return amp;
    }
    
    public void truncateSources(int maxSources) {
    	while ((pointsPassed.size()+linesPassed.size()) > maxSources) {
    		float lowestAmpPoint = 999999999;
    		int indexOfLowestPoint = -1;
    		for (PointSource ps : pointsPassed) {
    			if (ps.getAmplitude() < (lowestAmpPoint)) {
    				lowestAmpPoint = ps.getAmplitude();
    				indexOfLowestPoint = pointsPassed.indexOf(ps);
    			}
    		}
    		float lowestAmpLine = 999999999;
    		int indexOfLowestLine = -1;
    		for (LineSource ls : linesPassed) {
    			if (ls.getAmplitude() < lowestAmpLine) {
    				lowestAmpLine = ls.getAmplitude();
    				indexOfLowestLine = linesPassed.indexOf(ls);
    			}
    		}
    		System.out.println("Size: " + String.valueOf(pointsPassed.size()+linesPassed.size()));
    		System.out.println("lowest point: " + String.valueOf(indexOfLowestPoint) + "/ amp " + lowestAmpPoint);
    		System.out.println("lowest line: " + String.valueOf(indexOfLowestLine) + "/ amp " + lowestAmpLine);
    		if (lowestAmpLine > lowestAmpPoint){
    			pointsPassed.remove(indexOfLowestPoint);
    		}
    		else{
    			linesPassed.remove(indexOfLowestLine);
    		}
    	}
    }

	public void setFps(int fps) {
    	this.fps = fps;
    }
    
    public void setResolution(int resolution) {
    	SimulationPanel.resolution = resolution;
    }
    
    public void updateA(Graphics g) {
        // Draw the grid
        rectWidth = getWidth() / (areaSize/resolution);
        rectHeight = getHeight() / (areaSize/resolution);
        
        angle -= anglePlus;
        if (angle <= -360){
        	angle = angle - 360;
        }
        //double[][] pointSourceLocation = {{1.0,1.0,0,0},{50.0,50.0}};
        Color currentColor = Color.black;
        for (int i = 0; i < (areaSize/resolution); i++) {
            for (int j = 0; j < (areaSize/resolution); j++) {
                // Upper left corner of this terrain rect
                int x = i * rectWidth;
                int y = j * rectHeight;
                double height = 0.0;
                for (PointSource ps : pointsPassed) {
                	height = height  + getHeight(i,j,ps);
                }
                for (LineSource ls : linesPassed) {
                	height = height  + getHeight(i,j,ls);;
                }
                currentColor = getColorA(height);
                g.setColor(currentColor);
                g.fillRect(x, y, rectWidth, rectHeight);
            }
        }
        if (saveFrames){
            if (framecounter < 360/anglePlus) {
            	saveFrames = false;
            	saveFrames(saveFramesInfo[0], saveFramesInfo[1],saveFramesInfo[2]);
            	saveFrames = true;
            }
            else {
            	saveFrames = false;
            	makeGif(saveFramesInfo[0], saveFramesInfo[1]);
            	myTimer.stop();
            }
        }
       framecounter++;
    }
    
    public void updateIteration(Graphics g) {
    	iterationTimer.stop();
        // Draw the grid
        rectWidth = getWidth() / (areaSize/resolution);
        rectHeight = getHeight() / (areaSize/resolution);
        
        angle -= anglePlus;
        if (angle <= -360){
        	angle = 0;
        }
        int psUsed = 0;
        int lsUsed = 0;
        //double[][] pointSourceLocation = {{1.0,1.0,0,0},{50.0,50.0}};
        Color currentColor = Color.black;
        //double numberOfOldsources  = pointsIlluminated.size() + linesIlluminated.size();
        checkPointsIlluminated(pointsPassed);
        checkLinesIlluminated(linesPassed);
        if (((pointsIlluminated.isEmpty())&&(linesIlluminated.isEmpty()))) {
        	System.out.println("No Source illuminated.");
        }
        for (int i = 0; i < (areaSize/resolution); i++) {
            for (int j = 0; j < (areaSize/resolution); j++) {
                // Upper left corner of this terrain rect
                int x = i * rectWidth;
                int y = j * rectHeight;
                if(checkIfMolten(i,j)&&((!pointsIlluminated.isEmpty())||(!linesIlluminated.isEmpty()))) {
                    //double height = 0.0;
                	double height =  matrixData[i][j]*oldFactor;//*numberOfOldsources;
                    for (PointSource ps : pointsIlluminated) {
                        height = height  + getHeight(i,j,ps);
                        if((i == 0)&&(j == 0)){
                        	psUsed++;
                        }
                    }
                    for (LineSource ls : linesIlluminated) {
	                    height = height  + getHeight(i,j,ls);
	                    if((i == 0)&&(j == 0)){
	                    	lsUsed++;
	                    }
                    }
                    currentColor = getColorA(height);
                    g.setColor(currentColor);
                    g.fillRect(x, y, rectWidth, rectHeight);
                    matrixData[i][j] = height;
                }
            }
        }
        System.out.println("");
        System.out.println("PS_Used: " + psUsed+ "| " +"LS_Used: " + lsUsed);
        if (iterate){
        	System.out.println("Im on updateIteration()");
        	if ((iterationsDone < Integer.parseInt(iterationsData[0]))) {
            	iterate = false;
            	iterate(saveFramesInfo[0], saveFramesInfo[1],saveFramesInfo[2]);
            	iterate = true;
            	normiert = true;
            	if (iterationsDone < (Integer.parseInt(iterationsData[0]))) {
                    String[] ImageSegmentationString = new String[]{(saveFramesInfo[0]+"iterations\\"  + saveFramesInfo[1] + "_" + String.valueOf(iterationsDone-1)+"."+saveFramesInfo[2]),iterationsData[1],iterationsData[2],iterationsData[4]};
                    //System.out.println(ImageSegmentationString[0]);
                    Object[][] newSources = new ImageSegmentation().oldRun(ImageSegmentationString);
                    org.opencv.core.Point[] newPointSources = (org.opencv.core.Point[]) newSources[0];
                    //org.opencv.core.Point[] newLinePointSources = (org.opencv.core.Point[]) newSources[2];
                    Line2D[] newLineSources = (Line2D[]) newSources[1];
                	System.out.println("Pointsources: ");
            		for(org.opencv.core.Point p: newPointSources) {
            			System.out.print(p);
            			System.out.print(" / ");
            		}
            		System.out.println("");
            		System.out.print("Linesources: ");
            		System.out.println(newLineSources.length);
            		for(Object l: newLineSources) {
            			System.out.print(((Line2D) l).getP1() + "," + ((Line2D) l).getP2());
            			System.out.print(" / ");
            		}
                	System.out.println("LinePointsources: ");
            		/*for(org.opencv.core.Point p: newLinePointSources) {
            			System.out.print(p);
            			System.out.print(" / ");
            		}
            		System.out.println("");
            		upDateSources(newPointSources, newLineSources, newLinePointSources, Integer.parseInt(iterationsData[3]));*/
            	}
        	}
        	else {
        		iterationTimer.stop();
        		SwingUtilities.getWindowAncestor(this).dispose();
        	}
        }
        iterationTimer.start();
    }

	public Color getColorA(double height) {
        height = (((height/maxAmplitude)+1)/2);
        Double[] currentColorDoubleMatrix = new Double[3];
        height = truncate(height, 0, 0.98);
        if (colored == true) {
            currentColorDoubleMatrix = colorMap[(int) (height*60)];
            return new Color(Integer.valueOf((int) (currentColorDoubleMatrix[0]*255)),Integer.valueOf((int) (currentColorDoubleMatrix[1]*255)),Integer.valueOf((int) (currentColorDoubleMatrix[2]*255)));
        }
        else {
        	return new Color((int) (height*255), (int) (height*255), (int) (height*255));
        }
    }
    
	private double truncate(double value, double min, double max) {
		   return Math.min(Math.max(value, min), max);
		}
	
	private double getHeight(int x, int y, PointSource ps) {
    	double[] functionData = {ps.getAmplitude(),ps.getFrequency()/10,ps.getPhase()};
    	double[] coordinates = {ps.getX()/1000*(areaSize/resolution),ps.getY()/1000*(areaSize/resolution)};
    	String decayType = ps.getZerfallsType();
    	double decayConst = ps.getZerfallsK();
    	double decay = 1;
    	double xDifference = (x - coordinates[0]);
    	double yDifference = (y - coordinates[1]);
    	double distance = Math.sqrt(Math.pow(xDifference,2) + Math.pow(yDifference,2))*resolution;
    	double theta = functionData[1]*distance;
    	double height = 0;
    	if (decayType.equalsIgnoreCase("linear")) {
    		decay = decayConst*distance;
    		height = functionData[0]*Math.sin(theta+Math.toRadians(angle+functionData[2]));
    		if (height >= 0) {
    			height = height - decay;
    			if (height < 0) {
    				height = 0;
    			}
    		}
    		else if (height < 0) {
    			height = height + decay;
    			if (height > 0) {
    				height = 0;
    			}
    		}
    	}
    	else if (decayType.equalsIgnoreCase("exponential")) {
    		decay = Math.exp(-decayConst*distance);
    		if (decay > 0) {
        		height = decay*functionData[0]*Math.sin(theta+Math.toRadians(angle+functionData[2]));
    		}
    		else {
    			height = 0;
    		}
    	}
    	else {
			if (decayErrorFlag = false) {
    			System.out.println("Unknown decay type: " + decayType);
    			decayErrorFlag = true;
    		}
    		height = functionData[0]*Math.sin(theta+Math.toRadians(angle+functionData[2]));
    	};
    	return height;
    }
	
	private double getHeight(int x, int y, LineSource ls) {
		Point closestPoint = getClosestPointOnLine(ls, x, y);
		if (closestPoint == null) {
			return 0;
		}
		PointSource ps = new PointSource(ls.getAmplitude(),ls.getFrequency(),ls.getPhase(),(float) closestPoint.getX(), (float) closestPoint.getY());
    	return getHeight(x,y,ps);
    }
	
	  public static Point getClosestPointOnLine(LineSource line, int x, int y)
	  {
		double sx1 = line.getX1()/1000*(areaSize/resolution);
		double sy1 = line.getY1()/1000*(areaSize/resolution);
		double sx2 = line.getX2()/1000*(areaSize/resolution);
		double sy2 = line.getY2()/1000*(areaSize/resolution);
	    double xDelta = sx2 - sx1;
	    double yDelta = sy2 - sy1;
	    int px = x;///1000*(areaSize/resolution);
	    int py = y;///1000*(areaSize/resolution);

	    if ((xDelta == 0) && (yDelta == 0))
	    {
	    	//throw new IllegalArgumentException(error);
	    	//System.out.println("Changed Line to point");
	    	return new Point((int)sx1, (int)sy1);
	    }

	    double u = ((px - sx1) * xDelta + (py - sy1) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

	    final Point closestPoint;
	    if (u < 0)
	    {
	      closestPoint = new Point((int)sx1, (int)sy1);
	    }
	    else if (u > 1)
	    {
	      closestPoint = new Point((int)sx2, (int)sy2);
	    }
	    else
	    {
	      closestPoint = new Point((int) Math.round(sx1 + u * xDelta), (int) Math.round(sy1 + u * yDelta));
	    }

	    return closestPoint;
	  }
	
	public void setAreaSize(int areaSize) {
		SimulationPanel.areaSize = areaSize;
	}
    
    public static void main(String[] args) {
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
    	ArrayList<PointSource> points = new ArrayList<PointSource>();
    	PointSource pointSource = new PointSource(1,10,0,50,50);
    	points.add(pointSource);
    	int fps = 20;
    	run(points, fps);       
    }

    public static void run(ArrayList<PointSource> pointsPassed, int fps) {
        JFrame frame = new JFrame("Simulation");
        final SimulationPanel map = new SimulationPanel();
        map.setPointSourceLocations(pointsPassed);
        frame.add(map);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        ;
    }
    
	public void saveImage(String filePath, String name,String type) {
		BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		paint(g);
    	if (normiert) {
    		normiereFrame(g);
    		System.out.println("normiere");
    	}
		try{
			File directory = new File(filePath);
    	    if (! directory.exists()){
    	        directory.mkdirs();
    	        // If you require it to make the entire directory path including parents,
    	        // use directory.mkdirs(); here instead.
    	    }
			ImageIO.write(image, type, new File(filePath + name+"."+type));
			System.out.println(name+" created successfully!");
		} catch (Exception e) {
			e.printStackTrace();
		}
}
	public void saveFrames(String filePath, String name,String type) {
		BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		paint(g);
    	if (normiert) {
    		normiereFrame(g);
    		System.out.println("normiere");
    	}
		try{
			File directory = new File(filePath+name+"\\");
    	    if (! directory.exists()){
    	        directory.mkdirs();
    	        // If you require it to make the entire directory path including parents,
    	        // use directory.mkdirs(); here instead.
    	    }
    	    frames[framecounter] = image;
			ImageIO.write(image, type, new File(filePath+name+"\\" + name+String.valueOf(framecounter)+"."+type));
			System.out.println(name+" created successfully! (Frame)");
		} catch (Exception e) {
			e.printStackTrace();
		}
}
	
	public void iterate(String filePath, String name,String type) {
		BufferedImage image = new BufferedImage(getWidth(),getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = image.createGraphics();
		System.out.println("Im on iterate()");
		//paint(g);																																//////////////////////
    	if (normiert) {
    		normiereFrame(g);
    		System.out.println("normiere");
    	}
		try{
			File directory = new File(filePath+"iterations\\");
    	    if (! directory.exists()){
    	        directory.mkdirs();
    	        // If you require it to make the entire directory path including parents,
    	        // use directory.mkdirs(); here instead.
    	    }
			ImageIO.write(image, type, new File(filePath+"iterations\\" + name+ "_" +String.valueOf(iterationsDone)+"."+type));
			System.out.println(name+" created successfully! (iteration)");
			iterationsDone++;
		} catch (Exception e) {
			e.printStackTrace();
		}
}
	
	public void makeGif(String filePath, String name) {
		try {
	        BufferedImage first = frames[0];
	        ImageOutputStream output = new FileImageOutputStream(new File(filePath+name+"\\" + name+".gif"));
	        GifSequenceWriter writer = new GifSequenceWriter(output, first.getType(), 100, true);
	        writer.writeToSequence(first);
	
	        for (BufferedImage f : frames) {
	            writer.writeToSequence(f);
	        }
	
	        writer.close();
	        output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setColored(boolean colored) {
		this.colored = colored;
	}
	
	public void setNormiert(boolean normiert) {
		this.normiert = normiert;
	}
	
	public void normiereFrame(Graphics g) {
        Color currentColor = Color.black;
        double maxHeight = getMaxHeight(matrixData);
        for (int i = 0; i < (areaSize/resolution); i++) {
            for (int j = 0; j < (areaSize/resolution); j++) {
	                // Upper left corner of this terrain rect
	                int x = i * rectWidth;
	                int y = j * rectHeight;
	                double height = matrixData[i][j];
	                height = height/maxHeight*maxAmplitude;
	                currentColor = getColorA(height);
	                g.setColor(currentColor);
	                g.fillRect(x, y, rectWidth, rectHeight);
	                matrixData[i][j] = height;
	            }
        }
	}
	
	public double getMaxHeight(double[][] heightData) {
		double maxHeight = 0;
        for (int i = 0; i < (areaSize/resolution); i++) {
            for (int j = 0; j < (areaSize/resolution); j++) {
                if (Math.abs(heightData[i][j])>maxHeight) {
                	maxHeight = Math.abs(heightData[i][j]);
                }
            }
        }
		return maxHeight;
	}
	
	public void saveFramesPassInfo(String filePath,String name, String type) {
		this.saveFramesInfo[0] = filePath;
		this.saveFramesInfo[1] = name;
		this.saveFramesInfo[2] = type;
	}
    
    public void setPointSourceLocations(ArrayList<PointSource> pointsPassed) {
    	this.pointsPassed = pointsPassed;
    	for (PointSource ps : pointsPassed) {
            this.maxAmplitude += ps.getAmplitude();
        }
    }
    
    public void setLineSourceLocations(ArrayList<LineSource> linesPassed) {
    	this.linesPassed = linesPassed;
    	for (LineSource ls : linesPassed) {
            this.maxAmplitude += ls.getAmplitude();
        }
    }
    
    private boolean checkIfMolten(int x, int y) {
		if(Boolean.valueOf(iterationsData[6])){
			double nmPerIteration = (areaSize + Double.parseDouble(iterationsData[5]))/Double.parseDouble(iterationsData[0]);  //in nm
			double offset = ((areaSize) + (Double.parseDouble(iterationsData[5])/2) - (iterationsDone*nmPerIteration))/resolution; //in px
	    	double xDifference = ((areaSize/(2*resolution) + offset) - x);
	    	double yDifference = (areaSize/(2*resolution) - y);
	    	double distance = Math.sqrt(Math.pow(xDifference,2) + Math.pow(yDifference,2))*resolution;  //nm
	    	if(distance < Double.parseDouble(iterationsData[5])){
	    		return true;
	    	}
	    	else {
	    		return false;
	    	}
		}
    	else {
    		return true; 	
    	}
    }
    
    private void checkPointsIlluminated(ArrayList<PointSource> pl) {
    	pointsIlluminated.clear();
        for (PointSource p : pl) {
        	int psX = (int) (p.getX()/1000*(areaSize/resolution));
        	int psY = (int) (p.getY()/1000*(areaSize/resolution));
        	if (checkIfMolten(psX, psY)) {			//wird die Quelle auch wirklich angestrahlt?
        		pointsIlluminated.add(p);
            	}
        	}
    }
    
    private void checkLinesIlluminated(ArrayList<LineSource> ll) {
    	linesIlluminated.clear();
        for (LineSource l : ll) {
    		int lX1 = (int) (l.getX1()/1000*(areaSize/resolution));
        	int lY1 = (int) (l.getY1()/1000*(areaSize/resolution));
        	int lX2 = (int) (l.getX2()/1000*(areaSize/resolution));
        	int lY2 = (int) (l.getY2()/1000*(areaSize/resolution));
        	if (checkIfMolten(lX1, lY1)||checkIfMolten(lX2,lY2)) {			//wird die Quelle auch wirklich angestrahlt?
        		linesIlluminated.add(l);
            	}
        	}
    }
    
    private double meanFilter(int x, int y, double[][] matrix, int blocksize) {
    	double sumHeight = 0;
    	int counterPixel = 0;
    	int xMin = (int)x-blocksize/2;
    	int xMax = (int)x+blocksize/2;
    	int yMin = (int)y-blocksize/2;
    	int yMax = (int)y+blocksize/2;
    	if (xMin < 0) {
    		xMin = 0;
    	}
    	if (xMax >= areaSize/resolution) {
    		xMax = areaSize/resolution-1;
    	}
    	if (yMin < 0) {
    		yMin = 0;
    	}
    	if (yMax >= areaSize/resolution) {
    		yMax = areaSize/resolution-1;
    	}
        for (int i = xMin; i < xMax; i++) {
            for (int j = yMin; j < yMax; j++) {
            	sumHeight = sumHeight + (matrix[i][j]+1)/2;
            	counterPixel++;
            }
        }
        double height = 2*(matrix[x][y]+1);
    	return  (height-(sumHeight/counterPixel)/height);
    }
}