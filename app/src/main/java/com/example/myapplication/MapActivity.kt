package com.example.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var gMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Современный способ запроса разрешений
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Точное местоположение разрешено.
                showUserLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Примерное местоположение разрешено.
                showUserLocation()
            }
            else -> {
                // В разрешении отказано.
                Toast.makeText(this, "Разрешение на геолокацию не предоставлено", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Инициализация клиента для получения геолокации
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Находим Fragment и получаем уведомление, когда карта будет готова
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Этот колбэк вызывается, когда карта готова к использованию.
    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Разрешение уже есть, показываем локацию
                showUserLocation()
            }
            // Можно добавить логику для объяснения, зачем нам нужно разрешение
            // shouldShowRequestPermissionRationale(...) -> { ... }
            else -> {
                // Разрешения нет, запрашиваем его
                locationPermissionRequest.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
        }
    }

    @SuppressLint("MissingPermission") // Мы проверяем разрешение в checkLocationPermission()
    private fun showUserLocation() {
        // Включаем слой с местоположением пользователя на карте (синяя точка)
        gMap?.isMyLocationEnabled = true

        // Получаем последнее известное местоположение
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // Местоположение найдено
                val userLatLng = LatLng(location.latitude, location.longitude)
                // Добавляем маркер
                gMap?.addMarker(MarkerOptions().position(userLatLng).title("Мое местоположение"))
                // Перемещаем камеру на пользователя
                gMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
            } else {
                Toast.makeText(this, "Не удалось определить местоположение", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
