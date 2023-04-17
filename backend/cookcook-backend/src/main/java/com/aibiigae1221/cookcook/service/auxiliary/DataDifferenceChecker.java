package com.aibiigae1221.cookcook.service.auxiliary;

public interface DataDifferenceChecker<X, Y>{
	boolean compare(X collection1Item, Y collection2Item);
}
