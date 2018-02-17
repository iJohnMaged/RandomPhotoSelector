package application;

/*
 * The ThreadedImageSearch class is used to create a new thread to do a google reverse image search from a local file.
 */

public class ThreadedImageSearch implements Runnable {

    private Thread searchThread;
    private String imagePath;

    public ThreadedImageSearch(String imagePath){
        this.imagePath = imagePath;
    }
    @Override
    public void run() {
        GoogleImageSearch.search(imagePath);
    }

    public void start(){
        if(searchThread == null){
            searchThread = new Thread(this, imagePath);
            searchThread.start();
        }
    }
}
