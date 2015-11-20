/*
 * Copyright 2014 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.knittr.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import de.cgarbs.knittr.data.Project;
import de.cgarbs.knittr.exception.RenderException;
import de.cgarbs.lib.exception.DataException;

/**
 * base class for rendering backends
 *
 * @author mitch
 *
 */
public abstract class AbstractRenderer
{
	Colormap colormap;
	protected Project p;
	protected BufferedImage bi;

	// MAGIC CONSTANT - can this change? where does this come from?
	protected double BATIK_DPI = 96;

	/**
	 * Default constructor for a renderer
	 * @throws IOException
	 * @throws DataException
	 */
	public AbstractRenderer(Project project) throws RenderException
	{
		try
		{
			p = project;
			bi = getSourceImage();

			if (Boolean.TRUE.equals(p.getValue(Project.GREYSCALE)))
			{
				colormap = new Colormap(Colormap.Type.GREYSCALE, bi);
			}
			else
			{
				colormap = new Colormap(Colormap.Type.NORMAL, bi);
			}
		}
		catch (IOException e)
		{
			wrapIOException(e);
		}
		catch (DataException e)
		{
			wrapRenderException(e);
		}
	}

	/**
	 * renders a whole project
	 * @param p the project
	 */
	abstract public void render() throws RenderException;

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
	 * @return true = image was rotated
	 */
	protected boolean ensurePortrait()
	{
		if (bi.getHeight() < bi.getWidth())
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

			return true;
		}
		return false;
	}

	// FIXME make this stuff configurable!
	int getPageBordersMM() throws DataException
	{
		return (Integer)p.getValue(Project.PAGEBORDER);
	}

	public int getTotalPageWidthMM() throws DataException
	{
		return (Integer)p.getValue(Project.PAGEWIDTH);
	}

	public int getTotalPageHeightMM() throws DataException
	{
		return (Integer)p.getValue(Project.PAGEHEIGHT);
	}

	public int getUsablePageWidthMM() throws DataException
	{
		return getTotalPageWidthMM() - (2 * getPageBordersMM());
	}

	public int getUsablePageHeightMM() throws DataException
	{
		return getTotalPageHeightMM() - (2 * getPageBordersMM());
	}

	/**
	 * Creates the colormap to an image.  Currently normal colors
	 * and greyscale conversion are supported.
	 * @param img the image to convert
	 * @param greyscale true: convert image to greyscale
	 * @return colormap of the image
	 */
	public static Map<Integer, Color> getColormap(BufferedImage img, Boolean greyscale)
	{
		// TODO Auto-generated method stub
		return null;
	}

	protected void wrapIOException(Throwable t) throws RenderException
	{
		throw new RenderException(RenderException.ERROR.IO, t);
	}

	protected void wrapRenderException(Throwable t) throws RenderException
	{
		throw new RenderException(RenderException.ERROR.RENDER, t);
	}
}
