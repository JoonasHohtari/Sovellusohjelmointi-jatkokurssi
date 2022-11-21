package com.example.week2exer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startPositioning(View view) {
        // Aloitetaan käyttäjän sijainnin seuraaminen ja kirjoitetaan tulokset textviewiin

        // Otetaan yhteys paikkatietopalveluun (LocationManager)
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Kysytään käyttäjän tämän hetkinen lokaatio
        // Location on ns. "dangerous" API, joten sen kysyminen edellyttää:
        // 1: Location permissionin manifest tiedostossa
        // 2: Run-time luvan käyttäjältä

        // Tarkistetaan, onko meillä Location Permission -lupa
        // Jos ei ole, niin kysytään käyttäjältä lupa (run-time permission)
        // Jos käyttäjä antaa luvan, voidaan hakea käyttäjän sijainti
        // Jos käyttäjä ei anna lupaa, ilmoitetaan käyttäjälle, että sijaintia ei voida hakea

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Kysytään käyttäjältä lupa
            String[] permissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };
            ActivityCompat.requestPermissions(this, permissions, 0);
            return;
        }
        // Käyttäjä on antanut luvan, joten voidaan hakea sijainti
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Käyttäjän sijainti on muuttunut, kirjoitetaan uusi sijainti textviewiin
        TextView tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvLocation.setText("Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
    }

    public void callPhone(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
            return;
        }
        // Soitetaan puhelu
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+358000000000"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void openMaps(View view) {
        // button onclick open maps app
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:61.49911, 23.78712?q="));
        System.out.println("Opening maps 1");
        System.out.println(intent.resolveActivity(getPackageManager()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            System.out.println("Opening maps 2");
            startActivity(intent);
        }

    }

    public void setAlarm(View view) {
        // button onclick set alarm
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Harjoitus");
        intent.putExtra(AlarmClock.EXTRA_HOUR, 12);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, 0);
        System.out.println("Creating alarm");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            System.out.println("Alarm created");
        }
    }

    public void createText(View view) {
        // button onclick create text
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello World!");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
