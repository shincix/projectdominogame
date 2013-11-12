package system;

import java.util.HashMap;
import java.util.Map;

/**
 * Generic Functions 
 *
 */
public final class Util {
	
	// Key map of message that contain a action.
	public final static String ACTION = "action";
	
	// Key map of message that contain the object of the action.
	public final static String PARAMETER = "parameter";

	public static final String KEY_USERNAME = "username";

	public static final String KEY_INROOM = "inroom";
	
	/**
	 * Format a message for send by remote connection
	 * All messages between client and server are on a Map<String, Object>
	 * The first are possible actions in system, defined by system.Actions.java
	 * The second are a object (any) refered to action. 
	 * 
	 * @param action
	 * @param parameter
	 * @return 
	 */
	public static final Map<String, Object> prepareMsg(String action, Object parameter) {
		
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put(ACTION, action);
		msg.put(PARAMETER, parameter);
		
		return msg;
	}
	
	/**
	 * Get action value of a message.
	 * 
	 * @param message
	 * @return The action
	 */
	public static String getAction(Map<String,Object> message) {
		String result = "";
		
		if(message != null ) {
			Object action = message.get(ACTION);
			if(action instanceof String) {
				result = (String) action;
			}
		}
		return result;
	}
	
	/**
	 * Get parameter value of a message.
	 * 
	 * @param message
	 * @return The parameter
	 */
	public static Object getParameter(Map<String, Object> message) {
		Object result = null;
		
		if(message != null) {
			result = message.get(PARAMETER);
		}
		return result;
	}
	
}
