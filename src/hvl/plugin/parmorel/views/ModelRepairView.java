package hvl.plugin.parmorel.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ModelRepairView extends ViewPart {

	public ModelRepairView() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {
//		createActions();
	}

//	private void createActions() {
//		IViewSite site = getViewSite();
//		IToolBarManager toolBarManager = site.getActionBars().getToolBarManager();
//
//		repairModelAction = new Action("Repair") {
//			public void run() {
//				System.out.println("repairing!");
//			}
//		};
//		
//		ImageDescriptor imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
//		try {
//			URL imageUrl = new File("./icons/bandage.png").toURI().toURL();
//			imageDescriptor = ImageDescriptor.createFromURL(imageUrl);
//			repairModelAction.setImageDescriptor(imageDescriptor);
//		} catch (MalformedURLException e) {
//			// Something went wrong
//		} finally {
//			repairModelAction.setImageDescriptor(imageDescriptor);
//		}
//		
//		toolBarManager.add(repairModelAction);
//	}
	
	@Override
	public void setFocus() {

	}

}
