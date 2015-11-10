package de.cgarbs.knittr.render;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
				SVGGraphics2D svgPage = renderPage(0, 0, bi.getWidth(), bi.getHeight(), 0, 0, false);

				// write SVG to target file
				writeSVG(svgPage, ((File)p.getValue(Project.TARGET_FILE)).getAbsolutePath());
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

					SVGGraphics2D svgPage = renderPage(0, y, bi.getWidth(), height, 0, bi.getHeight() - height - y, rotated);

					writeSVG(svgPage, filename + "." + pageNo + ".svg");
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
		catch (ClassCastException e)
		{
			e.printStackTrace();
		}
		catch (DOMException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}
		catch (SAXException e)
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

	private SVGGraphics2D initSVG() throws DOMException, DataException
	{
		// Get a DOMImplementation.
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();

		// Create an instance of org.w3c.dom.Document.
		String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document document = domImpl.createDocument(svgNS, "svg", null);

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

	/**
	 * Write the SVG file.
	 *
	 * Because Batik does not seem to want to set the page size,
	 * immediately reopen and fix the file through direct XML manipulation.
	 * @param svg the SVG to write
	 * @param filename the filename to use
	 */
	private void writeSVG(SVGGraphics2D svg, String filename) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, DOMException, DataException, ParserConfigurationException, SAXException, IOException
	{
		// write SVG to file
		svg.stream(filename, true);

		/** DOM v3 thingie - TOO SLOW  ~16s for the 7k test file **/

//		long time1 = System.currentTimeMillis();
//
//		// re-read written SVG as XML file
//		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
//		DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");
//		LSParser builder = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);
//
//		// FIXME: this is ultra-slow - why??? switch to plain text processing?!
//
//		Document document = builder.parseURI(filename);
//
//		long time2 = System.currentTimeMillis();
//

		/** XERCES/DOM -- TOO SLOW! also ~16s **/

//	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//	    DocumentBuilder dbuilder = factory.newDocumentBuilder();
//	    document = dbuilder.parse(filename);
//
//		long time3 = System.currentTimeMillis();
//
//		System.err.printf("LSParser:  %6dms  DocBuilder:  %6dms\n", (time2-time1), (time3-time2));
//
//		// set width and height to page size
//		Element root = document.getDocumentElement();
//		root.setAttributeNS(null, "width", getTotalPageWidthMM()+"mm");
//		root.setAttributeNS(null, "height", getTotalPageHeightMM()+"mm");
//
//		// write updated XML file
//		LSSerializer writer = impl.createLSSerializer();
//		writer.writeToURI(document, filename);


//		/** SAX Parsing -- also TOO SLOW!  also ~16s **/
//
//		SAXParserFactory factory = SAXParserFactory.newInstance();
//		SAXParser saxParser = factory.newSAXParser();
//
//		final OutputStreamWriter out = new OutputStreamWriter(System.out);
//
//		DefaultHandler handler = new DefaultHandler() {
//
//			private StringBuilder outputBuffer = new StringBuilder();
//
//			@Override
//			public void characters(char[] ch, int start, int length)
//					throws SAXException
//			{
//				outputBuffer.append(new String(ch, start, length));
//			}
//
//			@Override
//			public void endDocument() throws SAXException
//			{
//				flushOutputBuffer();
//			}
//
//			@Override
//			public void endElement(String uri, String localName, String qName)
//					throws SAXException
//			{
//				String elementName = localName;
//				if (elementName.isEmpty())
//				{
//					elementName = qName; // not namespace-aware
//				}
//
//				outputBuffer.append("</").append(elementName).append(">");
//
//				// write inbetween (save some memory)
//				if (outputBuffer.length() > 8192)
//				{
//					flushOutputBuffer();
//				}
//			}
//
//			@Override
//			public void startDocument() throws SAXException
//			{
//				outputBuffer.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"); // TODO is the charset correct? that's what the SVGWriter currently produces
//				outputBuffer.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.0//EN\" \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">\n"); // TODO is the charset correct? that's what the SVGWriter currently produces
//			}
//
//			@Override
//			public void startElement(String uri, String localName,
//					String qName, Attributes attributes) throws SAXException
//			{
//				String elementName = localName;
//				if (elementName.isEmpty())
//				{
//					elementName = qName; // not namespace-aware
//				}
//				outputBuffer.append("<").append(elementName);
//
//				// BEWARE: OUR ONE AND ONLY TRANSFORMATION,
//				// BECAUSE OF THESE TO ATTRIBUTES WE DO ALL
//				// THIS SAX STUFF:
//				if (elementName.equals("svg"))
//				{
//					try
//					{
//						outputBuffer.append(" width=\"").append(getTotalPageWidthMM()).append("mm\"");
//						outputBuffer.append(" height=\"").append(getTotalPageHeightMM()).append("mm\"");
//					}
//					catch (DataException e)
//					{
//						throw new SAXException(e);
//					}
//				}
//
//
//				if (attributes != null)
//				{
//					for (int i = 0; i < attributes.getLength(); i++)
//					{
//						String attributeName = attributes.getLocalName(i);
//						if (attributeName.isEmpty())
//						{
//							attributeName = attributes.getQName(i); // not namespace-aware
//						}
//						outputBuffer.append(" ");
//						outputBuffer.append(attributeName).append("=\"").append(attributes.getValue(i)).append("\"");
//					}
//				}
//
//				outputBuffer.append(">");
//			}
//
//			/**
//			 * wrap writes to convert exceptions
//			 * @param outputBuffer
//			 * @throws SAXException
//			 */
//			private void flushOutputBuffer() throws SAXException
//			{
//				try
//				{
//					out.write(outputBuffer.toString());
//					out.flush();
//					outputBuffer = new StringBuilder();
//
//				}
//				catch (IOException e)
//				{
//					throw new SAXException(e);
//				}
//			}
//		};
//
//		long timeA = System.currentTimeMillis();
//		saxParser.parse(filename, handler);
//		long timeB = System.currentTimeMillis();
//
//		System.err.printf("SAX passthrough:  %6dms\n", (timeB-timeA));

		/** DO PLAIN TEXT PROCESSING - yucky, but blazing fast **/

		// look out - weird logic and naming!
		File finalFile = new File(filename + ".tmp");
		File tempFile = new File(filename);

		BufferedReader reader = new BufferedReader(new FileReader(tempFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(finalFile));

		String line;
		while ((line = reader.readLine()) != null)
		{
			if (line.startsWith("<svg "))
			{
				writer.append(
						String.format(
							"<svg width=\"%dmm\" height=\"%dmm\" %s",
							getTotalPageWidthMM(),
							getTotalPageHeightMM(),
							line.substring(5)
							)
						);
			}
			else
			{
				writer.append(line);
			}
			writer.newLine();
		}

		reader.close();
		writer.close();

		tempFile.delete(); // FIXME: check return code
		finalFile.renameTo(tempFile); // FIXME: check return code
	}

}
