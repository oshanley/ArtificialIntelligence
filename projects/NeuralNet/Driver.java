public class Driver{

    public void train(){
        NeuralNet train = new NeuralNet();
        int correct = 0;
        int iter = 0;
        double accuracy = 0;

        System.out.println("\n====== TRAINING ======");

        //read from train directory and fill images[][] with pixels
        train.parseImages();

        //parse labels
        train.parseLabels();

        //train the network MAX_ITER times
        while(iter <= train.MAX_ITER && accuracy < train.STOP_ACCURACY){
            correct = 0;
            accuracy = 0;

            correct += train.trainNetwork();
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
        double accuracy = 0;

        System.out.println("\n====== TESTING ======");
        //read from train directory and fill images[][] with pixels
        test.parseImages();

        //parse labels
        test.parseLabels();

        //read in weights
        test.readWeights();

        correct = test.testNetwork();
        accuracy = (double)correct/test.TRAIN_IMAGES;

        System.out.println("\nTest accuracy: " + accuracy);
    }

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.train();
        driver.test();
    }

}
