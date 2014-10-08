package br.gfca.openstereogram.stereo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Utility class to manipulate images. The main
 * functions of this class are image resizing and text
 * manipulation inside images.
 * @author Gustavo
 */
public class ImageManipulator {

	/**
	 * Resizes a given depth map. The resizing is made without
	 * distortion of original map regardless of the new dimensions given.
	 * The map will be resized until it touches the
	 * box measuring {@code width}x{@code height} from inside.
	 * @param original The original depth map.
	 * @param width The new map width.
	 * @param height The new map height.
	 * @return A resized depth map measuring {@code width}x{@code height}.
	 */
	public static BufferedImage resizeDepthMap( BufferedImage original, int width, int height ) {
		// create image with new map size
		BufferedImage newMap = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
		// completely fill the image with black
		Graphics g = newMap.getGraphics();
		g.setColor( new Color(0,0,0) );
		g.fillRect(0, 0, width, height);
		
		// calculate the new height based on the new width 
		int newHeight = (original.getHeight() * width) / original.getWidth();
		// if the resized depth map is going to be placed inside a higher box  
		if ( newHeight <= height ) {
			// center the map along y axis
			int centeredY = (height - newHeight) / 2;
			g.drawImage( original, 0, centeredY, width, newHeight, null);
		}
		// if the resized depth map is going to be placed inside a wider box
		else {
			// calculate the new width based on the new height
			int newWidth = (original.getWidth() * height) / original.getHeight();
			// at this point this is always the case
			if ( newWidth <= width ) {
				// center the map along x axis
				int centeredX = (width - newWidth) / 2;
				g.drawImage( original, centeredX, 0, newWidth, height, null);
			}
			// should never get here
			else {
				g.drawImage( original, 0, 0, width, height, null );
			}
		}
		return newMap;
	}
	
	public static BufferedImage resizeTexturePattern(BufferedImage original, int maxSeparation) {
		if ( original.getWidth() < maxSeparation ) {
			int newHeight = (original.getHeight() * maxSeparation) / original.getWidth();
			BufferedImage resized = new BufferedImage( maxSeparation, newHeight, BufferedImage.TYPE_INT_RGB );
			resized.getGraphics().drawImage( original, 0, 0, resized.getWidth(), resized.getHeight(), null);
			return resized;
		}
		else {
			return original;
		}
	}
	
	public static BufferedImage generateTextDepthMap(String text, int fontSize, int width, int height ) {
		BufferedImage depthMap = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
		Graphics g = depthMap.getGraphics();
		g.setColor( new Color(0,0,0) );
		g.fillRect(0, 0, width, height);
		
		Font f = g.getFont().deriveFont( Font.BOLD, fontSize );
		g.setFont( f );
		int textWidth = (int)g.getFontMetrics().getStringBounds( text, g ).getWidth();
		int textHeight = g.getFontMetrics().getAscent();
		
		g.setColor( new Color(127,127,127) );
		g.drawString( text,
				(width - textWidth) / 2,
				((height - textHeight) / 2) + textHeight );
		
		return depthMap;
	}
}