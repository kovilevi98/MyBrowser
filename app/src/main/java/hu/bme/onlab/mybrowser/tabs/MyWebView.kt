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
import hu.bme.onlab.mybrowser.bookmarks__room.EntityBookMark
import kotlinx.android.synthetic.main.fragment_webview.*
import net.gotev.cookiestore.removeAll
import java.net.URL


class MyWebView : Fragment() {
    private val startPage = "https://www.google.com/"
    private var list: List<EntityBookMark>? = null
    var testArray: MutableList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as WebViewActivity).setText(startPage)
        initWebView()
        addRefreshListener()

    }

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun initWebView() {
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
        CookieManager.getInstance().acceptThirdPartyCookies(webView)


        webView.loadUrl(startPage)
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                val cookies = CookieManager.getInstance().getCookie(url)

                (activity as WebViewActivity).setText(webView.url)
                (activity as WebViewActivity).setTabText(webView.title)
                list?.let { (activity as WebViewActivity).starCheckLOCAL(it) }
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                url: String?
            ): WebResourceResponse? {
                val cookieStr = CookieManager.getInstance()
                    .getCookie(url) // android.webkit.CookieManager
                if (testArray == null)
                    testArray = mutableListOf()
                if (url != null) {
                    val uri = URL(url)
                    if (!testArray!!.contains(uri.host))
                        testArray!!.add(uri.host)

                    (activity as WebViewActivity).addCookie(uri)
                }
                return super.shouldInterceptRequest(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.e("the error:", error.toString())
            }


        }
        val settings = webView.settings
        settings.domStorageEnabled = true
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

        webView.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    val url = webView.url
                    (activity as WebViewActivity).setText(url)
                    (activity as WebViewActivity).setTabText(webView.title)
                }
            }

            v?.onTouchEvent(event) ?: true
        }
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

    fun deleteBackForwardList() {
        webView.clearHistory()
    }

    fun setList(list: List<EntityBookMark>?) {
        this.list = list
    }

    fun deleteAllCookies() {
        CookieManager.getInstance().removeAll()
    }


    private fun addRefreshListener() {
        webView.viewTreeObserver.addOnScrollChangedListener {
            if (webView.scrollY < 50 && webView.scrollY != 0) {
                (activity as WebViewActivity).setScroll(true)
            } else if (webView.scrollY != 0) {
                (activity as WebViewActivity).setScroll(false)
            }
        }
    }

    fun testCookieList() {
        testArray?.forEach {
            val cookieStr = CookieManager.getInstance()
                .getCookie(it)
            Log.e("az url", it)
            if (cookieStr != null)
                Log.e("cookies in string", cookieStr)
        }

    }


}
