package consultoria.modulos.push;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@ManagedBean
public class NotifyView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6002099891495939445L;

	private final static String	CHANNEL	= "/notify";

	private String							summary;

	private String							detail;

	private String							canal;

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getCanal() {
		canal = "/notify/" + summary;
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public void send() {
		try {
			EventBus eventBus = EventBusFactory.getDefault().eventBus();
			eventBus.publish(CHANNEL, new FacesMessage(FacesMessage.SEVERITY_INFO, "INFORMACION:", detail));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}