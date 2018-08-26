package com.example.thirdplatform.rxjava;

public class ObserverOnObservable<T> extends Observable<T> {
    final Observable<T> source;
    final Schedulers schedulers;

    ObserverOnObservable(Observable<T> source, Schedulers schedulers) {
        this.source = source;
        this.schedulers = schedulers;
    }

    @Override
    void subscribeActual(Observer<T> observer) {
        source.subscribe(new ObserverOnObserver(observer));
    }

    private class ObserverOnObserver implements Observer<T>, Runnable {
        final Observer<T> observer;
        private T item;

        ObserverOnObserver(Observer observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe() {
            observer.onSubscribe();
        }

        @Override
        public void onNext(T data) {
            item = data;
            schedulers.scheduleDirect(this);
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }

        @Override
        public void run() {
            observer.onNext(item);
        }
    }
}
