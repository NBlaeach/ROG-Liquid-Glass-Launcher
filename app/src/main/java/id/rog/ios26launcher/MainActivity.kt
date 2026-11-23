package id.rog.ios26launcher

import android.app.Activity
import android.app.WallpaperManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private val bg = Color.rgb(8, 25, 68)
    private val white = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(10, 35, 95)
        window.navigationBarColor = Color.rgb(8, 25, 68)
        window.decorView.systemUiVisibility = 0
        renderHome()
    }

    private fun rounded(color: Int, radius: Float = 28f): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
            setStroke(1, Color.argb(65, 255, 255, 255))
        }

    private fun glass(color: Int = Color.argb(65, 220, 235, 255)): GradientDrawable =
        GradientDrawable(GradientDrawable.Orientation.TL_BR,
            intArrayOf(color, Color.argb(35, 255, 255, 255))).apply {
            cornerRadius = 34f
            setStroke(1, Color.argb(110, 255, 255, 255))
        }

    private fun renderHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 22, 18, 16)
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR,
                intArrayOf(Color.rgb(8, 40, 115), Color.rgb(35, 105, 205), Color.rgb(18, 18, 80)))
        }

        val top = TextView(this).apply {
            text = SimpleDateFormat("EEEE, d MMMM", Locale("id", "ID")).format(Date())
            setTextColor(white); textSize = 17f; gravity = Gravity.CENTER
        }
        root.addView(top, LinearLayout.LayoutParams(-1, 42))

        val clock = TextView(this).apply {
            text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            setTextColor(white); textSize = 54f; gravity = Gravity.CENTER
            typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        }
        root.addView(clock, LinearLayout.LayoutParams(-1, 90))

        val info = TextView(this).apply {
            text = "ROG PHONE 5s  •  LIQUID GLASS"
            setTextColor(white); textSize = 13f; gravity = Gravity.CENTER
            background = glass(); setPadding(12, 16, 12, 16)
        }
        root.addView(info, LinearLayout.LayoutParams(-1, 72).apply { bottomMargin = 14 })

        val grid = GridLayout(this).apply { columnCount = 4 }
        val apps = loadApps()
        val chosen = apps.take(20)
        for (app in chosen) {
            val cell = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(4, 10, 4, 10)
            }
            val icon = ImageView(this).apply {
                setImageDrawable(app.loadIcon(packageManager))
                background = rounded(Color.argb(220, 245, 248, 255), 26f)
                setPadding(13, 13, 13, 13)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
            cell.addView(icon, LinearLayout.LayoutParams(62, 62))
            val label = TextView(this).apply {
                text = app.loadLabel(packageManager).toString()
                setTextColor(white); textSize = 11f; gravity = Gravity.CENTER
                maxLines = 1
            }
            cell.addView(label, LinearLayout.LayoutParams(-1, 28))
            cell.setOnClickListener {
                packageManager.getLaunchIntentForPackage(app.activityInfo.packageName)?.let { startActivity(it) }
            }
            grid.addView(cell, GridLayout.LayoutParams().apply {
                width = 0; height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            })
        }
        root.addView(grid, LinearLayout.LayoutParams(-1, 0, 1f))

        val dock = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            background = glass(Color.argb(100, 220, 235, 255))
            setPadding(8, 10, 8, 10)
        }
        chosen.takeLast(4).forEach { app ->
            val iv = ImageView(this).apply {
                setImageDrawable(app.loadIcon(packageManager))
                setPadding(12, 12, 12, 12)
                background = rounded(Color.argb(220, 250, 250, 255), 24f)
                scaleType = ImageView.ScaleType.FIT_CENTER
                setOnClickListener {
                    packageManager.getLaunchIntentForPackage(app.activityInfo.packageName)?.let { startActivity(it) }
                }
            }
            dock.addView(iv, LinearLayout.LayoutParams(62, 62, 1f).apply {
                setMargins(5, 0, 5, 0)
            })
        }
        root.addView(dock, LinearLayout.LayoutParams(-1, 82))
        setContentView(root)
    }

    private fun loadApps(): List<android.content.pm.ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        @Suppress("DEPRECATION")
        return packageManager.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != packageName }
            .sortedBy { it.loadLabel(packageManager).toString().lowercase() }
    }

    override fun onResume() {
        super.onResume()
        // Rebuild the app grid after returning from an app.
        renderHome()
    }
}
