# java11-concurrency-countdownlatch

# preface
* https://github.com/mtumilowicz/java11-concurrency-cyclicbarrier
* latches are like barriers but one-time object (cannot be reused)
* use case: startup of application (some number of one-time tasks
should be done before other activities)

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

# project description
Suppose we have MainService that could be started only
when two other services (Service 1 and Service 2) are running.
CountDownLatch is a perfect match.