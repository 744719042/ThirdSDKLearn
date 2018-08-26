package com.example.thirdplatform.rxjava;

public abstract class Observable<T> implements ObservableSource<T> {

    public static <T> Observable<T> just(T data) {
        return onAssembly(new ObservableJust<>(data));
    }

    private static <T> Observable<T> onAssembly(Observable<T> observable) {
        return observable;
    }

    @Override
    public void subscribe(Observer<T> observer) {
        subscribeActual(observer);
    }


    public void subscribe(Consumer<T> onNext) {
        subscribe(onNext, null, null);
    }

    private void subscribe(final Consumer<T> onNext, final Consumer<T> error, Consumer<T> complete) {
        subscribe(new LambdaObserver<>(onNext));
    }

    abstract void subscribeActual(Observer<T> observer);

    public <R> Observable<R> map(Function<T, R> function) {
        return onAssembly(new ObservableMap<>(this, function));
    }

    public Observable<T> subscribeOn(Schedulers schedulers) {
        return onAssembly(new SchedulersObservable(this, schedulers));
    }

    public Observable<T> observerOn(Schedulers schedulers) {
        return onAssembly(new ObserverOnObservable(this, schedulers));
    }
}
