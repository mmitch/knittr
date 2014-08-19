package de.cgarbs.knittr.render;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.cgarbs.knittr.data.Project;
import de.cgarbs.lib.exception.DataException;

/**
 * base class for rendering backends
 * 
 * @author mitch
 *
 */
public abstract class AbstractRenderer
{
	protected Project p;
	protected BufferedImage bi;
	protected Raster r;
	
	/**
	 * Default constructor for a renderer
	 * @throws IOException 
	 * @throws DataException 
	 */
	public AbstractRenderer(Project project) throws IOException, DataException
	{
		p = project;
		bi = getSourceImage();
		r = bi.getRaster();
	}
	
	/**
	 * renders a whole project
	 * @param p the project
	 */
	abstract public void render();
	
	/**
	 * reads the source image from the project
	 * @throws IOException 
	 * @throws DataException 
	 * @returns the source image
	 */
	BufferedImage getSourceImage() throws IOException, DataException
	{
		return ImageIO.read((File)p.getValue(Project.SOURCE_FILE));
	}
	
	/**
	 * rotates the image into portrait mode if needed
	 */
	void ensurePortrait()
	{
		if (r.getHeight() < r.getWidth())
		{
			BufferedImage biNew = new BufferedImage(
					bi.getHeight(),
					bi.getWidth(),
					bi.getType());

			AffineTransform at = new AffineTransform();
			at.translate(biNew.getWidth(), 0);
			at.rotate(Math.PI/2, 0, 0);
			Graphics2D g = biNew.createGraphics();
			g.drawImage(bi, at, null);
		
			bi = biNew;
			r = bi.getRaster();
		}
	}
}
