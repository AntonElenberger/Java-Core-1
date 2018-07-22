package ru.geekbrains.antonelenberger.javacore1.homework5;
/**
 * @author Anton Elenberger
 */

public class App {
    private final static int sizeOfArray = 10000000, halfSizeOfArray = sizeOfArray / 2;
    private static final float[] array = new float[sizeOfArray];
    private static final float[] halfArray1 = new float[halfSizeOfArray];
    private static final float[] halfArray2 = new float[halfSizeOfArray];
    private static long time;

    public static void main(String[] args) {
        fillArray(array);
        startBenchmark();
        final Thread holeThread = new Thread(new RunForHoleArray());
        holeThread.start();
        endBenchmark();
        startBenchmark();
        copy();
        final Thread threadForFirstHalf = new Thread(new RunForFirstHalfArray());
        final Thread threadForSecondHalf = new Thread(new RunForSecondHalfArray());
        threadForFirstHalf.start();
        threadForSecondHalf.start();
        try {
            threadForFirstHalf.join();
            threadForSecondHalf.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clay();
        endBenchmark();
    }

    public static class RunForHoleArray implements Runnable {
        @Override
        public void run() {
            for(int i = 0; i < array.length; i++) {
                array[i] = (float)(array[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        }
    }

    public static class RunForFirstHalfArray implements Runnable{
        @Override
        public void run() {
            for(int i = 0; i < halfArray1.length; i++) {
                halfArray1[i] = (float)(halfArray1[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        }
    }

    public static class RunForSecondHalfArray implements Runnable{
        @Override
        public void run() {
            for(int i = 0; i < halfArray2.length; i++) {
                halfArray2[i] = (float)(halfArray2[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }
        }
    }

    private static void startBenchmark() {
        time = System.currentTimeMillis();
    }

    private static void endBenchmark() {
        System.out.println("Расчет занял: " + (System.currentTimeMillis() - time));
    }

    private static void fillArray(float[] arr) {
        for(int i = 0; i <sizeOfArray; i++) {
            arr[i] = 1;
        }
    }

    private static void copy() {
        System.arraycopy(array,0,halfArray1,0,halfSizeOfArray);
        System.arraycopy(array,halfSizeOfArray,halfArray2,0,halfSizeOfArray);
    }

    private static void clay() {
        System.arraycopy(halfArray1,0,array,0,halfSizeOfArray);
        System.arraycopy(halfArray2,0,array,halfSizeOfArray,halfSizeOfArray);
    }
}
