import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class BatikDemo
{

	public static void paint(Graphics2D g2d)
	{
		g2d.setPaint(Color.red);
		g2d.fill(new Rectangle(10, 10, 100, 100));
	}

	public static void writeSVG()
	{
		try
		{
			// Get a DOMImplementation.
			DOMImplementation domImpl = GenericDOMImplementation
					.getDOMImplementation();

			// Create an instance of org.w3c.dom.Document.
			String svgNS = "http://www.w3.org/2000/svg";
			Document document = domImpl.createDocument(svgNS, "svg", null);

			// Create an instance of the SVG Generator.
			SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

			// Ask the test to render into the SVG Graphics2D implementation.
			paint(svgGenerator);

			svgGenerator.stream("/tmp/test.svg", true);
		}
		catch (SVGGraphics2DIOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writePDF()
	{
		try
		{
			// Get a DOMImplementation.
			DOMImplementation domImpl = GenericDOMImplementation
					.getDOMImplementation();

			// Create an instance of org.w3c.dom.Document.
			String svgNS = "http://www.w3.org/2000/svg";
			Document document = domImpl.createDocument(svgNS, "svg", null);

			// Create an instance of the SVG Generator.
			SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

			// Ask the test to render into the SVG Graphics2D implementation.
			paint(svgGenerator);

			PDFTranscoder t = new PDFTranscoder();
			TranscoderInput input = new TranscoderInput("/tmp/test.svg");
			TranscoderOutput output = new TranscoderOutput("/tmp/test.pdf");

			t.transcode(input, output);
		}
		catch (TranscoderException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
