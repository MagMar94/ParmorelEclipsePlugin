package hvl.plugin.parmorel.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

public class SolutionNumberColumnLabelProvider extends ColumnLabelProvider {

	private int id;

	public SolutionNumberColumnLabelProvider() {
		resetCounter();
	}

	@Override
	public String getText(Object element) {
		return Integer.toString(id++);
	}

//	public ColumnLabelProvider getFirstColumnLabelProvider() {
//		return new ColumnLabelProvider() {
//			@Override
//			public String getText(Object element) {
//				return Integer.toString(id++);
//			}
//		};
//	}

	public void resetCounter() {
		id = 1;
	}
}
