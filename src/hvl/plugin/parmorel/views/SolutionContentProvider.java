package hvl.plugin.parmorel.views;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.Viewer;

import hvl.projectparmorel.modelrepair.Solution;

class SolutionContentProvider extends ArrayContentProvider {
	private static SolutionContentProvider instance;

	/**
	 * Returns an instance of SolutionContentProvider. Since instances of this class
	 * do not maintain any state, they can be shared between multiple clients.
	 *
	 * @return an instance of SolutionContentProvider
	 */
	public static SolutionContentProvider getInstance() {
		synchronized (SolutionContentProvider.class) {
			if (instance == null) {
				instance = new SolutionContentProvider();
			}
			return instance;
		}
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (oldInput != null && newInput != null) {
			List<Solution> oldList = castToList(oldInput);
			List<Solution> newList = castToList(newInput);

			if (oldList.containsAll(newList) && newList.containsAll(oldList)) {
				System.out.println("yoyo");
				viewer.setInput(newInput);
				viewer.refresh();
			}
			System.out.println("hei");
		} else {
			super.inputChanged(viewer, oldInput, newInput);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Solution> castToList(Object input) {
		return (List<Solution>) input;
	}
}
