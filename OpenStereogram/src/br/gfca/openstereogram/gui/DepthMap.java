/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gfca.openstereogram.gui;

/**
 *
 * @author RajatBhageria
 */
public class DepthMap extends javax.swing.JFrame {
        public DepthMap()
        {
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            javax.swing.JPanel DepthMap = new javax.swing.JPanel();
            DepthMap.setBounds(20, 20, 600, 600);
            processing.core.PApplet sketch = new Kinect.DepthMap();
            DepthMap.add(sketch);
            this.add(DepthMap);
            sketch.init(); //this is the function used to start the execution of the sketch
            this.setVisible(true);
        }
    }