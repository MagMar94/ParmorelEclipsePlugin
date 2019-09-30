package hvl.plugin.parmorel.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class ModelRepairView extends ViewPart {
	private Label label;

	public ModelRepairView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
		label = new Label(parent, 0);
		label.setText("Hello world");
	}

	@Override
	public void setFocus() {
		label.setFocus();
	}

}
