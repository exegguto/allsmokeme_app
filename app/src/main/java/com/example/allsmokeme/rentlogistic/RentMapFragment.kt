package com.example.allsmokeme.rentlogistic

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.allsmokeme.OnRentLogisticDataListener
import com.example.allsmokeme.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

//выводит карту для поиска адреса

class RentMapFragment : DialogFragment(), View.OnClickListener, OnMapReadyCallback {
    private val TAG: String = RentLogisticActivity::class.java.simpleName
    private lateinit var mMap: GoogleMap
    var onClickMap: OnRentLogisticDataListener? = null

    // The entry point to the Fused Location Provider.
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val mDefaultLocation = LatLng(55.029657, 82.918660)
    private var latLngMap: LatLng = mDefaultLocation
    private val DEFAULT_ZOOM = 15.0F
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var mLocationPermissionGranted = false
    private val AUTOCOMPLETE_REQUEST_CODE = 1

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var mLastKnownLocation: Location? = null


    // Keys for storing activity state.
    private val KEY_CAMERA_POSITION = "camera_position"
    private val KEY_LOCATION = "location"

    private var supportMapFragment: SupportMapFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.setTitle("Выберите место на карте")
        val v: View = inflater.inflate(R.layout.fragment_rentmap, null)
        v.findViewById<View>(R.id.btnNo).setOnClickListener(this)
        v.findViewById<View>(R.id.btnYes).setOnClickListener(this)
        //Добавляем фрагмент карты
        val fm: FragmentManager? = childFragmentManager
        val ft = fm?.beginTransaction()
        supportMapFragment = fm?.findFragmentByTag("item_map") as? SupportMapFragment
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment()
            ft?.add(R.id.mapFragmentContainer, supportMapFragment!!, "item_map")
            ft?.commit()
        }
        supportMapFragment!!.getMapAsync(this)

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
        return v
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnYes -> onClickMap?.onDialogClickListener(latLngMap)
            else -> {}
        }
        dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (null != supportMapFragment) requireActivity().supportFragmentManager.beginTransaction()
            .remove(supportMapFragment!!)
            .commit()
    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map!!
        mMap.uiSettings.isZoomControlsEnabled = true

        getLocationPermission()

        // Setting a click event handler for the map
        mMap.setOnMapClickListener { latLng -> // Creating a marker
            latLngMap = latLng


            val addresses: List<Address> = Geocoder(
                context,
                Locale.getDefault()
            ).getFromLocation(
                latLng!!.latitude, latLng.longitude,
                1
            )

            val markerOptions = MarkerOptions()
            // Setting the position for the marker
            markerOptions.position(latLng)
            // Setting the title for the marker.
            // This will be displayed on taping the marker
            //markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)
            markerOptions.title(
                addresses[0].thoroughfare.toString()
                    .removePrefix("улица ") + ", " + addresses[0].subThoroughfare.toString()
            )
            // Clears the previously touched position
            mMap.clear()
            // Animating to the touched position
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            // Placing a marker on the touched position
            mMap.addMarker(markerOptions)
        }
    }

    private fun getLocationPermission() {

/* Запросить разрешение на местоположение, чтобы мы могли узнать местоположение
      * устройство. Результат запроса на разрешение обрабатывается обратным вызовом,
      * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            getDeviceLocation()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // Если запрос отменен, массивы результатов пусты.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                    mMap.isMyLocationEnabled = true
                    mMap.uiSettings.isMyLocationButtonEnabled = true
                    getDeviceLocation()
                }
            }
            else ->{
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                mLastKnownLocation = null
            }
        }
    }

    private fun getDeviceLocation() {
        /* Получите лучшее и самое последнее местоположение устройства, которое в редких случаях может быть нулевым
        * случаи, когда локация недоступна.*/
        try {
            if (mLocationPermissionGranted) {
                val locationResult = mFusedLocationProviderClient!!.lastLocation

                locationResult.addOnCompleteListener { task ->
                    if (task.result != null) {
                        // Устанавливаем положение камеры на карте в текущее местоположение устройства.
                        mLastKnownLocation = task.result
                        latLngMap = LatLng(
                            mLastKnownLocation?.latitude!!,
                            mLastKnownLocation?.longitude!!
                        )

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngMap, DEFAULT_ZOOM))
                        val addresses: List<Address> = Geocoder(
                            context,
                            Locale.getDefault()
                        ).getFromLocation(latLngMap.latitude, latLngMap.longitude, 1)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(latLngMap)
                                .title(addresses[0].thoroughfare.toString()
                                    .removePrefix("улица ") + ", " + addresses[0].subThoroughfare.toString())
                        )
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                mDefaultLocation,
                                DEFAULT_ZOOM
                            )
                        )
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }

            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        onClickMap = try {
            activity as OnRentLogisticDataListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                activity.toString() + " must implement onDialogClickListener"
            )
        }
    }

}