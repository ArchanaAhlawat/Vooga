package engine.operations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

import engine.VoogaException;
import engine.operations.booleanops.BooleanValue;
import engine.operations.doubleops.Value;
import engine.operations.stringops.SelfString;
import javafx.util.Pair;

/**
 * Assumes names are unique
 * 
 * @author Ian Eldridge-Allegra
 *
 */
public class OperationFactory {
	private static final String PACKAGE = OperationFactory.class.getPackage().getName();
	private static final String BUNDLE_LOCATION = PACKAGE + ".Operations";
	private static final String DELIMITER = ", ";
	private static final int FILE_LOCATION_INDEX = 0;
	private static final int CLASS_NAME_INDEX = 1;

	private Map<String, ResourceBundle> operationsByType;
	private Map<String, String> parTypeToBasicName;

	public OperationFactory() {
		populateMaps();
	}

	/**
	 * Note the return type of Object is to support Actions as parameters to other
	 * Actions without requiring more of authoring.
	 * 
	 * @param operationName
	 *            The name of the operation, as given by
	 *            {@link #getOperations(String)}
	 * @param parameters
	 *            Whatever parameters are required by the operation, as described by
	 *            {@link #getParametersWithNames(String)}
	 * @return The Operation
	 * @throws VoogaException
	 */
	public Object makeOperation(String operationName, Object... parameters) throws VoogaException {
		try {
			return getConstructor(operationName).newInstance(parameters);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| ClassNotFoundException e) {
			throw new VoogaException("CantMakeOperation", operationName);
		}
	}

	/**
	 * @deprecated @see {@link #getParametersWithNames(String)}
	 */
	@Deprecated
	public List<String> getParameters(String operationName) throws VoogaException {
		List<String> types = new ArrayList<>();
		try {
			for (Class<?> parameterType : getConstructor(operationName).getParameterTypes()) {
				types.add(parTypeToBasicName.get(parameterType.getSimpleName()));
			}
		} catch (ClassNotFoundException e) {
			throw new VoogaException("ClassNotFoundFor", operationName);
		}
		return types;
	}

	/**
	 * @param operationName
	 *            The operation whose parameters are needed, an element from
	 *            {@link #getOperations(VoogaType)}
	 * @return The parameters with descriptive names and types. 
	 * @throws VoogaException
	 */
	public List<VoogaParameter> getParametersWithNames(String operationName) throws VoogaException {
		List<VoogaParameter> types = new ArrayList<>();
		try {
			for (Parameter parameter : getConstructor(operationName).getParameters()) {
				types.add(new VoogaParameter(parameter.getAnnotation(VoogaAnnotation.class).name(),
						parameter.getAnnotation(VoogaAnnotation.class).type()));
			}
		} catch (ClassNotFoundException e) {
			throw new VoogaException("ClassNotFoundFor", operationName);
		}
		return types;
	}

	//
	// private String decamel(String name) {
	// name = name.replaceAll("([a-z])([A-Z])", "$1 $2");
	// name = name.substring(0, 1).toUpperCase()+name.substring(1);
	// return name;
	// }

	@Deprecated
	public List<String> getOperations(String operationType) {
		return operations(operationType);
	}

	//used to support deprecated code without duplication
	private List<String> operations(String operationType) {
		if (operationsByType.containsKey(operationType))
			return Collections.list(operationsByType.get(operationType).getKeys());
		return new ArrayList<>();
	}

	/**
	 * @param parameter The operations that fit the given type. 
	 * @return All operations known of the given type, from properties files. 
	 */
	public List<String> getOperations(VoogaType parameter) {
		return operations(parameter.getEngineType());
	}

	private void populateMaps() {
		operationsByType = getOperationsByType();
		parTypeToBasicName = getParameterTypeMap();
	}

	private Constructor<?> getConstructor(String operationName) throws ClassNotFoundException {
		return Class.forName(getClassName(operationName)).getDeclaredConstructors()[0];
	}

	private String getClassName(String operationName) {
		for (String key : operationsByType.keySet()) {
			if (operationsByType.get(key).containsKey(operationName))
				return operationsByType.get(key).getString(operationName);
		}
		throw new VoogaException("ClassNotFoundFor", operationName);
	}

	/**
	 * @return The 
	 */
	public static Map<String, String> getParameterTypeMap() {
		return getFromBundle((key, properties) -> new Pair<String, String>(properties[CLASS_NAME_INDEX], key));
	}

	public static Map<String, ResourceBundle> getOperationsByType() {
		return getFromBundle(
				(key, properties) -> new Pair<>(key, ResourceBundle.getBundle(properties[FILE_LOCATION_INDEX])));
	}

	public Object wrap(Object value) {
		if (value instanceof Double)
			return new Value((Double) value);
		if (value instanceof Boolean)
			return new BooleanValue((Boolean) value);
		if (value instanceof String)
			return new SelfString((String) value);
		throw new RuntimeException("Cannot wrap " + value + " of type " + value.getClass().getSimpleName());
	}

	private static <T> Map<String, T> getFromBundle(BiFunction<String, String[], Pair<String, T>> function) {
		ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_LOCATION);
		Map<String, T> parTypes = new HashMap<>();
		for (String basicTypeName : Collections.list(bundle.getKeys())) {
			String[] properties = bundle.getString(basicTypeName).split(DELIMITER);
			Pair<String, T> entry = function.apply(basicTypeName, properties);
			parTypes.put(entry.getKey(), entry.getValue());
		}
		return parTypes;
	}
}
