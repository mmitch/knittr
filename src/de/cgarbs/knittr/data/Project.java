package de.cgarbs.knittr.data;
import java.awt.Color;
import java.io.Serializable;


public class Project implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8797415297158373144L;
	
	
	/** stroke width for normal grid line */
	private float gridWidthSmall;
	/** stroke width for thicker grid line */
	private float gridWidthBig;
	
	/** Maschen pro 10 cm */
	private int maschen;
	/** Reihen pro 10 cm */
	private int reihen;
	
	/** arbitrary global size factor */
	private int totalScale;
	
	/** text and thicker grid line every n cells */
	private int gridTextMod;
	
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
	
	/** source filename (PNG) */
	private String sourceFile;
	/** target filename (SVG) */
	private String targetFile;
	
	/**
	 * generate a project with default settings
	 */
	public Project()
	{
		sourceFile = "resource/image_test.png";
		targetFile = "/tmp/test.svg";
		
		gridWidthSmall = 0.5f;
		gridWidthBig   = 2f;
		gridTextMod = 5;
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
	
	public float getGridWidthSmall()
	{
		return gridWidthSmall;
	}
	
	public float getGridWidthBig()
	{
		return gridWidthBig;
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
	
	public int getGridTextMod()
	{
		return gridTextMod;
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

	public String getSourceFile()
	{
		return sourceFile;
	}

	public String getTargetFile()
	{
		return targetFile;
	}

	public void setGridWidthSmall(float gridWidthSmall)
	{
		this.gridWidthSmall = gridWidthSmall;
	}

	public void setGridWidthBig(float gridWidthBig)
	{
		this.gridWidthBig = gridWidthBig;
	}
	
	public void setSourceFile(String sourceFile)
	{
		this.sourceFile = sourceFile;
	}

	public void setTargetFile(String targetFile)
	{
		this.targetFile = targetFile;
	}

	public void setMaschen(int maschen)
	{
		this.maschen = maschen;
	}

	public void setReihen(int reihen)
	{
		this.reihen = reihen;
	}
	
}
