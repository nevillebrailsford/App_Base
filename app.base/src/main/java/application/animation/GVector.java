package application.animation;

public class GVector {
	public float x = 0;
	public float y = 0;
	public float z = 0;

	public static GVector add(GVector v1, GVector v2) {
		return add(v1, v2, null);
	}

	public static GVector add(GVector v1, GVector v2, GVector target) {
		if (target == null) {
			target = new GVector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
		} else {
			target.set(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
		}
		return target;
	}

	public static GVector sub(GVector v1, GVector v2) {
		return sub(v1, v2, null);
	}

	public static GVector sub(GVector v1, GVector v2, GVector target) {
		if (target == null) {
			target = new GVector(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
		} else {
			target.set(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
		}
		return target;
	}

	public static GVector mult(GVector v, float n) {
		return mult(v, n, null);
	}

	public static GVector mult(GVector v, float n, GVector target) {
		if (target == null) {
			target = new GVector(v.x * n, v.y * n, v.z * n);
		} else {
			target.set(v.x * n, v.y * n, v.z * n);
		}
		return target;
	}

	public static GVector div(GVector v, float n) {
		return div(v, n, null);
	}

	public static GVector div(GVector v, float n, GVector target) {
		if (target == null) {
			target = new GVector(v.x / n, v.y / n, v.z / n);
		} else {
			target.set(v.x / n, v.y / n, v.z / n);
		}
		return target;
	}

	public static GVector cross(GVector v1, GVector v2, GVector target) {
		float crossX = v1.y * v2.z - v2.y * v1.z;
		float crossY = v1.z * v2.x - v2.z * v1.x;
		float crossZ = v1.x * v2.y - v2.x * v1.y;

		if (target == null) {
			target = new GVector(crossX, crossY, crossZ);
		} else {
			target.set(crossX, crossY, crossZ);
		}
		return target;
	}

	public static float dist(GVector v1, GVector v2) {
		return (float) Math
				.sqrt((v2.x - v1.x) * (v2.x - v1.x) + (v2.y - v1.y) * (v2.y - v1.y) + (v2.z - v1.z) * (v2.z - v1.z));
	}

	public static float dot(GVector v1, GVector v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}

	public GVector() {
	}

	public GVector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public GVector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public GVector set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public GVector set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public GVector set(GVector v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		return this;
	}

	public GVector set(float[] source) {
		this.x = source[0];
		this.y = source[1];
		this.z = source[2];
		return this;
	}

	public GVector add(GVector v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		return this;
	}

	public GVector add(float x, float y) {
		add(z, y, 0);
		return this;
	}

	public GVector add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public GVector sub(GVector v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		return this;
	}

	public GVector sub(float x, float y) {
		add(z, y, 0);
		return this;
	}

	public GVector sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public GVector mult(float n) {
		x *= n;
		y *= n;
		z *= n;
		return this;
	}

	public GVector div(float n) {
		x /= n;
		y /= n;
		z /= n;
		return this;
	}

	public GVector copy() {
		return new GVector(x, y, z);
	}

	public float mag() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public GVector normalize() {
		float m = mag();
		if (m != 0 && m != 1) {
			div(m);
		}
		return this;
	}

	public GVector normalize(GVector target) {
		if (target == null) {
			target = new GVector();
		}
		float m = mag();
		if (m > 0) {
			target.set(x / m, y / m, z / m);
		} else {
			target.set(x, y, z);
		}
		return target;
	}

	public float dist(GVector v) {
		return (float) Math.sqrt((v.x - x) * (v.x - x) + (v.y - y) * (v.y - y) + (v.z - z) * (v.z - z));
	}

	public float dot(GVector v) {
		return v.x * x + v.y * y + v.z * z;
	}

	public float dot(float x, float y, float z) {
		return this.x * x + this.y * y + this.z * z;
	}

	public GVector cross(GVector v) {
		return cross(v, null);
	}

	public GVector cross(GVector v, GVector target) {
		float crossX = y * v.z - v.y * z;
		float crossY = z * v.x - v.z * x;
		float crossZ = x * v.y - v.x * y;

		if (target == null) {
			target = new GVector(crossX, crossY, crossZ);
		} else {
			target.set(crossX, crossY, crossZ);
		}
		return target;
	}

	public float[] array(GVector v) {
		float[] result = new float[3];
		result[0] = x;
		result[1] = y;
		result[2] = z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GVector)) {
			return false;
		}
		final GVector p = (GVector) obj;
		return x == p.x && y == p.y && z == p.z;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + Float.floatToIntBits(x);
		result = 31 * result + Float.floatToIntBits(y);
		result = 31 * result + Float.floatToIntBits(z);
		return result;
	}
}
