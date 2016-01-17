package com.nkyrim.thessapp.ui.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nkyrim.thessapp.R;
import com.nkyrim.thessapp.domain.ObjectivePoi;
import com.nkyrim.thessapp.domain.Poi;
import com.nkyrim.thessapp.persistence.DbHelper;
import com.nkyrim.thessapp.services.GeofenceTransitionsIntentService;
import com.nkyrim.thessapp.ui.base.BaseActivity;
import com.nkyrim.thessapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

import icepick.State;

public class MapActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
	public static final String ARG_POI = "ARG_POI";
	private static final float NEARBY_DIST = 50f;
	private static int F_ALL = 0;
	private static int F_NEARBY = 1;
	private static int F_OBJECTIVES = 2;
	private static int C_ALL = 0;
	private static int C_MONUMENT = 1;
	private static int C_MUSEUM = 2;
	private static int C_PARK = 3;

	private ArrayList<ObjectivePoi> objList;
	private Poi poi;
	@State int selectedFilter = 0;
	@State int selectedCategories = 0;

	private GoogleMap map;
	private GoogleApiClient gapi;

	@Override
	protected int getLayoutResource() {
		return R.layout.activity_map;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if(id == R.id.action_filter) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.filter)
					.setSingleChoiceItems(R.array.choice_filter, selectedFilter, (d, w) -> {
						selectedFilter = w;
						showMarkers();
						d.dismiss();
					})
					.create()
					.show();
			return true;
		} else if(id == R.id.action_categories) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.categories)
					.setSingleChoiceItems(R.array.choice_categories, selectedCategories, (d, w) -> {
						selectedCategories = w;
						showMarkers();
						d.dismiss();
					})
					.create()
					.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// prevent the device from going to standby
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		poi = (Poi) getIntent().getSerializableExtra(ARG_POI);
		objList = (ArrayList<ObjectivePoi>) DbHelper.getAllPoiObjectives();

		buildGoogleApiClient();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setMapToolbarEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		centerMap();
		showMarkers();
	}

	@Override
	protected void onResume() {
		addGeofences();
		super.onResume();
	}

	@Override
	protected void onPause() {
		removeGeofences();
		super.onPause();
	}

	protected void onStart() {
		gapi.connect();
		super.onStart();
	}

	protected void onStop() {
		gapi.disconnect();
		super.onStop();
	}

	private void showMarkers() {
		map.clear();
		List<Poi> list = null;
		if(selectedFilter == F_ALL) {
			if(selectedCategories == C_ALL) list = DbHelper.getPois(-1, false);
			if(selectedCategories == C_MONUMENT) list = DbHelper.getPois(Poi.TYPE_MONUMENT, false);
			if(selectedCategories == C_MUSEUM) list = DbHelper.getPois(Poi.TYPE_MUSEUM, false);
			if(selectedCategories == C_PARK) list = DbHelper.getPois(Poi.TYPE_PARK, false);
			showMarkers(list, false);
		} else if(selectedFilter == F_OBJECTIVES) {
			if(selectedCategories == C_ALL) list = DbHelper.getPois(-1, true);
			if(selectedCategories == C_MONUMENT) list = DbHelper.getPois(Poi.TYPE_MONUMENT, true);
			if(selectedCategories == C_MUSEUM) list = DbHelper.getPois(Poi.TYPE_MUSEUM, true);
			if(selectedCategories == C_PARK) list = DbHelper.getPois(Poi.TYPE_PARK, true);
			showMarkers(list, false);
		} else if(selectedFilter == F_NEARBY) {
			if(selectedCategories == C_ALL) list = DbHelper.getPois(-1, false);
			if(selectedCategories == C_MONUMENT) list = DbHelper.getPois(Poi.TYPE_MONUMENT, false);
			if(selectedCategories == C_MUSEUM) list = DbHelper.getPois(Poi.TYPE_MUSEUM, false);
			if(selectedCategories == C_PARK) list = DbHelper.getPois(Poi.TYPE_PARK, false);
			showMarkers(list, true);
		}
	}

	private void showMarkers(List<Poi> list, boolean nearby) {
		// Colored Markers
		BitmapDescriptor azure = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
		BitmapDescriptor orange = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
		BitmapDescriptor violet = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
		BitmapDescriptor current = null;

		float meters[] = new float[3];

		if(nearby) {
			Location loc = LocationServices.FusedLocationApi.getLastLocation(gapi);

			if(loc == null) {
				Toast.makeText(this, R.string.error_location_service, Toast.LENGTH_SHORT).show();
				return;
			}

			for (Poi p : list) {
				Location.distanceBetween(
						p.getLat(),
						p.getLng(),
						loc.getLatitude(),
						loc.getLongitude(),
						meters);

				if(meters[0] < NEARBY_DIST) {
					if(p.getType() == Poi.TYPE_MONUMENT) current = azure;
					else if(p.getType() == Poi.TYPE_MUSEUM) current = orange;
					else if(p.getType() == Poi.TYPE_PARK) current = violet;
					map.addMarker(new MarkerOptions().icon(current).position(new LatLng(p.getLat(), p.getLng())).title(p.getTitle()));
				}
			}
		} else {
			for (Poi p : list) {
				if(p.getType() == Poi.TYPE_MONUMENT) current = azure;
				else if(p.getType() == Poi.TYPE_MUSEUM) current = orange;
				else if(p.getType() == Poi.TYPE_PARK) current = violet;
				map.addMarker(new MarkerOptions().icon(current).position(new LatLng(p.getLat(), p.getLng())).title(p.getTitle()));
			}
		}
	}

	private void centerMap() {
		if(poi != null && map != null) {
			map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(poi.getLat(), poi.getLng()), 17, 0, 0)));
			map.addMarker(new MarkerOptions().position(new LatLng(poi.getLat(), poi.getLng())).title(poi.getTitle()));
		} else if(poi == null && map != null) {
			Location loc = LocationServices.FusedLocationApi.getLastLocation(gapi);
			if(loc != null) {
				map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(loc.getLatitude(), loc
						.getLongitude()), 17, 0, 0)));
			}
		}
	}

	private synchronized void buildGoogleApiClient() {
		if(gapi == null) {
			gapi = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}
	}

	private PendingIntent getGeofencePendingIntent() {
		Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
		return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private void removeGeofences() {
		if(gapi == null && gapi.isConnected()) LocationServices.GeofencingApi.removeGeofences(gapi, getGeofencePendingIntent());
	}

	private void addGeofences() {
		if(objList == null || objList.isEmpty() || !gapi.isConnected()) return;

		// create geofence list
		List<Geofence> glist = new ArrayList<>();
		for (ObjectivePoi ob : objList) {
			glist.add(new Geofence.Builder()
							  .setRequestId(String.valueOf(ob.getId()))
							  .setCircularRegion(
									  ob.getPoi().getLat(),
									  ob.getPoi().getLng(),
									  Constants.GEOFENCE_RADIUS
							  )
							  .setExpirationDuration(Geofence.NEVER_EXPIRE)
							  .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence
									  .GEOFENCE_TRANSITION_EXIT)
							  .setLoiteringDelay(1000)
							  .build());
		}

		// create geofence request
		GeofencingRequest grequest = new GeofencingRequest.Builder()
				.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL)
				.addGeofences(glist)
				.build();

		// Add the geofences to be monitored be the gservices
		LocationServices.GeofencingApi.addGeofences(
				gapi,
				grequest,
				getGeofencePendingIntent()
		).setResultCallback((status -> {
			if(!status.isSuccess()) {
				Toast.makeText(this, getString(R.string.error_geofences), Toast.LENGTH_LONG).show();
			}
		}));
	}

	@Override
	public void onConnected(@Nullable Bundle bundle) {
		centerMap();
		addGeofences();
	}

	@Override
	public void onConnectionSuspended(int i) {
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Toast.makeText(this, R.string.error_location_service, Toast.LENGTH_LONG).show();
	}
}
