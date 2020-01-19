package javatest;

import android.annotation.SuppressLint;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;

/**
 * @Author leizhiheng
 * @CreateDate 2019/10/28
 * @Description
 */
public class RxJavaTest {

    private static int count;

    @SuppressLint("CheckResult")
    public static void main(String[] args) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                print("subscribe");
                emitter.onNext("hello");
                emitter.onNext("RxJava");
                emitter.onComplete();
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                print("onSubscribe");
            }

            @Override
            public void onNext(String s) {
                print("onNext, s: " + s);
            }

            @Override
            public void onError(Throwable e) {
                print("onError");
            }

            @Override
            public void onComplete() {
                print("onComplete");
            }
        });

        Observable.defer(new Callable<ObservableSource<String>>() {
            @Override
            public ObservableSource<String> call() throws Exception {
                print("call");
                return new ObservableSource<String>() {
                    @Override
                    public void subscribe(Observer<? super String> observer) {
                        print("subscribe");
                        observer.onNext("hello defer");
                    }
                };
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                print("accept, s = " + s);
            }
        });

        Observable.fromArray("1", "2")
                .repeat(2)
                .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        if (count > 1) {
                            return  Observable.empty();
                        } else {
                            return Observable.just(0);
                        }
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        print("accept: s = " + s);
                        count++;
                    }
                });

        Observable.just(1, 2, 3, 4).buffer(2).subscribe(new Observer<List<Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Integer> integers) {
                print("size = " + integers.size());
                for(Integer i: integers) {
                    print("i = " + i);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        Observable.just(1, 2, 3, 4, 5, 2).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                if (integer % 2 == 0) {
                    return "double";
                }
                return "single";
            }
        }).subscribe(new Observer<GroupedObservable<String, Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(GroupedObservable<String, Integer> stringIntegerGroupedObservable) {
                print("onNext");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    public static void print(String msg) {
        System.out.println(msg + "  -  " + Thread.currentThread().getName());
    }
}
