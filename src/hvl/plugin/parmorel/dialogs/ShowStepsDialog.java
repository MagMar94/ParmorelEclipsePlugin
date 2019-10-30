package hvl.plugin.parmorel.dialogs;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import hvl.projectparmorel.modelrepair.AppliedAction;

public class ShowStepsDialog extends Dialog  {
	private List<AppliedAction> steps;
	
	public ShowStepsDialog(Shell parentShell, List<AppliedAction> steps) {
		super(parentShell);
		this.steps = steps;
	}

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        Button button = new Button(container, SWT.PUSH);
        button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
                false));
        button.setText("Press me");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                System.out.println("Pressed");
            }
        });

        return container;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Steps taken");
    }

    @Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }

}
