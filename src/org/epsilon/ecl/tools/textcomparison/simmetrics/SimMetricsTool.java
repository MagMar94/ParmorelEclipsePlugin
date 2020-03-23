package org.epsilon.ecl.tools.textcomparison.simmetrics;

import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;

import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;

public class SimMetricsTool {

	public double similarity(String s1, String s2, String algorithm) throws EolRuntimeException {
		AbstractStringMetric metric;
		if (s1 == null || s2 == null) {
			if (s1 == null && s2 == null) {
				return 1.0;
			} else {
				return 0.0;
			}
		}
		try {
			metric = (AbstractStringMetric) Class.forName("uk.ac.shef.wit.simmetrics.similaritymetrics." + algorithm)
					.newInstance();
			return metric.getSimilarity(s1, s2);
		} catch (Exception e) {
			throw new EolInternalException(e);
		}
	}
}