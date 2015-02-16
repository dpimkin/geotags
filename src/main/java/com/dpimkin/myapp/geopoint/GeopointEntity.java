/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dpimkin.myapp.geopoint;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "GEOPOINT")
//@XmlRootElement
/*@NamedQueries({
    @NamedQuery(name = "GeopointEntity.findAll", query = "SELECT g FROM GeopointEntity g"),
    @NamedQuery(name = "GeopointEntity.findById", query = "SELECT g FROM GeopointEntity g WHERE g.id = :id"),
    @NamedQuery(name = "GeopointEntity.findByTitle", query = "SELECT g FROM GeopointEntity g WHERE g.title = :title")})*/
public class GeopointEntity implements Serializable {

    private static final long serialVersionUID = -3874221001324780926L;  //private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    //@Basic(optional = false)
    //@NotNull
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "TITLE")
    private String title;

    @Basic(optional = false)
    @NotNull
    @Column(name = "LATITUDE")
    private Double latitude;

    @Basic(optional = false)
    @NotNull
    @Column(name = "LONGITUDE")
    private Double longitude;

    @Basic(optional = false)
    @NotNull
    @Column(name = "TYPE_ADDRESS")
    private String typeAddress;

    @Basic(optional = false)
    @NotNull
    @Column(name = "FMT_ADDRESS")
    private String formattedAddress;

    @Version
    @Column(name = "VERSION")
    private Integer version;


    public GeopointEntity() {
    }

    public GeopointEntity(Long id) {
        this.id = id;
    }

    public GeopointEntity(Long id, String title, Double latitude, Double longitude ) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public GeopointEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public GeopointEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public GeopointEntity setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public GeopointEntity setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getTypeAddress() {
        return typeAddress;
    }

    public GeopointEntity setTypeAddress(String typeAddress) {
        this.typeAddress = typeAddress;
        return this;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public GeopointEntity setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public GeopointEntity setVersion(Integer version) {
        this.version = version;
        return this;
    }

    /*@Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        GeopointEntity that = (GeopointEntity) other;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GeopointEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }*/
}
