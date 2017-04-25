import java.util.*;
import java.io.*;

public class TrainNet{
    int TRAIN_IMAGES = 5000;
    int NUM_PIXELS = 784;
    int NUM_OUTPUTS = 10;
    int NUM_LABELS = 11000;
    double ALPHA = .3; //learning rate (between 0-1)
    double [][] images; //stores each pixel of each image
    double [][] weights = new double [NUM_OUTPUTS][NUM_PIXELS+1]; //each of the 10 outputs has a weight for each input pixel plus a bias weight
    int [] labels = new int[NUM_LABELS]; //correct labels of input images
    double [] outputs = new double [NUM_OUTPUTS]; //output units of the network (g(in))
    double [] errors = new double[NUM_OUTPUTS]; //errors of each output unit

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

    public void parseImages(){
        images = new double [TRAIN_IMAGES][NUM_PIXELS+1];
        int height, width;
        int count = 0;

        File trainDir = new File("./train_images");
        File[] trainImages = trainDir.listFiles();

        //read from the train_images folder
        if (trainImages != null) {
                //for each image, read it in
                for (File img : trainImages) {
                    if(count == TRAIN_IMAGES){
                        // System.out.println("BREAK. Num imgs: " + images.length);
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
                            //System.out.println("Images[" + count + "][" + i + "]: " + images[count][i]);
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

            for (int j = 0; j < NUM_PIXELS; j++) {
                in += weights[i][j]*pixels[j];
            //System.out.println("in += weights["+i+"]["+j+"]*pixels["+j+"]");
            }

            //apply sigmoid activation function
            g = 1/(1 + Math.exp(-1*in));

            //store to array of outputs
            outputs[i] = g;

            //initialize max to first output value
            if( i == 0){
                max = outputs[i];
                answer = 1;
            }

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
        //System.out.println("Errors["+out_unit+"] = " + errors[out_unit]);

    }

    //description: traverses through weights and updates them
    public void updateWeights(int img){
        double gprime = 0;

        for (int w = 0; w < NUM_OUTPUTS ; w++) {    //loop through output units
            for (int i = 0; i < NUM_PIXELS+1; i++) { //loop through inputs
                gprime = outputs[w]*(1 - outputs[w]);

                //update weight: W += Alpha*Error*gprime*weight
                weights[w][i] += ALPHA*errors[w]*gprime*images[img][i];
                //System.out.println("Weights["+w+"]["+i+"]: " + weights[w][i]);
            }
        }

    }

    public int trainNetwork(){
        int output = 0;
        int count = 0;
        int correct = 0;
        int incorrect = 0;

        for(int img = 0; img < TRAIN_IMAGES; img++){

            //compute output weights to determine output of network
            output = computeOutputs(images[img]);

            if(output == labels[img]){
                //System.out.println("CORRECT");
                correct++;
            }
            else{
                //System.out.println("WRONG");
                incorrect++;
            }

            //for each output unit of the network, compute the error
            for(int out_unit = 0; out_unit < NUM_OUTPUTS; out_unit++){
                computeError(output, labels[img], out_unit);
            }

            //update the weights
            updateWeights(img);
            count++;
        }

        return correct;
    }

    public static void main(String[] args) {
        int MAX_ITER = 300;
        TrainNet train = new TrainNet();
        int correct = 0;

        //read from train directory and fill images[][] with pixels
        train.parseImages();

        //parse labels
        train.parseLabels();

        //train the network MAX_ITER times
        for (int iter = 0; iter < MAX_ITER; iter++) {
            correct += train.trainNetwork();
        }

        System.out.println("Learning rate: " + train.ALPHA);
        System.out.println("\nTotal runs: " + MAX_ITER*train.TRAIN_IMAGES);
        System.out.println("Num correct: " + correct);
        System.out.println("Accuracy: " + ((double)correct/(MAX_ITER*train.TRAIN_IMAGES)*100)+ "%");

    }
}
