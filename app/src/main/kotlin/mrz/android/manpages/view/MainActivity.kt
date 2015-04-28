package mrz.android.manpages.view

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import mrz.android.manpages
import mrz.android.manpages.R

public class MainActivity : AppCompatActivity(), NavigationDrawerFragment.NavigationDrawerCallbacks {

    private var mNavigationDrawerFragment: NavigationDrawerFragment? = null

    private var mTitle: CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNavigationDrawerFragment = getSupportFragmentManager().findFragmentById(
                R.id.navigation_drawer) as NavigationDrawerFragment
        mTitle = getTitle()

        // Set up the drawer.
        mNavigationDrawerFragment!!.setUp(R.id.navigation_drawer,
                findViewById(R.id.drawer_layout) as DrawerLayout)
    }

    override fun onNavigationDrawerItemSelected(position: Int) {
        // update the main content by replacing fragments
        val fragmentManager = getSupportFragmentManager()
        fragmentManager.beginTransaction().replace(R.id.container, MainFragment()).commit()
    }

    public fun restoreActionBar() {
        val actionBar = getSupportActionBar()
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD)
        actionBar.setDisplayShowTitleEnabled(true)
        actionBar.setTitle(mTitle)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (!mNavigationDrawerFragment!!.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu)
            restoreActionBar()
            return true
        }
        return super<AppCompatActivity>.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super<AppCompatActivity>.onOptionsItemSelected(item)
    }
}