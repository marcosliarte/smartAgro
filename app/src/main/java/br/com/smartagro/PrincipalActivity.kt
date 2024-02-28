package br.com.smartagro

import ClimaFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import br.com.smartagro.fragments.ConfiguracaoFragment
import br.com.smartagro.fragments.HomeFragment
import br.com.smartagro.fragments.LerNoticiaFragment
import br.com.smartagro.fragments.NoticiasFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class PrincipalActivity : AppCompatActivity(), NewsItemClickListener  {
    private val homeFragment = HomeFragment()
    private val noticiasFragment = NoticiasFragment()
    private val climaFragment = ClimaFragment()
    private val configFragment = ConfiguracaoFragment()
    private val lerNoticiaFragment = LerNoticiaFragment()


    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)

        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        makeCurrentFragment(homeFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> makeCurrentFragment(homeFragment)
                R.id.nav_news -> makeCurrentFragment(noticiasFragment)
                R.id.nav_clima -> makeCurrentFragment(climaFragment)
                R.id.nav_config -> makeCurrentFragment(configFragment)

            }
            true
        }
    }

     fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
    }

    override fun onNewsItemClick(url: String) {
        val bundle = Bundle()
        bundle.putString("url", url)
        lerNoticiaFragment.arguments = bundle

        makeCurrentFragment(lerNoticiaFragment)
    }
}
