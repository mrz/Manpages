package mrz.android.manpages.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import mrz.android.manpages.R

class WelcomeActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_welcome)

        if(savedInstanceState == null) {
            val fragment: WelcomeFragment = WelcomeFragment()
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit()
        }
    }
}

