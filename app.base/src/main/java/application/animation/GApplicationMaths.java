package application.animation;

import java.util.Random;

public abstract class GApplicationMaths extends GApplicationText {
	public static final float PI = 3.14159265358979323846f;
	public static final float HALF_PI = 1.57079632679489661923f;
	public static final float QUARTER_PI = 0.7853982f;
	public static final float TWO_PI = 6.28318530717958647693f;
	public static final float TAU = TWO_PI;

	private int randomSeed = 0;
	private boolean randomSeedSet = false;

	public static int toInt(float value) {
		return (int) value;
	}

	public static int toInt(long value) {
		return (int) value;
	}

// Trigonometry	

	public float acos(float value) {
		return (float) Math.acos(value);
	}

	public float asin(float value) {
		return (float) Math.asin(value);
	}

	public float atan(float value) {
		return (float) Math.atan(value);
	}

	public float cos(float angle) {
		return (float) Math.cos(angle);
	}

	public float degrees(float radian) {
		return (float) Math.toDegrees(radian);
	}

	public float radians(float degrees) {
		return (float) Math.toRadians(degrees);
	}

	public float sin(float angle) {
		return (float) Math.sin(angle);
	}

	public float tan(float angle) {
		return (float) Math.tan(angle);
	}

// Calculations

	public float abs(float value) {
		return Math.abs(value);
	}

	public float ceil(float value) {
		return (float) Math.ceil(value);
	}

	public int constrain(int amt, int low, int high) {
		return Math.max(Math.min(amt, high), low);
	}

	public float constrain(float amt, float low, float high) {
		return Math.max(Math.min(amt, high), low);
	}

	public float dist(float x1, float y1, float x2, float y2) {
		return sqrt(sq(x2 - x1) + sq(y2 - y1));
	}

	public float dist(float x1, float y1, float z1, float x2, float y2, float z2) {
		return sqrt(sq(x2 - x1) + sq(y2 - y1) + sq(z2 - z1));
	}

	public float exp(float n) {
		return (float) Math.exp(n);
	}

	public int floor(float x) {
		return (int) Math.floor(x);
	}

	public float lerp(float start, float end, float amt) {
		return start + ((end - start) * amt);
	}

	public float log(float x) {
		return (float) Math.log(x);
	}

	public float mag(float a, float b) {
		return dist(0, 0, a, b);
	}

	public static float map(float value, float start1, float stop1, float start2, float stop2) {
		return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
	}

	public int max(int a, int b) {
		return Math.max(a, b);
	}

	public float max(float a, float b) {
		return Math.max(a, b);
	}

	public int max(int[] list) {
		int result = list[0];
		for (int i = 1; i < list.length; i++) {
			result = Math.max(result, list[i]);
		}
		return result;
	}

	public float max(float[] list) {
		float result = list[0];
		for (int i = 1; i < list.length; i++) {
			result = Math.max(result, list[i]);
		}
		return result;
	}

	public int min(int a, int b) {
		return Math.min(a, b);
	}

	public float min(float a, float b) {
		return Math.min(a, b);
	}

	public int min(int[] list) {
		int result = list[0];
		for (int i = 1; i < list.length; i++) {
			result = Math.min(result, list[i]);
		}
		return result;
	}

	public float min(float[] list) {
		float result = list[0];
		for (int i = 1; i < list.length; i++) {
			result = Math.min(result, list[i]);
		}
		return result;
	}

	public float norm(float value, float start, float stop) {
		return map(value, start, stop, 0, 1);
	}

	public float pow(float n, float e) {
		return (float) Math.pow(n, e);
	}

	public int round(float n) {
		return Math.round(n);
	}

	public void randomSeed(int seed) {
		randomSeed = seed;
		randomSeedSet = true;
	}

	public float random(float high) {
		Random r = new Random();
		if (randomSeedSet) {
			r.setSeed(randomSeed);
		}
		return r.nextFloat(high);
	}

	public float random(float low, float high) {
		Random r = new Random();
		if (randomSeedSet) {
			r.setSeed(randomSeed);
		}
		return r.nextFloat(low, high);
	}

	public float randomGaussian() {
		Random r = new Random();
		return (float) r.nextGaussian();
	}

	public float sq(float n) {
		return n * n;
	}

	public float sqrt(float n) {
		return (float) Math.sqrt(n);
	}

}
