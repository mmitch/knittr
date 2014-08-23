package de.cgarbs.knittr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;

import de.cgarbs.knittr.data.Project;
import de.cgarbs.knittr.render.SVGWriter;
import de.cgarbs.lib.exception.DataException;

public class Test
{

	public static void main(String[] args)
	{
		try
		{
			System.out.println("test start");

			Project p = new Project();
			new SVGWriter(p).render();
			System.out.println("default rendered");

			PDFTranscoder t = new PDFTranscoder();
			TranscoderInput input = new TranscoderInput(new FileInputStream(new File("/tmp/test.svg")));
			TranscoderOutput output = new TranscoderOutput(new FileOutputStream(new File("/tmp/test.pdf")));
			t.transcode(input, output);
			System.out.println("default PDF rendered");

			p.setValue(Project.SOURCE_FILE, "/home/mitch/Dropbox/schnucki/RainbowDashStrick/Rainbowdashvornefertig.png");
			p.setValue(Project.TARGET_FILE, "/home/mitch/Dropbox/schnucki/RainbowDashStrick/Rainbowdashvornefertig.svg");
			p.setValue(Project.MASCHEN, 24);
			p.setValue(Project.REIHEN, 34);
			new SVGWriter(p).render();
			System.out.println("dash front rendered");

			p.setValue(Project.SOURCE_FILE, "/home/mitch/Dropbox/schnucki/RainbowDashStrick/Rainbowdashhintenfertig.png");
			p.setValue(Project.TARGET_FILE, "/home/mitch/Dropbox/schnucki/RainbowDashStrick/Rainbowdashhintenfertig.svg");
			p.setValue(Project.MASCHEN, 24);
			p.setValue(Project.REIHEN, 34);
			new SVGWriter(p).render();
			System.out.println("dash back rendered");

			System.out.println("test end");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (TranscoderException e)
		{
			e.printStackTrace();
		}
		catch (DataException e)
		{
			e.printStackTrace();
		}

	}

}
