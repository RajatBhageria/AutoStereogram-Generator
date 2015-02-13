package br.gfca.openstereogram.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import br.gfca.openstereogram.gui.StereogramWindow;
import br.gfca.openstereogram.stereo.ImageManipulator;

public class TestImageManipulator {
	public static void main(String[] args) throws IOException {
		testDepthMapResizing();
		testTextMapGeneration();
	}

	/**
	 * 
	 */
	private static void testTextMapGeneration() {
		StereogramWindow sw1 = new StereogramWindow(
				ImageManipulator.generateTextDepthMap("ASDF", 150, 640, 480));
		sw1.setVisible( true );
	}

	/**
	 * @throws IOException
	 */
	private static void testDepthMapResizing() throws IOException {
		BufferedImage original = ImageIO.read( new File("./images/depthMaps/Struna.jpg") );
		StereogramWindow sw1 = new StereogramWindow( original );
		sw1.setVisible( true );
		
		StereogramWindow sw2 = new StereogramWindow( ImageManipulator.resizeDepthMap(original, 800, 600) );
		sw2.setVisible( true );
	}
}
