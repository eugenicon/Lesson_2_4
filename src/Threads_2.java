/*
���� 10 ���������, 5 ������� � 5 �������. ����� ������ ������, �������� ���������� �����
������ � �������. �������� ������, ������� ������ ����������� �� ����� � ��������� ����� �������,
������������ ����������� �������� ������� ��������. �����������, ��������� ������.
�������� ��������, ����� ������� �� ����� ��������, ��������� ��� �� ������� �� ������, �� �������.
�������� ��������, ����� �������� ������������ �������� ����� ���� ������ ��� ���� �������.
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
