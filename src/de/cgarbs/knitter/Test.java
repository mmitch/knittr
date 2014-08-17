package de.cgarbs.knitter;

import java.io.IOException;

import de.cgarbs.knitter.data.Project;
import de.cgarbs.knitter.render.SVGWriter;

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
			
			p.setSourceFile("/home/mitch/Dropbox/schnucki/Rainbowdashvornefertig.png");
			p.setTargetFile("/home/mitch/Dropbox/schnucki/Rainbowdashvornefertig.svg");
			p.setMaschen(22);
			p.setReihen(33);
			new SVGWriter(p).render();
			System.out.println("dash front rendered");
			
			p.setSourceFile("/home/mitch/Dropbox/schnucki/Rainbowdashhintenfertig.png");
			p.setTargetFile("/home/mitch/Dropbox/schnucki/Rainbowdashhintenfertig.svg");
			p.setMaschen(22);
			p.setReihen(33);
			new SVGWriter(p).render();
			System.out.println("dash back rendered");

			System.out.println("test end");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

}
