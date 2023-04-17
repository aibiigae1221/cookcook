package com.aibiigae1221.cookcook.service.auxiliary;

import java.util.List;
import java.util.Set;

public interface CollectionComparer<X, Y> {
	boolean checkIfCollectionDataIsDifferent(Set<X> originalSet, List<Y> newEdittedList, DataDifferenceChecker<X, Y> diffChecker);
}
