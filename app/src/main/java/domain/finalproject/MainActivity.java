package domain.finalproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {
    private static final String REGISTER_URL = "http://final-tjamesa.cloudapps.unc.edu/volleyRegister.php";
    private static final String LOGIN_URL = "http://final-tjamesa.cloudapps.unc.edu/volleyLogin.php";
    private static final String CREATE_SPOT_URL = "http://final-tjamesa.cloudapps.unc.edu/createSpot.php";
    private static final String SEARCH_SPOT_URL = "http://final-tjamesa.cloudapps.unc.edu/searchSpot.php";
    private static final String VIEW_SPOT_URL = "http://final-tjamesa.cloudapps.unc.edu/viewSpot.php";
    private static final String VISITED_SPOTS_URL = "http://final-tjamesa.cloudapps.unc.edu/visitedSpots.php";
    private static final String MARK_VISITED_URL = "http://final-tjamesa.cloudapps.unc.edu/markVisited.php";
    private static final String CREATE_TOUR_URL = "http://final-tjamesa.cloudapps.unc.edu/createTour.php";
    private static final String SEARCH_TOUR_URL = "http://final-tjamesa.cloudapps.unc.edu/searchTour.php";
    private static final String VIEW_TOUR_URL = "http://final-tjamesa.cloudapps.unc.edu/viewTour.php";
    private static final String NEXT_SPOT_URL = "http://final-tjamesa.cloudapps.unc.edu/nextSpot.php";
    private static final String MARK_TAKEN_URL = "http://final-tjamesa.cloudapps.unc.edu/markTaken.php";
    private static final String TAKEN_TOURS_URL = "http://final-tjamesa.cloudapps.unc.edu/takenTours.php";



    String paramUser = "MYSQL_USER";
    String paramPass = "MYSQL_PASSWORD";
    String paramRoot = "MYSQL_ROOT_PASSWORD";
    String user = "user76A";
    String pass = "Qs8kH7wexoV1p0Cy";
    String db = "androiddb";
    String connectionURL = "mysql://mysql:3306/";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";


    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private String[] spotsToAdd = {"","","","","","","","","","",""};
    private int numSpotsToAdd = 0;
    private int currentSpotOnTour = 0;
    private SupportMapFragment mapFragment;

    private EditText loginUsername;
    private EditText loginPassword;
    private Button buttonSendToRegister;
    private Button buttonSendToLogin;
    private Button buttonLogin;
    private String iduser;
    private String sessionUsername;

    private Button buttonSendToCreateSpot;
    private Button buttonCreateSpot;

    private Button buttonSendToSearchSpot;
    private Button buttonSearchSpot;

    private Button buttonSendToCreateTour;

    private Button buttonSendToVisitedSpots;
    private Button buttonSendToTakenTours;
    private Button buttonSendToHome;
    private Button buttonMarkVisited;
    private Button buttonMarkVisitedOnTour;


    private Button addSpotSearch;
    private Button buttonCreateTour;

    private Button buttonSendToSearchTour;
    private Button buttonSearchTour;
    private Button buttonNextSpot;


    private int viewMarker;
    private double viewLat;
    private double viewLong;
    private String viewName;

    private static final String TAG = MainActivity.class.getSimpleName();
    GoogleApiClient c = null;
    TextView tv1 = null;
    LocationRequest req = null;
    private GoogleMap mMap;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private Location mLastKnownLocation;
    private Location storedLocToUpload;

    private Polyline poly;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable("location");
        }

        if (c == null)
            c = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

        //tv1 = (TextView) findViewById(R.id.tv);

        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);

        buttonSendToRegister = (Button) findViewById(R.id.buttonSendToRegister);
        buttonSendToRegister.setOnClickListener(this);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
    }

    protected void onSendToHome() {
        currentSpotOnTour = 0;
        if (mapFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mapFragment).commit();
        }

        setContentView(R.layout.home);

        buttonSendToCreateSpot = (Button) findViewById(R.id.buttonSendToCreateSpot);
        buttonSendToCreateSpot.setOnClickListener(this);
        buttonSendToSearchSpot = (Button) findViewById(R.id.buttonSendToSearchSpot);
        buttonSendToSearchSpot.setOnClickListener(this);
        buttonSendToTakenTours = (Button) findViewById(R.id.buttonSendToTakenTours);
        buttonSendToTakenTours.setOnClickListener(this);
        buttonSendToVisitedSpots = (Button) findViewById(R.id.buttonSendToVisitedSpots);
        buttonSendToVisitedSpots.setOnClickListener(this);
        buttonSendToCreateTour = (Button) findViewById(R.id.buttonSendToCreateTour);
        buttonSendToCreateTour.setOnClickListener(this);
        buttonSendToSearchTour = (Button) findViewById(R.id.buttonSendToSearchTour);
        buttonSendToSearchTour.setOnClickListener(this);
    }

    protected void onSendToLogin() {
        setContentView(R.layout.welcome);

        loginUsername = (EditText) findViewById(R.id.loginUsername);
        loginPassword = (EditText) findViewById(R.id.loginPassword);

        buttonSendToRegister = (Button) findViewById(R.id.buttonSendToRegister);
        buttonSendToRegister.setOnClickListener(this);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);
    }

    protected void onSendToRegister() {
        setContentView(R.layout.register);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
        buttonSendToLogin = (Button) findViewById(R.id.buttonSendToLogin);
        buttonSendToLogin.setOnClickListener(this);
    }

    protected void onSendToCreateSpot() {
        setContentView(R.layout.createspot);

        viewMarker = 0;
        buttonCreateSpot = (Button) findViewById(R.id.buttonCreateSpot);
        buttonCreateSpot.setOnClickListener(this);
        buttonSendToHome = (Button) findViewById(R.id.buttonSendToHomeFromCreateSpot);
        buttonSendToHome.setOnClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    protected void onSendToSearchSpot() {
        setContentView(R.layout.searchspot);

        buttonSearchSpot = (Button) findViewById(R.id.buttonSearchSpot);
        buttonSearchSpot.setOnClickListener(this);
        buttonSendToHome = (Button) findViewById(R.id.sendToHomeFromSearchSpot);
        buttonSendToHome.setOnClickListener(this);
    }

    protected void onSendToViewSpot() {
        setContentView(R.layout.viewspot);

        buttonMarkVisited = (Button) findViewById(R.id.buttonMarkVisited);
        buttonMarkVisited.setOnClickListener(this);
        buttonSendToHome = (Button) findViewById(R.id.sendToHome);
        buttonSendToHome.setOnClickListener(this);

        viewMarker=1;
        /*buttonSearchSpot = (Button) findViewById(R.id.buttonSearchSpot);
        buttonSearchSpot.setOnClickListener(this);*/

    }
    protected void onSendToVisitedSpots() {
        setContentView(R.layout.visitedspots);

        buttonSendToHome = (Button) findViewById(R.id.buttonSendToHome);
        buttonSendToHome.setOnClickListener(this);
    }

    protected void onSendToCreateTour() {
        setContentView(R.layout.createtour);

        addSpotSearch = (Button) findViewById(R.id.addSpotSearch);
        addSpotSearch.setOnClickListener(this);
        buttonCreateTour = (Button) findViewById(R.id.buttonCreateTour);
        buttonCreateTour.setOnClickListener(this);
    }

    protected void onSendToSearchTour() {
        setContentView(R.layout.searchtour);

        buttonSearchTour = (Button) findViewById(R.id.buttonSearchTour);
        buttonSearchTour.setOnClickListener(this);
        buttonSendToHome = (Button) findViewById(R.id.sendToHomeFromSearchTour);
        buttonSendToHome.setOnClickListener(this);
    }

    protected void onSendToViewTour() {
        setContentView(R.layout.viewtour);

        buttonMarkVisitedOnTour = (Button) findViewById(R.id.markVisitedOnTour);
        buttonMarkVisitedOnTour.setOnClickListener(this);
        buttonNextSpot = (Button) findViewById(R.id.buttonNextSpot);
        buttonNextSpot.setOnClickListener(this);
        buttonSendToHome = (Button) findViewById(R.id.sendToHome);
        buttonSendToHome.setOnClickListener(this);

        viewMarker=2;
        /*buttonSearchSpot = (Button) findViewById(R.id.buttonSearchSpot);
        buttonSearchSpot.setOnClickListener(this);*/

    }

    protected void onSendToTakenTours() {
        setContentView(R.layout.takentours);

        buttonSendToHome = (Button) findViewById(R.id.buttonSendToHomeFromTaken);
        buttonSendToHome.setOnClickListener(this);
    }

    private void registerUser(){
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loginUser(){
        final String username = loginUsername.getText().toString().trim();
        final String password = loginPassword.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            iduser = jsonObject.getString("idusers");
                            sessionUsername = jsonObject.getString("username");
                            onSendToHome();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void createSpot(){
        EditText spotName = (EditText) findViewById(R.id.spotName);
        EditText spotDescription = (EditText) findViewById(R.id.spotDescription);
        final String latitude = storedLocToUpload.getLatitude() + "";
        final String longitude = storedLocToUpload.getLongitude() + "";
        final String name = spotName.getText().toString().trim();
        final String description = spotDescription.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_SPOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                        onSendToHome();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("latitude",latitude);
                params.put("longitude",longitude);
                params.put("name",name);
                params.put("description", description);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void searchSpot(){
        EditText searchSpotName = (EditText) findViewById(R.id.searchSpotName);
        final String name = searchSpotName.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_SPOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            LinearLayout ll = (LinearLayout)findViewById(R.id.searchSpotContents);
                            ll.removeAllViews();
                            for(int i = 0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("idspots");
                                String currentName = jsonObject.getString("name");

                                Button myButton = new Button(getApplicationContext());

                                myButton.setText(currentName);
                                myButton.setTag("viewSpot" + id);
                                myButton.setOnClickListener(MainActivity.this);

                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                ll.addView(myButton, lp);
                            }
                            //Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void viewSpot(String id){
        final String idspots = id;
        onSendToViewSpot();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VIEW_SPOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            TextView name = (TextView)findViewById(R.id.viewSpotName);
                            TextView desc = (TextView)findViewById(R.id.viewSpotDescription);
                            name.setText(jsonObject.getString("name"));
                            name.setTag("id" + jsonObject.getString("idspots"));
                            desc.setText(jsonObject.getString("description"));
                            name.invalidate();
                            desc.invalidate();
                            viewLat = jsonObject.getDouble("latitude");
                            viewLong = jsonObject.getDouble("longitude");
                            viewName = jsonObject.getString("name");
                            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(MainActivity.this);
                            mGeoDataClient = Places.getGeoDataClient(MainActivity.this, null);

                            // Construct a PlaceDetectionClient.
                            mPlaceDetectionClient = Places.getPlaceDetectionClient(MainActivity.this, null);

                            // Construct a FusedLocationProviderClient.
                            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            //Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idspots",idspots);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void visitedSpots(){
        onSendToVisitedSpots();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VISITED_SPOTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            TextView name = (TextView)findViewById(R.id.username);
                            name.setText(sessionUsername);
                            name.invalidate();
                            JSONArray jsonArray = new JSONArray(response);
                            LinearLayout ll = (LinearLayout)findViewById(R.id.visitedSpotContents);
                            ll.removeAllViews();
                            for(int i = 0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("idspots");
                                String currentName = jsonObject.getString("name");

                                Button myButton = new Button(getApplicationContext());

                                myButton.setText(currentName);
                                myButton.setTag("viewSpot" + id);
                                myButton.setOnClickListener(MainActivity.this);

                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                ll.addView(myButton, lp);
                            }
                            //Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idusers",iduser);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void markVisited(String id){
        final String idspots = id;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MARK_VISITED_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idspots",idspots);
                params.put("idusers",iduser);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void createTour(){
        EditText tourName = (EditText) findViewById(R.id.tourName);
        final String name = tourName.getText().toString().trim();
        final String spot1 = spotsToAdd[0];
        final String spot2 = spotsToAdd[1];
        final String spot3 = spotsToAdd[2];
        final String spot4 = spotsToAdd[3];
        final String spot5 = spotsToAdd[4];
        final String spot6 = spotsToAdd[5];
        final String spot7 = spotsToAdd[6];
        final String spot8 = spotsToAdd[7];
        final String spot9 = spotsToAdd[8];
        final String spot10 = spotsToAdd[9];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CREATE_TOUR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                        spotsToAdd = new String[10];
                        for(int i=0; i<10; i++){
                            spotsToAdd[i]="";
                        }
                        numSpotsToAdd = 0;
                        onSendToHome();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("spot1",spot1);
                params.put("spot2",spot2);
                params.put("spot3",spot3);
                params.put("spot4",spot4);
                params.put("spot5",spot5);
                params.put("spot6",spot6);
                params.put("spot7",spot7);
                params.put("spot8",spot8);
                params.put("spot9",spot9);
                params.put("spot10",spot10);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addSpotSearch(){
        EditText searchSpotName = (EditText) findViewById(R.id.spotSearch);
        final String name = searchSpotName.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_SPOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            LinearLayout ll = (LinearLayout)findViewById(R.id.addSpotContents);
                            ll.removeAllViews();
                            for(int i = 0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("idspots");
                                String currentName = jsonObject.getString("name");

                                Button myButton = new Button(getApplicationContext());

                                myButton.setText(currentName);
                                myButton.setTag("addSpot" + id);
                                myButton.setOnClickListener(MainActivity.this);

                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                ll.addView(myButton, lp);
                            }
                            //Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void searchTour(){
        EditText searchTourName = (EditText) findViewById(R.id.searchTourName);
        final String name = searchTourName.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_TOUR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            LinearLayout ll = (LinearLayout)findViewById(R.id.searchTourContents);
                            ll.removeAllViews();
                            for(int i = 0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("idtours");
                                String currentName = jsonObject.getString("name");

                                Button myButton = new Button(getApplicationContext());

                                myButton.setText(currentName);
                                myButton.setTag("viewTour" + id);
                                myButton.setOnClickListener(MainActivity.this);

                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                ll.addView(myButton, lp);
                            }
                            //Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name",name);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void viewTour(String id){
        final String idtours = id;
        onSendToViewTour();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VIEW_TOUR_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            TextView name = (TextView)findViewById(R.id.viewTourName);
                            name.setText(jsonObject.getString("name"));
                            name.setTag("id" + jsonObject.getString("idtours"));
                            name.invalidate();
                            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(MainActivity.this);
                            mGeoDataClient = Places.getGeoDataClient(MainActivity.this, null);

                            // Construct a PlaceDetectionClient.
                            mPlaceDetectionClient = Places.getPlaceDetectionClient(MainActivity.this, null);

                            // Construct a FusedLocationProviderClient.
                            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this );
                            //Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idtours",idtours);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void nextSpot(int currentSpot){
        TextView viewTourName = (TextView) findViewById(R.id.viewTourName);
        final String idtours = ((String) viewTourName.getTag()).substring(2);
        final String spotIndex = currentSpot + "";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, NEXT_SPOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if(response.equals("null") || currentSpotOnTour >= 10) {
                                currentSpotOnTour = 0;
                                markTaken();
                                onSendToHome();
                            }
                            else {
                                JSONObject jsonObject = new JSONObject(response);
                                TextView name = (TextView) findViewById(R.id.viewTourSpotName);
                                name.setText(jsonObject.getString("name"));
                                name.setTag("id" + jsonObject.getString("idspots"));
                                name.invalidate();
                                TextView desc = (TextView) findViewById(R.id.viewTourSpotDescription);
                                desc.setText(jsonObject.getString("description"));
                                desc.invalidate();
                                viewLat = jsonObject.getDouble("latitude");
                                viewLong = jsonObject.getDouble("longitude");

                                mMap.clear();
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(viewLat, viewLong))
                                        .title(jsonObject.getString("name")));
                                poly = mMap.addPolyline(new PolylineOptions()
                                        .clickable(true)
                                        .add(
                                                new LatLng(mLastKnownLocation.getLatitude(),
                                                        mLastKnownLocation.getLongitude()),
                                                new LatLng(viewLat, viewLong)));
                                currentSpotOnTour++;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idtours",idtours);
                params.put("spotIndex",spotIndex);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        currentSpot++;
    }

    private void markTaken(){
        TextView viewTourName = (TextView) findViewById(R.id.viewTourName);
        final String idtours = ((String) viewTourName.getTag()).substring(2);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MARK_TAKEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idtours",idtours);
                params.put("idusers",iduser);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void takenTours(){
        onSendToTakenTours();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TAKEN_TOURS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            TextView name = (TextView)findViewById(R.id.usernameTakenTours);
                            name.setText(sessionUsername);
                            name.invalidate();
                            JSONArray jsonArray = new JSONArray(response);
                            LinearLayout ll = (LinearLayout)findViewById(R.id.takenTourContents);
                            ll.removeAllViews();
                            for(int i = 0; i<jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String id = jsonObject.getString("idtours");
                                String currentName = jsonObject.getString("name");

                                Button myButton = new Button(getApplicationContext());

                                myButton.setText(currentName);
                                myButton.setTag("viewTour" + id);
                                myButton.setOnClickListener(MainActivity.this);

                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                ll.addView(myButton, lp);
                            }
                            //Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("idusers",iduser);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonRegister){
            registerUser();
        }
        if(v == buttonLogin) {
            loginUser();
        }
        if(v == buttonSendToRegister) {
           onSendToRegister();
        }
        if(v == buttonSendToLogin) {
            onSendToLogin();
        }
        if(v == buttonSendToCreateSpot) {
            onSendToCreateSpot();
        }
        if(v == buttonCreateSpot) {
            createSpot();
        }
        if(v == buttonSendToSearchSpot) {
            onSendToSearchSpot();
        }
        if(v == buttonSearchSpot) {
            searchSpot();
        }
        if(v == addSpotSearch) {
            addSpotSearch();
        }
        if(v.getTag() != null) {
            String tag = ((String) v.getTag()).substring(0,8);
            if (tag.equals("viewSpot")) {
                viewSpot(v.getTag().toString().substring(8));
            }
            if (tag.equals("viewTour")) {
                viewTour(v.getTag().toString().substring(8));
            }
            tag = ((String) v.getTag()).substring(0,7);
            if (tag.equals("addSpot")) {
                if(numSpotsToAdd < 10) {
                    boolean isRepeat = false;
                    for (int i = 0; i < numSpotsToAdd; i++) {
                        if (spotsToAdd[i].equals(v.getTag().toString().substring(7))) {
                            isRepeat = true;
                        }
                    }
                    if (isRepeat) {
                        Toast.makeText(MainActivity.this, "This spot is already on this tour.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Spot Added", Toast.LENGTH_LONG).show();
                        spotsToAdd[numSpotsToAdd] = (v.getTag().toString().substring(7));
                        numSpotsToAdd++;
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "You have reached the maximum number of spots for this tour.", Toast.LENGTH_LONG).show();
                }
            }
        }
        if(v == buttonSendToHome) {
            onSendToHome();
        }
        if(v == buttonSendToCreateTour) {
            onSendToCreateTour();
        }
        if(v == buttonSendToVisitedSpots) {
            visitedSpots();
        }
        if(v == buttonMarkVisited) {
            TextView nameTV = (TextView) findViewById(R.id.viewSpotName);
            final String idspot = ((String) nameTV.getTag()).substring(2);
            markVisited(idspot);
        }
        if(v == buttonMarkVisitedOnTour) {
            TextView nameTV = (TextView) findViewById(R.id.viewTourSpotName);
            final String idspotOnTour = ((String) nameTV.getTag()).substring(2);
            if(!idspotOnTour.equals("-1")) {
                markVisited(idspotOnTour);
            }
        }
        if(v == buttonCreateTour) {
            createTour();
        }
        if(v == buttonSendToSearchTour) {
            onSendToSearchTour();
        }
        if(v == buttonSearchTour) {
            searchTour();
        }
        if(v == buttonNextSpot) {
            nextSpot(currentSpotOnTour);
        }
        if(v == buttonSendToTakenTours) {
            takenTours();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(c, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if(req != null)
                LocationServices.FusedLocationApi.requestLocationUpdates(c, req, this);
        }
        catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        c.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        c.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
       /* Log.v("Tag", "We are connected");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location loc = LocationServices.FusedLocationApi.getLastLocation(c);
        Log.v("Tag", "Lat = " + loc.getLatitude() + ", Long = " +
                loc.getLongitude());

        Geocoder gc = new Geocoder(this);
        try {
            List<Address> la =
                    gc.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            for (android.location.Address x: la) {
                Log.v("Tag", "Address: " + x.toString());
                tv1.setText("Address: " + x.toString());
                tv1.invalidate();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        req = new LocationRequest();
        req.setInterval(200);
        req.setFastestInterval(50);
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(c, req, this);*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location loc) {
        /*Log.v("Tag", "Lat = " + loc.getLatitude() + ", Long = " +
                loc.getLongitude());
        Geocoder gc = new Geocoder(this);
        try {
            List<android.location.Address> la =
                    gc.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            for (android.location.Address x: la) {
                Log.v("Tag", "Address: " + x.toString());
                tv1.setText("Address: " + x.toString());
                tv1.invalidate();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();
                        storedLocToUpload = mLastKnownLocation;
                        if(viewMarker == 0) {
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(storedLocToUpload.getLatitude(), storedLocToUpload.getLongitude()))
                                    .title("New Spot"));
                        }
                        else if(viewMarker == 1){
                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(viewLat, viewLong))
                                    .title(viewName));
                            Polyline polyline2 = mMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .add(
                                            new LatLng(mLastKnownLocation.getLatitude(),
                                                    mLastKnownLocation.getLongitude()),
                                            new LatLng(viewLat, viewLong)));

                        }
                        else if(viewMarker == 2) {

                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        mMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                }
            });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


}