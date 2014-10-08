package br.gfca.openstereogram;

import br.gfca.openstereogram.gui.StereogramWindow;
import br.gfca.openstereogram.stereo.StereogramGenerator;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SimpleStereogram {

	public void generateSIRD() {
		BufferedImage depthMap = getImage("./images/depthMaps/Struna.jpg");
		final BufferedImage stereogram = StereogramGenerator.generateSIRD(
				depthMap,
				Color.BLACK, Color.WHITE, Color.RED, 0.5f,
				640, 480,
				14f, 2.5f,
				12f, 0f,				
				72);

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StereogramWindow(stereogram).setVisible(true);
			}
		});
	}
	
	public void generateTexturedSIRD() {
		BufferedImage depthMap = getImage("./images/depthMaps/Struna.jpg");
		BufferedImage texturePattern = getImage("./images/texturePatterns/RAND7.jpg");
		
		final BufferedImage stereogram = StereogramGenerator.generateTexturedSIRD(
				depthMap, texturePattern,
				640, 480,
				14f, 2.5f,
				12f, 0f,
				72, 72);

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new StereogramWindow(stereogram).setVisible(true);
			}
		});
	}

	private BufferedImage getImage(String file) {
		BufferedImage bf = null;
		try {
			bf = ImageIO.read( new File(file) );
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bf;
	}
}