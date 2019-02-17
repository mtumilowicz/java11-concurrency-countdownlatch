import java.util.concurrent.CountDownLatch;

/**
 * Created by mtumilowicz on 2019-02-17.
 */
class MainService extends Thread {

    private final CountDownLatch latch;

    MainService(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            System.out.println("Main service is waiting for others to boot");
            latch.await();
            System.out.println("Main service is running!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
