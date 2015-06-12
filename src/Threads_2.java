/*
Есть 10 скрипачей, 5 скрипок и 5 смычков. Чтобы начать играть, скрипачу необходимо взять
смычек и скрипку. Закончив играть, скрипач кладет инструменты на место и некоторое время ожидает,
предоставляя возможность поиграть другому скрипачу. Реализовать, используя потоки.
Избежать ситуации, когда скрипач не может поиграть, поскольку ему не хватает то смычка, то скрипки.
Избежать ситуации, когда скрипачи одновременно пытаются взать один смычек или одну скрипку.
*/

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Threads_2 {

    public static void main(String[] args) throws InterruptedException {
        int instrumentQuantity = 5;
        int artistQuantity = 10;

        System.out.println("Type 1 to exit");

        Queue<Violin> violins = new LinkedList<>();
        for (int i = 1; i <= instrumentQuantity; i++) {
            violins.add(new Violin(Violin.class.getSimpleName() + " #" + i));
        }

        Queue<FiddleBow> fiddleBows = new LinkedList<>();
        for (int i = 1; i <= instrumentQuantity; i++) {
            fiddleBows.add(new FiddleBow(FiddleBow.class.getSimpleName() + " #" + i));
        }

        for (int i = 0; i < artistQuantity; i++) {
            Artist a = new Artist(Artist.class.getSimpleName() + " #" + (i + 1));
            a.setViolinPool(violins);
            a.setFiddleBowPool(fiddleBows);

            new Thread(a).start();

            Thread.sleep(250);
        }

        Scanner sc = new Scanner(System.in);
        int exit;
        do {
            exit = sc.nextByte();
        } while (exit != 1);

        sc.close();
        System.exit(0);

    }

}

class Violin {
    private String description;

    public Violin(String description) {
        this.description = description;
    }

    public String toString() {
        return this.description;
    }
}

class FiddleBow {
    private String description;

    public FiddleBow(String description) {
        this.description = description;
    }

    public String toString() {
        return this.description;
    }
}

class Artist implements Runnable {

    private String name;

    private Queue<Violin> violinPool;
    private Queue<FiddleBow> fiddleBowPool;

    private Violin violinTaken;
    private FiddleBow fiddleBowTaken;

    public Artist(String name) {
        this.name = name;
    }

    public void setFiddleBowPool(Queue<FiddleBow> fiddleBowPool) {
        this.fiddleBowPool = fiddleBowPool;
    }

    public void setViolinPool(Queue<Violin> violinPool) {
        this.violinPool = violinPool;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        do {

            try {

                takeInstruments();

                System.out.println(String.format("%s is playing %s and %s",
                        this, violinTaken, fiddleBowTaken));

                Thread.sleep(3000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                putInstruments();
            }

        } while (true);
    }

    private void takeInstruments() throws InterruptedException {
        int waitCount;
        synchronized (violinPool) {
            waitCount = 0;
            while (violinPool.isEmpty()) {
                System.out.println(String.format("%s is waiting for %s - %s time(s)",
                        this, Violin.class.getSimpleName(), ++waitCount));
                violinPool.wait();
            }
            violinTaken = violinPool.poll();

            synchronized (fiddleBowPool) {
                waitCount = 0;
                while (fiddleBowPool.isEmpty()) {
                    System.out.println(String.format("%s is waiting for %s - %s time(s)",
                            this, FiddleBow.class.getSimpleName(), ++waitCount));
                    fiddleBowPool.wait();
                }
                fiddleBowTaken = fiddleBowPool.poll();
                fiddleBowPool.notify();
            }

            violinPool.notify();

        }

    }

    private void putInstruments() {
        if (violinTaken != null) {
            synchronized (violinPool) {
                violinPool.add(violinTaken);
                System.out.println(this + " has put " + violinTaken);
                violinTaken = null;
                violinPool.notify();

            }
        }

        if (fiddleBowTaken != null) {
            synchronized (fiddleBowPool) {
                fiddleBowPool.add(fiddleBowTaken);
                System.out.println(this + " has put " + fiddleBowTaken);
                fiddleBowTaken = null;
                fiddleBowPool.notify();
            }
        }

    }

    @Override
    public String toString() {
        return this.name;
    }
}
