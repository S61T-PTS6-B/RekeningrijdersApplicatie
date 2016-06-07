var map;
var polylines = [];
var markers = [];

function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          zoom: 7,
          center: {lat: 52.172037, lng: 5.564575},
          mapTypeId: google.maps.MapTypeId.TERRAIN
        });        
      }

function drawLine(lat1, lng1, lat2, lng2) {
    var Coordinates = [
          {lat: lat1, lng: lng1},
          {lat: lat2, lng: lng2}
        ];
        
        var path = new google.maps.Polyline({
          path: Coordinates,
          geodesic: true,
          strokeColor: '#FF0000',
          strokeOpacity: 1.0,
          strokeWeight: 2
        });
        
        var startmarker = new google.maps.Marker({
            position: new google.maps.LatLng(lat1, lng1),
            map: map
        });
        
        var endmarker = new google.maps.Marker({
            position: new google.maps.LatLng(lat2, lng2),
            map: map
        });
        markers.push(startmarker);
        markers.push(endmarker);
        
        polylines.push(path);
        
        var clat = (lat1+lat2)/2;
        var clng = (lng1+lng2)/2;
        
        map.setCenter(new google.maps.LatLng(clat, clng));
        map.setZoom(16);
        
        for (i = 0; i < polylines.length; i++) {
            var line = polylines[i];
            line.setMap(null);
        }
        for (i = 0; i < markers.length; i++) {
            var marker = markers[i];
            marker.setMap(null);
        }

        startmarker.setMap(map);
        endmarker.setMap(map);
        path.setMap(map);
}