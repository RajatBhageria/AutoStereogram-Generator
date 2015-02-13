/**
 * 
 */
package br.gfca.openstereogram;

import br.gfca.openstereogram.gui.MainGUI;

/**
 * @author Gustavo
 *
 */
public class OpenStereogram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainGUI().setVisible(true);
			}
		});
	}
}
