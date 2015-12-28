package compta.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.ParseException;
import java.util.GregorianCalendar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import compta.AccountException;
import compta.controller.AccountController;
import compta.controller.FileUtils;
import compta.ihm.chart.BudgetRecordChartToolTipGenerator;
import compta.ihm.chart.ChartUtils;
import compta.ihm.table.BudgetRecordsTable;
import compta.ihm.table.BudgetRecordsTableModel;
import compta.ihm.table.HistoryRecordsTable;
import compta.ihm.table.HistoryRecordsTableModel;
import compta.model.AccountChangedEvent;
import compta.model.budget.BudgetRecord;
import compta.model.history.HistoryRecord;

public class MainFrame extends JFrame {

	private static final Logger logger = Logger.getLogger(MainFrame.class);

	private JPanel mainPanel = null;

	/** Menu ********************************************* */
	private JMenuBar myMenuBar = null;

	private JMenu menuFile = null;

	private JMenuItem menuItemOpenAccount = null;

	private JMenuItem menuItemNewAccount = null;

	private JMenuItem menuItemSaveAccount = null;

	private JMenuItem menuItemSaveaAsAccount = null;

	private JMenuItem menuItemExit = null;

	/** Tool Bar **************************************** */
	private JPanel panelToolBar = null;

	private JButton buttonToolBarOpen = null;

	private JButton buttonToolBarNew = null;

	private JButton buttonToolBarSave = null;

	/** * tabbed pane ***** */
	private JTabbedPane tabbedPane = null;

	/** * panel history *** */
	private JPanel panelHistory = null;

	private JPanel panelHistoryButtons = null;

	private JScrollPane scrollPaneTableHistory = null;

	private HistoryRecordsTable tableHistoryRecords = null;

	private JButton buttonDeleteHistoryRecord = null;

	private JButton buttonAddHistoryRecord = null;

	/** * panel graph *** */
	private JPanel panelPrevisions = null;

	private ChartPanel chartPanel = null;

	private JPanel panelScale = null;

	private MyDateChooser endDateChooser = null;

	private JCheckBox checkBoxShowShapes = null;

	/** Panel principal des comptes ********************* */
	private JPanel panelAccount = null;

	private JPanel panelCurrentProvision = null;

	private AmountTextField textCurrentProvision = null;

	private MyDateChooser dateChooserCurrent = null;

	private JButton buttonAddCurrentToHistory = null;

	private JPanel panelInfoBottom = null;

	private JLabel labelInfo = null;

	private JButton buttonAddBudgetRecord = null;

	private JButton buttonDeleteBudgetRecord = null;

	private JPanel panelBudgetTable = null;

	private JPanel panelButtonsAddRemove = null;

	private JScrollPane scrollPaneTableBudget = null;

	private BudgetRecordsTable tableBudgetRecords = null;

	/** * file chooser ************************** */
	private JFileChooser comptaFileChooser = null;

	private File currentFile = null;

	private AccountController controller = null;

	public MainFrame(AccountController _controller) {
		super();
		controller = _controller;

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(800, 500);
		this.setContentPane(getMainPanel());
		this.setJMenuBar(this.getMyMenuBar());
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				exitAction();
			}
		});

		setCurrentFile(null);
	}

	private JMenuBar getMyMenuBar() {
		if (myMenuBar == null) {
			myMenuBar = new JMenuBar();
			myMenuBar.add(getMenuFile());
		}
		return myMenuBar;
	}

	private JMenu getMenuFile() {
		if (menuFile == null) {
			menuFile = new JMenu("File");
			menuFile.add(getMenuItemNewAccount());
			menuFile.add(getMenuItemOpenAccount());
			menuFile.add(getMenuItemSaveAccount());
			menuFile.add(getMenuItemSaveaAsAccount());
			menuFile.addSeparator();
			menuFile.add(getMenuItemExit());
		}
		return menuFile;
	}

	private JMenuItem getMenuItemNewAccount() {
		if (menuItemNewAccount == null) {
			menuItemNewAccount = new JMenuItem("New");
			menuItemNewAccount.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					newAction();
				}
			});
		}
		return menuItemNewAccount;
	}

	private JMenuItem getMenuItemOpenAccount() {
		if (menuItemOpenAccount == null) {
			menuItemOpenAccount = new JMenuItem("Open");
			menuItemOpenAccount.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					openAction();
				}
			});
		}
		return menuItemOpenAccount;
	}

	private JMenuItem getMenuItemSaveAccount() {
		if (menuItemSaveAccount == null) {
			menuItemSaveAccount = new JMenuItem("Save");
			menuItemSaveAccount.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveAction();
				}
			});
		}
		return menuItemSaveAccount;
	}

	private JMenuItem getMenuItemSaveaAsAccount() {
		if (menuItemSaveaAsAccount == null) {
			menuItemSaveaAsAccount = new JMenuItem("Save as...");
			menuItemSaveaAsAccount.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					saveAsAction();
				}
			});
		}
		return menuItemSaveaAsAccount;
	}

	private JMenuItem getMenuItemExit() {
		if (menuItemExit == null) {
			menuItemExit = new JMenuItem("Exit");
			menuItemExit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					exitAction();
				}
			});
		}
		return menuItemExit;
	}

	/** ***** panel principal ***************************** */

	private JPanel getMainPanel() {
		if (mainPanel == null) {
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			mainPanel.add(getPanelToolBar(), BorderLayout.NORTH);
			mainPanel.add(getTabbedPane(), BorderLayout.CENTER);
			mainPanel.add(getPanelInfoBottom(), BorderLayout.SOUTH);
		}
		return mainPanel;
	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("History", getPanelHistory());
			tabbedPane.addTab("Budget", getPanelAccount());
			tabbedPane.addTab("Chart", getPanelPrevisions());
			tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
			tabbedPane.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					if (tabbedPane.getSelectedComponent() == getPanelPrevisions()) {
						updateChart();
					}
				}
			});
			tabbedPane.setSelectedIndex(1);
		}
		return tabbedPane;
	}

	private JPanel getPanelToolBar() {
		if (panelToolBar == null) {
			panelToolBar = new JPanel();
			panelToolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelToolBar.add(getButtonToolBarNew());
			panelToolBar.add(getButtonToolBarOpen());
			panelToolBar.add(getButtonToolBarSave());
		}
		return panelToolBar;
	}

	private JButton getButtonToolBarNew() {
		if (buttonToolBarNew == null) {
			buttonToolBarNew = new JButton(
					createImageIcon("images/32/filenew.png"));
			buttonToolBarNew.setToolTipText("New");
			buttonToolBarNew.setMargin(new Insets(0, 0, 0, 0));
			buttonToolBarNew.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					newAction();
				}
			});
		}
		return buttonToolBarNew;
	}

	private JButton getButtonToolBarOpen() {
		if (buttonToolBarOpen == null) {
			buttonToolBarOpen = new JButton(
					createImageIcon("images/32/fileopen.png"));
			buttonToolBarOpen.setToolTipText("Open...");
			buttonToolBarOpen.setMargin(new Insets(0, 0, 0, 0));
			buttonToolBarOpen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					openAction();
				}
			});
		}
		return buttonToolBarOpen;
	}

	private JButton getButtonToolBarSave() {
		if (buttonToolBarSave == null) {
			buttonToolBarSave = new JButton(
					createImageIcon("images/32/filesave.png"));
			buttonToolBarSave.setToolTipText("Save");
			buttonToolBarSave.setMargin(new Insets(0, 0, 0, 0));
			buttonToolBarSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					saveAction();
				}
			});
		}
		return buttonToolBarSave;
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	private static ImageIcon createImageIcon(String _path) {
		java.net.URL imgURL = MainFrame.class.getResource(_path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + _path);
			return null;
		}
	}

	private JPanel getPanelHistory() {
		if (panelHistory == null) {
			panelHistory = new JPanel();
			panelHistory.setLayout(new BorderLayout());
			panelHistory.add(getScrollPaneTableHistory(), BorderLayout.CENTER);
			panelHistory.add(getPanelHistoryButtons(), BorderLayout.EAST);
		}
		return panelHistory;
	}

	private JPanel getPanelHistoryButtons() {
		if (panelHistoryButtons == null) {
			panelHistoryButtons = new JPanel();
			panelHistoryButtons.setLayout(new BoxLayout(panelHistoryButtons,
					BoxLayout.PAGE_AXIS));

			panelHistoryButtons.add(Box.createVerticalGlue());
			panelHistoryButtons.add(getButtonAddHistoryRecord());
			panelHistoryButtons.add(getButtonDeleteHistoryRecord());
			panelHistoryButtons.add(Box.createVerticalGlue());
		}
		return panelHistoryButtons;
	}

	/** * panel comptes ****** */

	private JPanel getPanelAccount() {
		if (panelAccount == null) {
			panelAccount = new JPanel();
			panelAccount.setLayout(new BorderLayout());
			panelAccount.add(getPanelCurrentProvision(), BorderLayout.NORTH);
			panelAccount.add(getPanelBudgetTable(), BorderLayout.CENTER);
		}
		return panelAccount;
	}

	private JPanel getPanelCurrentProvision() {
		if (panelCurrentProvision == null) {
			panelCurrentProvision = new JPanel();
			panelCurrentProvision.setLayout(new FlowLayout(FlowLayout.LEFT, 10,
					10));
			JLabel labelCour = new JLabel("Current provision : ");
			// labelCour.setPreferredSize(new Dimension(150, 25));
			panelCurrentProvision.add(labelCour);
			panelCurrentProvision.add(getTextCurrentProvision());
			panelCurrentProvision.add(getDateChooserCurrent());
			panelCurrentProvision.add(getButtonAddCurrentToHistory());
		}
		return panelCurrentProvision;
	}

	private MyDateChooser getDateChooserCurrent() {
		if (dateChooserCurrent == null) {
			dateChooserCurrent = new MyDateChooser();
			dateChooserCurrent.setPreferredSize(new Dimension(150, 25));
			dateChooserCurrent
					.addDateChangeListener(new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent evt) {
							if (getDateChooserCurrent().getDate() != null) {
								if (getEndDateChooser().getDate() != null
										&& endDateChooser.getDate().before(
												getDateChooserCurrent()
														.getDate())) {
									// end date must be posterior to date solde
									endDateChooser
											.setDate(getDateChooserCurrent()
													.getDate());

								}

								controller.getAccount()
										.setCurrentProvisionDate(
												getDateChooserCurrent()
														.getDate());
							}
						}
					});
		}
		return dateChooserCurrent;
	}

	private AmountTextField getTextCurrentProvision() {
		if (textCurrentProvision == null) {
			textCurrentProvision = new AmountTextField();
			textCurrentProvision.setPreferredSize(new Dimension(100, 25));
			textCurrentProvision.setEditable(true);
			textCurrentProvision.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Number currentProvision = (Number) getTextCurrentProvision()
							.getValue();
					if (currentProvision != null) {
						controller.getAccount().setCurrentProvision(
								currentProvision.floatValue());
					}
				}
			});

			textCurrentProvision.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent evt) {
					Number currentProvision = (Number) getTextCurrentProvision()
							.getValue();
					if (currentProvision != null) {
						controller.getAccount().setCurrentProvision(
								currentProvision.floatValue());
					}
				}
			});
		}
		return textCurrentProvision;
	}

	private JButton getButtonAddCurrentToHistory() {
		if (buttonAddCurrentToHistory == null) {
			buttonAddCurrentToHistory = new JButton("Save to history..");
			buttonAddCurrentToHistory.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						getTextCurrentProvision().commitEdit();
					} catch (ParseException exc) {
						return;
					}

					HistoryRecord newHistoryRecord = new HistoryRecord(
							controller.getAccount().getCurrentProvisionDate(),
							controller.getAccount().getCurrentProvisionAmount());

					controller.getAccount().addHistoryRecord(newHistoryRecord);
				}
			});
		}
		return buttonAddCurrentToHistory;
	}

	private JScrollPane getScrollPaneTableHistory() {
		if (scrollPaneTableHistory == null) {
			scrollPaneTableHistory = new JScrollPane(getHistoryRecordsTable());
			scrollPaneTableHistory.setSize(600, 300);
		}
		return scrollPaneTableHistory;
	}

	private HistoryRecordsTable getHistoryRecordsTable() {
		if (tableHistoryRecords == null) {
			tableHistoryRecords = new HistoryRecordsTable(controller);

			tableHistoryRecords.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							// selection has changed in the table
							boolean isRowSelected = (tableHistoryRecords
									.getSelectedRow() >= 0);
							getButtonDeleteHistoryRecord().setEnabled(
									isRowSelected);
						}
					});
		}
		return tableHistoryRecords;
	}

	private JButton getButtonDeleteHistoryRecord() {
		if (buttonDeleteHistoryRecord == null) {
			buttonDeleteHistoryRecord = new JButton(
					createImageIcon("images/16/edit_remove.png"));
			buttonDeleteHistoryRecord.setToolTipText("Remove record");
			buttonDeleteHistoryRecord.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int selectedRowCount = getHistoryRecordsTable()
							.getSelectedRowCount();
					if (selectedRowCount >= 0) {
						int answer = JOptionPane.showConfirmDialog(
								MainFrame.this, "Delete selected records ?",
								"Confirm", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
						if (answer == JOptionPane.OK_OPTION) {
							stopEditingHistory();
							int[] selectedRowsViewIndexes = getHistoryRecordsTable()
									.getSelectedRows();
							HistoryRecord[] recordsToDelete = new HistoryRecord[selectedRowsViewIndexes.length];

							for (int i = 0; i < selectedRowsViewIndexes.length; i++) {
								recordsToDelete[i] = getHistoryRecordsTable()
										.getHistoryRecord(
												selectedRowsViewIndexes[i]);
							}

							for (int i = 0; i < selectedRowsViewIndexes.length; i++) {
								getHistoryRecordsTable().removeHistoryRecord(
										recordsToDelete[i]);
							}
						}
					}
				}
			});
		}
		return buttonDeleteHistoryRecord;
	}

	private JButton getButtonAddHistoryRecord() {
		if (buttonAddHistoryRecord == null) {
			buttonAddHistoryRecord = new JButton(
					createImageIcon("images/16/edit_add.png"));
			buttonAddHistoryRecord.setToolTipText("New record");
			buttonAddHistoryRecord.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					stopEditingHistory();
					HistoryRecord newRecord = new HistoryRecord();
					controller.getAccount().addHistoryRecord(newRecord);
					int viewIndex = getHistoryRecordsTable().getViewIndexOf(
							newRecord);
					// select the newly added entry
					getHistoryRecordsTable().getSelectionModel()
							.setSelectionInterval(viewIndex, viewIndex);
					getHistoryRecordsTable().scrollToVisible(viewIndex, 0);
				}
			});
		}
		return buttonAddHistoryRecord;
	}

	private JPanel getPanelBudgetTable() {
		if (panelBudgetTable == null) {
			panelBudgetTable = new JPanel();
			panelBudgetTable.setLayout(new BorderLayout());
			panelBudgetTable.add(getScrollPaneTableBudget(),
					BorderLayout.CENTER);
			panelBudgetTable.add(getPanelButtonsUpdDown(), BorderLayout.EAST);
		}
		return panelBudgetTable;
	}

	private JPanel getPanelButtonsUpdDown() {
		if (panelButtonsAddRemove == null) {
			panelButtonsAddRemove = new JPanel();
			panelButtonsAddRemove.setLayout(new BoxLayout(
					panelButtonsAddRemove, BoxLayout.PAGE_AXIS));

			panelButtonsAddRemove.add(Box.createVerticalGlue());
			panelButtonsAddRemove.add(getButtonAddBudgetRecord());
			panelButtonsAddRemove.add(getButtonDeleteBudgetRecord());
			panelButtonsAddRemove.add(Box.createVerticalGlue());
		}
		return panelButtonsAddRemove;
	}

	private JScrollPane getScrollPaneTableBudget() {
		if (scrollPaneTableBudget == null) {
			scrollPaneTableBudget = new JScrollPane(getBudgetRecordsTable());
			scrollPaneTableBudget.setSize(600, 300);
		}
		return scrollPaneTableBudget;
	}

	private BudgetRecordsTable getBudgetRecordsTable() {
		if (tableBudgetRecords == null) {
			tableBudgetRecords = new BudgetRecordsTable(controller);

			tableBudgetRecords.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							// selection has changed in the table
							boolean isRowSelected = (tableBudgetRecords
									.getSelectedRow() >= 0);
							getButtonDeleteBudgetRecord().setEnabled(
									isRowSelected);
						}
					});
		}
		return tableBudgetRecords;
	}

	private JPanel getPanelInfoBottom() {
		if (panelInfoBottom == null) {
			panelInfoBottom = new JPanel();
			panelInfoBottom.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
			panelInfoBottom.add(getLabelInfo());
		}
		return panelInfoBottom;
	}

	private JLabel getLabelInfo() {
		if (labelInfo == null) {
			labelInfo = new JLabel("");
			labelInfo.setPreferredSize(new Dimension(200, 24));
			labelInfo.setFont(labelInfo.getFont().deriveFont(11f).deriveFont(
					Font.PLAIN));
		}
		return labelInfo;
	}

	private void stopEditingBudget() {
		TableCellEditor cellEditor = getBudgetRecordsTable().getCellEditor();
		if (cellEditor != null) {
			cellEditor.stopCellEditing();
		}
	}

	private void stopEditingHistory() {
		TableCellEditor cellEditor = getHistoryRecordsTable().getCellEditor();
		if (cellEditor != null) {
			cellEditor.stopCellEditing();
		}
	}

	private JButton getButtonDeleteBudgetRecord() {
		if (buttonDeleteBudgetRecord == null) {
			buttonDeleteBudgetRecord = new JButton(
					createImageIcon("images/16/edit_remove.png"));
			buttonDeleteBudgetRecord.setToolTipText("Remove record");
			buttonDeleteBudgetRecord.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int selectedRowCount = getBudgetRecordsTable()
							.getSelectedRowCount();
					if (selectedRowCount >= 0) {
						int answer = JOptionPane.showConfirmDialog(
								MainFrame.this, "Delete selected records ?",
								"Confirm", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE);
						if (answer == JOptionPane.OK_OPTION) {
							stopEditingBudget();
							int[] selectedRowsViewIndexes = getBudgetRecordsTable()
									.getSelectedRows();
							BudgetRecord[] recordsToDelete = new BudgetRecord[selectedRowsViewIndexes.length];

							for (int i = 0; i < selectedRowsViewIndexes.length; i++) {
								recordsToDelete[i] = getBudgetRecordsTable()
										.getBudgetRecord(
												selectedRowsViewIndexes[i]);
							}

							for (int i = 0; i < selectedRowsViewIndexes.length; i++) {
								getBudgetRecordsTable().removeBudgetRecord(
										recordsToDelete[i]);
							}
						}
					}
				}
			});
		}
		return buttonDeleteBudgetRecord;
	}

	private JButton getButtonAddBudgetRecord() {
		if (buttonAddBudgetRecord == null) {
			buttonAddBudgetRecord = new JButton(
					createImageIcon("images/16/edit_add.png"));
			buttonAddBudgetRecord.setToolTipText("New record");
			buttonAddBudgetRecord.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					stopEditingBudget();
					BudgetRecord newRecord = new BudgetRecord();
					controller.getAccount().addBudgetRecord(newRecord);
					int viewIndex = getBudgetRecordsTable().getViewIndexOf(
							newRecord);
					// select the newly added entry
					getBudgetRecordsTable().getSelectionModel()
							.setSelectionInterval(viewIndex, viewIndex);
					getBudgetRecordsTable().scrollToVisible(viewIndex, 0);
				}
			});
		}
		return buttonAddBudgetRecord;
	}

	/** ********* panel Graph *************** */

	private JPanel getPanelPrevisions() {
		if (panelPrevisions == null) {
			panelPrevisions = new JPanel();
			panelPrevisions.setLayout(new BorderLayout());
			panelPrevisions.add(getChartPanel(), BorderLayout.CENTER);
			panelPrevisions.add(getPanelScale(), BorderLayout.SOUTH);
		}
		return panelPrevisions;
	}

	private ChartPanel getChartPanel() {
		if (chartPanel == null) {
			JFreeChart chart = ChartFactory.createTimeSeriesChart("Funds",
					"Date", "Euros", null, true, true, false);

			XYPlot plot = (XYPlot) chart.getPlot();
			XYItemRenderer r = plot.getRenderer();
			if (r instanceof XYLineAndShapeRenderer) {
				XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
				renderer.setShapesVisible(true);
				renderer.setShapesFilled(true);
			}
			chart.setAntiAlias(true);
			chart.setNotify(true);
			chartPanel = new ChartPanel(chart);
		}
		return chartPanel;
	}

	private JPanel getPanelScale() {
		if (panelScale == null) {
			panelScale = new JPanel();
			panelScale.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

			panelScale.add(getCheckBoxShowShapes());
			panelScale.add(Box.createHorizontalStrut(40));
			panelScale.add(new JLabel("Max date :"));
			panelScale.add(getEndDateChooser());

		}
		return panelScale;
	}

	private MyDateChooser getEndDateChooser() {
		if (endDateChooser == null) {
			endDateChooser = new MyDateChooser();
			endDateChooser.setPreferredSize(new Dimension(150, 25));

			GregorianCalendar date = new GregorianCalendar();
			date.setTime(controller.getAccount().getCurrentProvisionDate());
			date.add(GregorianCalendar.MONTH, 2);
			endDateChooser.setDate(date.getTime());
			endDateChooser.addDateChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					if (endDateChooser.getDate() != null
							&& getDateChooserCurrent().getDate() != null
							&& endDateChooser.getDate().before(
									getDateChooserCurrent().getDate())) {
						// end date must be posterior to date solde
						endDateChooser.setDate(getDateChooserCurrent()
								.getDate());

					}

					updateChart();
				}
			});
		}
		return endDateChooser;
	}

	private JCheckBox getCheckBoxShowShapes() {
		if (checkBoxShowShapes == null) {
			checkBoxShowShapes = new JCheckBox("Shapes");
			checkBoxShowShapes.setSelected(true);
			checkBoxShowShapes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					XYPlot plot = (XYPlot) getChartPanel().getChart().getPlot();
					XYItemRenderer r = plot.getRenderer();
					if (r instanceof XYLineAndShapeRenderer) {
						XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
						renderer.setShapesVisible(checkBoxShowShapes
								.isSelected());
						renderer.setShapesFilled(checkBoxShowShapes
								.isSelected());
					}
				}
			});
		}
		return checkBoxShowShapes;
	}

	private JFileChooser getComptaFileChooser() {
		if (comptaFileChooser == null) {
			comptaFileChooser = new JFileChooser();
			comptaFileChooser.setAcceptAllFileFilterUsed(false);
			// comptaFileChooser.addChoosableFileFilter(FileUtils.CSV_FILE_FILTER);
			comptaFileChooser.addChoosableFileFilter(FileUtils.XML_FILE_FILTER);
			comptaFileChooser.setCurrentDirectory(new File("."));
		}
		return comptaFileChooser;
	}

	private void updateChart() {

		// create chart
		TimeSeries previsionSeries = ChartUtils.computePrevision(controller
				.getAccount(), getEndDateChooser().getDate());

		TimeSeries historySeries = ChartUtils.computeHistorySeries(controller
				.getAccount());

		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(previsionSeries);
		dataset.addSeries(historySeries);

		JFreeChart chart = getChartPanel().getChart();
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setDataset(dataset);
		plot.getRenderer().setSeriesToolTipGenerator(0,
				new BudgetRecordChartToolTipGenerator());
	}

	/**
	 * 
	 * @param file
	 */
	private void setCurrentFile(File file) {
		currentFile = file;
		if (currentFile == null) {
			this.setTitle("Compta - (unsaved)");
		} else {
			this.setTitle("Compta - " + currentFile.getName());
		}
	}

	/**
	 * 
	 * 
	 */
	private void exitAction() {
		if (currentFile != null) {
			int returnVal = JOptionPane.showConfirmDialog(this,
					"All unsaved data will be lost. Continue ?",
					"Confirm exit", JOptionPane.YES_NO_OPTION);
			if (returnVal != JOptionPane.YES_OPTION) {
				return;
			}
		}
		this.dispose();
	}

	/**
	 * 
	 * 
	 */
	private void newAction() {
		if (currentFile != null) {
			int returnVal = JOptionPane.showConfirmDialog(this,
					"All unsaved data will be lost. Continue ?", "Confirm",
					JOptionPane.YES_NO_OPTION);
			if (returnVal != JOptionPane.YES_OPTION) {
				return;
			}
		}

		controller.closeAccount();
		setCurrentFile(null);
	}

	/**
	 * 
	 * 
	 */
	private void openAction() {
		if (currentFile != null) {
			int returnVal = JOptionPane.showConfirmDialog(this,
					"All unsaved data will be lost. Continue ?",
					"Confirm open", JOptionPane.YES_NO_OPTION);
			if (returnVal != JOptionPane.YES_OPTION) {
				return;
			}
		}

		int answer = getComptaFileChooser().showOpenDialog(this);
		if (answer == JFileChooser.APPROVE_OPTION) {
			File openedFile = getComptaFileChooser().getSelectedFile();

			if (!openedFile.exists()) {
				logger.error("File does not exist :"
						+ openedFile.getAbsolutePath());
				JOptionPane.showMessageDialog(this, openedFile
						.getAbsolutePath()
						+ "\ndoes not exist", "Error while loading file",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				displayInfo("Opening file " + openedFile.getName() + " ...");
				controller.openAccount(openedFile);
				setCurrentFile(openedFile);
				displayInfo(openedFile.getName() + " opened.");
			} catch (AccountException e) {
				logger.error(e.getMessage());
				JOptionPane.showMessageDialog(this, "Error while reading file "
						+ openedFile.getName() + " :\n" + e.getMessage(),
						"Error while loading file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 
	 * 
	 */
	private void saveAction() {
		if (currentFile == null) {
			saveAsAction();
		} else {
			try {
				displayInfo("Saving ...");
				controller.saveAccount(currentFile);
				displayInfo(currentFile.getName() + " saved.");
			} catch (Exception e) {
				logger.error("Uanble to save", e);
				JOptionPane.showMessageDialog(this, "Unable to save file "
						+ currentFile.getName() + " :\n" + e.getMessage(),
						"Error while loading file", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	/**
	 * 
	 * 
	 */
	private void saveAsAction() {
		int answer = getComptaFileChooser().showSaveDialog(this);
		if (answer == JFileChooser.APPROVE_OPTION) {
			File file = getComptaFileChooser().getSelectedFile();

			try {
				displayInfo("Saving  ...");
				controller.saveAccount(file);
				setCurrentFile(file);
				displayInfo(currentFile.getName() + " saved.");
			} catch (Exception e) {
				logger.error("Uanble to save", e);
				JOptionPane.showMessageDialog(this, "Unable to save file "
						+ currentFile.getName() + " :\n" + e.getMessage(),
						"Error while loading file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * 
	 * @param _evt
	 */
	public void budgetRecordChanged(AccountChangedEvent _evt) {

		// update table row
		getBudgetRecordsTable().fireBudgetRecordUpdated(
				(BudgetRecord) _evt.getRecord());
	}

	public void historyRecordChanged(AccountChangedEvent _evt) {
		logger.debug("history record changed");

		// update table row
		getHistoryRecordsTable().fireHistoryRecordUpdated(
				(HistoryRecord) _evt.getRecord());
	}

	/**
	 * This method notifies the UI that the current account has changed
	 * (provision, date, list of entries). This triggers the refersh of the UI
	 * 
	 * @param evt
	 *            the event triggered
	 */
	public void accountChanged(AccountChangedEvent evt) {
		logger.debug("comptesChanged()");

		// update table
		((BudgetRecordsTableModel) getBudgetRecordsTable().getModel())
				.fireTableDataChanged();

		((HistoryRecordsTableModel) getHistoryRecordsTable().getModel())
				.fireTableDataChanged();

		// update solde and date sole
		getTextCurrentProvision().setValue(
				controller.getAccount().getCurrentProvisionAmount());
		getDateChooserCurrent().setDate(
				controller.getAccount().getCurrentProvisionDate());
	}

	/**
	 * This method notifies the UI that a new Comptes has been opened. This
	 * triggers the referesh of the UI.
	 * 
	 * @param file
	 *            the newly opened file.
	 */
	public void accountOpened(File file) {
		// update table
		((BudgetRecordsTableModel) getBudgetRecordsTable().getModel())
				.fireTableDataChanged();
		((HistoryRecordsTableModel) getHistoryRecordsTable().getModel())
				.fireTableDataChanged();

		// update solde and date sole
		getTextCurrentProvision().setValue(
				controller.getAccount().getCurrentProvisionAmount());
		getDateChooserCurrent().setDate(
				controller.getAccount().getCurrentProvisionDate());

		// update chart
		updateChart();

		// set current file
		setCurrentFile(file);
	}

	/**
	 * Displays the MainFrame UI
	 * 
	 */
	public void display() {
		this.setLocationByPlatform(true);
		this.setVisible(true);
	}

	private Runnable timer = new Runnable() {
		public void run() {
			try {
				// sleep 3 sec
				Thread.sleep(3000);
				getLabelInfo().setText("");
			} catch (InterruptedException e) {
			}
		}
	};

	private Thread timerThread = null;

	/**
	 * Displays information in the bottom panel for a certain time and then
	 * clears the text.
	 * 
	 * @param info
	 *            the message to display
	 */
	private void displayInfo(String info) {
		if (timerThread != null) {
			timerThread.interrupt();
		}
		getLabelInfo().setText(info);
		timerThread = new Thread(timer);
		timerThread.start();
	}

}
