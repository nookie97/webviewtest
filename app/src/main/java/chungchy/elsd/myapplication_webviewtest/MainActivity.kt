package chungchy.elsd.myapplication_webviewtest

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.*
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {
    var mCurrentActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mCurrentActivity = this
        var webView = findViewById<WebView>(R.id.webview_privacy)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.loadsImagesAutomatically = true
        webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;
            CookieManager.getInstance().setAcceptCookie(true);
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }

        webView.webViewClient = DemoWebViewClient()

        //webView.webChromeClient = chromClient()

        webView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
                ): Boolean {

                val newWebView = WebView(this@MainActivity)
                val webSettings = newWebView.settings
                webSettings.javaScriptEnabled = true

                Log.i("FEWQFEWQ", view!!.url+", "+newWebView.url)

                val dialog = Dialog(this@MainActivity)
                dialog.setContentView(newWebView)

                val params: ViewGroup.LayoutParams = dialog.window!!.attributes
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
                dialog.window!!.attributes = params as WindowManager.LayoutParams
                dialog.show()
                //newWebView.loadUrl("https://www.elsd.co.kr/Api/App/Checkplus/checkplus_webview")

                newWebView.webViewClient = object  : WebViewClient(){
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        url: String
                    ): Boolean {
                        Log.i("shouldOverrideUrlLoading2", url.toString())
                        return false
                    }
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        Log.i("onPageStarted1 : ", "$url")
                        super.onPageStarted(view, url, favicon)
                    }

                override fun onPageFinished(view: WebView?, url: String?) {
                    Log.i("onPageFinished1 : ", "$url")
                    super.onPageFinished(view, url)
                }
            }

            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)


            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
                super.onPermissionRequest(request)
            }
        }

        webView.loadUrl("https://www.elsd.co.kr/Api/App/Checkplus/checkplus_webview")
    }
    class DemoWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
        ): Boolean {
            Log.i("shouldOverrideUrlLoading1", request.toString())
            //웹뷰 내 표준창에서 외부앱(통신사 인증앱)을 호출하려면 intent:// URI를 별도로 처리해줘야 합니다.
            //다음 소스를 적용 해주세요.
//            if (request.startsWith("intent://")) {
//                var intent: Intent? = null
//                try {
//                    intent = Intent.parseUri(
//                            request,
//                            Intent.URI_INTENT_SCHEME
//                    )
//                    //intent?.let { startActivity(it) }
//                } catch (e: URISyntaxException) {
//                    //URI 문법 오류 시 처리 구간
//                } catch (e: ActivityNotFoundException) {
//                    val packageName = intent!!.getPackage()
//                    if (packageName != "") {
//                        // 앱이 설치되어 있지 않을 경우 구글마켓 이동
////                        startActivity(
////                            Intent(
////                                Intent.ACTION_VIEW,
////                                Uri.parse("market://details?id=$packageName")
////                            )
////                        )
//                    }
//                }
//                //return  값을 반드시 true로 해야 합니다.
//                return true
//            } else if (url.startsWith("https://play.google.com/store/apps/details?id=") || url.startsWith(
//                            "market://details?id="
//                    )
//            ) {
//                //표준창 내 앱설치하기 버튼 클릭 시 PlayStore 앱으로 연결하기 위한 로직
//                val uri: Uri = Uri.parse(url)
//                val packageName: String? = uri.getQueryParameter("id")
//                if (packageName != null && packageName != "") {
//                    // 구글마켓 이동
////                    startActivity(
////                        Intent(
////                            Intent.ACTION_VIEW,
////                            Uri.parse("market://details?id=$packageName")
////                        )
////                    )
//                }
//                //return  값을 반드시 true로 해야 합니다.
//                return true
//            }


            //return  값을 반드시 false로 해야 합니다.
            return false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            Log.i("onPageStarted : ", "$url")
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            Log.i("onPageFinished : ", "$url")
            super.onPageFinished(view, url)
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            Log.i("onLoadResource : ", "$url")
            super.onLoadResource(view, url)
        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
        ) {
            Log.i("onReceivedError : ", "${error?.description}")
            super.onReceivedError(view, request, error)
        }

        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
            Log.i("fewq", view?.url)
            handler?.proceed()
            super.onReceivedSslError(view, handler, error)
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
        ): WebResourceResponse? {
            Log.i("shouldInterceptRequest : ", "${request?.url}")
            return super.shouldInterceptRequest(view, request)
        }
    }
}