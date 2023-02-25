import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;

public class PaintSurface extends JComponent {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Shape> shapes = new ArrayList<Shape>();
    //ArrayList<MyRectangle2D> rectangles = new ArrayList<MyRectangle2D>();
    //ArrayList<MyLine2D> lines = new ArrayList<MyLine2D>();
    //ArrayList<MyEllipse2D> points = new ArrayList<MyEllipse2D>();
    SelectingBox2D selectingBox = new SelectingBox2D();
    boolean actionOnSelectingBox = false;
    boolean selectSingleShape = false;
    boolean selectionMenuPopped = true;
    int mouseButton = 1;
	public String [] iterationsData = new String[7];

    Point startDrag, endDrag;
    String mode = "rectangle";

    public PaintSurface() {
      this.addMouseListener(new MouseAdapter() {
        public void mousePressed(MouseEvent e) {
        	mouseButton = e.getButton();
        	if (mouseButton == 1) {
            	if ((selectingBox.isEmptyArray()) || !(selectingBox.contains(new Point(e.getX(), e.getY())))) {
            		if((checkSingleOverlap(new Point(e.getX(), e.getY()),shapes) != null)&&(mode == "select")){
                		selectSingleShape = true;
                		actionOnSelectingBox = false;
                		startDrag = new Point(e.getX(), e.getY());
                    	endDrag = startDrag;
                			if ((checkSingleOverlap(startDrag,shapes) instanceof MyRectangle2D)) {
                				((MyRectangle2D) checkSingleOverlap(startDrag,shapes)).select();
                				}
                			else if((checkSingleOverlap(startDrag,shapes)  instanceof MyLine2D)) {
                				((MyLine2D) checkSingleOverlap(startDrag,shapes)).select();
                				}
                			else if((checkSingleOverlap(startDrag,shapes) instanceof MyEllipse2D)) {
                				((MyEllipse2D) checkSingleOverlap(startDrag,shapes)).select();
                				}
                		repaint();
                	}
            		else{
            			selectSingleShape = false;
            			actionOnSelectingBox = false;
                		startDrag = new Point(e.getX(), e.getY());
                    	endDrag = startDrag;
                    	repaint();
            		}
            	}
            
            	else{
            		selectSingleShape = false;
            		actionOnSelectingBox = true;
            		startDrag = new Point(e.getX(), e.getY());
                	endDrag = startDrag;
            	}	
        	}
        	else if (mouseButton == 3) {
        		if ((!selectingBox.isEmptyArray()) && (selectingBox.contains(new Point(e.getX(), e.getY())))) {
        			selectSingleShape = false;
        			actionOnSelectingBox = true;
            		startDrag = new Point(e.getX(), e.getY());
                	endDrag = startDrag;
                	repaint();
        		}
        		else {
        			selectSingleShape = false;
        			actionOnSelectingBox = false;
        		}
        	}
        	
        }

        public void mouseReleased(MouseEvent e) {
        	if (mouseButton == 1) {
        		selectionMenuPopped = false;
        		if (!actionOnSelectingBox && !selectSingleShape) {
            		if (mode == "rectangle") {
            			Shape r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
            			shapes.add(r);
            			//rectangles.add(makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY()));
            			createSelectingBox(r);
            		}
            		else if (mode == "line") {
            			Shape l = makeLine(startDrag.x, startDrag.y, e.getX(), e.getY());
            			shapes.add(l);
            			//lines.add(makeLine(startDrag.x, startDrag.y, e.getX(), e.getY()));
            			createSelectingBox(l);
            		}
            		else if (mode == "point") {
            			Shape p = makePoint(startDrag.x, startDrag.y);
            			shapes.add(p);
            			//points.add(makePoint(startDrag.x, startDrag.y));
            			createSelectingBox(p);
            		}
            		else if (mode == "select") {
            			selectingBox = makeSelectingBox(startDrag.x, startDrag.y, e.getX(), e.getY());
            			deselectAll(selectingBox);
            			for (Shape s : checkOverlap(selectingBox, shapes)) {
            				selectingBox.addShape(s);
            				if ((s instanceof MyRectangle2D)) {
            	        			((MyRectangle2D) s).select();
            				}
            				else if((s instanceof MyLine2D)) {
        	        			((MyLine2D) s).select();
            				}
            				else if((s instanceof MyEllipse2D)) {
        	        			((MyEllipse2D) s).select();
            				}
            			};
            			selectingBox.resizeToFit();
            			repaint();
            		}
            		else {
            			System.out.println("Please select a valid drawing mode");
            		}
            	startDrag = null;
              	endDrag = null;
            	}
            	else if (actionOnSelectingBox && !selectSingleShape){
                	int xOffset = (endDrag.x-startDrag.x);
                	int yOffset = (endDrag.y-startDrag.y);
                	selectingBox.move(xOffset, yOffset);
        			for (Shape s : selectingBox.getSelectedShapes()) {
        				if ((s instanceof MyRectangle2D)) {
        	        			((MyRectangle2D) s).move(xOffset, yOffset);
        				}
        				else if((s instanceof MyLine2D)) {
    	        			((MyLine2D) s).move(xOffset, yOffset);
        				}
        				else if((s instanceof MyEllipse2D)) {
    	        			((MyEllipse2D) s).move(xOffset, yOffset);
        				}
        			};
            	}
            	else if (!actionOnSelectingBox && selectSingleShape){
            			Shape selectedShape = checkSingleOverlap(new Point(e.getX(), e.getY()),shapes);
            			selectingBox = makeSelectingBox(startDrag.x, startDrag.y, e.getX(), e.getY());
            			selectingBox.clear();
            			selectingBox.addShape(selectedShape);
            			selectingBox.resizeToFit();
            		}
              	repaint();
        	}
        	else if (mouseButton == 3) {
        		if (actionOnSelectingBox){
//        			System.out.println("pop");
        			selectingBox.popMenu();
        			selectionMenuPopped = true;
            	}
        	}
        	}       
      });

      this.addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseDragged(MouseEvent e) {
        	if (mouseButton == 1) {
        		endDrag = new Point(e.getX(), e.getY());
        		repaint();
        	}   
        }
      });
    }
    private void paintBackground(Graphics2D g2){
      g2.setPaint(Color.LIGHT_GRAY);
      for (int i = 0; i < getSize().width; i += 10) {
        Shape line = new Line2D.Float(i, 0, i, getSize().height);
        g2.draw(line);
      }

      for (int i = 0; i < getSize().height; i += 10) {
        Shape line = new Line2D.Float(0, i, getSize().width, i);
        g2.draw(line);
      }
       g2.setPaint(Color.DARK_GRAY);
       for (int i = 0; i < getSize().width; i += 100) {
         Shape line = new Line2D.Float(i, 0, i, getSize().height);
         g2.draw(line);
       }

       for (int i = 0; i < getSize().height; i += 100) {
         Shape line = new Line2D.Float(0, i, getSize().width, i);
         g2.draw(line);
      }
       
       g2.setPaint(Color.black);
       Shape middle = new MyEllipse2D(497, 497, 6, 6);
       g2.draw(middle);
       g2.fill(middle);
      
    }
    
    public void paint(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      paintBackground(g2);
      Color[] colors = { Color.YELLOW, Color.MAGENTA, Color.CYAN , Color.RED, Color.BLUE, Color.PINK};

      g2.setStroke(new BasicStroke(2));
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

      for (Shape s : shapes) {
        g2.setPaint(Color.BLACK);
        g2.draw(s);
        if ((s instanceof MyRectangle2D)) {
        	if (((MyRectangle2D) s).isFilled()){
        		g2.fill(s);
        	}
        	if (((MyRectangle2D) s).isSelected()){
        		g2.setPaint(colors[0]);
        	 	g2.draw3DRect(((int) ((MyRectangle2D) s).getX()), ((int) ((MyRectangle2D) s).getY()), ((int) ((MyRectangle2D) s).getWidth()), ((int) ((MyRectangle2D) s).getHeight()), true);
        	}
        }
        else if ((s instanceof MyEllipse2D)) {
        	if (((MyEllipse2D) s).isFilled()){
        		g2.fill(s);
        	}
        	if (((MyEllipse2D) s).isSelected()){
        		g2.setPaint(colors[0]);
        	 	g2.draw3DRect(((int) ((MyEllipse2D) s).getX()), ((int) ((MyEllipse2D) s).getY()), ((int) ((MyEllipse2D) s).getWidth()), ((int) ((MyEllipse2D) s).getHeight()), true);
        	}
        }
        else if ((s instanceof MyLine2D)) {
        	if (((MyLine2D) s).isSelected()){
        		g2.setPaint(colors[0]);
        		g2.draw(s);
        	}
        }
      }
      
      //Draw the outlines of the selectingBox if not empty
      if (!selectingBox.isEmptyArray()){
    	  g2.setPaint(colors[0]);
    	  g2.draw(selectingBox);
      }
      
      if (startDrag != null && endDrag != null) {
        g2.setPaint(Color.LIGHT_GRAY);
        if (!actionOnSelectingBox) {
            Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);
            g2.draw(r);
        }
        else{
        	int xOffset = (endDrag.x-startDrag.x);
        	int yOffset = (endDrag.y-startDrag.y);
        	Shape r = makeRectangle((int) selectingBox.getX() + xOffset, (int) selectingBox.getY() + yOffset, ((int) selectingBox.getX() + (int) selectingBox.getWidth() + xOffset),((int) selectingBox.getY() + (int) selectingBox.getHeight() + yOffset));
            g2.draw(r);
        }
      }
    }

    public void mode (String mode) {
    	this.mode = mode;
    }
    
    public ArrayList<Shape> checkOverlap(MyRectangle2D selectingBox, ArrayList<Shape> shapes) {
    	ArrayList<Shape> overlappingShapes = new ArrayList<Shape>();
    	 for (Shape s : shapes) {
    		 if (selectingBox.contains(s.getBounds2D())) {
    			 overlappingShapes.add(s);
    		 }
    	 }
    	 //System.out.println(overlappingShapes);
    	return overlappingShapes;
    }
    
    public Shape checkSingleOverlap(Point pointer, ArrayList<Shape> shapes) {
    	System.out.println(pointer.getX());
    	System.out.println(pointer.getY());
    	 for (Shape s : shapes) {
    		 if (s.contains(pointer)) {
//    		     System.out.println(s);
    			 return s;
    		 }
    	 }
    	return null;
    }
    
    public ArrayList<Shape> checkOverlap(SelectingBox2D selectingBox, ArrayList<Shape> shapes) {
    	ArrayList<Shape> overlappingShapes = new ArrayList<Shape>();
    	 for (Shape s : shapes) {
    		 if (selectingBox.contains(s.getBounds2D())) {
    			 overlappingShapes.add(s);
    		 }
    	 }
    	//System.out.println(selectingBox.getWidth());
    	//System.out.println(overlappingShapes);
    	return overlappingShapes;
    }
    
	public void setIterations(String iterations, String min_treshold, String max_treshold, String maxSources, String iterationWithLines, String spotsize, String isMoving) {
		this.iterationsData[0] = iterations;
		this.iterationsData[1] = min_treshold;
		this.iterationsData[2] = max_treshold;
		this.iterationsData[3] = maxSources;
		this.iterationsData[4] = iterationWithLines;
		this.iterationsData[5] = spotsize;
		this.iterationsData[6] = isMoving;
	}
    
    public void createRandomPoints(int num,float amp, float ampDev, float freq, float freqDev, float phase, float phaseDev, int xOrigin, int yOrigin, int xDev, int yDev) {
    	int count = 0;
    	float randomFreq;
		float randomAmp;
		float randomPhase; 
		int x = xOrigin;
		int y = yOrigin; 
		MyEllipse2D p;
    	Random random = new Random();
    	while (count < num) {
    		if (freqDev != 0) {
    			randomFreq = freq + random.nextFloat() * (2*freqDev) - freqDev;
    		}
    		else {
    			randomFreq = freq;
    		}
    		if (ampDev != 0) {
    			randomAmp = amp + random.nextFloat() * (2*ampDev) - ampDev;
    		}
    		else {
    			randomAmp = amp;
    		}
    		if (phaseDev != 0) {
    			randomPhase = phase + random.nextFloat() * (2*phaseDev) - phaseDev; 
    		}
    		else {
    			randomPhase = phase;
    		}
    		if (xDev != 0) {
    			x = xOrigin + random.nextInt(2*xDev) - xDev;
    		}
    		else {
    			x = xOrigin;
    		}
    		if (yDev != 0) {
    			y = yOrigin + random.nextInt(2*yDev) - yDev; 
    		}
    		else {
    			y = yOrigin;
    		}
    		p = new MyEllipse2D(x, y, 10, 10, randomAmp, randomFreq, randomPhase);
    		shapes.add(p);
    		count ++;
    	}
    	this.repaint();
    }
    
    public void deselectAll(SelectingBox2D box) {
    	for (Shape s : shapes) {
    		try {
    			 if ((s instanceof MyRectangle2D)) {
    				 ((MyRectangle2D) s).unSelect();
    		        }
    		        else if ((s instanceof MyEllipse2D)) {
    		        	((MyEllipse2D) s).unSelect();
    		        }	
    		        else if ((s instanceof MyLine2D)) {
        		        ((MyLine2D) s).unSelect();
        		        }
    		box.clear();
    		repaint();
			} catch (Exception e) {
				//e.printStackTrace();
			}
   		 }
   	 }
    
    private void createSelectingBox(Shape s) {
		selectingBox = makeSelectingBox(0,0,0,0);
		selectingBox.addShape(s);
		selectingBox.resizeToFit();
    }
    
    private MyRectangle2D makeRectangle(int x1, int y1, int x2, int y2) {
      return new MyRectangle2D(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
    
    private SelectingBox2D makeSelectingBox(int x1, int y1, int x2, int y2) {
        return new SelectingBox2D(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
      }
    
    private MyLine2D makeLine(int x1, int y1, int x2, int y2) {
        return new MyLine2D(x1, y1, x2, y2);
      }
    
    private MyEllipse2D makePoint(int x1, int y1) {
        return new MyEllipse2D(x1, y1, 10, 10);
      }
    
    public ArrayList<MyEllipse2D> getPoints(){
    	ArrayList<MyEllipse2D> points = new ArrayList<MyEllipse2D>();
    	for (Shape s : shapes) {
    		if ((s instanceof MyEllipse2D)) {
    			points.add((MyEllipse2D)s);
       		}
    	}
    	return points;
    }
    
    public ArrayList<MyLine2D> getLines(){
    	ArrayList<MyLine2D> lines = new ArrayList<MyLine2D>();
    	for (Shape s : shapes) {
    		if ((s instanceof MyLine2D)) {
    			lines.add((MyLine2D)s);
       		}
    	}
    	return lines;
    }
    
    public ArrayList<MyRectangle2D> getRectangles(){
    	ArrayList<MyRectangle2D> rectangles = new ArrayList<MyRectangle2D>();
    	for (Shape s : shapes) {
    		if ((s instanceof MyRectangle2D)) {
    			rectangles.add((MyRectangle2D)s);
       		}
    	}
    	return rectangles;
    }
    
    public void deleteSelection() {
    	for (Shape s : selectingBox.getSelectedShapes()) {
    		shapes.remove(s);
   			 /*if ((s instanceof MyRectangle2D)) {
   				rectangles.remove(s);
   		        }
   		        else if ((s instanceof MyEllipse2D)) {
   		        	points.remove(s);
   		        }	
   		        else if ((s instanceof MyLine2D)) {
   		        	lines.remove(s);
       		        }*/
    	}
			 deselectAll(selectingBox);
			 selectionMenuPopped = false;
			 repaint();
    }
  }
