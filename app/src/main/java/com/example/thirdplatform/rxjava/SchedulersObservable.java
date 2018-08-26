package com.example.thirdplatform.rxjava;

class SchedulersObservable<T> extends Observable<T> {
    final Observable<T> source;
    final Schedulers schedulers;

    SchedulersObservable(Observable<T> source, Schedulers schedulers) {
        this.source = source;
        this.schedulers = schedulers;
    }

    @Override
    void subscribeActual(Observer<T> observer) {
        schedulers.scheduleDirect(new SchedulerTask(observer));
    }

    private class SchedulerTask implements Runnable {
        final Observer<T> observer;

        SchedulerTask(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void run() {
            source.subscribe(observer);
        }
    }
}
