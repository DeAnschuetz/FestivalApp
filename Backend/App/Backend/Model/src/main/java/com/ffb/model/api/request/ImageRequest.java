package com.ffb.model.api.request;


import jakarta.ws.rs.FormParam;
import org.jboss.resteasy.reactive.RestForm;

import java.io.InputStream;

public class ImageRequest {

    @FormParam("loginNr")
    private String loginNr;

    @RestForm("file")
    private InputStream file;


    public ImageRequest(String loginNr, InputStream file) {
        this.loginNr = loginNr;
        this.file = file;
    }

    public String getLoginNr() {
        return loginNr;
    }

    public void setLoginNr(String loginNr) {
        this.loginNr = loginNr;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

}
