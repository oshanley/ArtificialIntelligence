/*
Olivia Shanley
Project III: Handwritten Digit Recognition
CSC380 Artificial Intelligence

Description: This file contains all of the functions necessary for training and testing a neural network.

*/

import java.util.*;
import java.io.*;

public class NeuralNet{
    int TRAIN_IMAGES = 10000;   //number of images to train the network on
    int TEST_IMAGES = 1000;     //number of images to test the network on
    int NUM_PIXELS = 784;       //number of pixels of each image
    int NUM_OUTPUTS = 10;       //number of output units of the network
    int NUM_LABELS = 11000;     //number of labels in labels.bin
    int MAX_ITER = 200;         //maximum iterations of the data to be performed
    double STOP_ACCURACY = 0.95;//stopping criteria based on the accuract of the network
    double ALPHA = .3;          //learning rate (between 0-1)
    double [][] images;         //array of images and all of their pixels
    double [][] weights = new double [NUM_OUTPUTS][NUM_PIXELS+1]; //Array of weights for each output unit
    int [] labels = new int[NUM_LABELS]; //correct labels of input images
    double [] outputs = new double [NUM_OUTPUTS]; //output units of the network (g(in))
    double [] errors = new double[NUM_OUTPUTS]; //errors of each output unit

    //description: parses Labels.bin and assigns to array of labels such that labels[i] corresponds to images[i]
    public void parseLabels(){
        int height, width;

        File labelDir = new File("./labels.bin");

        //for each image, read in each pixel
        try {
    	    FileInputStream fis = new FileInputStream(labelDir);
    	    DataInputStream dis = new DataInputStream(fis);
    	    height = dis.readInt();
    	    width = dis.readInt();

    	    if (width != 1) {
    		System.err.println("Doesn't look like a list ");
    	    }
    	    else{
        	    for (int i=0; i < NUM_LABELS; i++) {
                    labels[i] = dis.readInt();
        	    }
        	    fis.close();
        	    dis.close();
        	}
        }
        catch(IOException e) {
    	    System.err.println("Error reading files ");
    	}
    }

    //description: parses and stores pixels of images from a given directory normalized to values between 0 and 1
    public void parseImages(File dir, int numImgs){
        images = new double [numImgs][NUM_PIXELS+1];
        int height, width;
        int count = 0;

        File [] imgs = dir.listFiles();

        //read from the train_images folder
        if (dir != null) {
                //for each image, read in its pixels
                for (File img : imgs) {
                    if(count == numImgs){
                        break;
                    }
                try {
            	    FileInputStream fis = new FileInputStream(img);
            	    DataInputStream dis = new DataInputStream(fis);
            	    height = dis.readInt();
            	    width = dis.readInt();

                    //read in each pixel
            	    for (int i=0; i < NUM_PIXELS+1; i++) {

                        //set bias weight
                        if(i == 0){
                            images[count][i] = -1;
                        }
                        else {
                            //divide pixel values by 255 to normalize between 0 or 1
                            images[count][i] = ((double)dis.readInt())/255;
                        }
            	    }
            	    fis.close();
            	    dis.close();
                    count++;
            	}
                catch(IOException e) {
            	    System.err.println("Error reading files ");
            	}
            }
        }
        else {
            System.out.print("ERROR: Unable to read from directory");
        }
    }

    //input: array of pixels from an image
    //output: the decision of the network
    //description: computes the output weights for a given image and selects the one with the highest value as the answer to the input
    public int computeOutputs(double [] pixels){
        double in = 0;
        int answer = 0;
        double g, max = 0;

        //calculate the output weights for a given image
        for (int i = 0; i < NUM_OUTPUTS; i++) {
            in = 0;
            g = 0;

            //initialize max to first output value
            if( i == 0){
                max = outputs[i];
                answer = 1;
            }

            //update weight[i][j]
            for (int j = 0; j < NUM_PIXELS; j++) {
                in += weights[i][j]*pixels[j];
            }

            //apply sigmoid activation function
            g = 1/(1 + Math.exp(-1*in));

            //store to array of outputs
            outputs[i] = g;

            //check if current output value is max value
            if(outputs[i] > max){
                max = outputs[i];
                answer = i;
            }
        }

        //return the decision
        return answer;
    }

    //input: output of the network; correct label of input; number of output unit the error is being calculated for
    //description: computes the error of a given output unit
    public void computeError(int output, int label, int out_unit){
        double error, y;

        //make the error on the output that matches the label higher than the others
        if(out_unit == label){
            y = 1;
        }
        else{
            y = 0;
        }

        //calculate the error
        errors[out_unit] = y - outputs[out_unit];
    }

    //description: traverses through weights and updates them
    public void updateWeights(int img){
        double gprime = 0;

        for (int w = 0; w < NUM_OUTPUTS ; w++) {
            for (int i = 0; i < NUM_PIXELS+1; i++) {
                gprime = outputs[w]*(1 - outputs[w]);

                //update weight: W += Alpha*Error*gprime*weight
                weights[w][i] += ALPHA*errors[w]*gprime*images[img][i];
            }
        }
    }

    //for testing purposes; reads in weights calculated during training
    public void readWeights(){
        File weightsDir = new File("./weights.bin");
        int rows = 0;
        int cols = 0;
        //for each image, read in each pixel
        try {
            FileInputStream fis = new FileInputStream(weightsDir);
            DataInputStream dis = new DataInputStream(fis);

            //read dimensions from file
            rows = dis.readInt();
            cols = dis.readInt();

            //initialize weights array
            weights = new double[rows][cols];

            //read weights
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols ; j++) {
                    weights[i][j] = dis.readDouble();
                }
            }

            fis.close();
            dis.close();
        }
        catch(IOException e) {
            System.err.println("Error reading files ");
        }
    }

    //writes the weights of each output unit to a file to be used during testing
    public void writeWeights(){
        FileOutputStream fos = null;
        DataOutputStream dos = null;

        try {
            fos = new FileOutputStream("weights.bin", false); //false flag overwrites any existing file
            dos = new DataOutputStream(fos);

            //write dimensions to file
            dos.writeInt(weights.length);
            dos.writeInt(weights[0].length);

            for (int i = 0; i < weights.length; i++) {
                for (int j =0; j < weights[i].length ; j++) {
                    dos.writeDouble(weights[i][j]);
                }
            }

            dos.close();
        } catch (IOException e) {
            System.err.println("ERROR: unable to write weights to file");
        }
    }
}
