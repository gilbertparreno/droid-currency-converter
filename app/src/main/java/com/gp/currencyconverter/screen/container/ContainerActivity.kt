package com.gp.currencyconverter.screen.container

import android.os.Bundle
import com.gp.currencyconverter.R
import com.gp.currencyconverter.screen.base.BaseActivity
import com.gp.currencyconverter.screen.main.MainFragment
import kotlinx.android.synthetic.main.toolbar.*

class ContainerActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.addToBackStack(MainFragment.TAG)
            val fragment = MainFragment.newInstance()
            ft.add(R.id.container, fragment, MainFragment.TAG).commit()
        }
    }
}
