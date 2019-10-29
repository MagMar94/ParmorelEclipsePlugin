package hvl.plugin.parmorel.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;

import hvl.plugin.parmorel.model.PossibleSolutions;
import hvl.projectparmorel.modelrepair.Solution;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;

import java.util.List;

import javax.inject.Inject;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FillLayout;

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
	private Action action1;
	private Action doubleClickAction;
	private SolutionNumberColumnLabelProvider solutionNumberColumnLabelProvider; 
	
	private Button compareButton;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			String txt = getText(obj);
			if (txt.equals(""))
				txt = "a";
			return getText(obj);
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

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
		compareButton.setText("Compare solution");
		compareButton.setEnabled(false);
		
		Button selectButton = new Button(buttonGroup, SWT.NONE);
		selectButton.setAlignment(SWT.LEFT);
		selectButton.setText("Repair with selected solution");
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
		colSolutionNumber.getColumn().setWidth(100);
		colSolutionNumber.getColumn().setText("Solution number");
		colSolutionNumber.getColumn().setAlignment(SWT.RIGHT);
		solutionNumberColumnLabelProvider = new SolutionNumberColumnLabelProvider();
		colSolutionNumber.setLabelProvider(solutionNumberColumnLabelProvider);
		
		TableViewerColumn colWeight = new TableViewerColumn(viewer, SWT.NONE);
		colWeight.getColumn().setWidth(100);
		colWeight.getColumn().setText("Weight");
		colWeight.setLabelProvider(new WeightColumnLabelProvider());
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
		manager.add(action1);
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
	}

	private void makeActions() {
		action1 = getCompareAction();
		action1.setText("Compare");
		action1.setToolTipText("Compare the solution with the original.");
		action1.setImageDescriptor(
				null);

		doubleClickAction = getCompareAction();
	}
	
	private Action getCompareAction() {
		return new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				if(obj instanceof Solution) {
					Solution solution = (Solution) obj;
					CompareModelView.compare(solution.getOriginal(), solution.getModel());
				} else {
					showMessage("An error occured when retrieving the solution.");
				}
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Select solution", message);
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
		} else {
			compareButton.setEnabled(true);
		}
	}
}
