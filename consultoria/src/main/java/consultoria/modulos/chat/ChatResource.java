package consultoria.modulos.chat;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;

@PushEndpoint("/{room}/{user}")
public class ChatResource {

	// private final Logger logger = LoggerFactory.getLogger(ChatResource.class);

	@PathParam("room")
	private String					room;

	@PathParam("user")
	private String					username;

	@Inject
	private ServletContext	ctx;

	@OnOpen
	public void onOpen(RemoteEndpoint r, EventBus eventBus) {
		try {
			// logger.info("OnOpen {}", r);

			eventBus.publish(room + "/*", new Message(String.format("%s has entered the room '%s'", username, room), true));

		} catch (Exception e) {

		}
	}

	@OnClose
	public void onClose(RemoteEndpoint r, EventBus eventBus) {
		try {
			ChatUsers users = (ChatUsers) ctx.getAttribute("chatUsers");
			users.remove(username);

			eventBus.publish(room + "/*", new Message(String.format("%s has left the room", username), true));

		} catch (Exception e) {

		}
	}

	@OnMessage(decoders = { MessageDecoder.class }, encoders = { MessageEncoder.class })
	public Message onMessage(Message message) {
		return message;
	}

}
