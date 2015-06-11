/*
Есть 10 скрипачей, 5 скрипок и 5 смычков. Чтобы начать играть, скрипачу необходимо взять
смычек и скрипку. Закончив играть, скрипач кладет инструменты на место и некоторое время ожидает,
предоставляя возможность поиграть другому скрипачу. Реализовать, используя потоки.
Избежать ситуации, когда скрипач не может поиграть, поскольку ему не хватает то смычка, то скрипки.
Избежать ситуации, когда скрипачи одновременно пытаются взать один смычек или одну скрипку.
*/

import java.util.Arrays;

public class Threads_2 {

    public static void main(String[] args) {
        int instrumentQuantity = 5;
        int artistQuantity = 5;

        Violin violins[] = new Violin[instrumentQuantity];
        for (int i = 0; i < violins.length; i++) {
            violins[i] = new Violin();
        }

        FiddleBow fiddleBows[] = new FiddleBow[instrumentQuantity];
        for (int i = 0; i < violins.length; i++) {
            fiddleBows[i] = new FiddleBow();
        }

        System.out.println(Arrays.toString(violins));
        System.out.println(Arrays.toString(fiddleBows));

    }

}

abstract class SimpleEntity{
    protected static int totalInstances;
    protected String description;

    public SimpleEntity() {
        this.description = this.getClass().getSimpleName() + " #" + ++totalInstances;
    }

    @Override
    public String toString() {
        return this.description;
    }
}

class Violin extends SimpleEntity{

}

class FiddleBow extends SimpleEntity{

}

class Artist extends Thread{
    @Override
    public void run() {
        super.run();
    }
}
