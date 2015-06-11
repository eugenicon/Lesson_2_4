
/*

ѕросуммировать числа от 1 до 1 000 000 000, использу€ потоки
–азбить вычисление на 1-50 потоков, т.е. сначала вычисление выполн€етс€ в 1м потоке,
затем в 2х, 3х и т.д. «америть врем€ вычислени€ каждого из вариантов. ќпределить,
с каким количеством потоков врем€ выполнени€ минимально.

*/

import java.util.ArrayList;
import java.util.Date;

public class Threads_1 {

    public static void main(String[] args) {
        threadCalcTest();
    }

    public static void threadCalcTest() {
        int numberOfThreads = 0, maxThreads = 50, fastestNumberOfThreads = 0;
        long finishNum = 1_000_000_000L;
        long fastestSpeed = Long.MAX_VALUE, currentSpeed;
        long startTime, finishTime;
        long result;

        ArrayList<Thread> runningThreads = new ArrayList<>();

        for (int i = 1; i <= maxThreads; i++) {
            startTime = System.currentTimeMillis();

            System.out.println(String.format(
                    "%s threads started at %s",
                    ++numberOfThreads, new Date(startTime)));

            prepareThreads(runningThreads, numberOfThreads, finishNum);

            //long result = calculateResultUsingLoop(runningThreads);
            result = calculateResultUsingSleep(runningThreads);

            finishTime = System.currentTimeMillis();
            currentSpeed = finishTime - startTime;

            System.out.println(String.format(
                    "Calculation finished at %s\n"
                            + "Result: %s\n"
                            + "Total time spent with %s threads: %s mills\n"
                            + "---------------------------------------------",
                    new Date(finishTime), result, numberOfThreads, currentSpeed));

            if (currentSpeed < fastestSpeed){
                fastestSpeed = currentSpeed;
                fastestNumberOfThreads = numberOfThreads;
            }
        }

        System.out.println(String.format("Best speed at %s threads for %s mills", fastestNumberOfThreads, fastestSpeed));

    }

    private static void prepareThreads(ArrayList<Thread> runningThreads, int numberOfThreads, long finishNum) {

        long step = finishNum / numberOfThreads;

        runningThreads.clear();

        for (long i = 1, nextFinishNum, startNum = 0; i <= numberOfThreads; i++) {
            nextFinishNum = Math.min(startNum + step, finishNum);
            Thread thread = new Calc(startNum, nextFinishNum);
            thread.start();
            runningThreads.add(thread);
            startNum = nextFinishNum + 1;
        }

    }

    private static long calculateResultUsingLoop(ArrayList<Thread> runningThreads) {
        boolean allThreadsFinished;
        long result;

        do {
            result = 0;
            allThreadsFinished = true;
            for (Thread thread : runningThreads) {
                if (thread.isAlive()) {
                    allThreadsFinished = false;
                } else {
                    result += ((Calc) thread).getResult();
                }
            }
        } while (!allThreadsFinished);

        return result;
    }

    private static long calculateResultUsingSleep(ArrayList<Thread> runningThreads) {

        long result = 0;

        for (Thread thread : runningThreads) {
            if (thread.isAlive()) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Thread thread : runningThreads) {
                result += ((Calc) thread).getResult();
        }

        return result;
    }

}

class Calc extends Thread {

    private final boolean SHOW_DEBUG_INFO = false;
    private long start;
    private long finish;
    private long result;

    public Calc(long start, long finish) {
        this.start = start;
        this.finish = finish;
    }

    public long getResult() {
        return result;
    }

    @Override
    public void run() {
        if (SHOW_DEBUG_INFO)
            System.out.println(String.format("Calculating from %s to %s", this.start, this.finish));

        for (; start <= finish; start++) {
            result += start;
        }
    }
}