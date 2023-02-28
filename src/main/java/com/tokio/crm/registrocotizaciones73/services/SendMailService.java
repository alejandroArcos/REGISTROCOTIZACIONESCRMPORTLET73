package com.tokio.crm.registrocotizaciones73.services;

public interface SendMailService {
    void enviarPendienteAutorizar(String folio, long solicitanteId, String cliente, String url);

    void enviarAutorizacion(String folio, long solicitanteId, String cliente, String url);

    void enviarRechazo(String folio, long solicitanteId, String cliente, String url);

}
