package de.cgarbs.knittr.render;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Maps the colors of a @{BufferedImage} to the output file
 * @author mitch
 *
 */
public class Colormap
{
	// enum: quick and easy, non-bloat
	// if 'free mapping' gets added, a Colormap class hierarchy and factory are needed
	enum Type { NORMAL, GREYSCALE};



	private Map<Integer,Color> colorMap;

	public Colormap(Type type, BufferedImage img)
	{
		// FIXME: save colormap type for later? currently unneeded

		// scan image for all colors
		Set<Integer> colorSet = new TreeSet<Integer>(); // TreeSet for greyscale sorting!
		for (int y = img.getMinY(); y < img.getHeight(); y++)
		{
			for (int x = img.getMinX(); x < img.getWidth(); x++)
			{
				colorSet.add(img.getRGB(x, y));
			}
		}

		switch (type)
		{
			case NORMAL:
				colorMap = new HashMap<Integer, Color>();
				for (Integer rgb: colorSet)
				{
					colorMap.put(rgb, new Color(rgb));
				}

				break;

			case GREYSCALE:
				colorMap = new HashMap<Integer, Color>();

				double colors = colorSet.size();
				double i = 1;

				for (Integer rgb: colorSet)
				{
					int value = (int) Math.round(255d * ( i / colors));
					colorMap.put(rgb, new Color(value, value, value));
					i++;
				}

				break;

			default:
				throw new RuntimeException("unreachable code in Colormap!"); // FIXME: create nicer exception
		}

	}

	public Color get(int rgb)
	{
		Color color = colorMap.get((Integer) rgb);

		if (color == null)
		{
			throw new RuntimeException("missing color in Colormap!"); // FIXME: create nicer exception
		}

		return color;
	}
}
