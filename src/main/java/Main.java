
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class Main {
    final static String LETTERS = "abc";
    final static int ROUTE_LENGTH = 10_000;
    final static int SIZE_TEXT = 100_000;
    public static BlockingQueue<String> arrayBlockingQueue1 = new ArrayBlockingQueue(100);
    public static BlockingQueue<String> arrayBlockingQueue2 = new ArrayBlockingQueue(100);
    public static BlockingQueue<String> arrayBlockingQueue3 = new ArrayBlockingQueue(100);

    public static AtomicInteger sumA = new AtomicInteger(0);
    public static AtomicInteger sumB = new AtomicInteger(0);
    public static AtomicInteger sumC = new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            for (int i = 0; i < ROUTE_LENGTH; i++) {
                String text = generateText(LETTERS, SIZE_TEXT);
                try {
                    arrayBlockingQueue1.put(text);
                    arrayBlockingQueue2.put(text);
                    arrayBlockingQueue3.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < ROUTE_LENGTH; i++) {
                try {
                    String rout = arrayBlockingQueue1.take();
                    numberLettersPerLine(rout, 'a', sumA);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Самое большое количество букв 'a' в строке " + sumA);
        }).start();

        new Thread(() -> {
            for (int i = 0; i < ROUTE_LENGTH; i++) {
                try {
                    String rout = arrayBlockingQueue2.take();
                    numberLettersPerLine(rout, 'b', sumB);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Самое большое количество букв 'b' в строке " + sumB);
        }).start();

        new Thread(() -> {
            for (int i = 0; i < ROUTE_LENGTH; i++) {
                try {
                    String rout = arrayBlockingQueue3.take();
                    numberLettersPerLine(rout, 'c', sumC);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Самое большое количество букв 'c' в строке " + sumC);
        }).start();
    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    public static void numberLettersPerLine(String text, char symbol, AtomicInteger sum) {

        int frequency = (int) text.chars().filter(ch -> ch == symbol).count();
        if (sum.get() < frequency) {
            sum.set(frequency);
        }
    }
}