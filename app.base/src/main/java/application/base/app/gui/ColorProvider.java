package application.base.app.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ColorProvider {
	public static final String[] name = new String[] { "aliceblue", "antiquewhite", "aqua", "aquamarine", "azure",
			"beige", "bisque", "black", "blanchedalmond", "blue", "blueviolet", "brown", "burlywood", "cadetblue",
			"chartreuse", "chocolate", "coral", "cornflowerblue", "cornsilk", "crimson", "cyan", "darkblue", "darkcyan",
			"darkgoldenrod", "darkgray", "darkgreen", "darkkhaki", "darkmagenta", "darkolivegreen", "darkorange",
			"darkorchid", "darkred", "darksalmon", "darkseagreen", "darkslateblue", "darkslategray", "darkturquoise",
			"darkviolet", "deeppink", "deepskyblue", "dimgray", "dodgerblue", "firebrick", "floralwhite", "forestgreen",
			"fuchsia", "gainsboro", "ghostwhite", "gold", "goldenrod", "gray", "green", "greenyellow", "honeydew",
			"hotpink", "indianred", "indigo", "ivory", "khaki", "lavender", "lavenderblush", "lawngreen",
			"lemonchiffon", "lightblue", "lightcoral", "lightcyan", "lightgoldenrodyellow", "lightgray", "lightgreen",
			"lightpink", "lightsalmon", "lightseagreen", "lightskyblue", "lightslategray", "lightsteelblue",
			"lightyellow", "lime", "limegreen", "linen", "magenta", "maroon", "mediumaquamarine", "mediumblue",
			"mediumorchid", "mediumpurple", "mediumseagreen", "mediumslateblue", "mediumspringgreen", "mediumturquoise",
			"mediumvioletred", "midnightblue", "mintcream", "mistyrose", "moccasin", "navajowhite", "navy", "oldlace",
			"olive", "olivedrab", "orange", "orangered", "orchid", "palegoldenrod", "palegreen", "paleturquoise",
			"palevioletred", "papayawhip", "peachpuff", "peru", "pink", "plum", "powderblue", "purple", "red",
			"rosybrown", "royalblue", "saddlebrown", "salmon", "sandybrown", "seagreen", "seashell", "sienna", "silver",
			"skyblue", "slateblue", "slategray", "snow", "springgreen", "steelblue", "tan", "teal", "thistle", "tomato",
			"turquoise", "violet", "wheat", "white", "whitesmoke", "yellow", "yellowgreen" };

	private static String[] hex = new String[] { "#f0f8ff", "#faebd7", "#00ffff", "#7fffd4", "#f0ffff", "#f5f5dc",
			"#ffe4c4", "#000000", "#ffebcd", "#0000ff", "#8a2be2", "#a52a2a", "#deb887", "#5f9ea0", "#7fff00",
			"#d2691e", "#ff7f50", "#6495ed", "#fff8dc", "#dc143c", "#00ffff", "#00008b", "#008b8b", "#b8860b",
			"#a9a9a9", "#006400", "#bdb76b", "#8b008b", "#556b2f", "#ff8c00", "#9932cc", "#8b0000", "#e9967a",
			"#8fbc8f", "#483d8b", "#2f4f4f", "#00ced1", "#9400d3", "#ff1493", "#00bfff", "#696969", "#1e90ff",
			"#b22222", "#fffaf0", "#228b22", "#ff00ff", "#dcdcdc", "#f8f8ff", "#ffd700", "#daa520", "#808080",
			"#008000", "#adff2f", "#f0fff0", "#ff69b4", "#cd5c5c", "#4b0082", "#fffff0", "#f0e68c", "#e6e6fa",
			"#fff0f5", "#7cfc00", "#fffacd", "#add8e6", "#f08080", "#e0ffff", "#fafad2", "#d3d3d3", "#90ee90",
			"#ffb6c1", "#ffa07a", "#20b2aa", "#87cefa", "#778899", "#b0c4de", "#ffffe0", "#00ff00", "#32cd32",
			"#faf0e6", "#ff00ff", "#800000", "#66cdaa", "#0000cd", "#ba55d3", "#9370db", "#3cb371", "#7b68ee",
			"#00fa9a", "#48d1cc", "#c71585", "#191970", "#f5fffa", "#ffe4e1", "#ffe4b5", "#ffdead", "#000080",
			"#fdf5e6", "#808000", "#6b8e23", "#ffa500", "#ff4500", "#da70d6", "#eee8aa", "#98fb98", "#afeeee",
			"#db7093", "#ffefd5", "#ffdab9", "#cd853f", "#ffc0cb", "#dda0dd", "#b0e0e6", "#800080", "#ff0000",
			"#bc8f8f", "#4169e1", "#8b4513", "#fa8072", "#f4a460", "#2e8b57", "#fff5ee", "#a0522d", "#c0c0c0",
			"#87ceeb", "#6a5acd", "#708090", "#fffafa", "#00ff7f", "#4682b4", "#d2b48c", "#008080", "#d8bfd8",
			"#ff6347", "#40e0d0", "#ee82ee", "#f5deb3", "#ffffff", "#f5f5f5", "#ffff00", "#9acd32" };

	private static final Map<Color, Color> colors = getMap();
	private static final Map<String, Color> names = getNameMap();
	private static Color currentColor = hex2Rgb(hex[0]);

	private static Map<Color, Color> getMap() {
		List<String> hexList = new ArrayList<>();
		for (int i = 0; i < hex.length; i++) {
			hexList.add(hex[i]);
		}
		Collections.shuffle(hexList);
		String[] sortedHex = hexList.toArray(new String[hex.length]);
		Map<Color, Color> result = new HashMap<Color, Color>();
		for (int i = 0; i < sortedHex.length - 1; i++) {
			result.put(hex2Rgb(sortedHex[i]), hex2Rgb(sortedHex[i + 1]));
		}
		result.put(hex2Rgb(sortedHex[sortedHex.length - 1]), hex2Rgb(sortedHex[0]));
		return result;
	}

	private static Map<String, Color> getNameMap() {
		Map<String, Color> result = new HashMap<String, Color>();
		for (int i = 0; i < hex.length; i++) {
			result.put(name[i], hex2Rgb(hex[i]));
		}
		return result;
	}

	public synchronized static Color nextColor() {
		Color result = currentColor;
		currentColor = colors.get(currentColor);
		return result;
	}

	public synchronized static void startColor(int start) {
		if (start >= 0 || start < hex.length - 1) {
			currentColor = hex2Rgb(hex[start]);
		}
	}

	public static int colors() {
		return colors.size();
	}

	public static Color hex2Rgb(String colorStr) {
		return new Color(Integer.valueOf(colorStr.substring(1, 3), 16), Integer.valueOf(colorStr.substring(3, 5), 16),
				Integer.valueOf(colorStr.substring(5, 7), 16));
	}

	public synchronized static Color get(String name) {
		return names.get(name);
	}
}
