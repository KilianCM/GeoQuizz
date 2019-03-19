package com.example.geoquizz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuizzGeolocalisation extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>, ActivityCompat.OnRequestPermissionsResultCallback {

    private TextView mNameText;
    static public City mCity = new City();

    private static final String LOG_TAG =
            QuizzGeolocalisation.class.getSimpleName();

    private QuestionLibrary mQuestionLibrary;

    private TextView mScoreView;
    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button mButtonChoice2;
    private Button mButtonChoice3;
    private Button mButtonChoice4;
    private ProgressBar mProgessBar;
    private ImageView mBackground;

    private Object mAnswer;
    private int mScore = 0;
    private int mQuestionNumber = 0;

    //Identifiant de la demande de permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    //Si l'utilisateur a autorisé la localisation ou non
    private Boolean mLocationPermissionGranted = false;
    //Dernière position détéctée
    private Location mLastKnowLocation;
    //Permet de récuperer la position
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Double mLongitude;
    private Double mLatitude;

    private int mQuizzType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quizz_geolocalisation);

        mProgessBar = findViewById(R.id.simpleProgressBar);
        mBackground = findViewById(R.id.imageView);
        mQuestionView = findViewById(R.id.text_question);
        mButtonChoice1 = findViewById(R.id.button_answer1);
        mButtonChoice2 = findViewById(R.id.button_answer2);
        mButtonChoice3 = findViewById(R.id.button_answer3);
        mButtonChoice4 = findViewById(R.id.button_answer4);
        mScoreView = findViewById(R.id.score);
        mNameText = findViewById(R.id.text_city);

        mQuestionView.setVisibility(View.INVISIBLE);
        mButtonChoice1.setVisibility(View.INVISIBLE);
        mButtonChoice2.setVisibility(View.INVISIBLE);
        mButtonChoice3.setVisibility(View.INVISIBLE);
        mButtonChoice4.setVisibility(View.INVISIBLE);
        mScoreView.setVisibility(View.INVISIBLE);
        mNameText.setVisibility(View.INVISIBLE);
        mProgessBar.setVisibility(View.VISIBLE);


        Bundle extras = getIntent().getExtras();

        //set background gradient according to the quizz type (geolocalisation or not)
        if(extras != null) {
            mQuizzType = extras.getInt("QUIZZ_TYPE");
            if (extras.getInt("QUIZZ_TYPE") == 0) {
                mBackground.setImageResource(R.drawable.gradient_bg_red);
                if (getSupportLoaderManager().getLoader(0) != null) {
                    getSupportLoaderManager().initLoader(0, null, this);
                }

                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                this.getLocationPermission();
                this.getDeviceLocation();
            } else {
                mBackground.setImageResource(R.drawable.gradient_bg_green);
                Log.d(LOG_TAG, "lon " + extras.getDouble("LON"));

                callApi(extras.getDouble("LAT"), extras.getDouble("LON"));

            }
        }

        //mTitleText = findViewById(R.id.titleText);
    }

    private void launchQuizz(){
        mScoreView.setText("" + mScore + "/" + mQuestionLibrary.mCorrectAnswers.length);

        updateQuestion();
        mQuestionView.setVisibility(View.VISIBLE);
        mButtonChoice1.setVisibility(View.VISIBLE);
        mButtonChoice2.setVisibility(View.VISIBLE);
        mButtonChoice3.setVisibility(View.VISIBLE);
        mButtonChoice4.setVisibility(View.VISIBLE);
        mScoreView.setVisibility(View.VISIBLE);
        mNameText.setVisibility(View.VISIBLE);
        mProgessBar.setVisibility(View.GONE);

        final Toast toastOK = Toast.makeText(QuizzGeolocalisation.this, "Bonne réponse !", Toast.LENGTH_SHORT);
        toastOK.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 5);


        //Start of Button Listener for Button1
        mButtonChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG,"Reponse : " + mButtonChoice1.getText() + " - Type : " + mButtonChoice1.getText().getClass() + " - Longueur : " + mButtonChoice1.getText().length());
                Log.d(LOG_TAG,"Reponse vraie : " + mAnswer.toString() + " - Type : " + mAnswer.getClass()  + " - Longueur : " + mAnswer.toString().length());
                Log.d(LOG_TAG,"Test : " + (mButtonChoice1.getText().equals(mAnswer.toString())));
                if (mButtonChoice1.getText().equals(mAnswer.toString())) {
                    //bonne réponse
                    toastOK.show();
                    mScore = mScore + 1;
                    updateScore(mScore);
                    updateQuestion();
                } else {
                    //mauvaise réponse
                    Toast toast = Toast.makeText(QuizzGeolocalisation.this, "Mauvaise réponse, c'était " + mAnswer, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 5);
                    toast.show();
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button1

        //Start of Button Listener for Button2
        mButtonChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG,"Reponse : " + mButtonChoice2.getText() + " - Type : " + mButtonChoice2.getText().getClass() + " - Longueur : " + mButtonChoice2.getText().length());
                Log.d(LOG_TAG,"Reponse vraie : " + mAnswer.toString() + " - Type : " + mAnswer.getClass()  + " - Longueur : " + mAnswer.toString().length());
                Log.d(LOG_TAG,"Test : " + (mButtonChoice2.getText().equals(mAnswer.toString())));

                if (mButtonChoice2.getText().equals(mAnswer.toString())) {
                    toastOK.show();
                    mScore = mScore + 1;
                    updateScore(mScore);
                    updateQuestion();

                } else {
                    Toast toast = Toast.makeText(QuizzGeolocalisation.this, "Mauvaise réponse, c'était " + mAnswer, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 5);
                    toast.show();
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button2


        //Start of Button Listener for Button3
        mButtonChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG,"Reponse : " + mButtonChoice3.getText() + " - Type : " + mButtonChoice3.getText().getClass() + " - Longueur : " + mButtonChoice3.getText().length());
                Log.d(LOG_TAG,"Reponse vraie : " + mAnswer.toString() + " - Type : " + mAnswer.getClass()  + " - Longueur : " + mAnswer.toString().length());
                Log.d(LOG_TAG,"Test : " + (mButtonChoice3.getText().equals(mAnswer.toString())));

                if (mButtonChoice3.getText().equals(mAnswer.toString())) {
                    toastOK.show();
                    mScore = mScore + 1;
                    updateScore(mScore);
                    updateQuestion();

                } else {
                    Toast toast = Toast.makeText(QuizzGeolocalisation.this, "Mauvaise réponse, c'était " + mAnswer, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 5);
                    toast.show();
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button3


        //Start of Button Listener for Button4
        mButtonChoice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG,"Reponse : " + mButtonChoice4.getText() + " - Type : " + mButtonChoice4.getText().getClass() + " - Longueur : " + mButtonChoice4.getText().length());
                Log.d(LOG_TAG,"Reponse vraie : " + mAnswer.toString() + " - Type : " + mAnswer.getClass()  + " - Longueur : " + mAnswer.toString().length());
                Log.d(LOG_TAG,"Test : " + (mButtonChoice4.getText().equals(mAnswer.toString())));

                if (mButtonChoice4.getText().equals(mAnswer.toString())) {
                    toastOK.show();
                    mScore = mScore + 1;
                    updateScore(mScore);
                    updateQuestion();

                } else {
                    Toast toast = Toast.makeText(QuizzGeolocalisation.this, "Mauvaise réponse, c'était " + mAnswer, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 5);
                    toast.show();
                    updateQuestion();
                }
            }
        });

        //End of Button Listener for Button4

    }

    private void updateQuestion(){

        if(mQuestionNumber < mQuestionLibrary.mCorrectAnswers.length){
            mQuestionView.setText(mQuestionLibrary.getQuestion(mQuestionNumber));
            mButtonChoice1.setText(mQuestionLibrary.getChoice1(mQuestionNumber).toString());
            mButtonChoice2.setText(mQuestionLibrary.getChoice2(mQuestionNumber).toString());
            mButtonChoice3.setText(mQuestionLibrary.getChoice3(mQuestionNumber).toString());
            mButtonChoice4.setText(mQuestionLibrary.getChoice4(mQuestionNumber).toString());

            mAnswer = mQuestionLibrary.getCorrectAnswer(mQuestionNumber);
            mQuestionNumber++;
            Log.d(LOG_TAG, "---- Question " + mQuestionNumber);
        }
        else{
            Intent intent = new Intent(this, EndQuizzGeolocalisation.class);
            intent.putExtra("CITY_NAME",mCity.getName());
            intent.putExtra("SCORE_TEXT", mScore + "/" + mQuestionLibrary.mCorrectAnswers.length);
            intent.putExtra("SCORE_VALUE", mScore);
            intent.putExtra("QUIZZ_TYPE",mQuizzType);
            startActivity(intent);
        }

    }


    private void updateScore(int point) {
        mScoreView.setText("" + point + "/" + mQuestionLibrary.mCorrectAnswers.length);
    }


    public void callApi(Double latitude, Double longitude){
        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search fields
        // is not empty, start a CityLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()
                && latitude != null && longitude != null) {

            Bundle queryBundle = new Bundle();
            queryBundle.putDouble("longitude", longitude);
            queryBundle.putDouble("latitude", latitude);
            Log.d(LOG_TAG,"LAT : " + latitude + " - LON : " + longitude);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);

        }

        else {
            if (latitude == null || longitude == null) {
                Log.d(LOG_TAG,"Valeurs nulles");

            }
        }
    }


    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Double longitude = null;
        Double latitude = null;

        if (args != null) {
            longitude = args.getDouble("longitude");
            latitude = args.getDouble("latitude");
        }

        return new CityLoader(this, longitude,latitude);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        try {
            // Get the JSONArray of city items.
            //JSONArray itemsArray = jsonObject.getJSONArray("items");
            JSONArray itemsArray = new JSONArray(data);
            Log.d(LOG_TAG,itemsArray.toString());

            // Initialize iterator and results fields.
            int i = 0;
            String name = null;

            // Look for results in the items array, exiting when both the
            // title and author are found or when all items have been checked.
            while (i < itemsArray.length() &&
                    (name == null)) {
                // Get the current item information.
                JSONObject city = itemsArray.getJSONObject(i);

                // Try to get the name of the city,
                // catch if either field is empty and move on.
                try {
                    name = city.getString("nom");
                    mCity.setName(city.getString("nom"));
                    mCity.setCode(city.getString("code"));
                    mCity.setSurface(Math.round(city.getDouble("surface")));
                    mCity.setPopulation(city.getString("population"));
                    mCity.setRegion(new Region(city.getJSONObject("region").getString("nom"),city.getJSONObject("region").getString("code")));
                    mCity.setDepartment(new Department(city.getJSONObject("departement").getString("nom"),city.getJSONObject("departement").getString("code")));
                    this.mQuestionLibrary = new QuestionLibrary();
                    this.launchQuizz();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }

            // If both are found, display the result.
            if (name != null) {
                mNameText.setText(name);
            } else {
                // If none are found, update the UI to show failed results.
                mNameText.setText("Ville introuvable");
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(QuizzGeolocalisation.this, "Une erreur est survenue. Votre position est introuvable.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            mNameText.setText("Erreur");
            e.printStackTrace();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(QuizzGeolocalisation.this, "Une erreur est survenue. Veuillez vérifier votre connexion à internet.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    //On reçoit la réponse de l'utilisateur ici
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    this.getDeviceLocation();
                }
                else{
                    Toast.makeText(QuizzGeolocalisation.this, "Impossible de lancer le quizz sans autorisation de localisation ! :(", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    //On demande la permission ici
    protected void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void getDeviceLocation() {
        try {
            final Context context = this;
            //On Check toujours si on a le droit, sinon ca plante.
            if(mLocationPermissionGranted){
                //On demande à notre service de localisation de trouver la position actuelle
                @SuppressLint("MissingPermission") Task locationResult = mFusedLocationProviderClient.getLastLocation();
                //Puisque c'est de l'asynchrone. On ajoute un écouteur qui nous préviens quand la detection est finie.
                //On ne sait jamais si le GPS marche, si il est lent #wiko, le temps de match des satellites.
                // De plus la position peut-etre déduite par pléthore de systèmes : Wifi, Bluetooth, Antenne LTE, etc.)
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        //Une fois la detection effectuée, on vérifie que tout est OK
                        if(task.isSuccessful()){
                            //Si oui on attribue la dernière localisation à notre variable pour la garder dans un coin.
                            mLastKnowLocation = (Location) task.getResult();
                            if(mLastKnowLocation != null){
                                mLongitude = mLastKnowLocation.getLongitude();
                                mLatitude = mLastKnowLocation.getLatitude();
                                callApi(mLatitude, mLongitude);
                            }
                            else{
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(QuizzGeolocalisation.this, "Une erreur est survenue. Votre position est introuvable.", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            //Traitement du cas où on ne trouve pas de position.
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(QuizzGeolocalisation.this, "Une erreur est survenue. Votre position est introuvable.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        } catch (SecurityException e){
            Log.e("TAG", e.getMessage());
            Toast.makeText(QuizzGeolocalisation.this, "Impossible de lancer le quizz sans autorisation de localisation ! :(", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        final Context context = this;
        new AlertDialog.Builder(context)
                .setTitle("Retour au menu")
                .setMessage("Vous êtes sûr de vouloir quitter le quizz ?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

}
