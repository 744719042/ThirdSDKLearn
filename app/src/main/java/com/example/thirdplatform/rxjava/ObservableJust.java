package com.example.thirdplatform.rxjava;

class ObservableJust<T> extends Observable<T> {
    private T data;

    ObservableJust(T data) {
        this.data = data;
    }

    @Override
    void subscribeActual(Observer<T> observer) {
        ScalarDisposable sd = new ScalarDisposable(observer, data);
        observer.onSubscribe();
        sd.run();
    }

    private static class ScalarDisposable<T> {
        private Observer observer;
        private T data;

        ScalarDisposable(Observer<T> observer, T data) {
            this.observer = observer;
            this.data = data;
        }

        public void run() {
            try {
                observer.onNext(data);
                observer.onComplete();
            } catch (Throwable throwable) {
                observer.onError(throwable);
            }
        }
    }
}
