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
	public static void main(String args[])
	{
		try
		{
			float GRID_WIDTH_SMALL = 0.5f;
			float GRID_WIDTH_BIG   = 2f;
			
			int MASCHEN = 24;
			int REIHEN  = 32;
			
			int TOTAL_SCALE = 512;
			
			int GRIDTEXTMOD = 5;
			
			Color GRIDCOLOR = Color.BLACK; 

			int SCALE_X = TOTAL_SCALE / MASCHEN;
			int SCALE_Y = TOTAL_SCALE / REIHEN;
			
			int OFFSET = 3;
			
			String FONTNAME = "DialogInput";
			Color TEXTCOLOR = Color.BLACK;
			
			
			
			BufferedImage source = ImageIO.read(new File("resource/image_test.png"));
			
			
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
			svg.setStroke(new BasicStroke(GRID_WIDTH_SMALL));
			int[] rgb = new int[4];
			for (int x=0; x<r.getWidth(); x++)
			{
				for (int y=0; y<r.getHeight(); y++)
				{
					rgb = r.getPixel(x, y, rgb);
					svg.setPaint(new Color(rgb[0], rgb[1], rgb[2]));
					svg.fill(new Rectangle( x*SCALE_X, y*SCALE_Y, SCALE_X, SCALE_Y));
					svg.setPaint(GRIDCOLOR);
					svg.draw(new Rectangle( x*SCALE_X, y*SCALE_Y, SCALE_X, SCALE_Y));
				}
			}
			
			// text + thick grid
			int max_x = r.getWidth()  * SCALE_X + 1;
			int max_y = r.getHeight() * SCALE_Y + 1;
			svg.setFont(new Font(FONTNAME, Font.PLAIN, SCALE_Y - OFFSET * 2));
			svg.setStroke(new BasicStroke(GRID_WIDTH_BIG));
			
			for (int x=0, col=r.getWidth(); x<r.getWidth(); x++, col--)
			{
				if (col % GRIDTEXTMOD == 0)
				{
					svg.setPaint(TEXTCOLOR);
					svg.drawString(String.valueOf(col), x * SCALE_X + OFFSET, max_y - OFFSET);
					svg.drawString(String.valueOf(col), x * SCALE_X + OFFSET, SCALE_Y - OFFSET);
					svg.setPaint(GRIDCOLOR);
					svg.drawLine(x * SCALE_X, 0, x * SCALE_X, max_y);
				}
			}			
			for (int y=0, row=r.getHeight()+1; y<r.getHeight(); y++, row--)
			{
				if (row % GRIDTEXTMOD == 0)
				{
					svg.setPaint(TEXTCOLOR);
					svg.drawString(String.valueOf(row), OFFSET, 				  y * SCALE_Y - OFFSET);
					svg.drawString(String.valueOf(row), max_x - SCALE_X + OFFSET, y * SCALE_Y - OFFSET);
				}
				if (row % GRIDTEXTMOD == 1)
				{
					svg.setPaint(GRIDCOLOR);
					svg.drawLine(0, y * SCALE_Y, max_x, y * SCALE_Y);
				}
			}			

			svg.stream("/tmp/test.svg", true);
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
