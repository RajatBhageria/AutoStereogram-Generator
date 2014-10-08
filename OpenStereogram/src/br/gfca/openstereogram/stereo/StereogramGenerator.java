package br.gfca.openstereogram.stereo;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class StereogramGenerator {

	public static BufferedImage generateSIRD( BufferedImage depthMap,
			Color color1, Color color2, Color color3, float color1Intensity,
			int width, int height,
			float observationDistanceInches, float eyeSeparationInches,
			float maxDepthInches, float minDepthInches,
			int horizontalPPI ) {
		
		depthMap = ImageManipulator.resizeDepthMap(depthMap, width, height);
		ColorGenerator colors; 
		if ( color3 == null ) {
			colors = new UnbalancedColorGenerator( color1.getRGB(), color2.getRGB(), color1Intensity );
		}
		else {
			colors = new ColorGenerator( color1.getRGB(), color2.getRGB(), color3.getRGB() );
		}
		
		BufferedImage stereogram = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] linksL = new int[width];
		int[] linksR = new int[width];
		int observationDistance = convertoToPixels(observationDistanceInches, horizontalPPI);
		int eyeSeparation = convertoToPixels(eyeSeparationInches, horizontalPPI);
		int maxdepth = getMaxDepth( convertoToPixels(maxDepthInches, horizontalPPI), observationDistance );
		int minDepth = getMinDepth( 0.55f, maxdepth, observationDistance, convertoToPixels(minDepthInches, horizontalPPI) );

		for ( int l = 0; l < height; l++ ) {
			for ( int c = 0; c < width; c++ ) {
				linksL[c] = c;
				linksR[c] = c;
			}
			
			for ( int c = 0; c < width; c++ ) {
				int depth = obtainDepth( depthMap.getRGB(c, l), maxdepth, minDepth );
				int separation = getSeparation( observationDistance, eyeSeparation, depth );
				int left = c - (separation / 2);
				int right = left + separation;
				
				if ( left >= 0 && right < width ) {
					boolean visible = true;
					
					if ( linksL[right] != right) {
						if ( linksL[right] < left) {
							linksR[linksL[right]] = linksL[right];
							linksL[right] = right;
						}
						else {
							visible = false;
						}
					}
					if ( linksR[left] != left) {
						if ( linksR[left] > right) {
							linksL[linksR[left]] = linksR[left];
							linksR[left] = left;
						}
						else {
							visible = false;
						}
					}
					
					if ( visible ) {
						linksL[right] = left;
						linksR[left] = right;
					}					
				}
			}
			
			for ( int c = 0; c < width; c++ ) {
				if ( linksL[c] == c ) {
					stereogram.setRGB( c, l, colors.getRandomColor() );
				}
				else {
					stereogram.setRGB( c, l, stereogram.getRGB(linksL[c], l) );
				}
			}
		}
		
		return stereogram;
	}

	private static int getMinDepth(float separationFactor, int maxdepth, int observationDistance, int suppliedMinDepth) {
		int computedMinDepth = (int)( (separationFactor * maxdepth * observationDistance) /
			(((1 - separationFactor) * maxdepth) + observationDistance) );
		
		return Math.min( Math.max( computedMinDepth, suppliedMinDepth), maxdepth);
	}

	private static int getMaxDepth(int suppliedMaxDepth, int observationDistance) {
		return Math.max( Math.min( suppliedMaxDepth, observationDistance), 0);
	}
	
	private static int convertoToPixels(float valueInches, int ppi) {
		return (int)(valueInches * ppi);
	}

	private static int obtainDepth(int depth, int maxDepth, int minDepth) {
		return maxDepth - ((new Color( depth )).getRed() * (maxDepth - minDepth) / 255);
	}
	
	private static int getSeparation(int observationDistance, int eyeSeparation, int depth) {
		return (eyeSeparation * depth) / (depth + observationDistance);
	}

	public static BufferedImage generateTexturedSIRD( BufferedImage depthMap, BufferedImage texturePattern,
			int width, int height,
			float observationDistanceInches, float eyeSeparationInches,
			float maxDepthInches, float minDepthInches,
			int horizontalPPI, int verticalPPI ) {
		
		depthMap = ImageManipulator.resizeDepthMap(depthMap, width, height);		
		BufferedImage stereogram = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] linksL = new int[width];
		int[] linksR = new int[width];
		int observationDistance = convertoToPixels(observationDistanceInches, horizontalPPI);
		int eyeSeparation = convertoToPixels(eyeSeparationInches, horizontalPPI);
		int maxDepth = getMaxDepth( convertoToPixels(maxDepthInches, horizontalPPI), observationDistance );
		int minDepth = getMinDepth( 0.55f, maxDepth, observationDistance, convertoToPixels(minDepthInches, horizontalPPI) );
		int verticalShift = verticalPPI / 16;
		int maxSeparation = getSeparation(observationDistance, eyeSeparation, maxDepth);
		texturePattern = ImageManipulator.resizeTexturePattern( texturePattern, maxSeparation );

		for ( int l = 0; l < height; l++ ) {
			for ( int c = 0; c < width; c++ ) {
				linksL[c] = c;
				linksR[c] = c;
			}
			
			for ( int c = 0; c < width; c++ ) {
				int depth = obtainDepth( depthMap.getRGB(c, l), maxDepth, minDepth );
				int separation = getSeparation(observationDistance, eyeSeparation, depth);
				int left = c - (separation / 2);
				int right = left + separation;
				
				if ( left >= 0 && right < width ) {
					boolean visible = true;
					
					if ( linksL[right] != right) {
						if ( linksL[right] < left) {
							linksR[linksL[right]] = linksL[right];
							linksL[right] = right;
						}
						else {
							visible = false;
						}
					}
					if ( linksR[left] != left) {
						if ( linksR[left] > right) {
							linksL[linksR[left]] = linksR[left];
							linksR[left] = left;
						}
						else {
							visible = false;
						}
					}
					
					if ( visible ) {
						linksL[right] = left;
						linksR[left] = right;
					}					
				}
			}
			
			int lastLinked = -10;
			for (int c = 0; c < width; c++) {
				if ( linksL[c] == c ) {
					if (lastLinked == c - 1) {
						stereogram.setRGB( c, l, stereogram.getRGB(c - 1, l) );
					}
					else {
						stereogram.setRGB(c, l, texturePattern.getRGB(
								c % maxSeparation,
								(l + ((c / maxSeparation) * verticalShift)) % texturePattern.getHeight() ));
					}
				}
				else {
					stereogram.setRGB( c, l, stereogram.getRGB(linksL[c], l) );
					lastLinked = c;
				}
			}
		}
		
		return stereogram;
	}
}