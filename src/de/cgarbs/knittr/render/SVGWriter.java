package de.cgarbs.knittr.render;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import de.cgarbs.knittr.data.Project;
import de.cgarbs.lib.exception.DataException;

/**
 * SVG rendering backend
 *
 * @author mitch
 *
 */
public class SVGWriter extends AbstractRenderer
{

	public SVGWriter(Project project) throws IOException, DataException
	{
		super(project);
	}

	@Override
	/**
	 * renders the whole project to a file in SVG format
	 * @param p
	 */
	public void render()
	{
		try
		{
			// RENDER WHOLE PAGE
			{
				SVGGraphics2D svg = renderPage(0, 0, bi.getWidth(), bi.getHeight(), 0, 0, false);

				// write SVG to target file
				svg.stream(((File)p.getValue(Project.TARGET_FILE)).getAbsolutePath(), true);
			}

			// RENDER MULTIPAGE
			{
				double PAGEMINFILL = 0.01 * (Integer)p.getValue(Project.PAGEMINFILL);
				String filename = ((File)p.getValue(Project.TARGET_FILE)).getAbsolutePath();
				filename = filename.replace(".svg", "");

				boolean rotated = ensurePortrait();

				// DIN A aspect ratio - landscape format
				double pageAspect  = (double)getUsablePageWidthMM() / (double)getUsablePageHeightMM(); // landscape
				double pixelAspect = (double) getScaleX(p) / (double) getScaleY(p);
				if (rotated)
				{
					pixelAspect = 1/pixelAspect;
				}

				int pageHeight = (int) Math.floor(bi.getWidth() * pageAspect * pixelAspect);
				// calculate height of last page
				int lastPageHeight = bi.getHeight() % pageHeight;
				while (lastPageHeight > 0 && lastPageHeight < pageHeight * PAGEMINFILL)
				{
					// last page less than PAGEMINFILL % filled, rescale to get rid of it
					// this could be calculated, but I don't want to think of a formula, just use a loop :-)
					pageHeight++;
					lastPageHeight = bi.getHeight() % pageHeight;
				}

				int pageNo = 1;
				int height = pageHeight;
				for (int y=0; y<bi.getHeight(); y+=pageHeight, pageNo++)
				{
					if (y + height > bi.getHeight())
					{
						height = bi.getHeight() - y;
					}

					SVGGraphics2D svg = renderPage(0, y, bi.getWidth(), height, 0, bi.getHeight() - height - y, rotated);

					svg.stream(filename + "." + pageNo + ".svg", true);
				}
			}
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (DataException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * renders a single page
	 *
	 * @param X x start position to render
	 * @param Y y start position to render
	 * @param W width to render
	 * @param H height to render
	 * @param C columns offset of rendered page
	 * @param R row offset of rendered page
	 * @param rotated if the image was rotated from landscape to portrait
	 * @return rendered page
	 * @throws DataException
	 */
	private SVGGraphics2D renderPage(int X, int Y, int W, int H, int C, int R, boolean rotated) throws DataException
	{

		// init variables
		int rgb = 0;
		SVGGraphics2D svg = initSVG();

		// cache values

		// FIXME better use doubles? or set TOTAL_SCALE to higher value?
		int SCALE_X = (int) Math.floor(getScaleX(p));
		int SCALE_Y = (int) Math.floor(getScaleY(p));
		if (rotated)
		{
			int tmp = SCALE_X;
			SCALE_X = SCALE_Y;
			SCALE_Y = tmp;
		}

		int OFFSET  = (Integer) p.getValue(Project.OFFSET);
		int MAX_XT = W * SCALE_X;
		int MAX_YT = H * SCALE_Y;
		int GRIDTEXTMOD = (Integer) p.getValue(Project.GRIDTEXTMOD);
		Color TEXTCOLOR = (Color) p.getValue(Project.TEXTCOLOR);
		Color GRIDCOLOR = (Color) p.getValue(Project.GRIDCOLOR);
		Color ROWMARKCOLOR = (Color) p.getValue(Project.ROWMARKCOLOR);

		// set a scale factor to start calculationg in mm
		// initially: 1 coordinate = 1 pixel
		// Batik uses BATIK_DPI pixels per inch
		// there are 25.4mm to an inch
		//
		//      BATIK_DPI px = 1 in
		// <=>          1 px = 1/BATIK_DPI in
		// <=>     1/25.4 px = 1/BATIK_DPI mm
		// <=>          1 px = 25.4/BATIK_DPI mm
		double factor = BATIK_DPI/25.4d;

		// translate to page start (skip the border)
		svg.translate(getPageBordersMM() * factor, -getPageBordersMM() * factor);

		// we produce and calculate landscape, but for easier printing portrait is better
		// just rotate EVERYTHING once we're finished
		// rotation center found by good old trial&error!
		double center = getUsablePageHeightMM() * factor / 2;
		svg.rotate(-Math.PI/2, center, center);

		// scale to fit page contents
		factor = (double) (getUsablePageHeightMM() - 2*getPageBordersMM()) * factor / (double) MAX_XT; // we scale to fit on X, so take this as the factor
		svg.scale(factor, factor); // keep aspect ratio, same factor in both dimensions

		// render pixels into squares
		svg.setStroke(new BasicStroke((Float)p.getValue(Project.GRIDWIDTHSMALL)));
		for (int ys=Y, yt=0; ys<Y+H; ys++, yt+=SCALE_Y)
		{
			// OPTIMIZATION: use only one block for adjacent pixels with the same color
			int lastXt = 0;
			Color lastColor = null;
			for (int xs=X, xt=0; xs<X+W; xs++, xt+=SCALE_X)
			{
				rgb = bi.getRGB(xs, ys);
				Color newColor = colormap.get(rgb);
				if (newColor.equals(lastColor))
				{
					// just stack this for larger blocks
				}
				else
				{
					if (lastColor != null)
					{
						svg.setPaint(lastColor);
						svg.fill(new Rectangle( lastXt, yt, xt - lastXt, SCALE_Y));
					}
					lastColor = newColor;
					lastXt = xt;
				}
			}
			svg.setPaint(lastColor);
			svg.fill(new Rectangle( lastXt, yt, MAX_XT - lastXt, SCALE_Y));
		}

		// mark every other line
		// FIXME: depending on the rotation, this draws either \ or / lines - don't care for now
		svg.setPaint(ROWMARKCOLOR);
		if (! rotated)
		{
			for (int ys=Y, yt=0; ys<Y+H; ys++, yt+=SCALE_Y)
			{
				if (ys % 2 == 0)
				{
					for (int xs=X, xt=0; xs<X+W; xs++, xt+=SCALE_X)
					{
						svg.drawLine(xt, yt, xt+SCALE_X, yt+SCALE_Y);
					}
				}
			}
		}
		else
		{
			for (int xs=X, xt=0; xs<X+W; xs++, xt+=SCALE_X)
			{
				if (xs % 2 == 0)
				{
					for (int ys=Y, yt=0; ys<Y+H; ys++, yt+=SCALE_Y)
					{
						svg.drawLine(xt, yt, xt+SCALE_X, yt+SCALE_Y);
					}
				}
			}
		}

		// thin grid
		svg.setPaint(GRIDCOLOR);
		for (int xs=X, xt=0, col=C+W; xs<X+W; xs++, xt+=SCALE_X, col--)
		{
			if (col % GRIDTEXTMOD != 0)
			{
				svg.drawLine(xt, 0, xt, MAX_YT);
			}
		}
		for (int ys=Y, yt=0, row=R+H+1; ys<Y+H; ys++, yt+=SCALE_Y, row--)
		{
			if (row % GRIDTEXTMOD != 1)
			{
				svg.drawLine(0, yt, MAX_XT, yt);
			}
		}
		svg.drawLine(MAX_XT, 0, MAX_XT, MAX_YT);
		svg.drawLine(0, MAX_YT, MAX_XT, MAX_YT);

		// thick grid
		svg.setStroke(new BasicStroke((Float)p.getValue(Project.GRIDWIDTHBIG), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		for (int xs=X, xt=0, col=C+W; xs<X+W; xs++, xt+=SCALE_X, col--)
		{
			if (col % GRIDTEXTMOD == 0)
			{
				svg.drawLine(xt, 0, xt, MAX_YT);
			}
		}
		for (int ys=Y, yt=0, row=R+H+1; ys<Y+H; ys++, yt+=SCALE_Y, row--)
		{
			if (row % GRIDTEXTMOD == 1)
			{
				svg.drawLine(0, yt, MAX_XT, yt);
			}
		}

		// texts
		svg.setFont(new Font((String) p.getValue(Project.FONTNAME), Font.BOLD, SCALE_Y - OFFSET * 2));
		svg.setPaint(TEXTCOLOR);

		for (int xs=X, xt=0, col=C+W; xs<X+W; xs++, xt+=SCALE_X, col--)
		{
			if (col % GRIDTEXTMOD == 0)
			{
				svg.drawString(String.valueOf(col), xt + OFFSET, MAX_YT  - OFFSET);
				svg.drawString(String.valueOf(col), xt + OFFSET, SCALE_Y - OFFSET);
			}
		}
		for (int ys=Y, yt=0, row=R+H+1; ys<Y+H; ys++, yt+=SCALE_Y, row--)
		{
			if (row % GRIDTEXTMOD == 1)
			{
				svg.setPaint(TEXTCOLOR);
				svg.drawString(String.valueOf(row-1), OFFSET,                    yt - OFFSET + SCALE_Y);
				svg.drawString(String.valueOf(row-1), MAX_XT - SCALE_X + OFFSET, yt - OFFSET + SCALE_Y);
			}
		}

		return svg;
	}

	private SVGGraphics2D initSVG()
	{
		// Get a DOMImplementation.
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document document = domImpl.createDocument(svgNS, "svg", null);

		// add page size data
		// scaling see here: http://stackoverflow.com/questions/1346922/
		// after this, 1 unit is 1 mm and the printable area is 1 page
		// FIXME: REMOVE
//		Element root = document.getDocumentElement();
//		root.setAttributeNS(null, "width", getTotalPageWidthMM()+"mm");
//		root.setAttributeNS(null, "height", getTotalPageHeightMM()+"mm");
//		root.setAttributeNS(null, "viewbox", "0 0 " + getTotalPageWidthMM() + "0 "+ getTotalPageHeightMM() + "0");

		// Create an instance of the SVG Generator.
		return new SVGGraphics2D(document);
	}

	// FIXME move where? to Project?
	double getScaleX(Project p) throws DataException
	{
		double scale = (Integer) p.getValue(Project.TOTALSCALE);
		double value = (Integer) p.getValue(Project.MASCHEN);
		return scale / value;
	}
	double getScaleY(Project p) throws DataException
	{
		double scale = (Integer) p.getValue(Project.TOTALSCALE);
		double value = (Integer) p.getValue(Project.REIHEN);
		return scale / value;
	}
}
