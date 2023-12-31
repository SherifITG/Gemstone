package com.itgates.ultra.pulpo.cira.utilities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.itgates.ultra.pulpo.cira.ui.activities.MainActivity
import com.google.android.gms.location.*
import com.instacart.library.truetime.TrueTime
import java.util.*
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.roomDataBase.entity.generalData.OfflineLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.time.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

object Utilities {
    fun extractZipFile(zipFilePath: String, destinationDirectory: String): Boolean {
        val buffer = ByteArray(1024)
        try {
            val zipInputStream = ZipInputStream(FileInputStream(zipFilePath))
            var zipEntry: ZipEntry? = zipInputStream.nextEntry
            while (zipEntry != null) {
                val file = File(destinationDirectory, zipEntry.name)
                if (zipEntry.isDirectory) {
                    file.mkdirs()
                } else {
                    val parent = file.parentFile
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs()
                    }
                    val fileOutputStream = FileOutputStream(file)
                    var bytesRead = zipInputStream.read(buffer)
                    while (bytesRead != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead)
                        bytesRead = zipInputStream.read(buffer)
                    }
                    fileOutputStream.close()
                }
                zipEntry = zipInputStream.nextEntry
            }

            zipInputStream.closeEntry()
            zipInputStream.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun getAssetFile(context: Context, fileName: String): File? {
        val assetManager = context.assets

        try {
            val inputStream = assetManager.open(fileName)
            val outputFile = File.createTempFile("temp", null, context.cacheDir)

            FileOutputStream(outputFile).use { outputStream ->
                val buffer = ByteArray(4 * 1024) // Adjust the buffer size as per your requirements
                var bytesRead: Int

                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                outputStream.flush()
            }

            return outputFile
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun convertToEnglishDigits(value: String): String {
        return value.replace("١", "1").replace("٢", "2")
            .replace("٣", "3").replace("٤", "4")
            .replace("٥", "5").replace("٦", "6")
            .replace("٧", "7").replace("٨", "8")
            .replace("٩", "9").replace("٠", "0")
            .replace("۱", "1").replace("۲", "2")
            .replace("۳", "3").replace("۴", "4")
            .replace("۵", "5").replace("۶", "6")
            .replace("۷", "7").replace("۸", "8")
            .replace("۹", "9").replace("۰", "0")
    }

    @SuppressLint("MissingInflatedId")
    fun createCustomToast(context: Context, text: String) {
        // Step 3: Create a reference to the LayoutInflater
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Step 4: Create a new Toast object
        val toast = Toast.makeText(context, "Custom Toast", Toast.LENGTH_LONG)

        // Step 5: Inflate your custom layout into the Toast view
        val layout = inflater.inflate(R.layout.custom_toast_layout, null)

        val textView = layout.findViewById<TextView>(R.id.toastText)
        textView.text = text

        toast.view = layout

        // Step 6: Set the duration and show the Toast
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    fun createOfflineLog(context: Context, message: String, actionId: Int, formId: Int): OfflineLog {
        val messageId = when(message) {
            context.getString(R.string.some_error_with_location) -> 0
            context.getString(R.string.missing_location_permission) -> 1

            else -> {105}
        }
        return OfflineLog(messageId, 2, "33", actionId, formId)
    }

    fun isAutomaticTimeEnabled(context: Context): Boolean {
        //        return (Settings.Global.getInt(context.contentResolver, Settings.Global.AUTO_TIME_ZONE) == 1)
        //        return TimeZone.getDefault().id == "Asia/Riyadh"
        return listOf("Africa/Cairo", "Asia/Riyadh").contains(TimeZone.getDefault().id)
    }

    suspend fun isDeviceClockAccurate(): Boolean = withContext(Dispatchers.IO){
        if (!TrueTime.isInitialized())
            TrueTime.build().initialize()
        println("---------- %%%%% ${TrueTime.isInitialized()}")
        println("---------- %%%%% ${TrueTime.now()}")
        println("---------- %%%%% ${Date()}")
        true
    }

    fun getActualTimeInTimeZone(timeZoneId: String): LocalDateTime {
        val zoneId = ZoneId.of(timeZoneId)
        val zonedDateTime = ZonedDateTime.now(zoneId)

        return zonedDateTime.toLocalDateTime()
    }



    private const val LOCATION_PERMISSION_REQUEST_CODE = 101
    fun Context.checkLocationPermission(activity: Activity): Boolean {
        var returnedValue = true
        if (!this.hasLocationPermission()) {
            val permissionsList = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            val shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (shouldShow) {
                ActivityCompat.requestPermissions(activity, permissionsList, LOCATION_PERMISSION_REQUEST_CODE)
            }
            else {
                // User has not been prompted for the permission yet, show the permission dialog
                ActivityCompat.requestPermissions(activity, permissionsList, LOCATION_PERMISSION_REQUEST_CODE)
            }
        }
        else {
            returnedValue = false
        }
        return returnedValue
    }

    fun Context.hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        &&
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun shouldShowPermissionRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        ) || ActivityCompat.shouldShowRequestPermissionRationale(
            activity, Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun checkOnlineState(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }


    private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 0
    fun getPermissions(activity: Activity?) {
        val listPermissionsNeeded: MutableList<String> = ArrayList()

        //int permissionCamera = ContextCompat.checkSelfPermission(activity,
        //        Manifest.permission.CAMERA);
        val locationPermission =
            ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
        val storagePermission =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        val storageWritePermission =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionWindow =
            ContextCompat.checkSelfPermission(activity, Manifest.permission.SYSTEM_ALERT_WINDOW)
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        //        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.CAMERA);
//        }
        if (permissionWindow != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
        }
        if (storageWritePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                activity,
                listPermissionsNeeded.toTypedArray<String>(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
        }
    }

    fun isFromMockLocation(location: Location): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) location.isMock else location.isFromMockProvider
    }

    fun getListOfFakeLocationApps(context: Context): List<String> {
        val runningApps = getRunningApps(context)
        val fakeApps: MutableList<String> = ArrayList()
        for (app in runningApps) {
            if (!isSystemPackage(context, app) && hasAppPermission(
                    context, app, "android.permission.ACCESS_MOCK_LOCATION"
                )
            ) {
                fakeApps.add(getApplicationName(context, app))
            }
        }
        return fakeApps
    }

    // has deprecated syntax
    private fun getRunningApps(context: Context): List<String> {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = HashSet<String>()
        try {
            val runAppsList = activityManager.runningAppProcesses
            for (processInfo in runAppsList) {
                runningApps.addAll(Arrays.asList(*processInfo.pkgList))
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        try {
            //can throw securityException at api<18 (maybe need "android.permission.GET_TASKS")
            val runningTasks = activityManager.getRunningTasks(1000)
            for (taskInfo in runningTasks) {
                runningApps.add(taskInfo.topActivity!!.packageName)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        try {
            val runningServices = activityManager.getRunningServices(1000)
            for (serviceInfo in runningServices) {
                runningApps.add(serviceInfo.service.packageName)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ArrayList(runningApps)
    }

    // has deprecated syntax
    private fun isSystemPackage(context: Context, app: String?): Boolean {
        val packageManager = context.packageManager
        try {
            val pkgInfo = packageManager.getPackageInfo(app!!, 0)
            return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }


    // has deprecated syntax
    private fun hasAppPermission(context: Context, app: String?, permission: String): Boolean {
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        try {
            packageInfo = packageManager.getPackageInfo(app!!, PackageManager.GET_PERMISSIONS)
            if (packageInfo.requestedPermissions != null) {
                for (requestedPermission in packageInfo.requestedPermissions) {
                    if (requestedPermission == permission) {
                        return true
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    // has deprecated syntax
    private fun getApplicationName(context: Context, packageName: String): String {
        var appName = packageName
        val packageManager = context.packageManager
        try {
            appName = packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(
                    packageName,
                    PackageManager.GET_META_DATA
                )
            ).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appName
    }

    fun navigateToMainActivity(context: Context, isNewUser: Boolean = false) {
        PassedValues.mainActivity_isNewUser = isNewUser
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}