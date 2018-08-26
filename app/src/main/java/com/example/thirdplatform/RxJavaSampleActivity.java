package com.example.thirdplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


public class RxJavaSampleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RxJavaSampleActivity";
    private Button create;
    private Button map;
    private Button zip;
    private Button concat;
    private Button flatMap;
    private Button concatMap;
    private Button distinct;
    private Button filter;
    private Button buffer;
    private Button timer;
    private Button interval;
    private Button doOnNext;
    private Button skip;
    private Button take;
    private Button single;
    private Button debounce;
    private Button defer;
    private Button last;
    private Button merge;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_sample);
        create = findViewById(R.id.create);
        create.setOnClickListener(this);
        map = findViewById(R.id.map);
        map.setOnClickListener(this);
        zip = findViewById(R.id.zip);
        zip.setOnClickListener(this);
        concat = findViewById(R.id.concat);
        concat.setOnClickListener(this);
        flatMap = findViewById(R.id.flatMap);
        flatMap.setOnClickListener(this);
        concatMap = findViewById(R.id.concatMap);
        concatMap.setOnClickListener(this);
        distinct = findViewById(R.id.distinct);
        distinct.setOnClickListener(this);
        filter = findViewById(R.id.filter);
        filter.setOnClickListener(this);
        buffer = findViewById(R.id.buffer);
        buffer.setOnClickListener(this);
        timer = findViewById(R.id.timer);
        timer.setOnClickListener(this);
        interval = findViewById(R.id.interval);
        interval.setOnClickListener(this);
        doOnNext = findViewById(R.id.doOnNext);
        doOnNext.setOnClickListener(this);
        skip = findViewById(R.id.skip);
        skip.setOnClickListener(this);
        take = findViewById(R.id.take);
        take.setOnClickListener(this);
        single = findViewById(R.id.single);
        single.setOnClickListener(this);
        debounce = findViewById(R.id.debounce);
        debounce.setOnClickListener(this);
        defer = findViewById(R.id.defer);
        defer.setOnClickListener(this);
        last = findViewById(R.id.last);
        last.setOnClickListener(this);
        merge = findViewById(R.id.merge);
        merge.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == create) {
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    Log.e(TAG, "emitter start");
                    Log.e(TAG, "onNext() emitter 1");
                    emitter.onNext("1");
                    Log.e(TAG, "onNext() emitter 2");
                    emitter.onNext("2");
                    Log.e(TAG, "onNext() emitter 3");
                    emitter.onNext("3");
                    Log.e(TAG, "emitter onComplete");
                    emitter.onComplete();
                    Log.e(TAG, "onNext() emitter 4");
                    emitter.onNext("4");

                }
            }).subscribe(new Observer<String>() {
                Disposable disposable;

                @Override
                public void onSubscribe(Disposable d) {
                    this.disposable = d;
                    Log.e(TAG, "onSubscribe");
                }

                @Override
                public void onNext(String s) {
                    Log.e(TAG, "onNext " + s);
                    if (s.equals("2")) {
                        disposable.dispose();
                        Log.e(TAG, "onNext dispose => " + disposable.isDisposed());
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError");
                }

                @Override
                public void onComplete() {
                    Log.e(TAG, "onComplete");
                }
            });
        } else if (v == map) {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    Log.e(TAG, "Emitter 1");
                    emitter.onNext(1);
                    Log.e(TAG, "Emitter 2");
                    emitter.onNext(2);
                    Log.e(TAG, "Emitter 3");
                    emitter.onNext(3);
                }
            }).map(new Function<Integer, Integer>() {
                @Override
                public Integer apply(Integer integer) throws Exception {
                    return integer * 3;
                }
            }).subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    Log.e(TAG, String.valueOf(integer));
                }
            });
        } else if (v == zip) {
            Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                    emitter.onNext("Hello");
                    emitter.onNext("World");
                    emitter.onNext("Good");
                }
            });

            Observable<Integer> integerObservable = Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    emitter.onNext(100);
                    emitter.onNext(200);
                    emitter.onNext(300);
                    emitter.onNext(400);
                    emitter.onNext(500);
                }
            });

            Observable.zip(observable, integerObservable, new BiFunction<String, Integer, String>() {
                @Override
                public String apply(String s, Integer integer) throws Exception {
                    return s + integer;
                }
            }).subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    Log.e(TAG, "result = " + s);
                }
            });
        } else if (v == concat) {
            Observable<Integer> observable = Observable.just(1, 2, 3);
            Observable<Integer> observable1 = Observable.just(4, 5, 6);
            Observable.concat(observable, observable1)
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, String.valueOf(integer));
                        }
                    });
        } else if (v == flatMap) {
            Observable.just(100, 200, 300)
                    .flatMap(new Function<Integer, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(Integer integer) throws Exception {
                            List<String> list = new ArrayList<>();
                            list.add("My value is " + integer);
                            list.add("My value is " + integer);
                            list.add("My value is " + integer);
                            int delay = (int) (1 + Math.random() * 10);
                            return Observable.fromIterable(list).delay(delay, TimeUnit.MILLISECONDS);
                        }
                    })
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Log.e(TAG, s);
                        }
                    });
        } else if (v == concatMap) {
            Observable.just(100, 200, 300)
                    .concatMap(new Function<Integer, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(Integer integer) throws Exception {
                            List<String> list = new ArrayList<>();
                            list.add("My value is " + integer);
                            list.add("My value is " + integer);
                            list.add("My value is " + integer);
                            int delay = (int) (1 + Math.random() * 10);
                            return Observable.fromIterable(list).delay(delay, TimeUnit.MILLISECONDS);
                        }
                    })
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            Log.e(TAG, s);
                        }
                    });
        } else if (v == distinct) {
            Observable.just(2, 2, 2, 3, 3, 4, 4, 5)
                    .distinct()
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, String.valueOf(integer));
                        }
                    });
        } else if (v == filter) {
            Observable.just(2, 2, 2, 3, 3, 4, 4, 5)
                    .filter(new Predicate<Integer>() {
                        @Override
                        public boolean test(Integer integer) throws Exception {
                            return integer % 2 == 0;
                        }
                    })
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, String.valueOf(integer));
                        }
                    });
        } else if (v == buffer) {
            Observable.just(1, 2, 3, 4, 5, 6, 7)
                    .buffer(3, 1)
                    .subscribe(new Consumer<List<Integer>>() {
                        @Override
                        public void accept(List<Integer> integer) throws Exception {
                            Log.e(TAG, integer.toString());
                        }
                    });
        } else if (v == timer) {
            Observable.timer(5, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            Log.e(TAG, "timer " + aLong);
                        }
                    });
        } else if (v == interval) {
            disposable = Observable.interval(5, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            Log.e(TAG, "interval = " + aLong);
                        }
                    });
        } else if (v == doOnNext) {
            Observable.just(1, 2, 3, 4, 5, 6, 7)
                    .doOnNext(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, "save " + integer);
                        }
                    })
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, integer.toString());
                        }
                    });
        } else if (v == skip) {
            Observable.just(1, 2, 3, 4, 5, 6, 7)
                    .skip(3)
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, integer.toString());
                        }
                    });
        } else if (v == take) {
            Observable.just(1, 2, 3, 4, 5, 6, 7)
                    .take(4)
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, integer.toString());
                        }
                    });
        } else if (v == single) {
            Single.just(100)
                    .subscribe(new SingleObserver<Integer>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.e(TAG, "single => onSubscribe");
                        }

                        @Override
                        public void onSuccess(Integer integer) {
                            Log.e(TAG, "single => onSuccess " + integer);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "single => onError");
                        }
                    });
        } else if (v == debounce) {
            Observable.create(new ObservableOnSubscribe<Integer>() {
                @Override
                public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                    emitter.onNext(100);
                    Thread.sleep(1000);
                    emitter.onNext(200);
                    Thread.sleep(1000);
                    emitter.onNext(300);
                    Thread.sleep(200);
                    emitter.onNext(400);
                    Thread.sleep(300);
                    emitter.onNext(500);
                    Thread.sleep(300);
                    emitter.onNext(600);
                    Thread.sleep(300);
                    emitter.onNext(700);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .debounce(1, TimeUnit.SECONDS)
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, String.valueOf(integer));
                        }
                    });
        } else if (v == defer) {
            Observable observable = Observable.defer(new Callable<ObservableSource>() {
                @Override
                public ObservableSource call() throws Exception {
                    return Observable.just(100, 200, 300, 400);
                }
            });

            observable.subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer o) throws Exception {
                    Log.e(TAG, String.valueOf(o));
                }
            });
        } else if (v == last) {
            Observable.just(1, 2, 3, 4, 5)
                    .last(4)
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            Log.e(TAG, String.valueOf(integer));
                        }
                    });
        } else if (v == merge) {
            Observable observable = Observable.just(1, 3, 5, 7, 9);
            Observable observable1 = Observable.just(100, 200, 300, 400, 500, 600, 700);
            Observable.merge(observable, observable1)
                    .subscribe(new Consumer() {
                        @Override
                        public void accept(Object o) throws Exception {
                            Log.e(TAG, String.valueOf(o));
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
    }
}
