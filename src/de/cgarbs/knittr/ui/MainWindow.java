package de.cgarbs.knittr.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.cgarbs.knittr.data.Project;
import de.cgarbs.knittr.render.SVGWriter;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.GlueException;
import de.cgarbs.lib.exception.ValidationErrorList;
import de.cgarbs.lib.glue.Binding;
import de.cgarbs.lib.glue.Glue;
import de.cgarbs.lib.ui.SimpleTabbedLayout;

public class MainWindow extends JFrame
{

	private File currentFile;

	private Glue<Project> glue;

	/**
	 *
	 */
	private static final long serialVersionUID = -6117333503098355182L;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 *
	 * @throws DataException
	 * @throws GlueException
	 */
	public MainWindow(Project p, File f) throws GlueException, DataException
	{
		currentFile = f;

		// add bindings
		glue = new Glue<Project>(p);

		Binding b_source_file = glue.addBinding(Project.SOURCE_FILE);
		Binding b_target_file = glue.addBinding(Project.TARGET_FILE);

		Binding b_maschen = glue.addBinding(Project.MASCHEN);
		Binding b_reihen = glue.addBinding(Project.REIHEN);

		Binding b_gridwidthsmall = glue.addBinding(Project.GRIDWIDTHSMALL);
		Binding b_gridwidthbig = glue.addBinding(Project.GRIDWIDTHBIG);
		Binding b_gridtextmod = glue.addBinding(Project.GRIDTEXTMOD);
		Binding b_gridcolor = glue.addBinding(Project.GRIDCOLOR);

		Binding b_textcolor = glue.addBinding(Project.TEXTCOLOR);
		Binding b_fontname = glue.addBinding(Project.FONTNAME);
		Binding b_offset = glue.addBinding(Project.OFFSET);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(500, 300));
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		contentPane.setLayout(gbl_contentPane);

		JTextArea infoText = new JTextArea();
		infoText.setEditable(false);
		infoText.setText("Knittr\n2014 (C) by Christian Garbs <mitch@cgarbs.de>\nlicensed under GNU GPL v3 or later\nhttps://github.com/mmitch/knittr/");

//		JComponent jthingie = SimpleVerticalLayout.builder()
		JComponent jthingie = SimpleTabbedLayout.builder()
//		JComponent jthingie = BorderedVerticalLayout.builder()
				.startNextGroup("files")
				.addAttribute(b_source_file).addAttribute(b_target_file)
				.startNextGroup("maschenprobe")
				.addAttribute(b_maschen).addAttribute(b_reihen)
				.startNextGroup("grid")
				.addAttribute(b_gridtextmod).addAttribute(b_gridwidthbig).addAttribute(b_gridwidthsmall).addAttribute(b_gridcolor)
				.startNextGroup("font")
				.addAttribute(b_textcolor).addAttribute(b_fontname).addAttribute(b_offset)
				.startNextGroup("about")
				.addComponent(infoText)
				.build();

		GridBagConstraints gbc_pnlGrid = new GridBagConstraints();
//		gbc_pnlGrid.insets = new Insets(0, 0, 5, 5);
		gbc_pnlGrid.fill = GridBagConstraints.BOTH;
		gbc_pnlGrid.gridx = 0;
		gbc_pnlGrid.gridy = 0;
		gbc_pnlGrid.weightx = 1;
		gbc_pnlGrid.weighty = 1;
		contentPane.add(jthingie, gbc_pnlGrid);

		JPanel pnlActions = new JPanel();
		GridBagConstraints gbc_pnlActions = new GridBagConstraints();
//		gbc_pnlActions.insets = new Insets(0, 0, 5, 5);
		gbc_pnlActions.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlActions.gridx = 0;
		gbc_pnlActions.gridy = 1;
		gbc_pnlActions.weightx = 1;
		gbc_pnlActions.weighty = 0;
		contentPane.add(pnlActions, gbc_pnlActions);
		GridBagLayout gbl_pnlActions = new GridBagLayout();
		pnlActions.setLayout(gbl_pnlActions);

		JButton btnLoad = new JButton("LOAD");
		GridBagConstraints gbc_btnLoad = new GridBagConstraints();
		gbc_btnLoad.insets = new Insets(0, 0, 0, 5);
		gbc_btnLoad.gridx = 0;
		gbc_btnLoad.gridy = 0;
		pnlActions.add(btnLoad, gbc_btnLoad);
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				File chosen = MainWindow.this.chooseFile("load project");
				if (chosen != null)
				{
					currentFile = chosen;
					try
					{
						glue.getModel().readFromFile(chosen);
						glue.syncToView();
					}
					catch (FileNotFoundException e1)
					{
						e1.printStackTrace();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					catch (ClassNotFoundException e1)
					{
						e1.printStackTrace();
					}
					catch (DataException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});

		JButton btnSave = new JButton("SAVE");
		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.insets = new Insets(0, 0, 0, 5);
		gbc_btnSave.gridx = 1;
		gbc_btnSave.gridy = 0;
		pnlActions.add(btnSave, gbc_btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				File chosen = MainWindow.this.chooseFile("save project");
				if (chosen != null)
				{
					currentFile = chosen;
					try
					{
						glue.syncToModel();
						glue.getModel().writeToFile(chosen);
					}
					catch (FileNotFoundException e1)
					{
						e1.printStackTrace();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					catch (DataException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});

		GridBagConstraints gbc_spacer1 = new GridBagConstraints();
		gbc_spacer1.gridx = 2;
		gbc_spacer1.gridy = 0;
		gbc_spacer1.weightx = 0.5;
		gbc_spacer1.fill = GridBagConstraints.HORIZONTAL;
		pnlActions.add(Box.createGlue(), gbc_spacer1);

		JButton btnRender = new JButton("Render to SVG");
		btnRender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					glue.validate();
					glue.syncToModel();
					new SVGWriter(glue.getModel()).render();
					JOptionPane.showMessageDialog(
							MainWindow.this,
							"rendering successful"
							);
				}
				catch (IOException e)
				{
					showError(e.getMessage());
					e.printStackTrace();
				}
				catch (DataException e)
				{
					showError(e.getMessage());
					e.printStackTrace();
				}
				catch (ValidationErrorList e)
				{
					showError(e.getErrorList());
					e.printStackTrace();
				}
			}
		});

		GridBagConstraints gbc_btnRender = new GridBagConstraints();
		gbc_btnRender.insets = new Insets(0, 0, 0, 5);
		gbc_btnRender.gridx = 3;
		gbc_btnRender.gridy = 0;
		pnlActions.add(btnRender, gbc_btnRender);

		GridBagConstraints gbc_spacer2 = new GridBagConstraints();
		gbc_spacer2.gridx = 4;
		gbc_spacer2.gridy = 0;
		gbc_spacer2.weightx = 0.5;
		gbc_spacer2.fill = GridBagConstraints.HORIZONTAL;
		pnlActions.add(Box.createGlue(), gbc_spacer2);

		JButton btnQuit = new JButton("QUIT");
		GridBagConstraints gbc_btnQuit = new GridBagConstraints();
		gbc_btnQuit.insets = new Insets(0, 0, 0, 5);
		gbc_btnQuit.gridx = 5;
		gbc_btnQuit.gridy = 0;
		pnlActions.add(btnQuit, gbc_btnQuit);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				MainWindow.this.dispose();
			}
		});

		// show data
		glue.syncToView();
	}

	protected void showError(String message)
	{
		JOptionPane.showMessageDialog(
				this,
				message,
				"Error",
				JOptionPane.ERROR_MESSAGE
				);
	}

	private File chooseFile(String label)
	{
		JFileChooser fc = new JFileChooser();
		FileFilter knit = new FileNameExtensionFilter("knittr projects", "knit");
		fc.setFileFilter(knit);
		if (currentFile != null)
		{
			fc.setSelectedFile(currentFile);
		}
		if (fc.showDialog(MainWindow.this, label) // FIXME i18n
				== JFileChooser.APPROVE_OPTION)
		{
			return fc.getSelectedFile();
		}
		else
		{
			return null;
		}
	}
}
