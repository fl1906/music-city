package top.flya.system.xxlJob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorUtils {

    // 定义一个方法用于安排定时任务，同时接受传入的参数
    public static void scheduleTask(String timeString, RunnableWithParams task, Object... params) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date targetTime = sdf.parse(timeString);
        long initialDelay = targetTime.getTime() - System.currentTimeMillis();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // 创建一个任务实例，可以将传入的参数传递给任务逻辑
        Runnable runnable = () -> {
            task.run(params);
        };

        scheduler.schedule(runnable, initialDelay, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        try {
            String targetTimeString = "2023-08-20 15:30:00"; // Change this to your desired time

            // 创建一个任务逻辑，接受参数并在任务执行时使用
            RunnableWithParams task = (params) -> {
                System.out.println("Task executed at: " + new Date());
                if (params.length > 0) {
                    System.out.println("Parameter received: " + params[0]);
                }
                // 在这里添加你的任务逻辑
            };

            String parameter = "Hello, World!"; // 传递给任务逻辑的参数

            // 调用方法安排定时任务，并传递参数
            scheduleTask(targetTimeString, task, parameter);

            System.out.println("Scheduled task for: " + targetTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // 定义一个函数式接口，允许任务逻辑接受参数
    @FunctionalInterface
    public interface RunnableWithParams {
        void run(Object... params);
    }
}
