var map;

function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          zoom: 7,
          center: {lat: 52.172037, lng: 5.564575},
          mapTypeId: google.maps.MapTypeId.TERRAIN
        });        
      }

function drawLine(lat1, lng1, lat2, lng2) {
    var flightPlanCoordinates = [
          {lat: lat1, lng: lng1},
          {lat: lat2, lng: lng2}
        ];
        var path = new google.maps.Polyline({
          path: flightPlanCoordinates,
          geodesic: true,
          strokeColor: '#FF0000',
          strokeOpacity: 1.0,
          strokeWeight: 2
        });
        
        var clat = (lat1+lat2)/2;
        var clng = (lng1+lng2)/2;
        
        map.setCenter(new google.maps.LatLng(clat, clng));
        map.setZoom(16);

        path.setMap(map);
}