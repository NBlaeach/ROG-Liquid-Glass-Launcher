package id.rog.ios26launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var clock: TextView
    private lateinit var date: TextView

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()

    private fun rounded(color: Int, radius: Float): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = dp(radius.toInt()).toFloat()
            setStroke(dp(1), Color.argb(65, 255, 255, 255))
        }

    private fun glass(): GradientDrawable =
        GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.argb(115, 255, 255, 255),
                Color.argb(38, 220, 235, 255),
                Color.argb(75, 180, 205, 255)
            )
        ).apply {
            cornerRadius = dp(30).toFloat()
            setStroke(dp(1), Color.argb(115, 255, 255, 255))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(18, 39, 91)
        window.navigationBarColor = Color.rgb(12, 27, 66)
        window.decorView.systemUiVisibility = 0
        renderHome()
    }

    private fun updateClock() {
        val now = Date()
        clock.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(now)
        date.text = SimpleDateFormat(
            "EEEE, d MMMM",
            Locale("id", "ID")
        ).format(now)
    }

    private val clockTick = object : Runnable {
        override fun run() {
            updateClock()
            handler.postDelayed(this, 30_000)
        }
    }

    override fun onResume() {
        super.onResume()
        handler.removeCallbacks(clockTick)
        handler.post(clockTick)
    }

    override fun onPause() {
        handler.removeCallbacks(clockTick)
        super.onPause()
    }

    private fun launch(app: ResolveInfo) {
        val intent = packageManager.getLaunchIntentForPackage(
            app.activityInfo.packageName
        )
        if (intent != null) startActivity(intent)
    }

    private fun renderHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(12), dp(18), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.rgb(12, 36, 91),
                    Color.rgb(42, 104, 190),
                    Color.rgb(20, 39, 100),
                    Color.rgb(8, 19, 55)
                )
            )
        }

        date = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 17f
            gravity = Gravity.CENTER
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        }
        root.addView(date, LinearLayout.LayoutParams(-1, dp(34)))

        clock = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 66f
            gravity = Gravity.CENTER
            typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
            letterSpacing = 0.02f
        }
        root.addView(clock, LinearLayout.LayoutParams(-1, dp(91)))

        val subtitle = TextView(this).apply {
            text = "ROG PHONE 5s  •  LIQUID GLASS"
            setTextColor(Color.WHITE)
            textSize = 13f
            gravity = Gravity.CENTER
            letterSpacing = 0.07f
            background = glass()
            setPadding(dp(12), dp(12), dp(12), dp(12))
        }
        root.addView(
            subtitle,
            LinearLayout.LayoutParams(-1, dp(54)).apply {
                bottomMargin = dp(18)
            }
        )

        val search = EditText(this).apply {
            hint = "⌕  Cari aplikasi"
            setHintTextColor(Color.argb(190, 255, 255, 255))
            setTextColor(Color.WHITE)
            textSize = 15f
            setSingleLine(true)
            setPadding(dp(18), dp(5), dp(18), dp(5))
            background = glass()
        }
        root.addView(
            search,
            LinearLayout.LayoutParams(-1, dp(50)).apply {
                bottomMargin = dp(8)
            }
        )

        val controlButton = Button(this).apply {
            text = "⚙  CONTROL CENTER"
            setTextColor(Color.WHITE)
            background = glass()
            isAllCaps = false
            setOnClickListener { showControlCenter() }
        }
        root.addView(
            controlButton,
            LinearLayout.LayoutParams(-1, dp(48)).apply {
                bottomMargin = dp(10)
            }
        )

        val apps = loadApps()

        val scroll = ScrollView(this).apply {
            isFillViewport = false
            clipToPadding = true
            setPadding(0, dp(10), 0, dp(90))
            overScrollMode = View.OVER_SCROLL_NEVER
        }

        val grid = GridLayout(this).apply {
            columnCount = 4
            useDefaultMargins = false
            alignmentMode = GridLayout.ALIGN_BOUNDS
        }

        apps.forEach { app ->
            val cell = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(3), dp(7), dp(3), dp(9))
                isClickable = true
                isFocusable = true
                setOnClickListener { launch(app) }
            }

            val icon = ImageView(this).apply {
                setImageDrawable(app.loadIcon(packageManager))
                scaleType = ImageView.ScaleType.FIT_CENTER
            }

            cell.tag = app.loadLabel(packageManager).toString().lowercase()
            cell.addView(icon, LinearLayout.LayoutParams(dp(62), dp(62)))

            val label = TextView(this).apply {
                text = app.loadLabel(packageManager).toString()
                setTextColor(Color.WHITE)
                textSize = 11f
                gravity = Gravity.CENTER
                maxLines = 1
                ellipsize = android.text.TextUtils.TruncateAt.END
            }
            cell.addView(
                label,
                LinearLayout.LayoutParams(-1, dp(25))
            )

            grid.addView(
                cell,
                GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(
                        GridLayout.UNDEFINED, 1f
                    )
                }
            )
        }

        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?, start: Int, count: Int, after: Int
            ) {}

            override fun onTextChanged(
                text: CharSequence?, start: Int, before: Int, count: Int
            ) {
                val query = text?.toString()?.trim()?.lowercase().orEmpty()
                for (i in 0 until grid.childCount) {
                    val cell = grid.getChildAt(i)
                    cell.visibility =
                        if (cell.tag.toString().contains(query)) View.VISIBLE
                        else View.GONE
                }
            }

            override fun afterTextChanged(text: android.text.Editable?) {}
        })

        scroll.addView(
            grid,
            android.view.ViewGroup.LayoutParams(-1, -2)
        )
        root.addView(
            scroll,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )

        val dockApps = chooseDockApps(apps)

        val dock = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            background = glass()
            setPadding(dp(10), dp(9), dp(10), dp(9))
            clipToOutline = true
        }

        dockApps.forEach { app ->
            val tile = FrameLayout(this).apply {
                background = rounded(
                    Color.argb(38, 255, 255, 255), 18f
                )
                clipToOutline = true
                contentDescription = app.loadLabel(packageManager).toString()
                setOnClickListener { launch(app) }
            }

            val icon = ImageView(this).apply {
                setImageDrawable(app.loadIcon(packageManager))
                scaleType = ImageView.ScaleType.FIT_CENTER
                setPadding(dp(3), dp(3), dp(3), dp(3))
            }

            tile.addView(
                icon,
                FrameLayout.LayoutParams(dp(52), dp(52), Gravity.CENTER)
            )

            dock.addView(
                tile,
                LinearLayout.LayoutParams(0, dp(60), 1f).apply {
                    setMargins(dp(4), 0, dp(4), 0)
                }
            )
        }

        root.addView(
            dock,
            LinearLayout.LayoutParams(-1, dp(78)).apply {
                topMargin = dp(12)
            }
        )

        setContentView(root)
        root.isFocusableInTouchMode = true
        root.requestFocus()
        scroll.post { scroll.scrollTo(0, 0) }
        updateClock()
    }

    private fun showControlCenter() {
        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(8), dp(18), dp(8))
            background = glass()
        }

        val actions = listOf(
            "Wi-Fi" to android.provider.Settings.ACTION_WIFI_SETTINGS,
            "Bluetooth" to android.provider.Settings.ACTION_BLUETOOTH_SETTINGS,
            "Layar" to android.provider.Settings.ACTION_DISPLAY_SETTINGS,
            "Suara" to android.provider.Settings.ACTION_SOUND_SETTINGS,
            "Pengaturan Android" to android.provider.Settings.ACTION_SETTINGS
        )

        actions.forEach { (label, action) ->
            val button = Button(this).apply {
                text = label
                isAllCaps = false
                setOnClickListener {
                    try {
                        startActivity(Intent(action))
                    } catch (_: Exception) {
                        startActivity(
                            Intent(android.provider.Settings.ACTION_SETTINGS)
                        )
                    }
                }
            }
            panel.addView(
                button,
                LinearLayout.LayoutParams(-1, dp(52)).apply {
                    bottomMargin = dp(5)
                }
            )
        }

        android.app.AlertDialog.Builder(this)
            .setTitle("Control Center")
            .setView(panel)
            .setNegativeButton("Tutup", null)
            .show()
    }

    private fun chooseDockApps(
        apps: List<ResolveInfo>
    ): List<ResolveInfo> {
        val preferred = listOf(
            listOf("telepon", "phone", "dialer"),
            listOf("pesan", "message", "messages"),
            listOf("chrome"),
            listOf("kamera", "camera")
        )

        val selected = mutableListOf<ResolveInfo>()

        preferred.forEach { keywords ->
            val match = apps.firstOrNull { app ->
                val label = app.loadLabel(packageManager)
                    .toString().lowercase()
                keywords.any { label.contains(it) } &&
                    selected.none {
                        it.activityInfo.packageName ==
                            app.activityInfo.packageName
                    }
            }
            if (match != null) selected.add(match)
        }

        apps.forEach { app ->
            if (selected.size < 4 &&
                selected.none {
                    it.activityInfo.packageName ==
                        app.activityInfo.packageName
                }
            ) selected.add(app)
        }

        return selected.take(4)
    }

    private fun loadApps(): List<ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)

        @Suppress("DEPRECATION")
        return packageManager.queryIntentActivities(intent, 0)
            .filter { it.activityInfo.packageName != packageName }
            .sortedBy {
                it.loadLabel(packageManager).toString().lowercase()
            }
    }
}
