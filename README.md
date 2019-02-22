[![Build Status](https://travis-ci.com/mtumilowicz/java11-concurrency-countdownlatch.svg?branch=master)](https://travis-ci.com/mtumilowicz/java11-concurrency-countdownlatch)

# java11-concurrency-countdownlatch

# preface
* https://github.com/mtumilowicz/java11-concurrency-cyclicbarrier
* latches are like barriers but can be used only once
* use case: startup of application (some number of one-time tasks
should be done before other tasks)
* use case: Ensuring that a computation does not proceed until resources it needs have
been initialized. A simple binary (two-state) latch could be used to indicate
“Resource R has been initialized”, and any activity that requires R would
wait first on this latch.

# java
* latch is represented in Java by `CountDownLatch` class
* only one constructor: `CountDownLatch(int count)`
* methods:
    * `void	await()` - causes the current thread to wait 
    until the latch has counted down to zero, unless the 
    thread is interrupted
    * `boolean	await​(long timeout, TimeUnit unit)`
    * `void	countDown()` - decrements the count of the latch, 
    releasing all waiting threads if the count reaches zero
    * `long	getCount()` - current count.
* FutureTask also acts like a latch.
   * A computation represented
   by a FutureTask is implemented with a Callable
   * Once a FutureTask
   enters the completed state, it stays in that state forever.
   * Future.get depends on the state of the task: 
      * If it is completed, get returns the result immediately, 
      * otherwise blocks until the task transitions to the completed state and then returns the result or throws an exception

# project description
Suppose we have main service that could be started only
when two other config services are already running.
`CountDownLatch` is a perfect match.
1. main service
    ```
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
    ```
1. config service
    ```
    class ConfigService extends Thread {
    
        private final int id;
        private final CountDownLatch latch;
    
        ConfigService(int id, CountDownLatch latch) {
            this.id = id;
            this.latch = latch;
        }
    
        @Override
        public void run() {
            try {
                System.out.println("Service " + id + " is booting...");
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50) + 4);
                System.out.println("Service " + id + " is running!");
            } catch (InterruptedException e) {
                // not used
            } finally {
                this.latch.countDown();
            }
        }
    }
    ```
1. simulation
    ```
    var latch = new CountDownLatch(2);
    var mainService = new MainService(latch);
    var configService1 = new ConfigService(1, latch);
    var configService2 = new ConfigService(2, latch);

    mainService.start();
    configService1.start();
    configService2.start();
    
    mainService.join();
    ```
    could produce output
    ```
    Main service is waiting for others to boot
    Service 2 is booting...
    Service 1 is booting...
    Service 1 is running!
    Service 2 is running!
    Main service is running!
    ```
