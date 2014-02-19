package com.csc.bikelaner;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.method.LinkMovementMethod;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

public class GPSHandler implements android.location.LocationListener {
	/** The interactive Google Map fragment. */
	private GoogleMap m_vwMap;
	private final LocationManager locationManager;
	private final FragmentManager fragmentManager;

	/** The list of locations, each having a latitude and longitude. */
	private ArrayList<LatLng> m_arrPathPoints;

	/** The continuous set of lines drawn between points on the map. */
	private Polyline m_pathLine;

	public static final int DEFAULT_ZOOM_LEVEL = 20;
	private static final int CIRCLE_RADIUS = 1;

	
	private static final int[] ALT_HEATMAP_GRADIENT_COLORS = {
			Color.argb(0, 0, 255, 255),// transparent
			Color.argb(255 / 3 * 2, 0, 255, 255), Color.rgb(0, 191, 255),
			Color.rgb(0, 0, 127), Color.rgb(255, 0, 0) };

	public static final float[] ALT_HEATMAP_GRADIENT_START_POINTS = { 0.0f,
			0.10f, 0.20f, 0.60f, 1.0f };

	public static final Gradient ALT_HEATMAP_GRADIENT = new Gradient(
			ALT_HEATMAP_GRADIENT_COLORS, ALT_HEATMAP_GRADIENT_START_POINTS);

	private HeatmapTileProvider mProvider;
	private TileOverlay mOverlay;

	private boolean mDefaultGradient = true;
	private boolean mDefaultRadius = true;
	private boolean mDefaultOpacity = true;

	public GPSHandler(LocationManager locationManager,
			FragmentManager fragmentManager) {
		this.locationManager = locationManager;
		this.fragmentManager = fragmentManager;
		m_arrPathPoints = new ArrayList<LatLng>();

	}

	public void createPointsOnMap() {
		m_pathLine = m_vwMap.addPolyline(new PolylineOptions());
		m_pathLine.setColor(Color.GREEN);
		// TIMER: start the timer to call onlocation call.
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				10000, 0, this);
	}

	public void initiate() {
		initMapLayout();
		initMapSettings();
		setCameraToMyLocation();
		createPointsOnMap();
		makeDummyHeatMap();
	}

	public void initMapLayout() {
		// Obtain the support map fragment specified in the XML to get the map
		// object
		new com.google.android.gms.maps.model.LatLng(37.782551, -122.445368);
		SupportMapFragment map = (SupportMapFragment) fragmentManager
				.findFragmentById(R.id.map);
		m_vwMap = map.getMap();
		assert (m_vwMap != null);

	}

	public void initMapSettings() {
		// Enables in the view the ability to track your location.
		// Rest is self explanatory.
		m_vwMap.setMyLocationEnabled(true);
		m_vwMap.getUiSettings().setCompassEnabled(true);
		m_vwMap.getUiSettings().setZoomControlsEnabled(true);

	}

	public void setCameraToMyLocation() {
		// Create a location manager to get a service location provider
		// to find my location.
		String provider = locationManager.getBestProvider(new Criteria(), true);
		Location location = locationManager.getLastKnownLocation(provider);

		// Set the camera to my location
		if (location != null) {
			LatLng latLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			m_vwMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
					DEFAULT_ZOOM_LEVEL));
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		onLocationChanged(location);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			LatLng loc = new LatLng(location.getLatitude(),
					location.getLongitude());
			m_arrPathPoints.add(loc);

			m_pathLine.setPoints(m_arrPathPoints);
			m_vwMap.addCircle(new CircleOptions().center(loc)
					.radius(CIRCLE_RADIUS).fillColor(Color.CYAN)
					.strokeColor(Color.BLUE));
			m_vwMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,
					DEFAULT_ZOOM_LEVEL));
		}
	}

	public void makeDummyHeatMap() {
		ArrayList<LatLng> list = new ArrayList<LatLng>(
				Arrays.asList(dummy_taxiData));
		System.out.println("GPSHANDLER: " + list.size());
		mProvider = new HeatmapTileProvider.Builder().data(list).build();
		mOverlay = m_vwMap.addTileOverlay(
				new TileOverlayOptions().tileProvider(mProvider));

		//TODO: remove this dummy taxi initial data
		LatLng latLng = new LatLng(37.782551, -122.445368);
		m_vwMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
				DEFAULT_ZOOM_LEVEL));
	}
   
   public LatLng getLocation() {
	// Create a location manager to get a service location provider
      // to find my location.
      String provider = locationManager.getBestProvider(new Criteria(), true);
      Location location = locationManager.getLastKnownLocation(provider);

      // Set the camera to my location
      if (location != null) {
         return new LatLng(location.getLatitude(),
               location.getLongitude());
      }
      return null;
	}
   
   private static final LatLng dummy_taxiData[] = {
	   new LatLng(37.782551, -122.445368),
	   new LatLng(37.782745, -122.444586),
	   new LatLng(37.782842, -122.443688),
	   new LatLng(37.782919, -122.442815),
	   new LatLng(37.782992, -122.442112),
	   new LatLng(37.783100, -122.441461),
	   new LatLng(37.783206, -122.440829),
	   new LatLng(37.783273, -122.440324),
	   new LatLng(37.783316, -122.440023),
	   new LatLng(37.783357, -122.439794),
	   new LatLng(37.783371, -122.439687),
	   new LatLng(37.783368, -122.439666),
	   new LatLng(37.783383, -122.439594),
	   new LatLng(37.783508, -122.439525),
	   new LatLng(37.783842, -122.439591),
	   new LatLng(37.784147, -122.439668),
	   new LatLng(37.784206, -122.439686),
	   new LatLng(37.784386, -122.439790),
	   new LatLng(37.784701, -122.439902),
	   new LatLng(37.784965, -122.439938),
	   new LatLng(37.785010, -122.439947),
	   new LatLng(37.785360, -122.439952),
	   new LatLng(37.785715, -122.440030),
	   new LatLng(37.786117, -122.440119),
	   new LatLng(37.786564, -122.440209),
	   new LatLng(37.786905, -122.440270),
	   new LatLng(37.786956, -122.440279),
	   new LatLng(37.800224, -122.433520),
	   new LatLng(37.800155, -122.434101),
	   new LatLng(37.800160, -122.434430),
	   new LatLng(37.800378, -122.434527),
	   new LatLng(37.800738, -122.434598),
	   new LatLng(37.800938, -122.434650),
	   new LatLng(37.801024, -122.434889),
	   new LatLng(37.800955, -122.435392),
	   new LatLng(37.800886, -122.435959),
	   new LatLng(37.800811, -122.436275),
	   new LatLng(37.800788, -122.436299),
	   new LatLng(37.800719, -122.436302),
	   new LatLng(37.800702, -122.436298),
	   new LatLng(37.800661, -122.436273),
	   new LatLng(37.800395, -122.436172),
	   new LatLng(37.800228, -122.436116),
	   new LatLng(37.800169, -122.436130),
	   new LatLng(37.800066, -122.436167),
	   new LatLng(37.784345, -122.422922),
	   new LatLng(37.784389, -122.422926),
	   new LatLng(37.784437, -122.422924),
	   new LatLng(37.784746, -122.422818),
	   new LatLng(37.785436, -122.422959),
	   new LatLng(37.786120, -122.423112),
	   new LatLng(37.786433, -122.423029),
	   new LatLng(37.786631, -122.421213),
	   new LatLng(37.786660, -122.421033),
	   new LatLng(37.786801, -122.420141),
	   new LatLng(37.786823, -122.420034),
	   new LatLng(37.786831, -122.419916),
	   new LatLng(37.787034, -122.418208),
	   new LatLng(37.787056, -122.418034),
	   new LatLng(37.787169, -122.417145),
	   new LatLng(37.787217, -122.416715),
	   new LatLng(37.786144, -122.416403),
	   new LatLng(37.785292, -122.416257),
	   new LatLng(37.780666, -122.390374),
	   new LatLng(37.780501, -122.391281),
	   new LatLng(37.780148, -122.392052),
	   new LatLng(37.780173, -122.391148),
	   new LatLng(37.780693, -122.390592),
	   new LatLng(37.781261, -122.391142),
	   new LatLng(37.781808, -122.391730),
	   new LatLng(37.782340, -122.392341),
	   new LatLng(37.782812, -122.393022),
	   new LatLng(37.783300, -122.393672),
	   new LatLng(37.783809, -122.394275),
	   new LatLng(37.784246, -122.394979),
	   new LatLng(37.784791, -122.395958),
	   new LatLng(37.785675, -122.396746),
	   new LatLng(37.786262, -122.395780),
	   new LatLng(37.786776, -122.395093),
	   new LatLng(37.787282, -122.394426),
	   new LatLng(37.787783, -122.393767),
	   new LatLng(37.788343, -122.393184),
	   new LatLng(37.788895, -122.392506),
	   new LatLng(37.789371, -122.391701),
	   new LatLng(37.789722, -122.390952),
	   new LatLng(37.790315, -122.390305),
	   new LatLng(37.790738, -122.389616),
	   new LatLng(37.779448, -122.438702),
	   new LatLng(37.779023, -122.438585),
	   new LatLng(37.778542, -122.438492),
	   new LatLng(37.778100, -122.438411),
	   new LatLng(37.777986, -122.438376),
	   new LatLng(37.777680, -122.438313),
	   new LatLng(37.777316, -122.438273),
	   new LatLng(37.777135, -122.438254),
	   new LatLng(37.776987, -122.438303),
	   new LatLng(37.776946, -122.438404),
	   new LatLng(37.776944, -122.438467),
	   new LatLng(37.776892, -122.438459),
	   new LatLng(37.776842, -122.438442),
	   new LatLng(37.776822, -122.438391),
	   new LatLng(37.776814, -122.438412),
	   new LatLng(37.776787, -122.438628),
	   new LatLng(37.776729, -122.438650),
	   new LatLng(37.776759, -122.438677),
	   new LatLng(37.776772, -122.438498),
	   new LatLng(37.776787, -122.438389),
	   new LatLng(37.776848, -122.438283),
	   new LatLng(37.776870, -122.438239),
	   new LatLng(37.777015, -122.438198),
	   new LatLng(37.777333, -122.438256),
	   new LatLng(37.777595, -122.438308),
	   new LatLng(37.777797, -122.438344),
	   new LatLng(37.778160, -122.438442),
	   new LatLng(37.778414, -122.438508),
	   new LatLng(37.778445, -122.438516),
	   new LatLng(37.778503, -122.438529),
	   new LatLng(37.778607, -122.438549),
	   new LatLng(37.778670, -122.438644),
	   new LatLng(37.778847, -122.438706),
	   new LatLng(37.779240, -122.438744),
	   new LatLng(37.779738, -122.438822),
	   new LatLng(37.780201, -122.438882),
	   new LatLng(37.780400, -122.438905),
	   new LatLng(37.780501, -122.438921),
	   new LatLng(37.780892, -122.438986),
	   new LatLng(37.781446, -122.439087),
	   new LatLng(37.781985, -122.439199),
	   new LatLng(37.782239, -122.439249),
	   new LatLng(37.782286, -122.439266),
	   new LatLng(37.797847, -122.429388),
	   new LatLng(37.797874, -122.429180),
	   new LatLng(37.797885, -122.429069),
	   new LatLng(37.797887, -122.429050),
	   new LatLng(37.797933, -122.428954),
	   new LatLng(37.798242, -122.428990),
	   new LatLng(37.798617, -122.429075),
	   new LatLng(37.798719, -122.429092),
	   new LatLng(37.798944, -122.429145),
	   new LatLng(37.799320, -122.429251),
	   new LatLng(37.799590, -122.429309),
	   new LatLng(37.799677, -122.429324),
	   new LatLng(37.799966, -122.429360),
	   new LatLng(37.800288, -122.429430),
	   new LatLng(37.800443, -122.429461),
	   new LatLng(37.800465, -122.429474),
	   new LatLng(37.800644, -122.429540),
	   new LatLng(37.800948, -122.429620),
	   new LatLng(37.801242, -122.429685),
	   new LatLng(37.801375, -122.429702),
	   new LatLng(37.801400, -122.429703),
	   new LatLng(37.801453, -122.429707),
	   new LatLng(37.801473, -122.429709),
	   new LatLng(37.801532, -122.429707),
	   new LatLng(37.801852, -122.429729),
	   new LatLng(37.802173, -122.429789),
	   new LatLng(37.802459, -122.429847),
	   new LatLng(37.802554, -122.429825),
	   new LatLng(37.802647, -122.429549),
	   new LatLng(37.802693, -122.429179),
	   new LatLng(37.802729, -122.428751),
	   new LatLng(37.766104, -122.409291),
	   new LatLng(37.766103, -122.409268),
	   new LatLng(37.766138, -122.409229),
	   new LatLng(37.766183, -122.409231),
	   new LatLng(37.766153, -122.409276),
	   new LatLng(37.766005, -122.409365),
	   new LatLng(37.765897, -122.409570),
	   new LatLng(37.765767, -122.409739),
	   new LatLng(37.765693, -122.410389),
	   new LatLng(37.765615, -122.411201),
	   new LatLng(37.765533, -122.412121),
	   new LatLng(37.765467, -122.412939),
	   new LatLng(37.765444, -122.414821),
	   new LatLng(37.765444, -122.414964),
	   new LatLng(37.765318, -122.415424),
	   new LatLng(37.763961, -122.415296),
	   new LatLng(37.763115, -122.415196),
	   new LatLng(37.762967, -122.415183),
	   new LatLng(37.762278, -122.415127),
	   new LatLng(37.761675, -122.415055),
	   new LatLng(37.760932, -122.414988),
	   new LatLng(37.759337, -122.414862),
	   new LatLng(37.773187, -122.421922),
	   new LatLng(37.773043, -122.422118),
	   new LatLng(37.773007, -122.422165),
	   new LatLng(37.772979, -122.422219),
	   new LatLng(37.772865, -122.422394),
	   new LatLng(37.772779, -122.422503),
	   new LatLng(37.772676, -122.422701),
	   new LatLng(37.772606, -122.422806),
	   new LatLng(37.772566, -122.422840),
	   new LatLng(37.772508, -122.422852),
	   new LatLng(37.772387, -122.423011),
	   new LatLng(37.772099, -122.423328),
	   new LatLng(37.771704, -122.423783),
	   new LatLng(37.771481, -122.424081),
	   new LatLng(37.771400, -122.424179),
	   new LatLng(37.771352, -122.424220),
	   new LatLng(37.771248, -122.424327),
	   new LatLng(37.770904, -122.424781),
	   new LatLng(37.770520, -122.425283),
	   new LatLng(37.770337, -122.425553),
	   new LatLng(37.770128, -122.425832),
	   new LatLng(37.769756, -122.426331),
	   new LatLng(37.769300, -122.426902),
	   new LatLng(37.769132, -122.427065),
	   new LatLng(37.769092, -122.427103),
	   new LatLng(37.768979, -122.427172),
	   new LatLng(37.768595, -122.427634),
	   new LatLng(37.768372, -122.427913),
	   new LatLng(37.768337, -122.427961),
	   new LatLng(37.768244, -122.428138),
	   new LatLng(37.767942, -122.428581),
	   new LatLng(37.767482, -122.429094),
	   new LatLng(37.767031, -122.429606),
	   new LatLng(37.766732, -122.429986),
	   new LatLng(37.766680, -122.430058),
	   new LatLng(37.766633, -122.430109),
	   new LatLng(37.766580, -122.430211),
	   new LatLng(37.766367, -122.430594),
	   new LatLng(37.765910, -122.431137),
	   new LatLng(37.765353, -122.431806),
	   new LatLng(37.764962, -122.432298),
	   new LatLng(37.764868, -122.432486),
	   new LatLng(37.764518, -122.432913),
	   new LatLng(37.763435, -122.434173),
	   new LatLng(37.762847, -122.434953),
	   new LatLng(37.762291, -122.435935),
	   new LatLng(37.762224, -122.436074),
	   new LatLng(37.761957, -122.436892),
	   new LatLng(37.761652, -122.438886),
	   new LatLng(37.761284, -122.439955),
	   new LatLng(37.761210, -122.440068),
	   new LatLng(37.761064, -122.440720),
	   new LatLng(37.761040, -122.441411),
	   new LatLng(37.761048, -122.442324),
	   new LatLng(37.760851, -122.443118),
	   new LatLng(37.759977, -122.444591),
	   new LatLng(37.759913, -122.444698),
	   new LatLng(37.759623, -122.445065),
	   new LatLng(37.758902, -122.445158),
	   new LatLng(37.758428, -122.444570),
	   new LatLng(37.757687, -122.443340),
	   new LatLng(37.757583, -122.443240),
	   new LatLng(37.757019, -122.442787),
	   new LatLng(37.756603, -122.442322),
	   new LatLng(37.756380, -122.441602),
	   new LatLng(37.755790, -122.441382),
	   new LatLng(37.754493, -122.442133),
	   new LatLng(37.754361, -122.442206),
	   new LatLng(37.753719, -122.442650),
	   new LatLng(37.753096, -122.442915),
	   new LatLng(37.751617, -122.443211),
	   new LatLng(37.751496, -122.443246),
	   new LatLng(37.750733, -122.443428),
	   new LatLng(37.750126, -122.443536),
	   new LatLng(37.750103, -122.443784),
	   new LatLng(37.750390, -122.444010),
	   new LatLng(37.750448, -122.444013),
	   new LatLng(37.750536, -122.444040),
	   new LatLng(37.750493, -122.444141),
	   new LatLng(37.790859, -122.402808),
	   new LatLng(37.790864, -122.402768),
	   new LatLng(37.790995, -122.402539),
	   new LatLng(37.791148, -122.402172),
	   new LatLng(37.791385, -122.401312),
	   new LatLng(37.791405, -122.400776),
	   new LatLng(37.791288, -122.400528),
	   new LatLng(37.791113, -122.400441),
	   new LatLng(37.791027, -122.400395),
	   new LatLng(37.791094, -122.400311),
	   new LatLng(37.791211, -122.400183),
	   new LatLng(37.791060, -122.399334),
	   new LatLng(37.790538, -122.398718),
	   new LatLng(37.790095, -122.398086),
	   new LatLng(37.789644, -122.397360),
	   new LatLng(37.789254, -122.396844),
	   new LatLng(37.788855, -122.396397),
	   new LatLng(37.788483, -122.395963),
	   new LatLng(37.788015, -122.395365),
	   new LatLng(37.787558, -122.394735),
	   new LatLng(37.787472, -122.394323),
	   new LatLng(37.787630, -122.394025),
	   new LatLng(37.787767, -122.393987),
	   new LatLng(37.787486, -122.394452),
	   new LatLng(37.786977, -122.395043),
	   new LatLng(37.786583, -122.395552),
	   new LatLng(37.786540, -122.395610),
	   new LatLng(37.786516, -122.395659),
	   new LatLng(37.786378, -122.395707),
	   new LatLng(37.786044, -122.395362),
	   new LatLng(37.785598, -122.394715),
	   new LatLng(37.785321, -122.394361),
	   new LatLng(37.785207, -122.394236),
	   new LatLng(37.785751, -122.394062),
	   new LatLng(37.785996, -122.393881),
	   new LatLng(37.786092, -122.393830),
	   new LatLng(37.785998, -122.393899),
	   new LatLng(37.785114, -122.394365),
	   new LatLng(37.785022, -122.394441),
	   new LatLng(37.784823, -122.394635),
	   new LatLng(37.784719, -122.394629),
	   new LatLng(37.785069, -122.394176),
	   new LatLng(37.785500, -122.393650),
	   new LatLng(37.785770, -122.393291),
	   new LatLng(37.785839, -122.393159),
	   new LatLng(37.782651, -122.400628),
	   new LatLng(37.782616, -122.400599),
	   new LatLng(37.782702, -122.400470),
	   new LatLng(37.782915, -122.400192),
	   new LatLng(37.783137, -122.399887),
	   new LatLng(37.783414, -122.399519),
	   new LatLng(37.783629, -122.399237),
	   new LatLng(37.783688, -122.399157),
	   new LatLng(37.783716, -122.399106),
	   new LatLng(37.783798, -122.399072),
	   new LatLng(37.783997, -122.399186),
	   new LatLng(37.784271, -122.399538),
	   new LatLng(37.784577, -122.399948),
	   new LatLng(37.784828, -122.400260),
	   new LatLng(37.784999, -122.400477),
	   new LatLng(37.785113, -122.400651),
	   new LatLng(37.785155, -122.400703),
	   new LatLng(37.785192, -122.400749),
	   new LatLng(37.785278, -122.400839),
	   new LatLng(37.785387, -122.400857),
	   new LatLng(37.785478, -122.400890),
	   new LatLng(37.785526, -122.401022),
	   new LatLng(37.785598, -122.401148),
	   new LatLng(37.785631, -122.401202),
	   new LatLng(37.785660, -122.401267),
	   new LatLng(37.803986, -122.426035),
	   new LatLng(37.804102, -122.425089),
	   new LatLng(37.804211, -122.424156),
	   new LatLng(37.803861, -122.423385),
	   new LatLng(37.803151, -122.423214),
	   new LatLng(37.802439, -122.423077),
	   new LatLng(37.801740, -122.422905),
	   new LatLng(37.801069, -122.422785),
	   new LatLng(37.800345, -122.422649),
	   new LatLng(37.799633, -122.422603),
	   new LatLng(37.799750, -122.421700),
	   new LatLng(37.799885, -122.420854),
	   new LatLng(37.799209, -122.420607),
	   new LatLng(37.795656, -122.400395),
	   new LatLng(37.795203, -122.400304),
	   new LatLng(37.778738, -122.415584),
	   new LatLng(37.778812, -122.415189),
	   new LatLng(37.778824, -122.415092),
	   new LatLng(37.778833, -122.414932),
	   new LatLng(37.778834, -122.414898),
	   new LatLng(37.778740, -122.414757),
	   new LatLng(37.778501, -122.414433),
	   new LatLng(37.778182, -122.414026),
	   new LatLng(37.777851, -122.413623),
	   new LatLng(37.777486, -122.413166),
	   new LatLng(37.777109, -122.412674),
	   new LatLng(37.776743, -122.412186),
	   new LatLng(37.776440, -122.411800),
	   new LatLng(37.776295, -122.411614),
	   new LatLng(37.776158, -122.411440),
	   new LatLng(37.775806, -122.410997),
	   new LatLng(37.775422, -122.410484),
	   new LatLng(37.775126, -122.410087),
	   new LatLng(37.775012, -122.409854),
	   new LatLng(37.775164, -122.409573),
	   new LatLng(37.775498, -122.409180),
	   new LatLng(37.775868, -122.408730),
	   new LatLng(37.776256, -122.408240),
	   new LatLng(37.776519, -122.407928),
	   new LatLng(37.776539, -122.407904),
	   new LatLng(37.776595, -122.407854),
	   new LatLng(37.776853, -122.407547),
	   new LatLng(37.777234, -122.407087),
	   new LatLng(37.777644, -122.406558),
	   new LatLng(37.778066, -122.406017),
	   new LatLng(37.778468, -122.405499),
	   new LatLng(37.778866, -122.404995),
	   new LatLng(37.779295, -122.404455),
	   new LatLng(37.779695, -122.403950),
	   new LatLng(37.779982, -122.403584),
	   new LatLng(37.780295, -122.403223),
	   new LatLng(37.780664, -122.402766),
	   new LatLng(37.781043, -122.402288),
	   new LatLng(37.781399, -122.401823),
	   new LatLng(37.781727, -122.401407),
	   new LatLng(37.781853, -122.401247),
	   new LatLng(37.781894, -122.401195),
	   new LatLng(37.782076, -122.400977),
	   new LatLng(37.782338, -122.400603),
	   new LatLng(37.782666, -122.400133),
	   new LatLng(37.783048, -122.399634),
	   new LatLng(37.783450, -122.399198),
	   new LatLng(37.783791, -122.398998),
	   new LatLng(37.784177, -122.398959),
	   new LatLng(37.784388, -122.398971),
	   new LatLng(37.784404, -122.399128),
	   new LatLng(37.784586, -122.399524),
	   new LatLng(37.784835, -122.399927),
	   new LatLng(37.785116, -122.400307),
	   new LatLng(37.785282, -122.400539),
	   new LatLng(37.785346, -122.400692),
	   new LatLng(37.765769, -122.407201),
	   new LatLng(37.765790, -122.407414),
	   new LatLng(37.765802, -122.407755),
	   new LatLng(37.765791, -122.408219),
	   new LatLng(37.765763, -122.408759),
	   new LatLng(37.765726, -122.409348),
	   new LatLng(37.765716, -122.409882),
	   new LatLng(37.765708, -122.410202),
	   new LatLng(37.765705, -122.410253),
	   new LatLng(37.765707, -122.410369),
	   new LatLng(37.765692, -122.410720),
	   new LatLng(37.765699, -122.411215),
	   new LatLng(37.765687, -122.411789),
	   new LatLng(37.765666, -122.412373),
	   new LatLng(37.765598, -122.412883),
	   new LatLng(37.765543, -122.413039),
	   new LatLng(37.765532, -122.413125),
	   new LatLng(37.765500, -122.413553),
	   new LatLng(37.765448, -122.414053),
	   new LatLng(37.765388, -122.414645),
	   new LatLng(37.765323, -122.415250),
	   new LatLng(37.765303, -122.415847),
	   new LatLng(37.765251, -122.416439),
	   new LatLng(37.765204, -122.417020),
	   new LatLng(37.765172, -122.417556),
	   new LatLng(37.765164, -122.418075),
	   new LatLng(37.765153, -122.418618),
	   new LatLng(37.765136, -122.419112),
	   new LatLng(37.765129, -122.419378),
	   new LatLng(37.765119, -122.419481),
	   new LatLng(37.765100, -122.419852),
	   new LatLng(37.765083, -122.420349),
	   new LatLng(37.765045, -122.420930),
	   new LatLng(37.764992, -122.421481),
	   new LatLng(37.764980, -122.421695),
	   new LatLng(37.764993, -122.421843),
	   new LatLng(37.764986, -122.422255),
	   new LatLng(37.764975, -122.422823),
	   new LatLng(37.764939, -122.423411),
	   new LatLng(37.764902, -122.424014),
	   new LatLng(37.764853, -122.424576),
	   new LatLng(37.764826, -122.424922),
	   new LatLng(37.764796, -122.425375),
	   new LatLng(37.764782, -122.425869),
	   new LatLng(37.764768, -122.426089),
	   new LatLng(37.764766, -122.426117),
	   new LatLng(37.764723, -122.426276),
	   new LatLng(37.764681, -122.426649),
	   new LatLng(37.782012, -122.404200),
	   new LatLng(37.781574, -122.404911),
	   new LatLng(37.781055, -122.405597),
	   new LatLng(37.780479, -122.406341),
	   new LatLng(37.779996, -122.406939),
	   new LatLng(37.779459, -122.407613),
	   new LatLng(37.778953, -122.408228),
	   new LatLng(37.778409, -122.408839),
	   new LatLng(37.777842, -122.409501),
	   new LatLng(37.777334, -122.410181),
	   new LatLng(37.776809, -122.410836),
	   new LatLng(37.776240, -122.411514),
	   new LatLng(37.775725, -122.412145),
	   new LatLng(37.775190, -122.412805),
	   new LatLng(37.774672, -122.413464),
	   new LatLng(37.774084, -122.414186),
	   new LatLng(37.773533, -122.413636),
	   new LatLng(37.773021, -122.413009),
	   new LatLng(37.772501, -122.412371),
	   new LatLng(37.771964, -122.411681),
	   new LatLng(37.771479, -122.411078),
	   new LatLng(37.770992, -122.410477),
	   new LatLng(37.770467, -122.409801),
	   new LatLng(37.770090, -122.408904),
	   new LatLng(37.769657, -122.408103),
	   new LatLng(37.769132, -122.407276),
	   new LatLng(37.768564, -122.406469),
	   new LatLng(37.767980, -122.405745),
	   new LatLng(37.767380, -122.405299),
	   new LatLng(37.766604, -122.405297),
	   new LatLng(37.765838, -122.405200),
	   new LatLng(37.765139, -122.405139),
	   new LatLng(37.764457, -122.405094),
	   new LatLng(37.763716, -122.405142),
	   new LatLng(37.762932, -122.405398),
	   new LatLng(37.762126, -122.405813),
	   new LatLng(37.761344, -122.406215),
	   new LatLng(37.760556, -122.406495),
	   new LatLng(37.759732, -122.406484),
	   new LatLng(37.758910, -122.406228),
	   new LatLng(37.758182, -122.405695),
	   new LatLng(37.757676, -122.405118),
	   new LatLng(37.757039, -122.404346),
	   new LatLng(37.756335, -122.403719),
	   new LatLng(37.755503, -122.403406),
	   new LatLng(37.754665, -122.403242),
	   new LatLng(37.753837, -122.403172),
	   new LatLng(37.752986, -122.403112),
	   new LatLng(37.751266, -122.403355)
	 };
}
