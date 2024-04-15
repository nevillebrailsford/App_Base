package application.animation;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class GApplicationStrings extends GApplicationMaths {

	public String join(String[] list, String separator) {
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for (String s : list) {
			if (!first) {
				builder.append(separator);
			}
			first = false;
			builder.append(s);
		}
		return builder.toString();
	}

	public String[] match(String str, String regExp) {
		String[] result = null;
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			result = new String[2];
			result[0] = matcher.group(0);
			result[1] = matcher.group(1);
		}
		return result;
	}

	public String[][] matchAll(String str, String regExp) {
		String[][] result = null;
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(str);
		Vector<String> v0 = new Vector<>(0, 1);
		while (matcher.find()) {
			v0.add(matcher.group(0));
			v0.add(matcher.group(1));
		}
		if (v0.capacity() > 0) {
			result = new String[v0.capacity() / 2][2];
			for (int i = 0; i < v0.capacity() - 1; i += 2) {
				result[i / 2][0] = v0.get(i);
				result[i / 2][1] = v0.get(i + 1);
			}
		}
		return result;
	}

	public String[] split(String value, char delim) {
		return split(value, String.valueOf(delim));
	}

	public String[] split(String value, String delim) {
		StringTokenizer st = new StringTokenizer(value, delim);
		int size = st.countTokens();
		String[] result = new String[size];
		for (int i = 0; i < size; i++) {
			result[i] = st.nextToken();
		}
		return result;
	}

	public String[] splitTokens(String value) {
		return splitTokens(value, "\t\n\r\f ");
	}

	private String[] splitTokens(String value, char delim) {
		return split(value, delim);
	}

	public String[] splitTokens(String value, String delim) {
		String[] result = splitTokens(value, delim.charAt(0));
		for (int i = 1; i < delim.length(); i++) {
			result = splitTokens(result, delim.charAt(i));
		}
		return result;
	}

	private String[] splitTokens(String[] values, char delim) {
		int size = 0;
		for (int i = 0; i < values.length; i++) {
			String[] arr = split(values[i], delim);
			size += arr.length;
		}
		String[] result = new String[size];
		int index = 0;
		for (int i = 0; i < values.length; i++) {
			String[] arr = split(values[i], delim);
			for (int j = 0; j < arr.length; j++) {
				result[index++] = arr[j];
			}
		}
		return result;
	}

	public String trim(String str) {
		return str.trim();
	}

	public String[] trim(String[] array) {
		String[] result = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			result[i] = trim(array[i]);
		}
		return result;
	}

	public String nf(int num) {
		return String.format("%0d", num);
	}

	public String nf(int num, int digits) {
		String formatter = null;
		if (digits == 0) {
			formatter = "%d";
		} else {
			formatter = "%0" + Integer.toString(digits) + "d";
		}
		return String.format(formatter, num);
	}

	public String[] nf(int[] nums) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i]);
		}
		return result;
	}

	public String[] nf(int[] nums, int digits) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i], digits);
		}
		return result;
	}

	public String nf(float num) {
		return String.format("%0f", num);
	}

	public String nf(float num, int digits) {
		String formatter = "%0" + Integer.toString(digits) + "f";
		return String.format(formatter, num);
	}

	public String nf(float num, int left, int right) {
		String formatter = "%0" + Integer.toString(left + right + 1) + "." + Integer.toString(right) + "f";
		return String.format(formatter, num);
	}

	public String[] nf(float[] nums) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i]);
		}
		return result;
	}

	public String[] nf(float[] nums, int digits) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i], digits);
		}
		return result;
	}

	public String[] nf(float[] nums, int left, int right) {
		String[] result = new String[nums.length];
		for (int i = 0; i < nums.length; i++) {
			result[i] = nf(nums[i], left, right);
		}
		return result;
	}

}
