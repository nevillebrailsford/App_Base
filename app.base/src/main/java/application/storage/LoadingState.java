package application.storage;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoadingState {
	private static AtomicBoolean loadingData = new AtomicBoolean(false);

	public static void startLoading() {
		synchronized (loadingData) {
			while (!loadingData.compareAndSet(false, true)) {
				try {
					loadingData.wait();
				} catch (InterruptedException e) {
				}
			}
			loadingData.notifyAll();
		}
	}

	public static void stopLoading() {
		synchronized (loadingData) {
			while (!loadingData.compareAndSet(true, false)) {
				try {
					loadingData.wait();
				} catch (InterruptedException e) {
				}
			}
			loadingData.notifyAll();
		}
	}

	public static void reset() {
		loadingData.set(false);
	}

	public static boolean isLoading() {
		return loadingData.get();
	}

}
