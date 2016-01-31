knittr
======

[![Build Status](https://travis-ci.org/mmitch/knittr.svg?branch=master)](https://travis-ci.org/mmitch/knittr)
[![Coverage Status](https://codecov.io/github/mmitch/knittr/coverage.svg?branch=master)](https://codecov.io/github/mmitch/knittr?branch=master)
[![GPL 3+](https://img.shields.io/badge/license-GPL%203%2B-blue.svg)](http://www.gnu.org/licenses/gpl-3.0-standalone.html)


about
-----

knittr is a tool to brush up knitting patterns for printing.  You can
for example start with a 16x16 pixel smilie icon and generate a
full-page knitting pattern from it.

Features are:

- scalable vector graphics output
- multi-page output (eg. use full page width and generate multiple
  pages vertically)
- configurable grid, column and row numbers
- mark every other row (distingish left and right rows)

The project homepage is at <https://github.com/mmitch/knittr/>


but why
-------

My wife wanted to create her own knitting patterns.  The internet is
full of instructions saying "open Gimp, zoom in 1600%, activate the
grid, print a screenshot", which from my nerd standpoint is totally
lame and has certain drawbacks:

- resolution is limited by your screen size
- no row and column numbering
- only square pixels (stitches are seldom square as I have learnt)

So I first wrote a [shell script][1] to overcome these limitations and
make my wife happy.  In the end, things escalated, the shell script
turned into this Java application, I got My Little Pony themed
sweaters and pillows out of it as well as my own [Java GUI library][2]
and multiple nights of coding.  Yaaaay!

   [1]: <https://github.com/mmitch/mitchscripts/blob/master/bash/knit.sh>
   [2]: <https://github.com/mmitch/cgarbs-javalib/>


dependencies
------------

- for running knittr:
  - Java runtime

- for building knittr:
  - Gradle build environment
  - bash, perl for extended build tools (both optional)


usage
-----

	usage: knittr.jar [options ...] [--] [<inputfile>]
	
	available options:");
	  --layout=<layout>    - set layout
	  --style=<style>      - set look and feel
	  --help               - show this help text
	
	use `help' for <layout> or <style> to a list available options


quickstart guide
----------------

1. Run ``java -jar knittr.jar`` to start a new knittr project.

2. Select a bitmap to be converted as ``source file``.
   The bitmap will show up in the preview.  Small bitmaps with few
   and different colors will work best.  The bitmap needs to be
   preprocessed so that one pixel corresponds to one stitch.

3. Select a ``target file`` to contain the result of the conversion.
   Target file format is SVG.  This file will be overwritten!

4. Go to the ``gauge swatch`` tab and enter the ``stitches`` and
   ``rows`` values for your yarn.  ``stitches`` means the number of
   horizontal stitches you need to knit 10cm (4in), while ``rows``
   means the number of vertical rows you need to knit 10cm (4in).
   Both values are used to provide proper scaling of your output.

5. Select ``Render to SVG``.  Afterwards, open and print the generated
   SVG with your favourite SVG editor or viewer.

6. You can ``Save`` and ``Load`` knittr projects for later use or for
   experimenting with different settings.

7. Try out the settings in the other tabs!


building with Gradle
--------------------

Building via ``build.gradle`` should be straightforward.

There are some additional build targets available:

* ``fullJar`` builds a jar containing all dependencies in
  ``build/libs/knittr-full.jar``

* ``fixit`` runs a bash script to fix line breaks and indentation on
  empty lines.

* ``checkl10n`` checks the .property files for missing translations

* ``publishDropbox`` and ``publishDropboxFat`` copy the generated jars
  to my Dropbox to sync them to my wife's computer - this propably is of
  no use to you :-)


copyright
---------

knittr - brush up knitting patterns for printing  
Copyright (C) 2014  Christian Garbs <mitch@cgarbs.de>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
