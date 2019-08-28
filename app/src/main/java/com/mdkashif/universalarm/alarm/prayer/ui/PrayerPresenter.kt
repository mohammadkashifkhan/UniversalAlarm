package com.mdkashif.universalarm.alarm.prayer.ui

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.mdkashif.universalarm.alarm.prayer.misc.PrayerManager
import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.persistence.AppPreferences
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.concurrent.TimeUnit


class PrayerPresenter(private val disposable: CompositeDisposable, private val prayerViewCallback: PrayerViewCallback, private val context: Context) : KoinComponent {
    private val appPreferences: AppPreferences by inject()

    private var prayerManager = PrayerManager()

    private var locationRequest: LocationRequest? = null
    private val rxLocation = RxLocation(context)

    fun getPrayerDetails() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)

        disposable.add(
                rxLocation.settings().checkAndHandleResolution(locationRequest!!)
                        .flatMapObservable { getAddressObservable(it) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(({ t: Address? ->
                            appPreferences.city = t!!.locality
                            appPreferences.country = t.countryName
                            prayerManager.getPrayerDetails(
                                    onSuccess = {
                                        prayerViewCallback.onPrayerDetailSuccess(it)
                                    },
                                    onFailure = {
                                        prayerViewCallback.onError(it)
                                    }
                            )
                        }), { throwable -> Log.e("PrayerPresenter", "Error fetching location/address updates", throwable) })
        )
    }

    private fun getAddressObservable(success: Boolean): Observable<Address>? {
        when {
            success ->
                when {
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED -> return rxLocation.location().updates(locationRequest!!)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext { mLocation = it }
                            .flatMap(({ this.getAddressFromLocation(it) }))
                }
            else ->
                when {
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED -> return rxLocation.location().lastLocation()
                            .doOnSuccess { mLocation = it }
                            .flatMapObservable { this.getAddressFromLocation(it) }
                }
        }
        return null
    }

    companion object {
        private lateinit var mLocation: Location
        fun getLocationContinuously(): Observable<Location> { // For Location Alarms
            return Observable.interval(0, 10, TimeUnit.SECONDS)
                    .flatMap { Observable.just(mLocation) }
        }
    }

    private fun getAddressFromLocation(location: Location): Observable<Address> {
        return rxLocation.geocoding().fromLocation(location).toObservable()
                .subscribeOn(Schedulers.io())
    }

    interface PrayerViewCallback {
        fun onPrayerDetailSuccess(prayerApiResponse: PrayerApiResponse?)
        fun onError(error: String)
    }
}