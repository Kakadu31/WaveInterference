import org.opencv.core.Core;
import org.opencv.core.Point;

public class sanbox {
    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Object[][]newSources = new ImageSegmentation().run(args);
    	org.opencv.core.Point[] newPointSources = (org.opencv.core.Point[]) newSources[0];
        for (Point point : newPointSources) {
        	System.out.println(point.toString());
        }
    	}
    } 