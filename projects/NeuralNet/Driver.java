/*
Olivia Shanley
Project III: Handwritten Digit Recognition
CSC380 Artificial Intelligence

Description: This file is the driver for training and testing a neural network

*/

import java.util.*;
import java.io.*;

public class Driver{

    public void train(){
        NeuralNet train = new NeuralNet();
        int correct = 0;
        int iter = 0;
        int output = 0;
        double accuracy = 0;
        File trainDir = new File("./train_images");

        System.out.println("\n====== TRAINING ======");

        //read from train directory and fill images[][] with pixels
        train.parseImages(trainDir, train.TRAIN_IMAGES);

        //parse labels
        train.parseLabels();

        //train the network MAX_ITER times or until STOP_ACCURACY is reached
        while(iter <= train.MAX_ITER && accuracy < train.STOP_ACCURACY){
            correct = 0;
            accuracy = 0;

            //for every image, train the network
            for(int img = 0; img < train.TRAIN_IMAGES; img++){

                //compute output weights to determine output of network
                output = train.computeOutputs(train.images[img]);

                if(output == train.labels[img]){
                    correct++;
                }

                //for each output unit of the network, compute the error
                for(int out_unit = 0; out_unit < train.NUM_OUTPUTS; out_unit++){
                    train.computeError(output, train.labels[img], out_unit);
                }

                //update the weights
                train.updateWeights(img);
            }

            //compute the accuracy
            accuracy = (double)correct/train.TRAIN_IMAGES;

            System.out.println("=== Epoch #" + iter + " ===");
            System.out.println("Accuracy: " + accuracy);
            iter++;
        }

        System.out.println("\nLearning rate: " + train.ALPHA);
        System.out.println("Number of training images: " + train.TRAIN_IMAGES);
        System.out.println("Total epochs: " + train.MAX_ITER);

        //write weights to a file
        train.writeWeights();
    }

    public void test(){
        NeuralNet test = new NeuralNet();
        int correct = 0;
        int output = 0;
        double accuracy = 0;
        File testDir = new File("./test_images");

        System.out.println("\n====== TESTING ======");

        //read from test directory and fill images[][] with pixels
        test.parseImages(testDir, test.TEST_IMAGES);

        //parse labels
        test.parseLabels();

        //read in weights
        test.readWeights();

        //for every image, test the network
        for(int img = 0; img < test.TEST_IMAGES; img++){

            //compute output weights to determine output of network
            output = test.computeOutputs(test.images[img]);

            //check if decision is correct (offset by the training labels)
            if(output == test.labels[10000 + img]){
                correct++;
            }

            //for each output unit of the network, compute the error
            for(int out_unit = 0; out_unit < test.NUM_OUTPUTS; out_unit++){
                test.computeError(output, test.labels[img], out_unit);
            }
        }

        //compute the accuracy
        accuracy = (double)correct/test.TEST_IMAGES;

        System.out.println("\nTest accuracy: " + accuracy);
    }

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.train();
        driver.test();
    }

}
