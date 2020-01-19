package com.lzh.sample;

import androidx.annotation.NonNull;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.observers.EmptyCompletableObserver;
import io.reactivex.internal.operators.observable.BlockingObservableIterable;
import io.reactivex.internal.operators.observable.ObservableFromCallable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;

/**
 * @Author leizhiheng
 * @CreateDate 2019/11/1
 * @Description
 */
public class RxJavaT {
    public static void main(String args[]) {
        //createAndDefer();
//        operation();
//        errorDeal();
//        assistFunction();
//        connect();
//        stringObservable();

        flatMap();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static Integer[] items = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 14};
    private static int count;
    private static CompositeDisposable disposable = new CompositeDisposable();

    public static void stringObservable() {

    }

    public static void flatMap() {
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                println("flatMap create");
            }
        }).flatMap(new Function<String, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(String s) throws Exception {
                println("flatMap flatMap");
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        println("flatMap flatMap new ");
                    }
                });
            }
        });

        observable.subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                println("flatMap onComplete");
            }
        });
    }

    public static void connect() {
        Observable observable = Observable.intervalRange(1, 10, 500, 500, TimeUnit.MILLISECONDS);
        Observer observer1 = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                println("observer1 onNext  o  = " + o);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        Observer observer2 = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                println("observer2 onNext  o  = " + o);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer1);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        observable.subscribe(observer2);
    }

    public static void assistFunction() {
        println("==> assistFunction");
        Observable.intervalRange(1, 5, 200, 200, TimeUnit.MILLISECONDS)
                .using(new Callable<Long>() {
                    @Override
                    public Long call() throws Exception {
                        println("using call ");
                        return 1000L;
                    }
                }, new Function<Long, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Long aLong) throws Exception {
                        println("using apply aLong = " + aLong);
                        return Observable.just(aLong);
                    }
                }, new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        println("using accept aLong = " + aLong);
                    }
                })
                .throttleWithTimeout(10, TimeUnit.MILLISECONDS)
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        println("doOnComplete");
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        println("doFinally");
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        println("doOnSubscribe");
                    }
                })
                .timeInterval(TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Timed<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Timed<Object> objectTimed) {
                        println("onNext vale = " + objectTimed.value());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


//        Observable.fromArray(items)
//                .materialize()
//                .dematerialize(new Function<Notification<Integer>, Notification<Integer>>() {
//                    @Override
//                    public Notification<Integer> apply(Notification<Integer> integerNotification) throws Exception {
//                        if (integerNotification.isOnNext()) {
//                            return Notification.createOnNext(integerNotification.getValue() * 10);
//                        } else {
//                            return null;
//                        }
//                    }
//                })
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Integer integerNotification) {
//                        println("onNext integerNotification value: " + integerNotification);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        println("onError");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        println("onComplete");
//                    }
//                });


        Observable.fromArray(items)
                .delay(1000, TimeUnit.MILLISECONDS)
                .ambWith(new ObservableSource<Integer>() {
                    @Override
                    public void subscribe(Observer<? super Integer> observer) {
                        observer.onNext(10000);
                        println("ambWith onsubscribe");
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        println("ambWith = " + integer);
                    }
                });

        Observable o1 = Observable.intervalRange(1, 5, 100, 100, TimeUnit.MILLISECONDS);
        Observable o2 = Observable.fromArray(items).delay(20, TimeUnit.MILLISECONDS);
        Observable.ambArray(o1, o2).subscribe(new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                println("onNext o = " + o.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        Observable.fromArray(items)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer + 100;
                    }
                })
                .concatMap(new Function<Integer, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<Integer> apply(Integer integer) throws Exception {
                        return Observable.just(integer * 10);
                    }
                })
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        println("concatMap o = " + o.toString());
                    }
                });
    }

    public static void errorDeal() {
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                emitter.onNext("1");
//                emitter.onNext("2");
//                emitter.onError(new Throwable("post error"));
//            }
//        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends String>>() {
//            @Override
//            public ObservableSource<? extends String> apply(Throwable throwable) throws Exception {
//                return Observable.just("hello", "world");
//            }
//        }).subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                println("result :" + s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                println("onError");
//            }
//
//            @Override
//            public void onComplete() {
//                println("onComplete");
//            }
//        });

//        count = 0;
//        Observable.create(new ObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
//                println("count: " + (++count));
//                println("count: " + (++count));
//                println("retry");
//                if (count < 10) {
//                    emitter.onError(new Throwable("retry error"));
//                } else {
//                    emitter.onComplete();
//                }
//            }
//        }).
//                retry(2).
//                subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        println("retry s : " + s);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        println("retry orror e : " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        println("retry onComplete");
//                    }
//                });

        count = 0;
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//
//                emitter.onNext(++count);
//                emitter.onNext(++count);
//                emitter.onNext(++count);
//                println("onError count = " + count);
//
//                if (count < 10) {
//                    emitter.onError(new NullPointerException("send error"));
//                } else {
//                    emitter.onError(new IllegalAccessException());
//                }
//            }
//        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
//            @Override
//            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
//                println("retryWhen count = " + count);

//                if (count < 7) {
//                    return Observable.just(0);
//                } else {
//                    return null;
//                }

//                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
//                        if (throwable instanceof  NullPointerException) {
//                            return Observable.just(0);
//                        } else {
//                            return Observable.error(new Throwable("after retry"));
//                        }
//                    }
//                });

//                return throwableObservable.zipWith(Observable.range(1, 3), new BiFunction<Throwable, Integer, Integer>() {
//                    @Override
//                    public Integer apply(Throwable n, Integer i) throws Exception {
//                        return i;
//                    }
//                }).map(new Function<Integer, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(Integer integer) throws Exception {
//                        if (integer < 2) {
//                            return Observable.just(0);
//                        } else {
//                            return Observable.error(new Throwable("hahah  "));
//                        }
//                    }
//                });
//
//            }
//        }).subscribe(new Observer<Integer>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                println("integer : " + integer);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                println("onError e: " + e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//                println("onComplete");
//            }
//        });

        nums.clear();
        Observable.create((ObservableOnSubscribe<String>) new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Thread.sleep(500);
                println("load props finish");
                emitter.onNext("finished");
            }
        }).flatMap(new Function<String, ObservableSource<List<Integer>>>() {
            @Override
            public ObservableSource<List<Integer>> apply(String s) throws Exception {
                return Observable.create(new ObservableOnSubscribe<List<Integer>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Integer>> emitter) throws Exception {
                        println("load list data, data size = " + nums.size());
                        nums.add(0);
                        nums.add(0);
                        if (nums.size() < 6) {
                            emitter.onError(new NotFullException());
                        } else {
                            emitter.onNext(nums);
                        }
                    }
                }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                        println("start retry");
//                        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
//                            @Override
//                            public ObservableSource<?> apply(Throwable throwable) throws Exception {
//                                println("judge retry");
//                                if (throwable instanceof NotFullException) {
//                                    return Observable.just(0);
//                                } else {
//                                    return Observable.error(new Throwable("finish retry"));
//                                }
//                            }
//                        });
                        return Observable.just(0);
                    }
                });
            }
        }).subscribe(new Observer<List<Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Integer> integers) {
                println("onNext integers = " + Arrays.toString(integers.toArray()));
            }

            @Override
            public void onError(Throwable e) {
                println("onError e : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                println("onComplete");
            }
        });
    }

    public static class NotFullException extends RuntimeException {
    }

    private static List<Integer> nums = new ArrayList<>();

    public static void operation() {
        Integer[] items = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 14};


        disposable.add(Observable.fromArray(items).buffer(new ObservableSource<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> observer) {
                println("buffer subscribe");
                observer.onNext(1);
            }
        }, 2).subscribe(new Consumer<List<Integer>>() {
            @Override
            public void accept(List<Integer> integers) throws Exception {
                println("buffer accept integers : " + Arrays.toString(integers.toArray()));
            }
        }));

        disposable.add(Observable.fromArray(items).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                        if (integer == 5) {
                            emitter.onError(new Throwable("flat error"));
                        } else {
                            emitter.onNext(integer + "-flatMap");
                        }
                    }
                });
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                println("sss = " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                println("error = " + throwable.getMessage());
            }
        }));


        disposable.add(Observable.fromArray(items).groupBy(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                if (integer % 2 == 0) {
                    return "Double";
                } else {
                    return "Single";
                }
            }
        }).subscribe(new Consumer<GroupedObservable<String, Integer>>() {
            @Override
            public void accept(GroupedObservable<String, Integer> stringIntegerGroupedObservable) throws Exception {
                println("key ：" + stringIntegerGroupedObservable.getKey());
                stringIntegerGroupedObservable.subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        println("after group key : " + stringIntegerGroupedObservable.getKey() + ", integer : " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }));

        disposable.add(Observable.fromArray(items).map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return integer + "--";
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                println("map s : " + s);
            }
        }));

//        disposable.add(Observable.fromArray(items).cast(String.class).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                println("cast s : " + s);
//            }
//        }));

        disposable.add(Observable.fromArray(items).scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                println("integer = " + integer + ", integer2 = " + integer2);
                return integer + integer2;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                println("after scan integer = " + integer);
            }
        }));

        disposable.add(Observable.fromArray(items).window(3).subscribe(new Consumer<Observable<Integer>>() {
            @Override
            public void accept(Observable<Integer> integerObservable) throws Exception {
                println("windowed");
                integerObservable.subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        println("window integer = " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }));

        disposable.add(Observable.fromArray(items).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer % 2 == 0;
            }
        })
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        println("filter integer = " + integer);
                    }
                }));

        Observable.intervalRange(1, 20, 100, 100, TimeUnit.MILLISECONDS)
                .throttleLatest(350, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        println("sample aLong = " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Observable o1 = Observable.intervalRange(1, 8, 100, 100, TimeUnit.MILLISECONDS);
        Observable o2 = Observable.intervalRange(3, 5, 200, 200, TimeUnit.MILLISECONDS);

        Observable.merge(o1, o2).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long o) {
                println("merge o = " + o);
            }

            @Override
            public void onError(Throwable e) {
                println("onError e : " + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });


        disposable.add(Observable.fromArray(items).zipWith(o1, new BiFunction<Integer, Long, String>() {
            @Override
            public String apply(Integer integer, Long u) throws Exception {
                return integer + "-" + u;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String o) throws Exception {
                println("zipWith = " + o);
            }
        }));


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void createAndDefer() {
        disposable.add(Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        }));


        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(1000);

        }).subscribeOn(Schedulers.computation())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        println("observer1111 : " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        println("observer1111 onError");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        disposable.add(Observable.just(1, 2, 3, 4)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Integer integer2) throws Exception {
                        println("integer1 = " + integer + ", integer2 = " + integer2);
                        return integer2 * 10;
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        println("scan : " + integer);
                    }
                }));


        disposable.add(Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> emitter) throws Exception {

            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        }));

        disposable.add(Observable.empty().doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                println("empty");
            }
        }).subscribe());

        disposable.add(Observable.never().doFinally(new Action() {
            @Override
            public void run() throws Exception {
                println("never");
            }
        }).subscribe());

        Observable.error(new Throwable("出错了"))
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("===========onSubscribe===========");
                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println("==========onNext============");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("===========onError===========" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("===========onComplete===========");
                    }
                });


//        Observable.interval(1000,  1000, TimeUnit.MILLISECONDS)
//                .onTerminateDetach()
//                .subscribe(aLong -> println("interval: along = " + aLong));
//
//        Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS)
//                .onTerminateDetach()
//                .observeOn(Schedulers.io())
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        println("interval: along = " + aLong);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//        disposable.add(Observable.intervalRange(1, 5, 1000, 1000, TimeUnit.MILLISECONDS)
//               // .subscribeOn(Schedulers.newThread())
//            .subscribe(new Consumer<Long>() {
//                @Override
//                public void accept(Long aLong) throws Exception {
//                    println("interval range num: = " + aLong);
//                }
//            }));


        count = 0;
        disposable.add(Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.newThread())
                .repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                        println("repeat when");

                        return Observable.timer(2, TimeUnit.SECONDS);
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        println("repeatWhen : " + integer);
                    }
                }));

        count = 0;
        long currentMillis = System.currentTimeMillis();
        disposable.add(Observable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .take(1)
                .repeatUntil(new BooleanSupplier() {
                    @Override
                    public boolean getAsBoolean() throws Exception {
                        count++;
                        println("getAsBoolean");
                        return count > 5;
                    }
                })
                .subscribe(aLong -> println("repeatUtil===" + aLong)));

        disposable.add(Observable.interval(1000, TimeUnit.MILLISECONDS)
                .take(3)
                .repeat(3)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        println("repeat times 3, num = " + aLong);
                    }
                }));


    }

    public static void println(String msg) {
        System.out.println("zhiheng  -  " + msg);
    }
}
