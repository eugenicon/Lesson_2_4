/*
Есть 10 скрипачей, 5 скрипок и 5 смычков. Чтобы начать играть, скрипачу необходимо взять
смычек и скрипку. Закончив играть, скрипач кладет инструменты на место и некоторое время ожидает,
предоставляя возможность поиграть другому скрипачу. Реализовать, используя потоки.
Избежать ситуации, когда скрипач не может поиграть, поскольку ему не хватает то смычка, то скрипки.
Избежать ситуации, когда скрипачи одновременно пытаются взать один смычек или одну скрипку.
*/

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Threads_2 {

    public static void main(String[] args) {
        int instrumentQuantity = 5;
        int artistQuantity = 10;

        Queue<Violin> violins = new LinkedList<>();
        for (int i = 1; i <= instrumentQuantity; i++) {
            violins.add(new Violin(Violin.class.getSimpleName() + " #" + i));
        }

        Queue<FiddleBow> fiddleBows = new LinkedList<>();
        for (int i = 1; i <= instrumentQuantity; i++) {
            fiddleBows.add(new FiddleBow(FiddleBow.class.getSimpleName() + " #" + i));
        }

        Artist artists[] = new Artist[artistQuantity];
        for (int i = 0; i < artists.length; i++) {
            Artist a = new Artist(Artist.class.getSimpleName() + " #" + (i + 1));
            a.setViolinPool(violins);
            a.setFiddleBowPool(fiddleBows);
            artists[i] = a;
            a.start();
        }

    }

}

class Violin{
    private String description;

    public Violin(String description) {
        this.description = description;
    }

    public String toString() {
        return this.description;
    }
}

class FiddleBow{
    private String description;

    public FiddleBow(String description) {
        this.description = description;
    }

    public String toString() {
        return this.description;
    }
}

class Artist extends Thread{

    private Queue<Violin> violinPool;
    private Queue<FiddleBow> fiddleBowPool;

    private Violin violinTaken;
    private FiddleBow fiddleBowTaken;

    public Artist(String name) {
        super(name);
    }

    public void setFiddleBowPool(Queue<FiddleBow> fiddleBowPool) {
        this.fiddleBowPool = fiddleBowPool;
    }

    public void setViolinPool(Queue<Violin> violinPool) {
        this.violinPool = violinPool;
    }

    @Override
    public void run() {
        do {
            takeInstruments();

            System.out.println(String.format("%s is playing %s and %s",
                    this, violinTaken, fiddleBowTaken));
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            putInstruments();

        }while (false);
    }

    private void takeInstruments() {
        do {
            synchronized (violinPool){
                violinTaken = violinPool.poll();
                synchronized (fiddleBowPool){
                    fiddleBowTaken = fiddleBowPool.poll();
                }
            }
        }while(violinTaken == null);
    }

    private void putInstruments() {
        synchronized (violinPool){
            violinPool.add(violinTaken);
            violinTaken = null;
        }
        synchronized (fiddleBowPool){
            fiddleBowPool.add(fiddleBowTaken);
            fiddleBowTaken = null;
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
