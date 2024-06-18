package com.materialism.utils

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.materialism.LoginActivity
import com.materialism.R
import com.materialism.SupportActivity

object DrawerUtils {

  fun setupPopupMenu(activity: Activity, menuIcon: ImageButton) {
    menuIcon.setOnClickListener { showPopupMenu(activity, it) }
  }

  private fun showPopupMenu(activity: Activity, view: View) {
    val inflater = activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val popupView = inflater.inflate(R.layout.burger_menu_layout, null)

    val width = LinearLayout.LayoutParams.WRAP_CONTENT
    val height = LinearLayout.LayoutParams.WRAP_CONTENT
    val focusable = true
    val popupWindow = PopupWindow(popupView, width, height, focusable)

    popupWindow.showAsDropDown(view)

    val navProfile = popupView.findViewById<Button>(R.id.nav_profile)
    val navSettings = popupView.findViewById<Button>(R.id.nav_settings)
    val navSupport = popupView.findViewById<Button>(R.id.nav_support)
    val navLogout = popupView.findViewById<Button>(R.id.nav_logout)

    navProfile.setOnClickListener {
      popupWindow.dismiss()
      // Add navigation logic here
    }

    navSettings.setOnClickListener {
      popupWindow.dismiss()
      // Add navigation logic here
    }

    navSupport.setOnClickListener {
      popupWindow.dismiss()
      val intent = Intent(activity, SupportActivity::class.java)
      activity.startActivity(intent)
    }

    navLogout.setOnClickListener {
      popupWindow.dismiss()
      val intent = Intent(activity, LoginActivity::class.java)
      activity.startActivity(intent)
    }
  }
}
