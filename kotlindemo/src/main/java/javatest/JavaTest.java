package javatest;

import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @Author leizhiheng
 * @CreateDate 2019/10/14
 * @Description
 */
public class JavaTest {
    public static void main(String[] args) {
        Demo demo = new Demo();
        Demo.DemoInner demoInner = demo.new DemoInner();
        Demo.DemoStaticInner demoStaticInner = new Demo.DemoStaticInner();

        String s = "hello world ";
        System.out.println(Integer.valueOf("010"));
        System.out.println(Integer.parseInt("010"));
        System.out.println(isNumeric("00"));

        Scheduler.Worker worker = Schedulers.newThread().createWorker();
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("in work");
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
