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
import com.materialism.sidebar.ProfilePageActivity
import com.materialism.sidebar.SettingsActivity
import com.materialism.sidebar.SupportActivity

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
      val intent = Intent(activity, ProfilePageActivity::class.java)
      activity.startActivity(intent)
    }

    navSettings.setOnClickListener {
      popupWindow.dismiss()
      val intent = Intent(activity, SettingsActivity::class.java)
      activity.startActivity(intent)
    }

    navSupport.setOnClickListener {
      popupWindow.dismiss()
      val intent = Intent(activity, SupportActivity::class.java)
      activity.startActivity(intent)
    }

    navLogout.setOnClickListener {
      popupWindow.dismiss()
      SessionManager.logout(activity)
      val intent = Intent(activity, LoginActivity::class.java)
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
      activity.startActivity(intent)
      activity.finish()
    }
  }
}
