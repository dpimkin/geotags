package com.dpimkin.myapp.geopoint;

import com.dpimkin.myapp.echo.EchoNotification;
import com.dpimkin.myapp.persistence.AbstractFacade;
import com.dpimkin.myapp.web.tier.geo.GeopointDTO;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateless
@Local( GeopointService.class )
public class GeopointServiceBean extends AbstractFacade<GeopointEntity> implements GeopointService, Serializable {

    private static final long serialVersionUID = -3409605440464619893L;

    private static final int RECENT_VERSION = 3;

    @PersistenceContext( unitName = "mainPU" )
    protected EntityManager em;

    @Inject
    @EchoNotification
    private Event<GeopointDTO> event;


    public GeopointServiceBean() {
        super( GeopointEntity.class );
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    @TransactionAttribute( TransactionAttributeType.REQUIRED )
    public List<GeopointEntity> findAll() {
        return super.findAll();
    }

    @Override
    @TransactionAttribute( TransactionAttributeType.REQUIRED )
    public GeopointEntity findById(Long id) {
        return super.find( id );
    }

    @Override
    @TransactionAttribute( TransactionAttributeType.REQUIRES_NEW )
    public Long share(String title, Double latitude, Double longitude, String typeAddress, String formattedAddress) {

        // record in data base
        GeopointEntity entity = new GeopointEntity()
                .setTitle(title)
                .setLatitude(latitude)
                .setLongitude(longitude)
                .setTypeAddress(typeAddress)
                .setFormattedAddress(formattedAddress)
                .setVersion(RECENT_VERSION);
        super.create( entity );

        // emit events for delivery
        GeopointDTO dto = new GeopointDTO();
        dto.title = title;
        dto.latitude = latitude;
        dto.longitude = longitude;
        event.fire( dto );

        return entity.getId();
    }
}
