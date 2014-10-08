package br.gfca.openstereogram;

import br.gfca.openstereogram.gui.StereogramWindow;
import br.gfca.openstereogram.stereo.StereogramGenerator;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;

/**
 * Supplement to the OpenStereogram project that allows for batch processing
 * of images.
 * 
 * To do: add better error handling
 * 
 * @author Nick Meyer
 * @version 1.0
 */
public class BatchStereogram {

    /**
     * For testing purposes only
     * @param args 
     */
    public static void main(String[] args) {
        //test:
        batchTexturedSIRD(new File("images/depthMaps"),
                new File("images/texturePatterns/"), new File("images/newnew/"),
                640, 480, 14f, 2.5f,
                12f, 0f, 72, 72, true);
    }
    //Adds only image files with valid extensions
    private static final List<String> imageFormats;
    //Initializes imageFormats list

    static {
        imageFormats = new ArrayList<String>();
        imageFormats.add("png");
        imageFormats.add("jpg");
        imageFormats.add("bmp");
        imageFormats.add("gif");
        imageFormats.add("jpeg");
    }
    static boolean echoOn = false;

    public static void echoOn() {
        echoOn = true;
    }

    public static void echoOff() {
        echoOn = false;
    }

    /**
     * Generates a list of images in the specified folder (not recursive)
     * @param folderPath the path of the folder to use
     * @return List of files (images)
     */
    public static List<File> imageFiles(File folderPath, boolean sort) {
        if (echoOn) {
            System.out.println("Searching for image files");
        }
        List<File> results = new ArrayList<File>();
        File[] files = folderPath.listFiles();
        int count = 0;
        for (File file : files) {
            String name = file.getName();
            if (file.isFile()
                    && imageFormats.contains(name.substring(name.indexOf(".") + 1).toLowerCase())) {
                results.add(file);
                count++;
            }
        }
        if (echoOn) {
            System.out.println(count + " image files queued");
        }
        if (sort) {
            Collections.sort(results);
        }
        return results;
    }

    /**
     * Generates an SIRD for all images in dirIn, outputting in png format
     * in directory dirOut
     * @param dirIn
     * @param dirOut
     * @param color1
     * @param color2
     * @param color3
     * @param color1Intensity
     * @param width
     * @param height
     * @param observationDistanceInches
     * @param eyeSeparationInches
     * @param maxDepthInches
     * @param minDepthInches
     * @param horizontalPPI
     * @param sort 
     */
    public static void batchSIRD(File dirIn, File dirOut, Color color1,
            Color color2, Color color3, float color1Intensity, int width,
            int height, float observationDistanceInches,
            float eyeSeparationInches, float maxDepthInches,
            float minDepthInches, int horizontalPPI, boolean sort) {
        if (echoOn) {
            System.out.println("Beginning batch operation");
        }
        List<File> files = imageFiles(dirIn, sort);
        batchSIRD(files, dirOut, color1, color2, color3, color1Intensity, width,
                height, observationDistanceInches, eyeSeparationInches,
                maxDepthInches, minDepthInches, horizontalPPI, sort);
    }

    /**
     * 
     * @param files
     * @param dirOut
     * @param color1
     * @param color2
     * @param color3
     * @param color1Intensity
     * @param width
     * @param height
     * @param observationDistanceInches
     * @param eyeSeparationInches
     * @param maxDepthInches
     * @param minDepthInches
     * @param horizontalPPI 
     */
    public static void batchSIRD(List<File> files, File dirOut, Color color1,
            Color color2, Color color3, float color1Intensity, int width,
            int height, float observationDistanceInches,
            float eyeSeparationInches, float maxDepthInches,
            float minDepthInches, int horizontalPPI, boolean sort) {
        if (echoOn) {
            System.out.println("Creating output directory");
        }
        if (!dirOut.exists()) {
            if (dirOut.mkdir()) {
                if (echoOn) {
                    System.out.println("Successfully created output directory");
                }
            } else {
                if (echoOn) {
                    System.out.println("Output directory creation failed");
                }
            }
        } else if (dirOut.isDirectory()) {
            if (echoOn) {
                System.out.println("Output directory already exists");
            }
        } else {
            if (echoOn) {
                System.out.println("Error: file exists with desired directory name");
            }
        }
        for (File f : files) {
            if (f.isDirectory()) {
                batchSIRD(f,dirOut,color1,color2,color3,color1Intensity,width,
                        height,observationDistanceInches,eyeSeparationInches,
                        maxDepthInches,minDepthInches,horizontalPPI,sort);
            } else {
                File outputFile = new File(dirOut, f.getName().substring(0, f.getName().lastIndexOf(".")) + ".png");
                saveFile(outputFile, generateSIRD(f, color1, color2, color3,
                        color1Intensity, width, height,
                        observationDistanceInches,
                        eyeSeparationInches, maxDepthInches,
                        minDepthInches, horizontalPPI, false),
                        "png");
            }
        }
        if (echoOn) {
            System.out.println("Finished batch operation");
        }
    }

    /**
     * Generates a TexturedSIRD for all images in dirIn, outputting in png
     * format in directory dirOut
     * @param dirIn input directory
     * @param tIn path of texture or directory containing textures.<br>
     *            If directory, loops through textures in directory tIn.
     *            If not directory, uses the texture image at that path for all.
     * @param dirOut output directory
     */
    public static void batchTexturedSIRD(File dirIn, File tIn, File dirOut,
            int width, int height, float observationDistanceInches,
            float eyeSeparationInches, float maxDepthInches, float minDepthInches,
            int horizontalPPI, int verticalPPI, boolean sort) {
        if (echoOn) {
            System.out.println("Beginning batch operation");
        }
        List<File> files = imageFiles(dirIn, sort);
        List<File> textures = new ArrayList<File>();
        if (!tIn.exists()) {
            if (echoOn) {
                System.out.println("Error: texture file/dir does not exist");
            }
        } else if (tIn.isDirectory()) {
            if (echoOn) {
                System.out.println("Texture directory detected");
            }
            textures.addAll(imageFiles(tIn, sort));
        } else {
            if (echoOn) {
                System.out.println("Single texture file detected");
            }
            textures.add(tIn);
        }
        batchTexturedSIRD(files, textures, dirOut, width, height,
                observationDistanceInches, eyeSeparationInches, maxDepthInches,
                minDepthInches, horizontalPPI, verticalPPI, sort);
    }

    public static void batchTexturedSIRD(List<File> filesIn, List<File> textures,
            File dirOut, int width, int height, float observationDistanceInches,
            float eyeSeparationInches, float maxDepthInches, float minDepthInches,
            int horizontalPPI, int verticalPPI, boolean sort) {
        if (echoOn) {
            System.out.println("Creating output directory");
        }
        if (!dirOut.exists()) {
            if (dirOut.mkdir()) {
                if (echoOn) {
                    System.out.println("Successfully created output directory");
                }
            } else {
                if (echoOn) {
                    System.out.println("Output directory creation failed");
                }
            }
        } else if (dirOut.isDirectory()) {
            if (echoOn) {
                System.out.println("Output directory already exists");
            }
        } else {
            if (echoOn) {
                System.out.println("Error: file exists with desired directory name");
            }
        }
        //todo
        Queue<File> tQueue = new LinkedList<File>();
        tQueue.addAll(textures);
        for (File f : filesIn) {
            File outputFile = new File(dirOut, f.getName().substring(0, f.getName().lastIndexOf(".")) + ".png");
            saveFile(outputFile, generateTexturedSIRD(f, tQueue.peek(),
                    width, height, observationDistanceInches, eyeSeparationInches,
                    maxDepthInches, minDepthInches, horizontalPPI, verticalPPI,
                    false), "png");
            tQueue.add(tQueue.poll());
        }
        if (echoOn) {
            System.out.println("Finished batch operation");
        }
    }

    /** Taken and modified from StereogramWindow, outputs the given
     * BufferedImage at the given path
     * 
     * @param filePath path to output to
     * @param image BufferedImage to save
     */
    private static void saveFile(File file, BufferedImage image, String format) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ImageIO.write(image, format, file);
            if (echoOn) {
                System.out.println("Successfully saved " + file.getPath());
            }
        } catch (IOException e) {
            System.err.println(e);
            System.out.println("Error saving " + file.getPath());
        }
    }

    //orig: dPath = "./images/depthMaps/Dino.jpg";
    /**
     * Generates SIRD using color pattern specified by the three color
     * parameters
     * @param dPath
     * @param disp
     * @return
     */
    public static BufferedImage generateSIRD(File dPath, Color color1,
            Color color2, Color color3, float color1Intensity, int width,
            int height, float observationDistanceInches,
            float eyeSeparationInches, float maxDepthInches,
            float minDepthInches, int horizontalPPI, boolean disp) {
        BufferedImage depthMap = getImage(dPath);
        final BufferedImage stereogram = StereogramGenerator.generateSIRD(
                depthMap,
                color1, color2, color3, color1Intensity,
                width, height,
                observationDistanceInches, eyeSeparationInches,
                maxDepthInches, minDepthInches,
                horizontalPPI);
        if (disp) {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new StereogramWindow(stereogram).setVisible(true);
                }
            });
        }
        return stereogram;
    }

    //orig: dPath = "./images/depthMaps/Dino.jpg"
    //      tPath = "./images/texturePatterns/RANDOM.jpg"
    /**
     * 
     * @param dPath
     * @param tPath
     * @param disp
     * @return
     */
    public static BufferedImage generateTexturedSIRD(File dPath, File tPath,
            int width, int height, float observationDistanceInches,
            float eyeSeparationInches, float maxDepthInches,
            float minDepthInches, int horizontalPPI, int verticalPPI,
            boolean disp) {
        BufferedImage depthMap = getImage(dPath);
        BufferedImage texturePattern = getImage(tPath);

        final BufferedImage stereogram = StereogramGenerator.generateTexturedSIRD(
                depthMap, texturePattern,
                width, height,
                observationDistanceInches, eyeSeparationInches,
                maxDepthInches, minDepthInches,
                horizontalPPI, verticalPPI);

        if (disp) {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new StereogramWindow(stereogram).setVisible(true);
                }
            });
        }
        return stereogram;
    }

    private static BufferedImage getImage(File file) {
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println(e);
        }
        return bf;
    }
}