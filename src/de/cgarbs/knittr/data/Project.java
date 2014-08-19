package de.cgarbs.knittr.data;
import java.awt.Color;

import de.cgarbs.lib.data.DataModel;
import de.cgarbs.lib.data.type.FileAttribute;
import de.cgarbs.lib.data.type.FloatAttribute;
import de.cgarbs.lib.data.type.IntAttribute;
import de.cgarbs.lib.exception.DataException;


public class Project extends DataModel
{
	
	/** Maschen pro 10 cm */
	private int maschen;
	/** Reihen pro 10 cm */
	private int reihen;
	
	/** arbitrary global size factor */
	private int totalScale;
	
	/** color for grid lines */
	private Color gridColor; 

	/** cell x width */
	private int scaleX;
	/** cell y height */
	private int scaleY;
	
	/** pixel offset from grid border for texts */
	private int offset;
	
	/** font name for texts */
	private String fontName;
	/** color for texts */
	private Color textColor;
	
	public static final String SOURCE_FILE = "SOURCE_FILE";
	public static final String TARGET_FILE = "TARGET_FILE";
	public static final String GRIDTEXTMOD = "GRIDTEXTMOD";
	public static final String GRIDWIDTHSMALL = "GRIDWIDTHSMALL";
	public static final String GRIDWIDTHBIG = "GRIDWIDTHBIG";
	
	/**
	 * generate a project with default settings
	 */
	public Project() throws DataException
	{
		addAttribute(SOURCE_FILE, FileAttribute.builder().setNullable(false).setMustExist(true).setMustRead(true).build());
		addAttribute(TARGET_FILE, FileAttribute.builder().setNullable(false).setMustWrite(true).build());
		addAttribute(GRIDTEXTMOD, IntAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(GRIDWIDTHSMALL, FloatAttribute.builder().build());
		addAttribute(GRIDWIDTHBIG, FloatAttribute.builder().build());
		
		setValue(SOURCE_FILE, "resource/image_test.png");
		setValue(TARGET_FILE, "/tmp/test.svg");
		
		setValue(GRIDWIDTHSMALL, 0.5f);
		setValue(GRIDWIDTHBIG, 2f);
		setValue(GRIDTEXTMOD, 5);
		totalScale = 512;
		maschen = 24;
		reihen  = 32;
		offset = 3;
		
		textColor = Color.BLACK;
		gridColor = Color.BLACK;
		
		fontName = "DialogInput";

		scaleX = totalScale / maschen;
		scaleY = totalScale / reihen;
	}
	
	public int getMaschen()
	{
		return maschen;
	}
	
	public int getReihen()
	{
		return reihen;
	}
	
	public int getTotalScale()
	{
		return totalScale;
	}
	
	public Color getGridColor()
	{
		return gridColor;
	}
	
	public int getScaleX()
	{
		return scaleX;
	}
	
	public int getScaleY()
	{
		return scaleY;
	}
	
	public int getOffset()
	{
		return offset;
	}
	
	public String getFontName()
	{
		return fontName;
	}

	public Color getTextColor()
	{
		return textColor;
	}

	public void setMaschen(int maschen)
	{
		this.maschen = maschen;
	}

	public void setReihen(int reihen)
	{
		this.reihen = reihen;
	}

	@Override
	public String getModelName()
	{
		return "KnittrProject";
	}
	
}
