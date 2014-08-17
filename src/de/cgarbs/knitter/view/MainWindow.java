package de.cgarbs.knitter.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;


import de.cgarbs.knitter.data.Project;
import de.cgarbs.knitter.render.SVGWriter;



public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6117333503098355182L;
	
	private JPanel contentPane;
	private JFormattedTextField tfGridWidthSmall;
	private JFormattedTextField tfGridWidthBig;
	
	/**
	 * Create the frame.
	 */
	public MainWindow() {

		// FIXME MOVE THIS SOMEWHERE ELSE - ideally some VObject like superclass for every data value
		NumberFormat gridWidthFormat = NumberFormat.getNumberInstance();
		gridWidthFormat.setMinimumFractionDigits(0);
		gridWidthFormat.setMaximumFractionDigits(2);
		gridWidthFormat.setMinimumIntegerDigits(1);
		gridWidthFormat.setMaximumIntegerDigits(3);
		NumberFormatter gridWidthFormatter = new NumberFormatter(gridWidthFormat);
//		gridWidthFormatter.setValueClass(Float.class);
		gridWidthFormatter.setMinimum(0);
//		gridWidthFormatter.setAllowsInvalid(true);
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		contentPane.setLayout(gbl_contentPane);
		
		JPanel pnlGrid = new JPanel();
		GridBagConstraints gbc_pnlGrid = new GridBagConstraints();
		gbc_pnlGrid.insets = new Insets(0, 0, 5, 5);
		gbc_pnlGrid.fill = GridBagConstraints.BOTH;
		gbc_pnlGrid.gridx = 0;
		gbc_pnlGrid.gridy = 0;
		contentPane.add(pnlGrid, gbc_pnlGrid);
		GridBagLayout gbl_pnlGrid = new GridBagLayout();
		pnlGrid.setLayout(gbl_pnlGrid);
		
		JLabel lblNewLabel_1 = new JLabel("Grid settings:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		pnlGrid.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		tfGridWidthSmall = new JFormattedTextField(gridWidthFormatter);
		tfGridWidthSmall.setFocusLostBehavior(JFormattedTextField.COMMIT);
		tfGridWidthSmall.setInputVerifier(new FormattedTextFieldVerifier());
		GridBagConstraints gbc_tfGridWidthSmall = new GridBagConstraints();
		gbc_tfGridWidthSmall.insets = new Insets(0, 0, 5, 0);
		gbc_tfGridWidthSmall.gridx = 0;
		gbc_tfGridWidthSmall.gridy = 1;
		pnlGrid.add(tfGridWidthSmall, gbc_tfGridWidthSmall);
		tfGridWidthSmall.setColumns(10);
		
		tfGridWidthBig = new JFormattedTextField(gridWidthFormatter);
		tfGridWidthBig.setFocusLostBehavior(JFormattedTextField.COMMIT);
		tfGridWidthBig.setInputVerifier(new FormattedTextFieldVerifier());
		GridBagConstraints gbc_tfGridWidthBig = new GridBagConstraints();
		gbc_tfGridWidthBig.insets = new Insets(0, 0, 5, 0);
		gbc_tfGridWidthBig.gridx = 1;
		gbc_tfGridWidthBig.gridy = 1;
		pnlGrid.add(tfGridWidthBig, gbc_tfGridWidthBig);
		tfGridWidthBig.setColumns(10);
		
		JPanel pnlActions = new JPanel();
		GridBagConstraints gbc_pnlActions = new GridBagConstraints();
		gbc_pnlActions.insets = new Insets(0, 0, 5, 5);
		gbc_pnlActions.gridx = 0;
		gbc_pnlActions.gridy = 1;
		contentPane.add(pnlActions, gbc_pnlActions);
		GridBagLayout gbl_pnlActions = new GridBagLayout();
		pnlActions.setLayout(gbl_pnlActions);
		
		JButton btnRender = new JButton("Render to SVG");
		btnRender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					new SVGWriter(getData()).render();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});
		
		JLabel lblNewLabel = new JLabel("Actions:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		pnlActions.add(lblNewLabel, gbc_lblNewLabel);
		GridBagConstraints gbc_btnRender = new GridBagConstraints();
		gbc_btnRender.insets = new Insets(0, 0, 0, 5);
		gbc_btnRender.gridx = 0;
		gbc_btnRender.gridy = 1;
		pnlActions.add(btnRender, gbc_btnRender);
		
		JButton btnQuit = new JButton("QUIT");
		GridBagConstraints gbc_btnQuit = new GridBagConstraints();
		gbc_btnQuit.insets = new Insets(0, 0, 0, 5);
		gbc_btnQuit.gridx = 1;
		gbc_btnQuit.gridy = 1;
		pnlActions.add(btnQuit, gbc_btnQuit);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainWindow.this.dispose();
			}
		});


		// init data
		setData(new Project());
	}

	/**
	 * move project data to fields in view
	 * @param project the project
	 */
	private void setData(Project project)
	{
		tfGridWidthBig.setValue((Float) project.getGridWidthBig());
		tfGridWidthSmall.setValue((Float) project.getGridWidthSmall());
		// TODO variables missing
	}

	/**
	 * get project data from fields in view
	 * @returns the project data
	 */
	private Project getData()
	{
		Project project = new Project();
		// TODO variables missing, too much default configuration
		
		try
		{
			project.setGridWidthBig((Float) tfGridWidthBig.getValue());
		}
		catch (NumberFormatException e)
		{
			// TODO display error instead of silently keeping the default value
		}
		
		try
		{
			project.setGridWidthSmall(Float.valueOf(String.valueOf(tfGridWidthSmall.getValue())));
		}
		catch (NumberFormatException e)
		{
			// TODO display error instead of silently keeping the default value
		}
		
		return project;
	}

}
