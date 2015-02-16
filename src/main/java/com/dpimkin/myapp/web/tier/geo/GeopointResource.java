package com.dpimkin.myapp.web.tier.geo;


import com.dpimkin.myapp.geopoint.GeopointEntity;
import com.dpimkin.myapp.geopoint.GeopointService;
import com.dpimkin.myapp.web.tier.Endpoint;
import com.google.common.base.Function;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;


import static com.google.common.collect.Collections2.transform;

@Path(Endpoint.GEOPOINT_RESOURCE)
@Produces( MediaType.APPLICATION_JSON )
public class GeopointResource {

    public static final String SHARE = "share";
    public static final String INFO = "info/{id}";
    public static final String FETCH = "fetch";


    @Inject
    GeopointService geopointService;

    @POST
    @Path( SHARE )
    public FireResponseDTO share(@FormParam("title") String title,
                                 @FormParam("lat") Double latitude,
                                 @FormParam("long") Double longitude,
                                 @FormParam("address") String typeAddress,
                                 @FormParam("fmt_address") String formattedAddress) {

        // validate mandatory parameters...
        if ( Strings.isNullOrEmpty( title ) || latitude == null  || longitude == null ) {
            throw new IllegalArgumentException();
            //return Response.serverError().build();
        }

        // Store geopoint in storage. And then deliver it for every online client...
        final Long idenity = geopointService.share(title, latitude, longitude, typeAddress, formattedAddress);

        FireResponseDTO response = new FireResponseDTO();
        response.id = idenity;
        return response;
    }

    @GET
    @Path( INFO )
    public GeopointDetailDTO info( @PathParam( "id" ) final Long id ) {
        if ( id == null ) {
            throw new IllegalArgumentException();
        }

        final GeopointEntity entity = geopointService.findById( id );
        GeopointDetailDTO response = new GeopointDetailDTO();
        response.id = entity.getId();
        response.title = entity.getTitle();
        response.latitude = entity.getLatitude();
        response.longitude = entity.getLongitude();
        response.formattedAddress = entity.getFormattedAddress();
        response.typeAddress = entity.getTypeAddress();
        return response;
    }

    @GET
    @Path( FETCH )
    public FetchResponse fetch() {
        return new FetchResponse() {{
            payload = transform( geopointService.findAll(), new Function<GeopointEntity, GeopointDTO>() {
                @Override
                public GeopointDTO apply( final GeopointEntity entity ) {
                    return new GeopointDTO() {{
                        id = entity.getId();
                        title = entity.getTitle();
                        longitude = entity.getLongitude();
                        latitude = entity.getLatitude();
                    }};
                }
            });
        }};
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
    public static class FetchResponse {

        @XmlElement
        public Collection<GeopointDTO> payload;
    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
    public static class GeopointDetailDTO {

        @XmlElement
        public Long id;

        @XmlElement
        public String title;

        @XmlElement
        public Double latitude;

        @XmlElement
        public Double longitude;

        @XmlElement
        public String typeAddress;

        @XmlElement
        public String formattedAddress;

    }

    @XmlRootElement
    @XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
    public static class FireResponseDTO {

        @XmlElement
        public Long id;
    }
}
