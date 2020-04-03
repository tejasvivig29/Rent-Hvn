package com.project.renthvn

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home__screen.*
import kotlinx.android.synthetic.main.bottom_navigation_footer.*
import java.util.*


class Home_Screen : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    //  private val mPager: ViewPager
    lateinit var dotsLayout: LinearLayout
    lateinit var mPager: ViewPager
    var path :IntArray = intArrayOf(R.drawable.rsz_slider_1,R.drawable.rsz_slide_2,R.drawable.rsz_slider_3)
    lateinit var dots:Array<ImageView>
    lateinit var adapter:PageView

    var currentPage: Int = 0
    lateinit var timer:Timer
    val DELAY_MS: Long = 2500
    val PERIOD_MS: Long = 2500

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home__screen)
        bottom_navigation.setSelectedItemId(R.id.navigation_rent)

        //navigation drawer handling [1]
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // navView.setNavigationItemSelectedListener(this)
        nav_view.setNavigationItemSelectedListener(this)

        
        //Page slider [2]
        //toolbar = findViewById(R.id.toolbar) as Toolbar
        mPager = findViewById(R.id.pager) as ViewPager
        adapter = PageView(this,path)
        mPager.adapter = adapter
        dotsLayout = findViewById(R.id.dotsLayout) as LinearLayout

        createDots(0)
        updatePage()

        mPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentPage = position
                createDots(position)
            }
        })
        // Create an ArrayAdapter
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.cityNames, android.R.layout.simple_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {

                (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                getValues(view)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val cart_listener = findViewById(R.id.cart_image) as ImageView
        // set on-click listener
        cart_listener.setOnClickListener {
            // your code to perform when the user clicks on the ImageView
            Toast.makeText(this, "You clicked on cart.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Cart::class.java)
            startActivity(intent)
        }

        val search_listener = findViewById(R.id.search_image) as ImageView
        // set on-click listener
        search_listener.setOnClickListener {
            // your code to perform when the user clicks on the ImageView
            Toast.makeText(this, "You clicked on search.", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, PushDataActivity::class.java)
//            startActivity(intent)
        }

        women_image.setOnClickListener {
            val intent = Intent(this, MenProductsActivity::class.java)
            intent.putExtra("gender", "Women")
            startActivity(intent)
        }


        men_image.setOnClickListener {
            val intent = Intent(this, MenProductsActivity::class.java)
            intent.putExtra("gender", "Men")
            startActivity(intent)
        }

    // bottom navigation handler [3]
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_rent-> {
                    Toast.makeText(this, "Got to rent", Toast.LENGTH_SHORT).show()
                }

                R.id.navigation_donation-> {
                    Toast.makeText(this, "Got to donate", Toast.LENGTH_SHORT).show()
                }

                R.id.navigation_help-> {

                    Toast.makeText(this, "Got to help", Toast.LENGTH_SHORT).show()
                }

            }
            false

        }



    }

    fun createDots(position: Int){
        if(dotsLayout!=null){
            dotsLayout.removeAllViews()
        }

        dots = Array(path.size, {i -> ImageView(this)})

        for(i in 0..path.size - 1){
            dots[i] = ImageView(this)
            if(i == position){
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots))
            }
            else{
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots))
            }
            var params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(3,0,3,0)
            dotsLayout.addView(dots[i], params)

        }

    }

    fun updatePage(){
        var handler = Handler()
        val Update: Runnable = Runnable {
            if(currentPage == path.size){
                currentPage = 0
            }
            mPager.setCurrentItem(currentPage++, true)

        }
        timer = Timer()

        timer.schedule(object: TimerTask(){
            override fun run() {
                handler.post(Update)
            }

        }, DELAY_MS, PERIOD_MS)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_men -> {
                Toast.makeText(this, "Men clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_women -> {
                Toast.makeText(this, "Women clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_update -> {
                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "logout clicked", Toast.LENGTH_SHORT).show()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    fun getValues(view: View) {
        Toast.makeText(this, "Spinner " + spinner.selectedItem.toString(), Toast.LENGTH_LONG).show()
    }

}


/*

[1] "Navigation drawer", Material Design, 2020. [Online]. Available: https://material.io/components/navigation-drawer/. [Accessed: 03- Apr- 2020].

[2]"Android Image Slider Tutorial - Javapapers", Javapapers, 2020. [Online]. Available: https://javapapers.com/android/android-image-slider-tutorial/. [Accessed: 03- Apr- 2020].

[3]"BottomNavigationView  |  Android Developers", Android Developers, 2020. [Online]. Available: https://developer.android.com/reference/android/support/design/widget/BottomNavigationView. [Accessed: 03- Apr- 2020].
