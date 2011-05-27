package com.sudhakar;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class TestGrabber {

	public static void main(String[] args) throws Exception {
		OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
		grabber.start();

		IplImage frame = grabber.grab();
		CanvasFrame canvasFrame = new CanvasFrame("Open Library");
		canvasFrame.setCanvasSize(frame.width(), frame.height());

		while (canvasFrame.isVisible() && (frame = grabber.grab()) != null) {
			System.out.println("Grabbing...");
		}
		grabber.stop();
        canvasFrame.dispose();
	}

}
