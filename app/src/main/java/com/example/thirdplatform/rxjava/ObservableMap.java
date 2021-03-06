package com.example.thirdplatform.rxjava;

public class ObservableMap<T, R> extends Observable<R> {

    final Observable<T> source;
    final Function<T, R> function;

    public ObservableMap(Observable<T> observable, Function<T, R> function) {
        this.source = observable;
        this.function = function;
    }

    @Override
    void subscribeActual(Observer<R> observer) {
        source.subscribe(new MapObserver<>(observer, function));
    }

    private class MapObserver<T> implements Observer<T> {
        final Observer<R> observer;
        final Function<T, R> function;

        MapObserver(Observer<R> observer, Function<T, R> function) {
            this.observer = observer;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            observer.onSubscribe();
        }

        @Override
        public void onNext(T data) {
            try {
                observer.onNext(function.apply(data));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
