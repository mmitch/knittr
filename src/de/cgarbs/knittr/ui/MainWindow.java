package de.cgarbs.knittr.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import de.cgarbs.knittr.data.Project;
import de.cgarbs.knittr.render.SVGWriter;
import de.cgarbs.lib.exception.DataException;
import de.cgarbs.lib.exception.GlueException;
import de.cgarbs.lib.glue.Binding;
import de.cgarbs.lib.glue.Glue;
import de.cgarbs.lib.ui.SimpleTabbedLayout;

public class MainWindow extends JFrame
{

	Glue<Project> glue = new Glue<Project>(new Project());

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
	public MainWindow() throws GlueException, DataException
	{

		// add bindings
		Binding b_gridtextmod = glue.addBinding(Project.GRIDTEXTMOD);
		Binding b_gridwidthsmall = glue.addBinding(Project.GRIDWIDTHSMALL);
		Binding b_gridwidthbig = glue.addBinding(Project.GRIDWIDTHBIG);
		Binding b_source_file = glue.addBinding(Project.SOURCE_FILE);
		Binding b_target_file = glue.addBinding(Project.TARGET_FILE);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		contentPane.setLayout(gbl_contentPane);

//		JComponent jthingie = SimpleVerticalLayout.builder()
		JComponent jthingie = SimpleTabbedLayout.builder()
//		JComponent jthingie = BorderedVerticalLayout.builder()
				.startNextGroup("grid settings").addAttribute(b_gridtextmod)
				.addAttribute(b_gridwidthbig).addAttribute(b_gridwidthsmall)
				.startNextGroup("files").addAttribute(b_source_file)
				.addAttribute(b_target_file).build();

		GridBagConstraints gbc_pnlGrid = new GridBagConstraints();
		gbc_pnlGrid.insets = new Insets(0, 0, 5, 5);
		gbc_pnlGrid.fill = GridBagConstraints.BOTH;
		gbc_pnlGrid.gridx = 0;
		gbc_pnlGrid.gridy = 0;
		contentPane.add(jthingie, gbc_pnlGrid);

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
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					glue.syncToModel();
					new SVGWriter(glue.getModel()).render();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (DataException e)
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
			public void actionPerformed(ActionEvent e)
			{
				MainWindow.this.dispose();
			}
		});

		// show data
		glue.syncToView();
	}
}
