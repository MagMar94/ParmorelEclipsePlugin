package hvl.plugin.parmorel.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import hvl.plugin.parmorel.actions.CompareAction;
import hvl.plugin.parmorel.actions.RepairWithSelectedFile;
import hvl.plugin.parmorel.actions.ShowStepsAction;
import hvl.plugin.parmorel.model.PossibleSolutions;
import hvl.projectparmorel.modelrepair.Solution;

import org.eclipse.jface.viewers.*;
import org.eclipse.jface.action.*;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;

import java.util.List;

import javax.inject.Inject;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class RepairSelectorView extends ViewPart {
	public RepairSelectorView() {
	}

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "hvl.plugin.parmorel.views.RepairSelectorView";

	@Inject
	IWorkbench workbench;

	private TableViewer viewer;
	private Action compareAction;
	private Action showStepsAction;
	private Action doubleClickAction;
	private SolutionNumberColumnLabelProvider solutionNumberColumnLabelProvider; 
	
	private Button compareButton;
	private Button repairWithSelectedButton;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.VERTICAL));
		
		createViewer(parent);

		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(viewer.getControl(), "hvl.plugin.parmorel.viewer");

		Composite buttonGroup = new Composite(parent, SWT.NONE);
		RowLayout rl_buttonGroup = new RowLayout(SWT.HORIZONTAL);
		buttonGroup.setLayout(rl_buttonGroup);
	
		compareButton = new Button(buttonGroup, SWT.RIGHT);
		compareButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				compareAction.run();
			}
		});
		compareButton.setText("Compare solution");
		compareButton.setEnabled(false);
		
		repairWithSelectedButton = new Button(buttonGroup, SWT.NONE);
		repairWithSelectedButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				RepairWithSelectedFile repairTool = new RepairWithSelectedFile(viewer);
				repairTool.fixModelWithSelectedSolution();
				repairWithSelectedButton.setEnabled(false);
			}
		});
		repairWithSelectedButton.setAlignment(SWT.LEFT);
		repairWithSelectedButton.setText("Repair with selected solution");
		repairWithSelectedButton.setEnabled(false);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);	
		
		createColumnsFor(viewer);
		
		// make lines and header visible
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(PossibleSolutions.INSTANCE.getSolutions());
		PossibleSolutions.INSTANCE.setViewer(this);
		
//		viewer.setLabelProvider(new ViewLabelProvider());
		
		getSite().setSelectionProvider(viewer);
	}
	
	private void createColumnsFor(TableViewer viewer2) {
		TableViewerColumn colSolutionNumber = new TableViewerColumn(viewer, SWT.NONE);
		colSolutionNumber.getColumn().setWidth(30);
		colSolutionNumber.getColumn().setText("#");
		colSolutionNumber.getColumn().setAlignment(SWT.RIGHT);
		solutionNumberColumnLabelProvider = new SolutionNumberColumnLabelProvider();
		colSolutionNumber.setLabelProvider(solutionNumberColumnLabelProvider);
		
		TableViewerColumn colWeight = new TableViewerColumn(viewer, SWT.NONE);
		colWeight.getColumn().setWidth(70);
		colWeight.getColumn().setText("Weight");
		colWeight.setLabelProvider(new WeightColumnLabelProvider());
		
		TableViewerColumn colNumberOfActions = new TableViewerColumn(viewer, SWT.NONE);
		colNumberOfActions.getColumn().setWidth(160);
		colNumberOfActions.getColumn().setText("Number of actions applied");
		colNumberOfActions.setLabelProvider(new NumberOfActionsAppliedColumnLabelProvider());
		
		TableViewerColumn colDistance = new TableViewerColumn(viewer, SWT.NONE);
		colDistance.getColumn().setWidth(160);
		colDistance.getColumn().setText("Distance");
		colDistance.setLabelProvider(new NumberOfActionsAppliedColumnLabelProvider());
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				RepairSelectorView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(compareAction);
		manager.add(showStepsAction);
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(compareAction);
		manager.add(showStepsAction);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(compareAction);
		manager.add(showStepsAction);
	}

	private void makeActions() {
		compareAction = new CompareAction(viewer);
		compareAction.setText("Compare");
		compareAction.setToolTipText("Compare the solution with the original.");
		compareAction.setImageDescriptor(null);
		
		showStepsAction = new ShowStepsAction(viewer);
		showStepsAction.setText("Show steps");
		showStepsAction.setToolTipText("See the actions taken to produce the solution.");
		showStepsAction.setImageDescriptor(null);
		
		doubleClickAction = new CompareAction(viewer);
	}
	
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void updateSolutions(List<Solution> updatedSolutions) {
		solutionNumberColumnLabelProvider.resetCounter();
		viewer.refresh();
		if(updatedSolutions.isEmpty()) {
			compareButton.setEnabled(false);
			repairWithSelectedButton.setEnabled(false);
		} else {
			compareButton.setEnabled(true);
			repairWithSelectedButton.setEnabled(true);
		}
	}
}
