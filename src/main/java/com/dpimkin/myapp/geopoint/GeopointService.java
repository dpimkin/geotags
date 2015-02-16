package com.dpimkin.myapp.geopoint;


import java.util.List;

public interface GeopointService {

    List<GeopointEntity> findAll();

    GeopointEntity findById( Long id );

    Long share(String title, Double latitude, Double lontitude, String typeAddress, String formattedAddress);

}
