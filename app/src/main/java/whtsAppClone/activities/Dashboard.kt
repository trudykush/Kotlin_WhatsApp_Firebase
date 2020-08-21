package whtsAppClone.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.kush.learningkotlin.R
import kotlinx.android.synthetic.main.activity_dashboard.*
import whtsAppClone.adapters.SectionPagerAdapter

class Dashboard : AppCompatActivity() {

    lateinit var username: String
    var sectionPagerAdapter: SectionPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        supportActionBar!!.title = "Dashboard"

        sectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)
        dashViewPagerID.adapter = sectionPagerAdapter
        mainTabs.setupWithViewPager(dashViewPagerID)

        mainTabs.setTabTextColors(Color.WHITE, Color.GREEN)

        if (intent.extras != null) {
            username = intent.extras!!.get("DisplayName").toString()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        if(item != null) {
            if (item.itemId == R.id.logoutId) {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainWhtsappActivity::class.java))
            }
            if (item.itemId == R.id.settingsId) {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
        }

        return true
    }
}