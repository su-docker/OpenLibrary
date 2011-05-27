package com.sudhakar;

import static com.googlecode.javacv.cpp.opencv_core.CV_AA;
import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_GAUSSIAN;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvSmooth;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

public class FaceDetector {

	// The cascade definition to be used for detection.
	private static final String CASCADE_FILE = "F:/dev/OpenCV2.2/data/haarcascades/haarcascade_frontalface_alt.xml";
	private static final String DETECTED_FILE_NAME = "face%s.jpg";
	private static int counter = 1;

	public static void main(String[] args) throws Exception {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		grabber.start();

		IplImage frame = grabber.grab();
		CanvasFrame canvasFrame = new CanvasFrame("Open Library");
		canvasFrame.setCanvasSize(frame.width(), frame.height());

		while (canvasFrame.isVisible() && (frame = grabber.grab()) != null) {
			Thread.sleep(500);

			cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
			writeToFile(frame);

			IplImage grayImage = IplImage.create(frame.width(), frame.height(),
					IPL_DEPTH_8U, 1);
			// We convert the original image to gray scale.
			cvCvtColor(frame, grayImage, CV_RGB2GRAY);
			CvMemStorage storage = CvMemStorage.create();
			// We instantiate a classifier cascade to be used for detection,
			// using the cascade definition.
			CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
					cvLoad(CASCADE_FILE));
			// We detect the faces.
			CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1,
					1, 0);
			if (faces.total() > 0) {
				// We iterate over the discovered faces and draw yellow
				// rectangles around them.
				for (int i = 0; i < faces.total(); i++) {
					CvRect r = new CvRect(cvGetSeqElem(faces, i));
					cvRectangle(frame, cvPoint(r.x(), r.y()),
							cvPoint(r.x() + r.width(), r.y() + r.height()),
							CvScalar.YELLOW, 1, CV_AA, 0);
				}

				// Save the image to a new file.
				cvSaveImage(String.format(DETECTED_FILE_NAME, counter), frame);
				counter++;
				break;
			}
		}
		 grabber.stop();
		 canvasFrame.dispose();
	}

	private static void writeToFile(IplImage image) {
		cvSaveImage(String.format(DETECTED_FILE_NAME, counter), image);
		counter++;
	}
}
