package com.dpimkin.myapp.web.tier.boundary;


import com.dpimkin.myapp.web.tier.Endpoint;
import com.dpimkin.myapp.web.tier.geo.GeopointResource;

import java.util.HashSet;
import java.util.Set;

@javax.ws.rs.ApplicationPath( Endpoint.REST_API )
public class ApplicationConfiguration extends javax.ws.rs.core.Application  {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>() {{
            add( GeopointResource.class );
        }};
    }

}
