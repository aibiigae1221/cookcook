package com.aibiigae1221.cookcook.service.auxiliary;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class CollectionComparerImpl<X, Y> implements CollectionComparer<X, Y>{

	// private static final Logger logger = LoggerFactory.getLogger(CollectionComparerImpl.class);
	
	@Override
	public boolean checkIfCollectionDataIsDifferent(Set<X> originalSet, List<Y> newEdittedList, DataDifferenceChecker<X, Y> diffChecker) {
		boolean edittedSignal = false;
		edittedSignal = edittedSignal | (originalSet.size() != newEdittedList.size());
		
		if(originalSet.size() == newEdittedList.size()) {
			Iterator<X> iter = originalSet.iterator();
			int idx = 0;
			while(iter.hasNext()) {
				X x = iter.next();
				edittedSignal = edittedSignal | diffChecker.compare(x, newEdittedList.get(idx));
				idx++;
			}
		}
		
		return edittedSignal;
	}

}
