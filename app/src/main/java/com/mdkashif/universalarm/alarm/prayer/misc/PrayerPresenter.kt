package com.mdkashif.universalarm.alarm.prayer.misc

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.mdkashif.universalarm.alarm.prayer.model.PrayerApiResponse
import com.mdkashif.universalarm.utils.persistence.SharedPrefHolder
import com.patloew.rxlocation.RxLocation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class PrayerPresenter(private val disposable: CompositeDisposable, private val prayerViewCallback: PrayerViewCallback, private val context: Context) : PrayerManager.PrayerPresenterCallback {
    private var prayerManager = PrayerManager(this)

    private var locationRequest: LocationRequest? = null
    private val rxLocation = RxLocation(context)

    fun getPrayerDetails() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)

        disposable.add(
                rxLocation.settings().checkAndHandleResolution(locationRequest!!)
                        .flatMapObservable { this.getAddressObservable(it) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(({ t: Address? ->
                            SharedPrefHolder.getInstance(context).city = t!!.locality
                            SharedPrefHolder.getInstance(context).country = t.countryName
                            prayerManager.getPrayerDetails(disposable, SharedPrefHolder.getInstance(context).city!!, SharedPrefHolder.getInstance(context).country!!)
                        }), { throwable -> Log.e("PrayerPresenter", "Error fetching location/address updates", throwable) })
        )
    }

    private fun getAddressObservable(success: Boolean): Observable<Address>? {
        when {
            success -> when {
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED -> return rxLocation.location().updates(locationRequest!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext { }
                        .flatMap(({ this.getAddressFromLocation(it) }))
            }
            else -> when {
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED -> return rxLocation.location().lastLocation()
                        .doOnSuccess {}
                        .flatMapObservable { this.getAddressFromLocation(it) }
            }
        }
        return null
    }

    private fun getAddressFromLocation(location: Location): Observable<Address> {
        return rxLocation.geocoding().fromLocation(location).toObservable()
                .subscribeOn(Schedulers.io())
    }

    override fun onGetPrayerDetails(prayerApiResponse: PrayerApiResponse?) {
        prayerViewCallback.onPrayerDetailSuccess(prayerApiResponse)
    }

    override fun onError(error: String) {
        prayerViewCallback.onError(error)
    }

    interface PrayerViewCallback {
        fun onPrayerDetailSuccess(prayerApiResponse: PrayerApiResponse?)
        fun onError(error: String)
    }

}