package org.stjs.generator.type;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.stjs.generator.utils.Lists;

/**
 * This class tries to implement the algorithm to select the matching method given a list of parameter types. The
 * complete algorithm is described here: http://java.sun.com/docs/books/jls/third_edition/html/expressions.html#292575 <br>
 * Given the main purpose of this class, here is presented a simpler version of this algorithm. It takes into account
 * that there should be exactly one match as the checked code is already compiled and it's working! So for example if in
 * the given list there is only one method (or one with the correct number of parameters) it returns it right away.
 * 
 * @author acraciun
 */
public final class MethodSelector {

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

	private MethodSelector() {
		//
	}

	private static MethodWrapper chooseOne(List<MethodWrapper> matching) {
		if (matching.isEmpty()) {
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
		return found;
	}

	private static Map<String, TypeWrapper> inferParameterTypes(MethodWrapper found, TypeWrapper[] argumentTypes) {
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
		return inferredTypes;
	}

	private static List<MethodWrapper> filterCandidates(Collection<MethodWrapper> candidates,
			TypeWrapper[] argumentTypes) {
		List<MethodWrapper> matching = new ArrayList<MethodWrapper>(candidates.size());
		// keep only the list of methods with the same number of parameters or the one with a vararg argument that may
		// match
		// see Phase1, Phase2, Phase3 (15.12.2.2-4)
		for (MethodWrapper w : candidates) {
			if (w.hasCompatibleNumberOfParams(argumentTypes.length) && w.isCompatibleParameterTypes(argumentTypes)) {
				matching.add(w);
			}
		}
		return matching;
	}

	/**
	 * chooses from the list of candidates the method that matches the given argument types. if none is found, null is
	 * returned. if the chosen method has type parameters, the returned method wrapper contains the resolved type for
	 * the return type
	 * 
	 * @param candidates
	 * @param argumentTypes
	 * @return
	 */
	public static MethodWrapper resolveMethod(Collection<MethodWrapper> candidates, TypeWrapper... argumentTypes) {
		if (Lists.isNullOrEmpty(candidates)) {
			return null;
		}
		List<MethodWrapper> matching = filterCandidates(candidates, argumentTypes);
		MethodWrapper found = chooseOne(matching);
		if (found == null) {
			return null;
		}

		if (!found.isGeneric()) {
			return found;
		}

		Map<String, TypeWrapper> inferredTypes = inferParameterTypes(found, argumentTypes);
		TypeVariableWrapper<?> returnTypeVar = (TypeVariableWrapper<?>) found.getReturnType();
		TypeWrapper inferredType = inferredTypes.get(((TypeVariable<?>) returnTypeVar.getType()).getName());

		if (inferredType == null) {
			return found;
		}
		return found.withReturnType(inferredType);
	}

	private static Map<String, TypeWrapper> resolveVariablesForParameterizedTypeWrapper(
			ParameterizedTypeWrapper paramType, TypeWrapper argumentType) {
		Map<String, TypeWrapper> inferredTypes = new HashMap<String, TypeWrapper>();
		if (argumentType instanceof ParameterizedTypeWrapper) {
			// let's hope the sizes are similar
			TypeWrapper[] paramTypeArgs = paramType.getActualTypeArguments();
			TypeWrapper[] argumentTypeArgs = ((ParameterizedTypeWrapper) argumentType).getActualTypeArguments();
			for (int i = 0; i < paramTypeArgs.length; ++i) {
				// TODO keep also the most specific
				inferredTypes.putAll(resolveTypeVariables(paramTypeArgs[i], argumentTypeArgs[i]));
			}
		}
		return inferredTypes;
	}

	private static Map<String, TypeWrapper> resolveVariablesForGenericArrayTypeWrapper(
			GenericArrayTypeWrapper paramType, TypeWrapper argumentType) {
		if (argumentType instanceof ParameterizedTypeWrapper) {
			return resolveTypeVariables(paramType.getComponentType(), argumentType.getComponentType());
		}
		return Collections.emptyMap();
	}

	private static Map<String, TypeWrapper> resolveVariablesForTypeVariableWrapper(TypeVariableWrapper<?> paramType,
			TypeWrapper argumentType) {
		return Collections.singletonMap(((TypeVariable<?>) paramType.getType()).getName(), argumentType);
	}

	private static Map<String, TypeWrapper> resolveTypeVariables(TypeWrapper paramType, TypeWrapper argumentType) {
		if (paramType instanceof TypeVariableWrapper) {
			return resolveVariablesForTypeVariableWrapper((TypeVariableWrapper<?>) paramType, argumentType);
		} else if (paramType instanceof ParameterizedTypeWrapper) {
			return resolveVariablesForParameterizedTypeWrapper((ParameterizedTypeWrapper) paramType, argumentType);
		} else if (paramType instanceof GenericArrayTypeWrapper) {
			return resolveVariablesForGenericArrayTypeWrapper((GenericArrayTypeWrapper) paramType, argumentType);
		}

		return Collections.emptyMap();
	}
}
