package br.gfca.openstereogram.stereo;

import java.util.Random;

/**
 * Color generator to randomly select a color from a
 * set of two colors, respecting a fixed hit percentage
 * for both colors. (E.g. color 1: 65% times / color 2: 35% times)
 * @author Gustavo
 */
public class UnbalancedColorGenerator extends ColorGenerator {

	private float color1Intensity; // hit percentage for the first color
	
	/**
	 * Creates a random color generator for 2 defined colors.
	 * @param color1 The first color.
	 * @param color2 The second color.
	 * @param color1Intensity The hit percentage for the first color.
	 * 1 - {@code color1Intensity} will be the hit percentage for
	 * the second color.
	 */
	public UnbalancedColorGenerator(int color1, int color2, float color1Intensity) {
		this.color1Intensity = color1Intensity;
		this.colors = new int[2];
		this.colors[0] = color1;
		this.colors[1] = color2;
		this.randomizer = new Random();
	}

	/**
	 * Select randomly one of two colors.
	 * @return A randomly selected color.
	 */
	@Override
	public int getRandomColor() {
		return this.randomizer.nextFloat() < color1Intensity ? colors[0] : colors[1];
	}
}