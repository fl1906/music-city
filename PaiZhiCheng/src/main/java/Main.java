import lombok.SneakyThrows;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

import static org.apache.xmlbeans.impl.schema.StscState.start;

public class Main {


    public static final int max = 100;

    public static int count = 0;

    public static Semaphore semaphoreA =new Semaphore(1);
    public static Semaphore semaphoreB =new Semaphore(0);
    @SneakyThrows
    public static void main(String[] args) throws InterruptedException {


        CompletableFuture.runAsync(()->{
            for (int i = 0; i < max/2; i++) {
                try {
                    semaphoreB.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("B");
                count++;
                semaphoreA.release();
            }
        });


        CompletableFuture.runAsync(()->{
            for (int i = 0; i < max/2; i++) {
                try {
                    semaphoreA.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("A");
                count++;
                semaphoreB.release();
            }
        });


        Thread.sleep(1000);

        System.out.println("count = " + count);
    }
}
