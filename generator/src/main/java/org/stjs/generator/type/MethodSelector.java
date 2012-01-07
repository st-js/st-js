package org.stjs.generator.type;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class tries to implement the algorithm to select the matching method given a list of parameter types. The
 * complete algorithm is described here: http://java.sun.com/docs/books/jls/third_edition/html/expressions.html#292575
 * 
 * <br>
 * Given the main purpose of this class, here is presented a simpler version of this algorithm. It takes into account
 * that there should be exactly one match as the checked code is already compiled and it's working! So for example if in
 * the given list there is only one method (or one with the correct number of parameters) it returns it right away.
 * 
 * @author acraciun
 * 
 */
public class MethodSelector {
	private static Comparator<MethodWrapper> methodSpecificityComparator = new Comparator<MethodWrapper>() {
		@Override
		public int compare(MethodWrapper m1, MethodWrapper m2) {
			if (m1.isCompatibleParameterTypes(m2.getParameterTypes())) {
				// as m1 accepts m2 param types, it is less specific
				return -1;
			}
			return 1;
		}
	};

	/**
	 * chooses from the list of candidates the method that matches the given argument types. if none is found, null is
	 * returned.
	 * 
	 * if the chosen method has type parameters, the returned method wrapper contains the resolved type for the return
	 * type
	 * 
	 * @param candidates
	 * @param argumentTypes
	 * @return
	 */
	public static MethodWrapper resolveMethod(Collection<MethodWrapper> candidates, TypeWrapper... argumentTypes) {
		if (candidates == null || candidates.isEmpty()) {
			return null;
		}
		List<MethodWrapper> matching = new ArrayList<MethodWrapper>(candidates.size());
		// keep only the list of methods with the same number of parameters or the one with a vararg argument that may
		// match
		// see Phase1, Phase2, Phase3 (15.12.2.2-4)
		for (MethodWrapper w : candidates) {
			if (w.hasCompatibleNumberOfParams(argumentTypes.length) && w.isCompatibleParameterTypes(argumentTypes)) {
				matching.add(w);
			}
		}

		if (matching.size() == 0) {
			return null;
		}

		MethodWrapper found;
		if (matching.size() == 1) {
			found = matching.get(0);
		} else {
			Collections.sort(matching, methodSpecificityComparator);
			// keep the most specific
			// XXX with the different rules for generics, this may be different
			found = matching.get(matching.size() - 1);
		}

		if (!(found.getReturnType() instanceof TypeVariableWrapper) || found.getTypeParameters() == null) {
			return found;
		}

		Map<String, TypeWrapper> inferredTypes = new HashMap<String, TypeWrapper>();

		for (int i = 0; i < argumentTypes.length; ++i) {
			TypeWrapper argumentType = argumentTypes[i];
			TypeWrapper paramType = i < found.getParameterTypes().length ? found.getParameterTypes()[i] : found
					.getVarargParamType();
			Map<String, TypeWrapper> inferredTypesForParam = resolveTypeVariables(paramType, argumentType);
			for (Map.Entry<String, TypeWrapper> e : inferredTypesForParam.entrySet()) {
				TypeWrapper existing = inferredTypes.get(e.getKey());
				if (existing == null || !existing.isAssignableFrom(e.getValue())) {
					inferredTypes.put(e.getKey(), e.getValue());
				}
			}
		}

		TypeVariableWrapper<?> returnTypeVar = (TypeVariableWrapper<?>) found.getReturnType();
		TypeWrapper inferredType = inferredTypes.get(((TypeVariable<?>) returnTypeVar.getType()).getName());

		if (inferredType == null) {
			return found;
		}
		return found.withReturnType(inferredType);
	}

	private static Map<String, TypeWrapper> resolveTypeVariables(TypeWrapper paramType, TypeWrapper argumentType) {
		Map<String, TypeWrapper> inferredTypes = new HashMap<String, TypeWrapper>();
		if (paramType instanceof TypeVariableWrapper) {
			inferredTypes.put(((TypeVariable<?>) paramType.getType()).getName(), argumentType);
		} else if (paramType instanceof ParameterizedTypeWrapper) {
			if (argumentType instanceof ParameterizedTypeWrapper) {
				// let's hope the sizes are similar
				TypeWrapper[] paramTypeArgs = ((ParameterizedTypeWrapper) paramType).getActualTypeArguments();
				TypeWrapper[] argumentTypeArgs = ((ParameterizedTypeWrapper) argumentType).getActualTypeArguments();
				for (int i = 0; i < paramTypeArgs.length; ++i) {
					// TODO keep also the most specific
					inferredTypes.putAll(resolveTypeVariables(paramTypeArgs[i], argumentTypeArgs[i]));
				}
			}
		} else if (paramType instanceof GenericArrayTypeWrapper) {
			if (argumentType instanceof ParameterizedTypeWrapper) {
				inferredTypes
						.putAll(resolveTypeVariables(paramType.getComponentType(), argumentType.getComponentType()));
			}
		}

		return inferredTypes;
	}
}
