package com.tokio.crm.registrocotizaciones73.services.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.tokio.crm.crmservices73.Constants.CrmDatabaseKey;
import com.tokio.crm.crmservices73.Util.SendMailJavaMail;
import com.tokio.crm.registrocotizaciones73.services.SendMailService;
import com.tokio.crm.servicebuilder73.model.User_Crm;
import com.tokio.crm.servicebuilder73.service.User_CrmLocalService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, properties = {}, service = SendMailService.class)
public class SendMailServiceImpl implements SendMailService {
	
	@Reference
	User_CrmLocalService _User_CrmLocalService;

    @Override
    public void enviarPendienteAutorizar(String folio, long solicitanteId, String cliente, String url){
        String subject = "Notificación - Folio de Cotización Pendiente de Autorizar";
        String body = "<!DOCTYPE html> \r\n"
                + "<html>   \r\n"
                + "<head>  \r\n"
                + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "	<section>\r\n"
                + "		<p>Hola ${nombreUsuario}</p>\r\n"
                + "		<p>Le informamos que se registro el siguiente folio de cotización: <b>${folio}</b>"
                + "		<p>Generado por: <b>${nombreSolicitante}</b>"
                + "		<p>Para el cliente: <b>${cliente}</b>"
                + "		<p><br/><br/>"
                + "     <p>ingresa al CRM para m&aacute;s informaci&oacute;n</p>\r\n"
                + "		<p><a href=\"${url}\">https://crm.tokiomarine.corp/</a></p>\r\n"
                + "		<p>Cualquier duda o comentario, por favor ac&eacute;rcate al equipo de Ventas.</p>\r\n"
                + "	</section>\r\n"
                + "</body>\r\n"
                + "</html>";
        final String bodyFinal = body
                .replace("${nombreSolicitante}",getUser(solicitanteId).getFullName())
                .replace("${cliente}", cliente)
                .replace("${folio}",folio).replace("${url}",url);
        List<User_Crm> managers = _User_CrmLocalService.getUsers_CrmByPerfil(CrmDatabaseKey.ID_PERFIL_MANAGER_VENTAS);
        sendMailByUsers(managers,subject,bodyFinal);
    }

    @Override
    public void enviarAutorizacion(String folio, long solicitanteId, String cliente, String url){
        String subject = "Notificación - Folio de Cotización Autorizado";
        String body = "<!DOCTYPE html> \r\n"
                + "<html>   \r\n"
                + "<head>  \r\n"
                + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "	<section>\r\n"
                + "		<p>Hola ${nombreUsuario}</p>\r\n"
                + "		<p>Le informamos que la cotización  con folio <b>${folio}</b> ha sido Autorizada"
                + "		<p>Para el cliente: <b>${cliente}</b>"
                + "		<p><br/><br/>"
                + "     <p>ingresa al CRM para m&aacute;s informaci&oacute;n</p>\r\n"
                + "		<p><a href=\"${url}\">https://crm.tokiomarine.corp/</a></p>\r\n"
                + "		<p>Cualquier duda o comentario, por favor ac&eacute;rcate al equipo de Ventas.</p>\r\n"
                + "	</section>\r\n"
                + "</body>\r\n"
                + "</html>";
        final String bodyFinal = body
                .replace("${folio}", folio)
                .replace("${cliente}", cliente)
                .replace("${url}",url);
        sendMailByUser(solicitanteId,subject,bodyFinal);
    }

    @Override
    public void enviarRechazo(String folio, long solicitanteId, String cliente, String url) {
        String subject = "Notificación - Folio de Cotización Rechazado";
        String body = "<!DOCTYPE html> \r\n"
                + "<html>   \r\n"
                + "<head>  \r\n"
                + "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "	<section>\r\n"
                + "		<p>Hola ${nombreUsuario}</p>\r\n"
                + "		<p>Le informamos que la cotización  con folio <b>${folio}</b> ha sido Rechazada"
                + "		<p>Para el cliente: <b>${cliente}</b>"
                + "		<p><br/><br/>"
                + "     <p>ingresa al CRM para m&aacute;s informaci&oacute;n</p>\r\n"
                + "		<p><a href=\"${url}\">https://crm.tokiomarine.corp/</a></p>\r\n"
                + "		<p>Cualquier duda o comentario, por favor ac&eacute;rcate al equipo de Ventas.</p>\r\n"
                + "	</section>\r\n"
                + "</body>\r\n"
                + "</html>";
        final String bodyFinal = body
                .replace("${folio}", folio)
                .replace("${cliente}", cliente)
                .replace("${url}",url);
        sendMailByUser(solicitanteId,subject,bodyFinal);
    }


    public User getUser(long userId) {
        try {
            return UserLocalServiceUtil.getUserById(userId);
        } catch (PortalException e) {
            return null;
        }
    }

    public User getUser(User_Crm usr) {
        try {
            return UserLocalServiceUtil.getUserById(new Long(usr.getUserId()));
        } catch (PortalException e) {
            return null;
        }
    }

    public String getEmailAddress(long userId) {
        try {
            return UserLocalServiceUtil.getUserById(userId).getEmailAddress();
        } catch (PortalException e) {
            return "";
        }
    }

    public String getEmailAddress(User_Crm usr) {
        try {
            return UserLocalServiceUtil.getUserById(new Long(usr.getUserId())).getEmailAddress();
        } catch (PortalException e) {
            return "";
        }
    }

    public void sendMailByUsers(List<User_Crm> usuarios, String subject, String body){
        for(User_Crm user_crm: usuarios){
            sendMailByUser(user_crm,subject,body);
        }
    }

    public void sendMailByUser( User_Crm usuario, String subject, String body){
        SendMailJavaMail sendMailJavaMail = new SendMailJavaMail(new String[]{getEmailAddress(usuario)});
        sendMailJavaMail.setSubject(subject);
        sendMailJavaMail.setBody(body.replace("${nombreUsuario}", getUser(usuario).getFullName()));
        sendMailJavaMail.addBody();
        sendMailJavaMail.send();
    }

    public void sendMailByUser( long usuario, String subject, String body){
        SendMailJavaMail sendMailJavaMail = new SendMailJavaMail(new String[]{getEmailAddress(usuario)});
        sendMailJavaMail.setSubject(subject);
        sendMailJavaMail.setBody(body.replace("${nombreUsuario}", getUser(usuario).getFullName()));
        sendMailJavaMail.addBody();
        sendMailJavaMail.send();
    }
}
