package com.stenleone.stanleysfilm.managers.filmControllers.hdRezka

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.DownloadListener
import android.webkit.WebSettings
import android.webkit.WebSettings.PluginState
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.FilmFilmManager.Companion.FAILED_FIND
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.FilmFilmManager.Companion.MAX_FOUND_WITH_DATE_TRY
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.FilmFilmManager.Companion.MAX_TRY
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.FilmFilmManager.Companion.USER_AGENT_WEB_VIEW
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.FindFilmController
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser.JavaScriptParserHdRezkaBlopVideo
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser.JavaScriptParserHdRezkaPage
import com.stenleone.stanleysfilm.managers.filmControllers.filmix.parser.JavaScriptParserHdRezkaVideo
import com.stenleone.stanleysfilm.managers.firebase.FirebaseRemoteConfigManager
import com.stenleone.stanleysfilm.model.entity.FirebaseConfigsEnum
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


class FindFilmHdRezkaController @Inject constructor(
    @ApplicationContext val context: Context,
    firebaseRemoteConfigManager: FirebaseRemoteConfigManager
) : FindFilmController {

    private val controllerJob = Job()
    private val controllerScope = CoroutineScope(Main + controllerJob)

    companion object {
        const val TAG = "FindFilmHdRezka"
    }

    override val progress = MutableLiveData<Int>()
    override val status = MutableLiveData<String>()

    private lateinit var webView: WebView
    private var countFoundTry = 1
    private lateinit var titleMovie: String
    private lateinit var dateMovie: String
    private lateinit var loadVideoCallBack: (String?) -> Unit
    private var webViewInProgress = false
    private val hdRezkaBaseUrl: String = firebaseRemoteConfigManager.getString(FirebaseConfigsEnum.HD_REZKA_BASE_URL)
    private val hdRezkaSearchUrl: String = "${hdRezkaBaseUrl}search/?do=search&subaction=search&q="

    override fun start(
        titleMovie: String,
        dateMovie: String,
        loadVideoCallBack: (String?) -> Unit
    ) {
        webView = WebView(context)
        this.titleMovie = titleMovie
        this.dateMovie = dateMovie
        this.loadVideoCallBack = loadVideoCallBack
        findPageUrlWithWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun findPageUrlWithWebView() {

        progress.postValue(0)
        status.postValue("ищем страницу \"$titleMovie\"")

        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(JavaScriptParserHdRezkaPage { if (it != null) onPageFind(it) else onPageNotFound() }, "PAGE")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {

                Handler(Looper.getMainLooper())
                    .postDelayed(
                        {
                            webView.loadUrl("javascript:window.PAGE.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")

                            status.postValue("парсинг страницы")
                            val oldProgress = progress.value ?: 0
                            val newProgress = 30 + Random.nextInt(-2, 20)

                            if (newProgress > oldProgress) {
                                progress.postValue(newProgress)
                            }
                        }, 1000 // Sometimes the webView does not have time to load, this waiting fixes problem
                    )
            }
        }
        webView.settings.userAgentString = USER_AGENT_WEB_VIEW

        val dataValue: String = if (countFoundTry < MAX_FOUND_WITH_DATE_TRY) {
            "-${dateMovie.substring(0, 4)}"
        } else {
            ""
        }

        Log.v(TAG, "$hdRezkaSearchUrl${titleMovie.replace(" ", "+")}$dataValue")
        webView.loadUrl("$hdRezkaSearchUrl${titleMovie.replace(" ", "+")}$dataValue")

    }

    @SuppressLint("SetJavaScriptEnabled")
    fun findVideoBlopUrlWithWebView(pageUrl: String) {
        webViewInProgress = true
        controllerScope.launch {
            status.postValue("поиск видео")
            val oldProgress = progress.value ?: 0
            val newProgress = 80 + Random.nextInt(-2, 10)

            if (newProgress > oldProgress) {
                progress.postValue(newProgress)
            }

            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            webView.settings.loadsImagesAutomatically = false
            webView.addJavascriptInterface(JavaScriptParserHdRezkaBlopVideo(
                {
                    if (it != null) {
                        findVideoUrlWithWebView(it)
                    } else {
                        loadVideoCallBack(null)
                    }
                }
            ), "BLOP")
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    Handler(Looper.getMainLooper())
                        .postDelayed(
                            {
                                webView.loadUrl("javascript:window.BLOP.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")
                                webViewInProgress = false

                            }, 5000 // Sometimes the webView does not have time to load, this waiting fixes problem
                        )

                }
            }
            webView.settings.userAgentString = USER_AGENT_WEB_VIEW

            webView.loadUrl(pageUrl)
        }
    }

    private fun getBase64StringFromBlobUrl(blobUrl: String): String {
        if(blobUrl.startsWith("blob")){
            return "javascript: var xhr = new XMLHttpRequest();" +
                    "xhr.open('GET', 'YOUR BLOB URL GOES HERE', true);" +
                    "xhr.setRequestHeader('Content-type','application/pdf');" +
                    "xhr.responseType = 'blob';" +
                    "xhr.onload = function(e) {" +
                    "    if (this.status == 200) {" +
                    "        var blobPdf = this.response;" +
                    "        var reader = new FileReader();" +
                    "        reader.readAsDataURL(blobPdf);" +
                    "        reader.onloadend = function() {" +
                    "            base64data = reader.result;" +
                    "            Android.getBase64FromBlobData(base64data);" +
                    "        }" +
                    "    }" +
                    "};" +
                    "xhr.send();"
        }
        return "javascript: console.log('It is not a Blob URL');"
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun findVideoUrlWithWebView(blopVideoUrl: String) {
        webViewInProgress = true
        controllerScope.launch {

            webView.getSettings().setJavaScriptEnabled(true)
            webView.setDownloadListener(DownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->
                webView.loadUrl(
                    getBase64StringFromBlobUrl(blopVideoUrl)                )
            })
            webView.getSettings().setAppCachePath(context.getCacheDir().getAbsolutePath())
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT)
            webView.getSettings().setDatabaseEnabled(true)
            webView.getSettings().setDomStorageEnabled(true)
            webView.getSettings().setUseWideViewPort(true)
            webView.getSettings().setLoadWithOverviewMode(true)
            webView.addJavascriptInterface(JavaScriptParserHdRezkaVideo(loadVideoCallBack), "Android")
            webView.getSettings().setPluginState(PluginState.ON)


            webView.loadUrl(getBase64StringFromBlobUrl(blopVideoUrl))
        }
    }

    private fun onPageFind(link: String) {
        Log.v(TAG, link)
        if (!webViewInProgress) {
            controllerScope.launch {
                findVideoBlopUrlWithWebView(link)
            }
        }
    }

    private fun onPageNotFound() {

        countFoundTry++
        controllerScope.launch {
            if (countFoundTry < MAX_TRY) {
                findPageUrlWithWebView()
                val oldProgress = progress.value ?: 0
                val newProgress = 50 + Random.nextInt(1, 20)

                if (newProgress > oldProgress) {
                    progress.postValue(newProgress)
                }
                status.postValue("перезагрузка страницы")
            } else {
                progress.postValue(FAILED_FIND)
                status.postValue("не удалось найти страницу")
            }
        }

    }

    override fun onDestroy() {
        controllerJob.cancel()
        webView.destroy()
    }
}