'use strict';

var mymap;
var main = angular.module('main', [])

    // cute animation for...
    .constant('splash_timeout',         500)

    // ...those seconds it takes Google Map Canvas to be able to accept new markers
    .constant('initial_delay',          500)

    // Websocket ( localhost?! yeah i know )
    .constant('url_notify',             'ws://localhost:8080/myapp/notify')

    .constant('url_fetch_geopoints',    'api/geopoint/fetch' )  // REST

    .constant('url_fire_geopoint',      'api/geopoint/share' )   // REST

    // event signature for incomming shared geopoint
    .constant('evt_arrival',            'ARRIVED_GEOPOINT' )

    .service( 'notify', [ '$rootScope', 'url_notify', 'evt_arrival', function( $rootScope, url_notify, evt_arrival ) {
        var websocket = new WebSocket( url_notify );

        websocket.onmessage = function( event ) {
            console.log( 'ws', event.data );
            $rootScope.$emit( evt_arrival, event.data );
        }
    }])

    .service( 'iogeo', [ '$http', 'url_fetch_geopoints', 'url_fire_geopoint', function( $http, url_fetch_geopoints, url_fire_geopoint ) {



        this.shareGeopoint = function( title, long, lat, fmt_address, address ) {
            return $http({
                method:     'POST',
                url:        url_fire_geopoint,
                headers:    { 'Content-Type': 'application/x-www-form-urlencoded' },
                data:       $.param({
                    'title':        title,
                    'long':         long,
                    'lat':          lat,
                    'fmt_address':  fmt_address,
                    'address':      address
                })
            });
        };



        this.fetchGeopoints = function() {
            return $http({
                method: 'GET',
                url:    url_fetch_geopoints
            });
        };
    }])


    .controller('launcher', [ '$scope', '$timeout', 'splash_timeout', function ($scope, $timeout, splash_timeout) {
        $scope.is_loading = true;

        $timeout(function () {
            $scope.is_loading = false;

        }, splash_timeout)
    }])

    .controller('omni', ['$scope', '$rootScope', '$timeout', 'iogeo', 'initial_delay', 'notify', 'evt_arrival', function ($scope, $rootScope, $timeout, iogeo, initial_delay, notify, evt_arrival ) { // creator/editor

        var geocoder = new google.maps.Geocoder();

        $scope.title = '';
        $scope.address = '';
        $scope.fmt_address = '';
        $scope.long = '';
        $scope.lat = '';
        $scope.disabled = true;
        $scope.title_is_not_empty = false;

        function addMarker( title, long, lat, id ) {
            console.log( 'title=', title, 'long=', long, 'lat=', lat );
            var latlong = new google.maps.LatLng( lat, long );

            var infowindow = new google.maps.InfoWindow({
                content: '<span><a href=tag.html?id=' + id +  '>' + title + '</a></span>'
            });
            var marker = new google.maps.Marker({
                position:   latlong,
                map:        mymap,
                title:      title
            });
            google.maps.event.addListener(marker, 'click', function() {
                infowindow.open(mymap,marker);
            });
            return marker;
        };

        function focusTo( marker ) {
            mymap.setZoom(12);
            mymap.panTo(marker.getPosition());
        };

        iogeo.fetchGeopoints().success( function( response ) {
            $timeout( function() {
                response.payload.forEach( function ( entry ) {
                    addMarker( entry.title, entry.longitude, entry.latitude, entry.id )
                });
            }, initial_delay );
        });

        $scope.share = function() {
            // for consistent state -- avoid steady state
            var title_ = $scope.title,
                long_ = $scope.long,
                lat_ = $scope.lat;

            iogeo.shareGeopoint( title_, long_, lat_, $scope.fmt_address, $scope.address).success(function( response ){
                console.log( 'response', response );
                focusTo(addMarker( title_, long_, lat_, response.id )); // for consistent state -- avoid steady state
            });
            $scope.title='';
            $scope.long='';
            $scope.lat='';
            $scope.address = '';
            $scope.fmt_address = '';
        };

        $rootScope.$on( evt_arrival, function( event, message ) {
            var data = JSON.parse( message );
            addMarker( data.title, data.longitude, data.latitude );
        });

        $scope.$watch( 'title', function() {
            if ( $scope.address.length > 1 ) {
                $scope.title_is_not_empty = false;
            } else {
                $scope.title_is_not_empty = true;
            }
        });

        $scope.$watch( 'address', function () {
            if ( $scope.address.length > 5 ) {

                geocoder.geocode( { 'address': $scope.address }, function(results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        $scope.disabled = false;

                        //console.log( results[0] );
                        var dto = results[0].geometry;
                        $scope.long = dto.location.A;
                        $scope.lat = dto.location.k;
                        $scope.fmt_address = results[0].formatted_address;
                        $scope.$apply();

                    } else {
                        //alert("Geocode was not successful for the following reason: " + status);
                        $scope.disabled = true;
                        $scope.long = '';
                        $scope.lat = '';
                    }
                });
            } else {
                $scope.disabled = true;
                $scope.long = '';
                $scope.lat = '';
            }
        });
    }]);

function initialize() {
    var mapOptions = {
        center: new google.maps.LatLng(55.699839, 37.852083),
        zoom: 8
    };
    mymap = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
}

$(function () {
    google.maps.event.addDomListener(window, 'load', initialize);
});

//55.699839, 37.852083
//-34.397, 150.644
//$scope.$apply();
//            var myLatlng = new google.maps.LatLng( $scope.lat, $scope.long );
//
//            var infowindow = new google.maps.InfoWindow({
//                content: '<span>' + $scope.title + '</span>'
//            });
//            var marker = new google.maps.Marker({
//                position:   myLatlng,
//                map:        mymap,
//                title:      $scope.title
//            });
//            google.maps.event.addListener(marker, 'click', function() {
//                infowindow.open(mymap,marker);
//            });
//            mymap.setZoom(12);
//            mymap.panTo(marker.getPosition());
//console.log('address', $scope.address);
// 1600 Amphitheatre Parkway, Mountain View, CA
// Russia, Moscow, Red Square




