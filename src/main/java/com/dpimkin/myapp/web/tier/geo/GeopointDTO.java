package com.dpimkin.myapp.web.tier.geo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@XmlAccessorType( XmlAccessType.PUBLIC_MEMBER )
public class GeopointDTO implements Serializable {

    private static final long serialVersionUID = 5692878697438120454L; // for events

    @XmlElement
    public Long id;

    @XmlElement
    public String title;

    @XmlElement
    public Double longitude;

    @XmlElement
    public Double latitude;

}
