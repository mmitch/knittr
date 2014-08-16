import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class TranscoderDemo
{
	/**
	 * FIXME remove, just for testing purposes
	 * @param args
	 */
	public static void main(String args[])
	{
		writeSVG(new Project());
	}

	public static void writeSVG(Project p)
	{
		try
		{
			
			BufferedImage source = ImageIO.read(new File(p.getSourceFile()));
			
			
			// Get a DOMImplementation.
			DOMImplementation domImpl = GenericDOMImplementation
					.getDOMImplementation();

			// Create an instance of org.w3c.dom.Document.
			String svgNS = "http://www.w3.org/2000/svg";
			Document document = domImpl.createDocument(svgNS, "svg", null);

			// Create an instance of the SVG Generator.
			SVGGraphics2D svg = new SVGGraphics2D(document);
			
			// convert
			Raster r = source.getData();
			svg.setStroke(new BasicStroke(p.getGridWidthSmall()));
			int[] rgb = new int[4];
			for (int x=0; x<r.getWidth(); x++)
			{
				for (int y=0; y<r.getHeight(); y++)
				{
					rgb = r.getPixel(x, y, rgb);
					svg.setPaint(new Color(rgb[0], rgb[1], rgb[2]));
					svg.fill(new Rectangle( x * p.getScaleX(), y*p.getScaleY(), p.getScaleX(), p.getScaleY()));
					svg.setPaint(p.getGridColor());
					svg.draw(new Rectangle( x * p.getScaleX(), y*p.getScaleY(), p.getScaleX(), p.getScaleY()));
				}
			}
			
			// text + thick grid
			int max_x = r.getWidth()  * p.getScaleX() + 1;
			int max_y = r.getHeight() * p.getScaleY() + 1;
			svg.setFont(new Font(p.getFontName(), Font.PLAIN, p.getScaleY() - p.getOffset() * 2));
			svg.setStroke(new BasicStroke(p.getGridWidthBig()));
			
			for (int x=0, col=r.getWidth(); x<r.getWidth(); x++, col--)
			{
				if (col % p.getGridTextMod() == 0)
				{
					svg.setPaint(p.getTextColor());
					svg.drawString(String.valueOf(col), x * p.getScaleX() + p.getOffset(), max_y - p.getOffset());
					svg.drawString(String.valueOf(col), x * p.getScaleX() + p.getOffset(), p.getScaleY() - p.getOffset());
					svg.setPaint(p.getGridColor());
					svg.drawLine(x * p.getScaleX(), 0, x * p.getScaleX(), max_y);
				}
			}			
			for (int y=0, row=r.getHeight()+1; y<r.getHeight(); y++, row--)
			{
				if (row % p.getGridTextMod() == 0)
				{
					svg.setPaint(p.getTextColor());
					svg.drawString(String.valueOf(row), p.getOffset(), 				  y * p.getScaleY() - p.getOffset());
					svg.drawString(String.valueOf(row), max_x - p.getScaleX() + p.getOffset(), y * p.getScaleY() - p.getOffset());
				}
				if (row % p.getGridTextMod() == 1)
				{
					svg.setPaint(p.getGridColor());
					svg.drawLine(0, y * p.getScaleY(), max_x, y * p.getScaleY());
				}
			}			

			svg.stream(p.getTargetFile(), true);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
}
