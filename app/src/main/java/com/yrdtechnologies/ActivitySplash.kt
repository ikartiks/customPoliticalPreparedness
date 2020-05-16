package com.yrdtechnologies

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.lifecycle.ViewModelProvider
import com.yrdtechnologies.vm.SplashViewModel

class ActivitySplash : ActivityBase() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(SplashViewModel::class.java)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        putInt(Companion.width, width)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if(viewModel.checkIfDataInserted()){
            Thread(Runnable {
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    goAhead()
                }
            }).start()
        }else{
            viewModel.downloadData{
               goAhead()
            }
        }
    }

    private fun goAhead(){
        val intent: Intent
        if(getBoolean(LoginActivity.isUserLoggedIn, false)) {
            intent = Intent(this@ActivitySplash, ActivityLanding::class.java)
        } else {
            intent = Intent(this@ActivitySplash, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }


    companion object {
        const val width = "width"
    }
}