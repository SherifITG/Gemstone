package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import com.itgates.ultra.pulpo.cira.CoroutineManager
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.utilities.Utilities.checkOnlineState
import com.itgates.ultra.pulpo.cira.viewModels.CacheViewModel
import com.itgates.ultra.pulpo.cira.viewModels.ServerViewModel
import com.itgates.ultra.pulpo.cira.dataStore.DataStoreService
import com.itgates.ultra.pulpo.cira.dataStore.PreferenceKeys
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.LoginPharmaResponse
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.UserDetailsData
import com.itgates.ultra.pulpo.cira.ui.composeUI.AppBarComposeView
import com.itgates.ultra.pulpo.cira.ui.composeUI.LoginPage
import com.itgates.ultra.pulpo.cira.utilities.GlobalFormats
import com.itgates.ultra.pulpo.cira.utilities.Utilities
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.network.models.responseModels.responses.UserPharmaResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val rememberMe = MutableLiveData(false)
    private val serverViewModel: ServerViewModel by viewModels()
    private val cacheViewModel: CacheViewModel by viewModels()
    private val internetStateFlow = MutableStateFlow(false)
    private val loadingStateFlow = MutableStateFlow(false)
    private val trial = MutableStateFlow(false)

    companion object {
        var accessToken = ""
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        println("----------------------------------------000 ${TimeZone.getDefault().displayName}")
//        println("----------------------------------------000 ${TimeZone.getDefault().id}")
//        println("----------------------------------------000 ${TimeZone.getDefault()}")
//
//        val timeZoneId = "Asia/Riyadh"
//        val actualTime = Utilities.getActualTimeInTimeZone(timeZoneId)
//        println("Current time in $timeZoneId: $actualTime")
//
//        CoroutineManager.getScope().launch {
//            println("----------------------------------------000 ${Utilities.isDeviceClockAccurate()}")
//        }


        setContent {
            PulpoUltraTheme {
                Scaffold(
                    topBar = { AppBarComposeView(text = getString(R.string.app_name)) }
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = padding_16)
                            .padding(horizontal = padding_16)
                        ,
                        color = MaterialTheme.colors.background
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            LoginPage(
                                internetStateFlow,
                                loadingStateFlow,
                                rememberMe
                            ) { username, password ->
                                if (username == "0" && password == "0") {
                                    Utilities.createCustomToast(applicationContext, "Invalid credentials")
                                }
                                else {
                                    this@LoginActivity.loginAction(username, password)
                                }
                            }

//                            serverViewModel.getFile(applicationContext, "presentation.zip", 5L)

////                            val file = File(applicationContext.cacheDir, "pdf1.pdf")
////                            cacheViewModel.saveFileData(ItgFile(5, EmbeddedEntity("file"), "link", file))

//                            val isLoaded = remember {
//                                mutableStateOf(false)
//                            }
//                            var file = File(applicationContext.cacheDir, "2.pdf")
//                            cacheViewModel.loadFileData()
//                            cacheViewModel.fileData.observe(this@LoginActivity) {
//                                file = it.file
//                                isLoaded.value = true
//                            }
//
//                            if (isLoaded.value) {
//                                if (!file.exists()) {
//                                    val inputStream = applicationContext.assets.open("pdf1.pdf")
//                                    val outputStream = FileOutputStream(file)
//                                    inputStream.copyTo(outputStream)
//                                    inputStream.close()
//                                    outputStream.close()
//                                }
//                                PdfViewer(pdfFile = file)
//                            }

//                            val isLoaded = trial.collectAsState()
//                            if (isLoaded.value) {
//                                println("ooooooooooooooooooooooooooooooooooooooooo")
//                                val file = serverViewModel.fileData.value!!
//                                if (!file.exists()) {
//                                    val inputStream = applicationContext.assets.open("pdf1.pdf")
//                                    val outputStream = FileOutputStream(file)
//                                    inputStream.copyTo(outputStream)
//                                    inputStream.close()
//                                    outputStream.close()
//                                }
//                                PdfViewer(context = applicationContext, pdfFile = file)
//                            }


//                            Surface(modifier = Modifier
//                                .height(250.dp)
//                                .background(Color.Red)
//                                .padding(padding_16)
//                            ) {
//                                AndroidView(factory = {
//                                    WebView(it).apply {
//                                        layoutParams = ViewGroup.LayoutParams(
//                                            ViewGroup.LayoutParams.WRAP_CONTENT,
//                                            ViewGroup.LayoutParams.WRAP_CONTENT
//                                        )
//                                        webViewClient = WebViewClient()
//                                    }
//                                }, update = {
//                                    val file2 = applicationContext.assets.open("html1.html")
//
//                                    var content = ""
//                                    file2.bufferedReader().use { fileContent ->
//                                        content = fileContent.readText()
//                                        println("${fileContent.readText()}")
//                                    }
//
//                                    it.settings.javaScriptEnabled = true
//                                    it.settings.allowFileAccess = true
//
//                                    println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
//                                    it.loadData(content, "text/html", "UTF-8")
//                                })
//                            }

                        }
                    }
                }
            }
        }

        setObservers()
    }

    private fun setObservers() {
        serverViewModel.authenticationData.observeForever {
            dealWithLoginResponse(it)
        }

        serverViewModel.userData.observeForever {
            loadingStateFlow.value = false
            dealWithUserResponse(it)
        }
    }

    private fun loginAction(username: String, password: String) {
        if (checkOnlineState(applicationContext)) {
            loadingStateFlow.value = true
            serverViewModel.userLoginPharma(username, password)
        }
        else {
            loadingStateFlow.value = true
        }
    }

    private fun dealWithLoginResponse(loginDataResponse: LoginPharmaResponse) {
        if (loginDataResponse.access_token.isNotEmpty()) {
            CoroutineManager.getScope().launch {
                println("-------------------------------------------------- 0000 -----")
                println(loginDataResponse.access_token)
                // TODO save access token
                accessToken = loginDataResponse.access_token

                serverViewModel.fetchUserData(loginDataResponse.access_token)
            }
        }
        else {
//            Utilities.createCustomToast(applicationContext, "There is error with login, Please try again")
            Utilities.createCustomToast(applicationContext, loginDataResponse.Status_Message)
            loadingStateFlow.value = false
        }
    }

    private fun dealWithUserResponse(userDataResponse: UserPharmaResponse) {
        CoroutineManager.getScope().launch {
            val userData = userDataResponse.user
            val dataStoreService = cacheViewModel.getDataStoreService()
            val isNewUser = dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await().isEmpty()
                    || userData.id.toString() != dataStoreService.getDataObjAsync(PreferenceKeys.USER_ID).await()

            if (isNewUser) {
//                    "Are you sure you want to login with different user?"
//                    must ensure that all data deleted
            }

            dealWithAuthData(dataStoreService, userData)
            Utilities.navigateToMainActivity(this@LoginActivity, isNewUser)
        }
    }

    private fun dealWithAuthData(dataStoreService: DataStoreService, userData: UserDetailsData) {
        // safe entrance [same user, or first user to app]
        dataStoreService.setDataObj(
            PreferenceKeys.LAST_LOGIN, GlobalFormats.getFullDate(Locale.getDefault(), Date())
        )
        println("$userData")
        dataStoreService.setDataObj(PreferenceKeys.USER_ID, userData.id.toString())
        dataStoreService.setDataObj(PreferenceKeys.CODE, userData.empCode ?: "0")
        dataStoreService.setDataObj(PreferenceKeys.USERNAME, userData.name)
        dataStoreService.setDataObj(PreferenceKeys.FULL_NAME, userData.fullName)
        dataStoreService.setDataObj(PreferenceKeys.REMEMBER_ME, rememberMe.value.toString())
    }
}