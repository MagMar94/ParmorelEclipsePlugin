package hvl.plugin.parmorel.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import hvl.projectparmorel.exceptions.DistanceUnavailableException;
import hvl.projectparmorel.qlearning.QSolution;

public class DistanceLabelProvider extends ColumnLabelProvider {	
	@Override
	public String getText(Object element) {
		if (element != null && element instanceof QSolution) {
			QSolution solution = (QSolution) element;
			double distance;
			try {
				distance = solution.calculateDistanceFromOriginal();
			} catch (DistanceUnavailableException e) {
				return "Not available";
			}
			return Double.toString(distance);
		}
		return "";
	}
}
