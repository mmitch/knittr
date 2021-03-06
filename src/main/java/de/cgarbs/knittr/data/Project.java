/*
 * Copyright 2014, 2020 (C)  Christian Garbs <mitch@cgarbs.de>
 * Licensed under GNU GPL 3 (or later)
 */
package de.cgarbs.knittr.data;

import java.awt.Color;
import java.io.File;

import de.cgarbs.lib.data.DataModel;
import de.cgarbs.lib.data.type.BooleanAttribute;
import de.cgarbs.lib.data.type.ColorAttribute;
import de.cgarbs.lib.data.type.FileAttribute;
import de.cgarbs.lib.data.type.FloatAttribute;
import de.cgarbs.lib.data.type.IntegerAttribute;
import de.cgarbs.lib.data.type.StringAttribute;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.ValidationError;
import de.cgarbs.lib.i18n.Resource;


public class Project extends DataModel
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String SOURCE_FILE = "SOURCE_FILE";
	public static final String TARGET_FILE = "TARGET_FILE";
	public static final String GRIDTEXTMOD = "GRIDTEXTMOD";
	public static final String GRIDWIDTHSMALL = "GRIDWIDTHSMALL";
	public static final String GRIDWIDTHBIG = "GRIDWIDTHBIG";
	public static final String FONTNAME = "FONTNAME";
	public static final String TEXTCOLOR = "TEXTCOLOR";
	public static final String OFFSET = "OFFSET";
	public static final String GRIDCOLOR = "GRIDCOLOR";
	public static final String TOTALSCALE = "TOTALSCALE";
	public static final String REIHEN = "REIHEN";
	public static final String MASCHEN = "MASCHEN";
	public static final String ROWMARK = "ROWMARK";
	public static final String ROWMARKCOLOR = "ROWMARKCOLOR";
	public static final String PAGEWIDTH = "PAGEWIDTH";
	public static final String PAGEHEIGHT = "PAGEHEIGHT";
	public static final String PAGEBORDER = "PAGEBORDER";
	public static final String PAGEMINFILL = "PAGEMINFILL";
	public static final String GREYSCALE = "GREYSCALE";

	/**
	 * generate a project with default settings
	 */
	public Project() throws DataException
	{
		super(new Resource("de.cgarbs.knittr.resource.Project"));

		// IO
		addAttribute(SOURCE_FILE, FileAttribute.builder().setNullable(false).setMustExist(true).setMustRead(true).addFileFilter(getResource().get("FLT_source_files"), "gif", "png").build());
		addAttribute(TARGET_FILE, FileAttribute.builder().setNullable(false).setMustWrite(true).addFileFilter(getResource().get("FLT_target_files"), "svg").build());

		setValue(SOURCE_FILE, "resource/image_test.png");
		setValue(TARGET_FILE, "/tmp/test.svg");

		// calculation
		addAttribute(TOTALSCALE, IntegerAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(MASCHEN, IntegerAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(REIHEN, IntegerAttribute.builder().setNullable(false).setMinValue(1).build());

		setValue(TOTALSCALE, 512);
		setValue(MASCHEN, 24);
		setValue(REIHEN, 32);

		// graphics
		addAttribute(GRIDWIDTHSMALL, FloatAttribute.builder().setDecimals(2).build());
		addAttribute(GRIDWIDTHBIG, FloatAttribute.builder().setDecimals(2).build());
		addAttribute(GRIDTEXTMOD, IntegerAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(GRIDCOLOR, ColorAttribute.builder().setNullable(false).build());
		addAttribute(TEXTCOLOR, ColorAttribute.builder().setNullable(false).build());
		addAttribute(FONTNAME, StringAttribute.builder().setNullable(false).setMinLength(1).build());
		addAttribute(OFFSET, IntegerAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(ROWMARK, BooleanAttribute.builder().setNullable(false).build());
		addAttribute(ROWMARKCOLOR, ColorAttribute.builder().setNullable(false).build());
		addAttribute(PAGEWIDTH, IntegerAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(PAGEHEIGHT, IntegerAttribute.builder().setNullable(false).setMinValue(1).build());
		addAttribute(PAGEBORDER, IntegerAttribute.builder().setNullable(false).setMinValue(0).build());
		addAttribute(PAGEMINFILL, IntegerAttribute.builder().setNullable(false).setMinValue(1).setMaxValue(99).build());
		addAttribute(GREYSCALE, BooleanAttribute.builder().setNullable(false).build());

		setValue(GRIDWIDTHSMALL, 0.5f);
		setValue(GRIDWIDTHBIG, 2f);
		setValue(GRIDTEXTMOD, 5);
		setValue(GRIDCOLOR, Color.BLACK);
		setValue(TEXTCOLOR, Color.BLACK);
		setValue(FONTNAME, "DialogInput");
		setValue(OFFSET, 3);
		setValue(ROWMARK, true);
		setValue(ROWMARKCOLOR, Color.GRAY);
		setValue(PAGEWIDTH, 210);
		setValue(PAGEHEIGHT, 297);
		setValue(PAGEBORDER, 10);
		setValue(PAGEMINFILL, 10);
		setValue(GREYSCALE, false);
	}

	@Override
	public String getModelName()
	{
		return "KnittrProject";
	}

	@Override
	public void additionalValidation() throws ValidationError
	{
		super.additionalValidation();

		try
		{
			File target = (File) getValue(TARGET_FILE);
			File source = (File) getValue(SOURCE_FILE);
			if (target != null && source != null
					&& target.getAbsoluteFile().equals(source.getAbsoluteFile()))
			{
				throw new ValidationError(
						getAttribute(TARGET_FILE),
						"target and source file are the same",
						getResource().get("EXC_TARGET_SOURCE_SAME")
						);
			}
		}
		catch (DataException e)
		{
			// FIXME ignore? don't ignore?
		}

	}
}
