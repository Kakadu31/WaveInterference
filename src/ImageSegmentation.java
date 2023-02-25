import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


class ImageSegmentation {
	
	//Data for Algorithm
	private double min_threshold;
	private double max_threshold;
	private int blocksize = 51;
	private double dotMinSize = 2;
	private double lineMinSize = 2;
	private double dotMaxSize = 800;
	private double lineMaxWidth = 40;
	private boolean iterationWithLines;
	//Storage for calculations
    MatOfPoint2f[] contoursPoly;
    MatOfPoint2f[] contoursPoly2;
    RotatedRect[] boundRect;
    Point[] centers;
    Point[] centers2;
    Point[] centerResults;
    Point[] centerResults2;
    Line2D[] lines;
    Line2D[] lineResults;
    float[][] radius;
    float[][] radius2;

	
    public Object[][] run(String[] args) {
        // Load the image
        String filename = (args.length > 0) ? args[0] : "not found";
        min_threshold = Double.parseDouble((args.length > 1) ? args[1] : "0.7");
        max_threshold = Double.parseDouble((args.length > 2) ? args[2] : "1.0");
        iterationWithLines = (((args.length > 3) ? Boolean.valueOf(args[3]) : false));
        Mat srcOriginal = Imgcodecs.imread(filename);
        if (srcOriginal.empty()) {
            System.err.println("Cannot read image: " + filename);
            System.exit(0);
        }
        //HighGui.imshow("myGray", srcOriginal);
        // Show source image
        // Change the background from white to black, since that will help later to
        // extract
        //needed?
        Mat src = srcOriginal.clone();
        Mat result = srcOriginal.clone();
        byte[] srcData = new byte[(int) (src.total() * src.channels())];
        src.get(0, 0, srcData);
        for (int i = 0; i < src.rows(); i++) {
            for (int j = 0; j < src.cols(); j++) {
                if (srcData[(i * src.cols() + j) * 3] == (byte) 255 && srcData[(i * src.cols() + j) * 3 + 1] == (byte) 255
                        && srcData[(i * src.cols() + j) * 3 + 2] == (byte) 255) {
                    srcData[(i * src.cols() + j) * 3] = 0;
                    srcData[(i * src.cols() + j) * 3 + 1] = 0;
                    srcData[(i * src.cols() + j) * 3 + 2] = 0;
                }
            }
        }
        src.put(0, 0, srcData);
        
        //Create all matrices needed
        Mat mySrc = srcOriginal.clone();
        Mat srcGray = new Mat();
        Mat srcGrayInverted = new Mat();
        Mat myPeaks = new Mat();
        Mat linePeaks = new Mat();
        Mat mask = new Mat();
        Mat newPeaks = new Mat();
        Imgproc.cvtColor(mySrc, srcGrayInverted, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.cvtColor(mySrc, srcGray, Imgproc.COLOR_BGR2GRAY);
        HighGui.imshow("myGray", srcGrayInverted);
        //Imgproc.threshold(srcGray, myPeaks, min_threshold*255, max_threshold*255, Imgproc.THRESH_BINARY);
        Core.bitwise_not(srcGray,srcGrayInverted);
        Imgproc.adaptiveThreshold(srcGrayInverted, myPeaks, max_threshold*255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY_INV, blocksize, min_threshold*min_threshold*40);
        Mat myDist_8u = new Mat();
        myPeaks.convertTo(myDist_8u, CvType.CV_8U);
        //HighGui.imshow("myGray", srcGray);
        //HighGui.imshow("myPeaks", myDist_8u);
        
        //Create black image for both masks
        Imgproc.threshold(srcGray, mask, 255, 255, Imgproc.THRESH_BINARY);
        Imgproc.threshold(srcGray, linePeaks, 255, 255, Imgproc.THRESH_BINARY);
        
        // Find total markers
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(myDist_8u, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        // Create the marker image
        Mat markers = Mat.zeros(myDist_8u.size(), CvType.CV_32S);
        // Draw the foreground markers
        ArrayList<Point> centerList = new ArrayList<Point>();
        ArrayList<Line2D> lineList = new ArrayList<Line2D>();
        List<MatOfPoint> boxContours = new ArrayList<>();
    	contoursPoly  = new MatOfPoint2f[contours.size()];
        centers = new Point[contours.size()];
        lines = new Line2D[contours.size()];
        radius = new float[contours.size()][1];
        boundRect = new RotatedRect[contours.size()];
        //If no lines: only draw pointsources = old algorithm
        if (!iterationWithLines) {
            for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(markers, contours, i, new Scalar(i + 1), -1);
                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
            	if (Imgproc.contourArea(contoursPoly[i])>2) {
                    centers[i] = new Point();
                    Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
                    Imgproc.circle (result,   centers[i], 10, new Scalar(0, 0, 255), 2);
                    centerList.add(centers[i]);
            	}
            }
            centerResults = new Point[centerList.size()];
            for (int i = 0; i < centerList.size(); i++) {
            	centerResults[i] = centerList.get(i);
            }
            lineResults = new Line2D[0];
            Imgcodecs.imwrite(filename.substring(0, filename.length() - 4)+"Peaks2.jpg", result);
        }
        //Else also check for line sources
        else {
            for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(result, contours, i, new Scalar(255, 0, 0), -1);
                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
                centers[i] = new Point();
                boundRect[i] = Imgproc.minAreaRect(contoursPoly[i]);
                Point[] vertices = new Point[4];
                boundRect[i].points(vertices);
                double heigth = Math.max(boundRect[i].size.width, boundRect[i].size.height);
                double width = Math.min(boundRect[i].size.width, boundRect[i].size.height);
                //If the polygon is not elongated and has the right size draw a point source
                if (((heigth/width)<2.4)&&(Imgproc.contourArea(contoursPoly[i])>dotMinSize)&&(Imgproc.contourArea(contoursPoly[i])<dotMaxSize)) {
                    Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
                    Imgproc.circle (result,   centers[i], 10, new Scalar(0, 0, 255), 2);
                    centerList.add(centers[i]);
                }
                //else check if its big enough draw a line and check further for pointsources
                else if ((Imgproc.contourArea(contoursPoly[i])>lineMinSize)){          
                    boxContours.add(new MatOfPoint(vertices));  
                    double x1;
                    double y1;
                    double x2;
                    double y2;
                    //System.out.println(vertices[1].x + "," + vertices[1].y + ","+ vertices[2].x + ","+ vertices[2].y + ",");
                    if(Math.abs(dist(vertices[1].x,vertices[1].y,vertices[0].x,vertices[0].y))<Math.abs(dist(vertices[1].x,vertices[1].y,vertices[2].x,vertices[2].y))) {
                    	x1 = (vertices[1].x+vertices[0].x)/2;
                    	y1 = (vertices[1].y+vertices[0].y)/2;
                    	x2 = (vertices[3].x+vertices[2].x)/2;
                    	y2 = (vertices[3].y+vertices[2].y)/2;
                    }
                    else {
                    	x1 = (vertices[3].x+vertices[0].x)/2;
                    	y1 = (vertices[3].y+vertices[0].y)/2;
                    	x2 = (vertices[1].x+vertices[2].x)/2;
                    	y2 = (vertices[1].y+vertices[2].y)/2;
                    }
                    /*for (int k = 0; k < boxContours.size(); k++) {
                    	Imgproc.drawContours(mask, boxContours, k, new Scalar(255, 255, 255), -1);
                    }*/
                    //if line is too small or does not seem to be a proper line, ignore it for linesources but still check for pointsources
                    if ((dist(x1,y1,x2,y2)>10)&&(width<lineMaxWidth)) {
                    	lineList.add(new Line2D.Double(x1,y1,x2,y2));
                    	Imgproc.line(result, new Point(x1,y1),new Point(x2,y2), new Scalar(0, 255, 0), 2);
                    }
                }
            }
            centerResults = new Point[centerList.size()];
            for (int i = 0; i < centerList.size(); i++) {
            	centerResults[i] = centerList.get(i);
            }
            lineResults = new Line2D[lineList.size()];
            for (int i = 0; i < lineList.size(); i++) {
            	lineResults[i] = lineList.get(i);
            }
            //Imgcodecs.imwrite(filename.substring(0, filename.length() - 4)+"Peaks2.jpg", result);
        }
        for (int k = 0; k < boxContours.size(); k++) {
        	Imgproc.drawContours(mask, boxContours, k, new Scalar(255, 255, 255), -1);
        }
        
        /*		NEW		*/
        Imgproc.adaptiveThreshold(srcGrayInverted, newPeaks, max_threshold*255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY_INV, blocksize, min_threshold*33);
        //HighGui.imshow("linePeaks1", newPeaks);
        newPeaks.copyTo(linePeaks, mask );
        //HighGui.imshow("mask", mask);      
        //HighGui.imshow("linePeaks2", linePeaks);
        
        Mat myDist_8u2 = new Mat();
        linePeaks.convertTo(myDist_8u2, CvType.CV_8U);
        // Find total markers
        List<MatOfPoint> contours2 = new ArrayList<>();
        Mat hierarchy2 = new Mat();
        Imgproc.findContours(myDist_8u2, contours2, hierarchy2, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        // Create the marker image for the watershed algorithm
        Mat markers2 = Mat.zeros(myDist_8u2.size(), CvType.CV_32S);
        // Draw the foreground markers
        ArrayList<Point> centerList2 = new ArrayList<Point>();
    	contoursPoly2  = new MatOfPoint2f[contours2.size()];
        centers2 = new Point[contours2.size()];
        radius2 = new float[contours2.size()][1];
        
        if (iterationWithLines) {
            for (int i = 0; i < contours2.size(); i++) {
                Imgproc.drawContours(markers2, contours2, i, new Scalar(i + 1), -1);
                contoursPoly2[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours2.get(i).toArray()), contoursPoly2[i], 3, true);
            	if (Imgproc.contourArea(contoursPoly2[i])>2) {
	                centers2[i] = new Point();
	                Imgproc.minEnclosingCircle(contoursPoly2[i], centers2[i], radius2[i]);
	                Imgproc.circle (result,   centers2[i], 10, new Scalar(0, 128, 255), 2);
	                centerList.add(centers2[i]);
            	}
            }
            centerResults2 = new Point[centerList2.size()];
            for (int i = 0; i < centerList2.size(); i++) {
            	centerResults2[i] = centerList2.get(i);
            }
        }
        
        Imgcodecs.imwrite(filename.substring(0, filename.length() - 4)+"Peaks3.jpg", result);
        
        // Visualize the final image
        //HighGui.imshow("Final Result", dst);
        //HighGui.waitKey();
        //System.exit(0);
        Object[][]results = new Object[3][1];
        results[0] = centerResults;
        results[1] = lineResults;
        results[2] = centerResults2;
        return results;
    }
    
    public Object[][] oldRun(String[] args) {
        // Load the image
        String filename = (args.length > 0) ? args[0] : "not found";
        min_threshold = Double.parseDouble((args.length > 1) ? args[1] : "0.7");
        max_threshold = Double.parseDouble((args.length > 2) ? args[2] : "1.0");
        iterationWithLines = (((args.length > 3) ? Boolean.valueOf(args[3]) : false));
        Mat srcOriginal = Imgcodecs.imread(filename);
        if (srcOriginal.empty()) {
            System.err.println("Cannot read image: " + filename);
            System.exit(0);
        }
        // Show source image
        //HighGui.imshow("Source Image", srcOriginal);
        // Change the background from white to black, since that will help later to
        // extract
        // better results during the use of Distance Transform
        Mat src = srcOriginal.clone();
        Mat result = srcOriginal.clone();
        byte[] srcData = new byte[(int) (src.total() * src.channels())];
        src.get(0, 0, srcData);
        for (int i = 0; i < src.rows(); i++) {
            for (int j = 0; j < src.cols(); j++) {
                if (srcData[(i * src.cols() + j) * 3] == (byte) 255 && srcData[(i * src.cols() + j) * 3 + 1] == (byte) 255
                        && srcData[(i * src.cols() + j) * 3 + 2] == (byte) 255) {
                    srcData[(i * src.cols() + j) * 3] = 0;
                    srcData[(i * src.cols() + j) * 3 + 1] = 0;
                    srcData[(i * src.cols() + j) * 3 + 2] = 0;
                }
            }
        }
        src.put(0, 0, srcData);
        
        Mat mySrc = srcOriginal.clone();
        Mat srcGray = new Mat();
        Mat srcGrayInverted = new Mat();
        Mat myPeaks = new Mat();
        Imgproc.cvtColor(mySrc, srcGray, Imgproc.COLOR_BGR2GRAY);
        //Imgproc.threshold(srcGray, myPeaks, min_threshold*255, max_threshold*255, Imgproc.THRESH_BINARY);
        Core.bitwise_not(srcGray,srcGrayInverted);
        Imgproc.adaptiveThreshold(srcGrayInverted, myPeaks, max_threshold*255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY_INV, 51, min_threshold*20);
        Mat myDist_8u = new Mat();
        myPeaks.convertTo(myDist_8u, CvType.CV_8U);
        //HighGui.imshow("myGray", srcGray);
        //HighGui.imshow("myPeaks", myDist_8u);
        
        // Find total markers
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(myDist_8u, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        // Create the marker image for the watershed algorithm
        Mat markers = Mat.zeros(myDist_8u.size(), CvType.CV_32S);
        // Draw the foreground markers
        ArrayList<Point> centerList = new ArrayList<Point>();
        ArrayList<Line2D> lineList = new ArrayList<Line2D>();
    	contoursPoly  = new MatOfPoint2f[contours.size()];
        centers = new Point[contours.size()];
        lines = new Line2D[contours.size()];
        radius = new float[contours.size()][1];
        boundRect = new RotatedRect[contours.size()];
        if (!iterationWithLines) {
            for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(markers, contours, i, new Scalar(i + 1), -1);
                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
                centers[i] = new Point();
                Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
                Imgproc.circle (result,   centers[i], 10, new Scalar(0, 0, 255), 2);
                centerList.add(centers[i]);
            }
            centerResults = new Point[centerList.size()];
            for (int i = 0; i < centerList.size(); i++) {
            	centerResults[i] = centerList.get(i);
            }
            lineResults = new Line2D[0];
            Imgcodecs.imwrite(filename.substring(0, filename.length() - 4)+"Peaks2.jpg", result);
        }
        else {
            for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(result, contours, i, new Scalar(255, 0, 0), -1);
                contoursPoly[i] = new MatOfPoint2f();
                Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), contoursPoly[i], 3, true);
                centers[i] = new Point();
                boundRect[i] = Imgproc.minAreaRect(contoursPoly[i]);
                Point[] vertices = new Point[4];
                boundRect[i].points(vertices);
                double heigth = Math.max(boundRect[i].size.width, boundRect[i].size.height);
                double width = Math.min(boundRect[i].size.width, boundRect[i].size.height);
                if ((heigth/width)<2) {
                    Imgproc.minEnclosingCircle(contoursPoly[i], centers[i], radius[i]);
                    Imgproc.circle (result,   centers[i], 10, new Scalar(0, 0, 255), 2);
                    centerList.add(centers[i]);
                }
                else {
                    List<MatOfPoint> boxContours = new ArrayList<>();
                    boxContours.add(new MatOfPoint(vertices));    
                    double x1;
                    double y1;
                    double x2;
                    double y2;
                    //System.out.println(vertices[1].x + "," + vertices[1].y + ","+ vertices[2].x + ","+ vertices[2].y + ",");
                    if(Math.abs(dist(vertices[1].x,vertices[1].y,vertices[0].x,vertices[0].y))<Math.abs(dist(vertices[1].x,vertices[1].y,vertices[2].x,vertices[2].y))) {
                    	x1 = (vertices[1].x+vertices[0].x)/2;
                    	y1 = (vertices[1].y+vertices[0].y)/2;
                    	x2 = (vertices[3].x+vertices[2].x)/2;
                    	y2 = (vertices[3].y+vertices[2].y)/2;
                    }
                    else {
                    	x1 = (vertices[3].x+vertices[0].x)/2;
                    	y1 = (vertices[3].y+vertices[0].y)/2;
                    	x2 = (vertices[1].x+vertices[2].x)/2;
                    	y2 = (vertices[1].y+vertices[2].y)/2;
                    }
                    Imgproc.line(result, new Point(x1,y1),new Point(x2,y2), new Scalar(0, 255, 0), 2);
                    if (dist(x1,y1,x2,y2)>10) {
                    	lineList.add(new Line2D.Double(x1,y1,x2,y2));
                    }
                }
            }
            centerResults = new Point[centerList.size()];
            for (int i = 0; i < centerList.size(); i++) {
            	centerResults[i] = centerList.get(i);
            }
            lineResults = new Line2D[lineList.size()];
            for (int i = 0; i < lineList.size(); i++) {
            	lineResults[i] = lineList.get(i);
            }
            Imgcodecs.imwrite(filename.substring(0, filename.length() - 4)+"Peaks2.jpg", result);
        }

        
        // Visualize the final image
        //HighGui.imshow("Final Result", dst);
        //HighGui.waitKey();
        //System.exit(0);
        Object[][]results = new Object[2][1];
        results[0] = centerResults;
        results[1] = lineResults;
        return results;
    }
    
    public double dist(
    		  double x1, 
    		  double y1, 
    		  double x2, 
    		  double y2) {       
    		    return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    		}
}