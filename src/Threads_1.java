
import java.util.ArrayList;
import java.util.Date;

public class Threads_1 {

    public static void main(String[] args) {
        new Threads_1().threadCalcTest();
    }

    public void threadCalcTest() {
        int numberOfThreads = 0;
        long finishNum = 1_000_000_000L;
        long speed, currentSpeed = Long.MAX_VALUE;

        ArrayList<Thread> runningThreads = new ArrayList<>();

        do {
            speed = currentSpeed;
            long startTime = System.currentTimeMillis();
            System.out.println(String.format(
                    "%s threads started at %s",
                    ++numberOfThreads, new Date(startTime)));

            prepareThreads(runningThreads, numberOfThreads, finishNum);
            long result = calculateResult(runningThreads);

            long finishTime = System.currentTimeMillis();
            currentSpeed = finishTime - startTime;

            System.out.println(String.format(
                    "Calculation finished at %s\n"
                            + "Result: %s\n"
                            + "Total time spent with %s threads: %s mills\n"
                            + "---",
                    new Date(finishTime), result, numberOfThreads, currentSpeed));

        } while (currentSpeed <= speed);

        System.out.println(String.format("Best speed at %s threads for %s mills", --numberOfThreads, speed));

    }

    private void prepareThreads(ArrayList<Thread> runningThreads, int numberOfThreads, long finishNum) {

        long step = finishNum / numberOfThreads;

        runningThreads.clear();

        for (long i = 0, steppedFinishNum, startNum = 0; i < numberOfThreads; i++) {
            steppedFinishNum = Math.min(startNum + step, finishNum);
            Thread thread = new Calc(startNum, steppedFinishNum);
            thread.start();
            runningThreads.add(thread);
            startNum = steppedFinishNum + 1;
        }

    }

    private long calculateResult(ArrayList<Thread> runningThreads) {
        boolean allThreadsFinished;
        long result;

        do {
            result = 0;
            allThreadsFinished = true;
            for (Thread thread : runningThreads) {
                if (thread.isAlive()) {
                    allThreadsFinished = false;
                } else {
                    result += ((Calc) thread).result;
                }
            }
        } while (!allThreadsFinished);

        return result;
    }

    class Calc extends Thread {

        long start;
        long finish;
        long result;

        public Calc(long start, long finish) {
            this.start = start;
            this.finish = finish;
            System.out.println(String.format("Calculating from %s to %s", this.start, this.finish));
        }

        @Override
        public void run() {
            for (; start <= finish; start++) {
                result += start;
            }
        }
    }

}