package mrz.android.manpages

import android.support.v7.app.ActionBarActivity
import android.support.v7.app.ActionBar
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout


public class MainActivity : ActionBarActivity(), NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private var mNavigationDrawerFragment: NavigationDrawerFragment? = null

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private var mTitle: CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super<ActionBarActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNavigationDrawerFragment = getSupportFragmentManager().findFragmentById(
                R.id.navigation_drawer) as NavigationDrawerFragment
        mTitle = getTitle()

        // Set up the drawer.
        mNavigationDrawerFragment!!.setUp(R.id.navigation_drawer, findViewById(R.id.drawer_layout) as DrawerLayout)
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
        return super<ActionBarActivity>.onCreateOptionsMenu(menu)
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

        return super<ActionBarActivity>.onOptionsItemSelected(item)
    }

    /**
     * A placeholder fragment containing a simple view.
     */


}
