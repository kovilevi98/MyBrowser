package hu.bme.onlab.mybrowser

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.baoyz.widget.PullRefreshLayout
import kotlinx.android.synthetic.main.activity_web_view.*
import kotlinx.android.synthetic.main.toolbar.view.*


class WebViewActivity : AppCompatActivity() {
        lateinit var fullscreenView: View
        private var URL = "https://google.com"
        private var isAlreadyCreated = false
        private val startPage="https://www.google.com/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        refresh()

        setSupportActionBar(findViewById(R.id.toolbar))
        // startLoaderAnimate()
        root_layout.visibility=View.VISIBLE
        fullscreen.visibility=View.GONE

        //webView.settings.javaScriptEnabled =true
        webView.settings.setSupportZoom(false)
        webView.setWebViewClient(WebViewClient())
        webView.getSettings().setJavaScriptEnabled(true)
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        webView.getSettings().setPluginState(WebSettings.PluginState.ON)
        //webView.getSettings().setMediaPlaybackRequiresUserGesture(false)
        webView.setWebChromeClient(WebChromeClient())
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                //       endLoaderAnimate()
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                //endLoaderAnimate()
                showErrorDialog("Error",
                    "No internet connection. Please check your connection.",
                    this@WebViewActivity)
            }
        }
        webView.webChromeClient = object: WebChromeClient(){
            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)

                if(view is FrameLayout){
                    fullscreenView = view
                    fullscreen.addView(fullscreenView)
                    fullscreen.visibility =View.VISIBLE
                    root_layout.visibility = View.GONE
                }



            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                fullscreen.removeView(fullscreenView)
                fullscreen.visibility = View.GONE
                root_layout.visibility = View.VISIBLE
            }
        }
        webView.loadUrl(startPage)
        toolbar.searchbutton.setOnClickListener(){
            var url=toolbar.url.text.toString()
            createurl(url)
            webView.loadUrl(URL)
            toolbar.url.setText(URL)
        }

        toolbar.url.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                var url=toolbar.url.text.toString()
                createurl(url)
                webView.loadUrl(URL)
                toolbar.url.setText(URL)
                return@OnKeyListener true
            }
            false
        })




    }

    private fun refresh() {
        swipeRefreshLayout.setOnRefreshListener {
                       webView.reload()
            swipeRefreshLayout.setRefreshing(false)
        }
    }

    private fun createurl(url: String) {
        if(url.take(3)=="www" || url.take(3)=="WWW")
            URL=("https://"+url)
        else{
            if(url.contains('.') && url.take(12)!="https://www.")
                URL=("https://www."+url)
            else{
                if(url.take(12)=="https://www.")
                    URL=url
                else URL=("https://www.google.com/search?q="+url)
            }

        }


    }

    //setting menu in action bar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_cut -> {
            // User chose the "Print" item
            Toast.makeText(this,"Action cut",Toast.LENGTH_LONG).show()
            true
        }
        android.R.id.home ->{
            Toast.makeText(this,"Home action",Toast.LENGTH_LONG).show()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        if (isAlreadyCreated && !isNetworkAvailable()) {
            isAlreadyCreated = false
            showErrorDialog("Error", "No internet connection. Please check your connection.",
                this@WebViewActivity)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectionManager =
            this@WebViewActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetworkInfo

        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showErrorDialog(title: String, message: String, context: Context) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setNegativeButton("Cancel", { _, _ ->
            this@WebViewActivity.finish()
        })

        dialog.setPositiveButton("Retry", { _, _ ->
            this@WebViewActivity.recreate()
        })
        dialog.create().show()
    }

    /*private fun endLoaderAnimate() {
        loaderImage.clearAnimation()
        loaderImage.visibility = View.GONE
    }

    private fun startLoaderAnimate() {
        val objectAnimator = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val startHeight = 170
                val newHeight = (startHeight + (startHeight + 40) * interpolatedTime).toInt()
                loaderImage.layoutParams.height = newHeight
                loaderImage.requestLayout()
            }

            override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
                super.initialize(width, height, parentWidth, parentHeight)
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        objectAnimator.repeatCount = -1
        objectAnimator.repeatMode = ValueAnimator.REVERSE
        objectAnimator.duration = 1000
        loaderImage.startAnimation(objectAnimator)
    }*/


}
