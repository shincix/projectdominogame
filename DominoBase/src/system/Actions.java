package system;

/**
 * Actions executes between client and server. 
 *
 */
public final class Actions {

	/**
	 * Client send to login command to server. The client not response this
	 * command.
	 */
	public static final String LOGON = "logon";

	/**
	 * Server send to Client when the command LOGON falied.
	 */
	public static final String LOGON_FAILURE = "failureLogon";

	/**
	 * Server send to Client when the command LOGIN sucessful.
	 */
	public static final String LOGON_SUCESSFUL = "sucessfulLogon";

	/**
	 * Server send generic message to client. Normally messages to be displayed
	 * to users.
	 */
	public static final String MESSAGE = "message";

	/**
	 * Client requests the current Room to server.
	 */
	public static final String GIVEME_ROOM = "givemeRoom";

	/**
	 * proceed Room update.
	 */
	public static final String UPDATE_ROOM = "updateRoom";

	/**
	 * Client request close connection.
	 */
	public static final String DISCONNECT = "disconnect";
	
	/**
	 * Get online users in server
	 */
	public static final String ONLINE_USERS = "onlineUsers";
	
	/**
	 * A client invite other to play
	 */
	public static final String INVITE = "invite";
	
	/**
	 * 
	 */
	public static final String CHAT_MESSAGE = "chatMessage";

	public static final String STATUS_INVITE = "statusInvite";

	public static final String RESPONSE_INVITE = "responseInvite";

	public static final String START_GAME = "startGame";

}
