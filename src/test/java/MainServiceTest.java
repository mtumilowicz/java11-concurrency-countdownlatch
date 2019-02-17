import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by mtumilowicz on 2019-02-17.
 */
public class MainServiceTest {
    
    @Test
    public void simulation() throws InterruptedException {
        var latch = new CountDownLatch(2);
        var mainService = new MainService(latch);
        var configService1 = new ConfigService(1, latch);
        var configService2 = new ConfigService(2, latch);

        mainService.start();
        configService1.start();
        configService2.start();
        
        mainService.join();
    }
}
