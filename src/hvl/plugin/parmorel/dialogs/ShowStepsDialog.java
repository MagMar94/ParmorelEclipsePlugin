package hvl.plugin.parmorel.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import hvl.plugin.parmorel.views.ActionDescriprionLabelProvider;
import hvl.plugin.parmorel.views.ErrorDescriptionLabelProvider;
import hvl.plugin.parmorel.views.SolutionNumberColumnLabelProvider;
import hvl.plugin.parmorel.views.WeightColumnLabelProvider;
import hvl.projectparmorel.qlearning.AppliedAction;

import org.eclipse.swt.widgets.Table;

public class ShowStepsDialog extends Dialog  {
	private List<AppliedAction> steps;
	private TableViewer viewer;
	
	public ShowStepsDialog(Shell parentShell, List<AppliedAction> steps) {
		super(parentShell);
		this.steps = steps;
	}

    @Override
    protected Control createDialogArea(Composite parent) {
        viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);	
        
        final Table table = viewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        createColumnsFor(viewer);
        
        viewer.setContentProvider(new ArrayContentProvider());
        viewer.setInput(steps);

        return viewer.getControl();
    }
    
    private void createColumnsFor(TableViewer viewer) {
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.NONE);
		col.getColumn().setWidth(30);
		col.getColumn().setText("#");
		col.getColumn().setAlignment(SWT.RIGHT);
		col.setLabelProvider(new SolutionNumberColumnLabelProvider());
		
		TableViewerColumn colWeight = new TableViewerColumn(viewer, SWT.NONE);
		colWeight.getColumn().setWidth(70);
		colWeight.getColumn().setText("Weight");
		colWeight.setLabelProvider(new WeightColumnLabelProvider());
		
		TableViewerColumn colChosenAction = new TableViewerColumn(viewer, SWT.NONE);
		colChosenAction.getColumn().setWidth(130);
		colChosenAction.getColumn().setText("Action");
		colChosenAction.setLabelProvider(new ActionDescriprionLabelProvider());
		
		TableViewerColumn colErrorDescription = new TableViewerColumn(viewer, SWT.NONE);
		colErrorDescription.getColumn().setWidth(800);
		colErrorDescription.getColumn().setText("Description of error");
		colErrorDescription.setLabelProvider(new ErrorDescriptionLabelProvider());
	}
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);

        Button cancelButton = getButton(IDialogConstants.CANCEL_ID);      
        cancelButton.setVisible(false);;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Steps taken");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(730, 300);
    }

    @Override
    protected boolean isResizable() {
        return true;
    }
}
