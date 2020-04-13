package hu.bme.onlab.mybrowser.tabs

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import hu.bme.onlab.mybrowser.R
import hu.bme.onlab.mybrowser.WebViewActivity
import hu.bme.onlab.mybrowser.bookmarks__room.h_b_Entity
import hu.bme.onlab.mybrowser.cookies.Cookie_Entity
import kotlinx.android.synthetic.main.fragment_webview.*
import net.gotev.cookiestore.removeAll


class MyWebView_ : Fragment() {
    val startPage = "https://www.google.com/"
    private var list: List<h_b_Entity>? = null
    var cookiesfordeleteName: MutableList<String>? = null
    var cookiesfordeleteValue: MutableList<String>? = null
    var cookiesTogether: MutableList<String>? = null
    var goodFacebook: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as WebViewActivity).setText(startPage)
        initWebView()
        addRefreshListener()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        cookiesfordeleteName = mutableListOf()
        cookiesfordeleteValue = mutableListOf()
        cookiesTogether = mutableListOf()
        webView.settings.setSupportZoom(false)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.pluginState = WebSettings.PluginState.ON_DEMAND
        webView.settings.mediaPlaybackRequiresUserGesture = false
        //webView.getSettings().setMediaPlaybackRequiresUserGesture(false)
        CookieSyncManager.getInstance().startSync()
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

        webView.loadUrl(startPage)
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {

                val cookies = CookieManager.getInstance().getCookie(url)
                if (cookies != null) {
                    Log.e("cookies in a string:", cookies)
                    val temp = cookies.split(";").toTypedArray()

                    temp.forEach {
                        val tempcookie = it.split("=")
                        if (tempcookie.size > 1) {
                            val tempCookieEntity =
                                Cookie_Entity(tempcookie[0], webView.url, tempcookie[1])
                            if (webView.title == "FACEBOOK" || webView.url == "https://m.facebook.com/") {
                                //Log.e(cookiesfordeleteName?.size.toString(),tempcookie[0]+"="+tempcookie[1])
                                cookiesTogether?.add(tempcookie[0] + "=" + tempcookie[1])
                                cookiesfordeleteName?.add(tempcookie[0])
                                cookiesfordeleteValue?.add(tempcookie[1])
                                goodFacebook = webView.url
                            }
                            (activity as WebViewActivity).insertCookie(tempCookieEntity)
                            // Log.e((activity as WebViewActivity).getCookies().size.toString()," a meret")
                        }


                    }
                }


                /*val cookieManager = WebKitSyncCookieManager(
                    createCookieStore(name = "myCookies", persistent = true),
                    CookiePolicy.ACCEPT_ALL
                )*/
                (activity as WebViewActivity).setText(webView.url)
                (activity as WebViewActivity).setTabText(webView.title)
                list?.let { (activity as WebViewActivity).starCheck_LOCAL(it) }
            }

        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)

                if (view is FrameLayout) {
                    (activity as WebViewActivity).setVideo(view)
                }

            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                (activity as WebViewActivity).removeVideo()
            }
        }


        webView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        var URL = webView.url
                        (activity as WebViewActivity).setText(URL)
                        (activity as WebViewActivity).setTabText(webView.title)
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    fun list() {
        val tmp = CookieManager.getInstance()
        cookiesTogether?.forEach {
            Log.e("", it)
        }
        var i = 0
        //Log.e("halok",cookiesfordeleteName.toString())
        cookiesfordeleteName?.forEach {
            tmp.setCookie("https://m.facebook.com/", cookiesTogether?.get(i))
            Log.e("namek", it)
            i++
        }
        /*cookiesfordeleteName?.forEach {
            tmp.setCookie("https://m.facebook.com/", it + "= ")
            Log.e("namek", it)
        }*/

        /* cookiesTogether?.forEach{
             tmp.setCookie("https://m.facebook.com/",it)
         }*/
    }


    fun setUrl(url: String) {
        webView.loadUrl(url)
    }

    fun getUrl(): String {
        return webView.url
    }

    fun canGoBack(): Boolean {
        return webView.canGoBack()
    }

    fun goBack() {
        webView.goBack()
    }

    fun canGoForward(): Boolean {
        return webView.canGoForward()
    }

    fun goForward() {
        webView.goForward()
    }

    fun reload() {
        webView.reload()
    }

    fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    fun getTitle(): String {
        return webView.title
    }

    fun copyBackForwardList(): WebBackForwardList {
        return webView.copyBackForwardList()
    }

    fun setList(list: List<h_b_Entity>?) {
        this.list = list
    }

    fun deleteAllCookies() {
        CookieManager.getInstance().removeAll()
    }

    fun addRefreshListener() {
        webView.viewTreeObserver.addOnScrollChangedListener {
            if (webView.scrollY < 50 && webView.scrollY != 0) {
                (activity as WebViewActivity).setScroll(true)
            } else if (webView.scrollY != 0) {
                (activity as WebViewActivity).setScroll(false)
            }
        }
    }

}
