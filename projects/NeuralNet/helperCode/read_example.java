public class read_example {
    public static void main(String[] args) {
	SillyImage si = new SillyImage("./train_images/image000001.bin");
	si.printImage();

	// Example of reading in the labels.
	LabelList ll = new LabelList("./labels.bin");
	//ll.printList();
    }

}
