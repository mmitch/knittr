package de.cgarbs.knitter.render;

import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.cgarbs.knitter.data.Project;

/**
 * base class for rendering backends
 * 
 * @author mitch
 *
 */
public abstract class AbstractRenderer
{
	protected Project p;
	protected Raster r;
	
	
	/**
	 * Default constructor for a renderer
	 * @throws IOException 
	 */
	public AbstractRenderer(Project project) throws IOException
	{
		p = project;
		r = getSourceImage();
	}
	
	/**
	 * renders a whole project
	 * @param p the project
	 */
	abstract public void render();
	
	/**
	 * reads the source image from the project
	 * @throws IOException 
	 * @returns the source image
	 */
	Raster getSourceImage() throws IOException
	{
		return ImageIO.read(new File(p.getSourceFile())).getRaster();
	}
}
