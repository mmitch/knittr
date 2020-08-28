/*
 * Copyright 2014, 2020 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
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
import de.cgarbs.knittr.exception.RenderException;
import de.cgarbs.knittr.render.SVGWriter;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.i18n.Resource;

public class Test
{

	@org.junit.Test
	public void oldFatTest()
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

			File tmpFile = File.createTempFile("knittr-tmp", "jobj");
			p.writeToFile(tmpFile);
			System.out.println("default written to disk...");

			p.readFromFile(tmpFile);
			System.out.println("...and read from disk again");


//			p.setValue(Project.SOURCE_FILE, "/home/mitch/Dropbox/schnucki/RainbowDashStrick/Rainbowdashvornefertig.png");
//			p.setValue(Project.TARGET_FILE, "/home/mitch/Dropbox/schnucki/RainbowDashStrick/Rainbowdashvornefertig.svg");
//			p.setValue(Project.MASCHEN, 24);
//			p.setValue(Project.REIHEN, 34);
//			new SVGWriter(p).render();
//			System.out.println("dash front rendered");
//
//			p.setValue(Project.SOURCE_FILE, "/home/mitch/Dropbox/schnucki/RainbowDashStrick/Rainbowdashhintenfertig.png");
//			p.setValue(Project.TARGET_FILE, "/home/mitch/Dropbox/schnucki/RainbowDashStrick/Rainbowdashhintenfertig.svg");
//			p.setValue(Project.MASCHEN, 24);
//			p.setValue(Project.REIHEN, 34);
//			new SVGWriter(p).render();
//			System.out.println("dash back rendered");

			System.out.println("resource tests:");
			Resource R = new Resource("de.cgarbs.knittr.resource.Test");
			System.out.println(R.get("key1"));
			System.out.println(R.get("key2", "A", "B"));
			System.out.println(R.get("key2", "B", "A"));
			System.out.println(R.get("key3", "A", "A"));
			System.out.println(R.get("missingNO"));

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
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (RenderException e)
		{
			e.printStackTrace();
		}

	}

}
