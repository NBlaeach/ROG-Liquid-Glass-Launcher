package id.rog.ios26launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.Shader
import android.graphics.LinearGradient
import android.graphics.RectF
import android.graphics.Path
import android.graphics.Paint
import android.graphics.Canvas
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
            setStroke(dp(1), Color.argb(105, 255, 255, 255))
        }

    private fun glass(): GradientDrawable =
        GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                Color.argb(215, 35, 65, 115),
                Color.argb(200, 50, 85, 145),
                Color.argb(220, 25, 45, 90)
            )
        ).apply {
            cornerRadius = dp(30).toFloat()
            setStroke(dp(1), Color.argb(135, 255, 255, 255))
        }

    private fun iconShine(): GradientDrawable =
        GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(
                Color.argb(90, 255, 255, 255),
                Color.argb(0, 255, 255, 255),
                Color.argb(24, 90, 160, 255)
            )
        ).apply {
            cornerRadius = dp(17).toFloat()
            setStroke(dp(1), Color.argb(90, 255, 255, 255))
        }


    private fun liquidIcon(app: ResolveInfo): View {
        val key = (
            app.loadLabel(packageManager).toString() + " " +
            app.activityInfo.packageName
        ).lowercase()

        val kind = when {
            listOf("phone", "telepon", "dialer").any { key.contains(it) } -> "phone"
            listOf("whatsapp", "com.whatsapp").any { key.contains(it) } -> "whatsapp"
            listOf("message", "pesan", "sms").any { key.contains(it) } -> "message"
            listOf("camera", "kamera").any { key.contains(it) } -> "camera"
            listOf("chrome", "browser", "internet").any { key.contains(it) } -> "browser"
            listOf("setting", "pengaturan").any { key.contains(it) } -> "settings"
            listOf("photo", "gallery", "galeri").any { key.contains(it) } -> "photos"
            listOf("clock", "jam", "alarm").any { key.contains(it) } -> "clock"
            listOf("calendar", "kalender").any { key.contains(it) } -> "calendar"
            listOf("music", "spotify").any { key.contains(it) } -> "music"
            listOf("file", "berkas", "manager").any { key.contains(it) } -> "files"
            listOf("contact", "kontak").any { key.contains(it) } -> "contacts"
            listOf("calculator", "kalkulator").any { key.contains(it) } -> "calculator"
            listOf("play store", "playstore").any { key.contains(it) } -> "play"
            else -> "apps"
        }

        val colors = when (kind) {
            "phone", "message" -> intArrayOf(0xFF55D98D.toInt(), 0xFF08764F.toInt())
            "whatsapp" -> intArrayOf(0xFF45E477.toInt(), 0xFF079447.toInt())
            "camera" -> intArrayOf(0xFF8795AA.toInt(), 0xFF30394F.toInt())
            "browser" -> intArrayOf(0xFF55C8F2.toInt(), 0xFF2452BD.toInt())
            "settings", "calculator" -> intArrayOf(0xFF9EADC2.toInt(), 0xFF46536A.toInt())
            "photos" -> intArrayOf(0xFFFF91B8.toInt(), 0xFF8D48C7.toInt())
            "clock" -> intArrayOf(0xFF72C8F5.toInt(), 0xFF354D9B.toInt())
            "calendar" -> intArrayOf(0xFFFF777D.toInt(), 0xFFB82D50.toInt())
            "music" -> intArrayOf(0xFFFF75B7.toInt(), 0xFF8A287E.toInt())
            "files" -> intArrayOf(0xFFFFC45D.toInt(), 0xFFB76B28.toInt())
            "contacts" -> intArrayOf(0xFF70BFFF.toInt(), 0xFF3854B8.toInt())
            "play" -> intArrayOf(0xFF55D5D0.toInt(), 0xFF2859B7.toInt())
            else -> intArrayOf(0xFF668FF0.toInt(), 0xFF293D88.toInt())
        }

        return object : View(this) {
            private val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                style = Paint.Style.FILL
            }
            private val line = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                style = Paint.Style.STROKE
                strokeWidth = 7f
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
            }

            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                val side = minOf(width, height).toFloat()
                if (side <= 0f) return

                val save = canvas.save()
                canvas.translate((width - side) / 2f, (height - side) / 2f)
                canvas.scale(side / 100f, side / 100f)

                val tile = RectF(3f, 3f, 97f, 97f)
                fill.shader = LinearGradient(
                    8f, 4f, 92f, 96f,
                    colors[0], colors[1], Shader.TileMode.CLAMP
                )
                canvas.drawRoundRect(tile, 23f, 23f, fill)
                fill.shader = null

                line.alpha = 180
                line.strokeWidth = 1.5f
                canvas.drawRoundRect(tile, 23f, 23f, line)

                fill.shader = LinearGradient(
                    0f, 4f, 0f, 60f,
                    intArrayOf(0x70FFFFFF, 0x00FFFFFF, 0x12FFFFFF),
                    null, Shader.TileMode.CLAMP
                )
                canvas.drawRoundRect(tile, 23f, 23f, fill)
                fill.shader = null

                line.alpha = 235
                line.strokeWidth = 7f

                when (kind) {
                    "whatsapp" -> {
                        canvas.drawCircle(50f, 48f, 29f, fill)
                        canvas.drawPath(Path().apply {
                            moveTo(29f, 70f)
                            lineTo(25f, 83f)
                            lineTo(41f, 77f)
                            close()
                        }, fill)
                        val phone = Path().apply {
                            moveTo(38f, 33f)
                            cubicTo(33f, 37f, 42f, 57f, 57f, 62f)
                            cubicTo(63f, 64f, 68f, 57f, 65f, 53f)
                            lineTo(56f, 47f)
                            lineTo(50f, 53f)
                            cubicTo(45f, 50f, 42f, 45f, 42f, 41f)
                            lineTo(47f, 37f)
                            close()
                        }
                        canvas.drawPath(phone, line)
                    }
                    "phone" -> {
                        val path = Path().apply {
                            moveTo(31f, 25f)
                            cubicTo(25f, 27f, 24f, 37f, 32f, 51f)
                            cubicTo(41f, 67f, 55f, 76f, 69f, 74f)
                            cubicTo(79f, 72f, 81f, 63f, 75f, 58f)
                            lineTo(64f, 50f)
                            lineTo(54f, 60f)
                            cubicTo(45f, 56f, 40f, 50f, 37f, 42f)
                            lineTo(46f, 33f)
                            lineTo(37f, 23f)
                            close()
                        }
                        canvas.drawPath(path, fill)
                    }
                    "message" -> {
                        canvas.drawRoundRect(20f, 23f, 80f, 68f, 17f, 17f, fill)
                        canvas.drawPath(Path().apply {
                            moveTo(34f, 65f)
                            lineTo(29f, 81f)
                            lineTo(50f, 67f)
                            close()
                        }, fill)
                        fill.color = colors[1]
                        canvas.drawCircle(37f, 46f, 3f, fill)
                        canvas.drawCircle(50f, 46f, 3f, fill)
                        canvas.drawCircle(63f, 46f, 3f, fill)
                        fill.color = Color.WHITE
                    }
                    "camera" -> {
                        canvas.drawRoundRect(17f, 32f, 83f, 75f, 11f, 11f, fill)
                        canvas.drawRoundRect(31f, 25f, 49f, 37f, 4f, 4f, fill)
                        fill.color = colors[1]
                        canvas.drawCircle(51f, 53f, 15f, fill)
                        fill.color = Color.WHITE
                        canvas.drawCircle(51f, 53f, 10f, fill)
                        fill.color = colors[0]
                        canvas.drawCircle(51f, 53f, 5f, fill)
                        fill.color = Color.WHITE
                        canvas.drawCircle(70f, 41f, 3f, fill)
                    }
                    "settings" -> {
                        for (i in 0 until 8) {
                            canvas.save()
                            canvas.rotate(i * 45f, 50f, 50f)
                            canvas.drawRoundRect(45f, 17f, 55f, 37f, 4f, 4f, fill)
                            canvas.restore()
                        }
                        canvas.drawCircle(50f, 50f, 25f, fill)
                        fill.color = colors[1]
                        canvas.drawCircle(50f, 50f, 12f, fill)
                        fill.color = Color.WHITE
                    }
                    "photos" -> {
                        for (i in 0 until 6) {
                            val angle = Math.toRadians(i * 60.0)
                            canvas.drawCircle(
                                50f + (15f * kotlin.math.cos(angle)).toFloat(),
                                50f + (15f * kotlin.math.sin(angle)).toFloat(),
                                11f, fill
                            )
                        }
                        fill.color = colors[1]
                        canvas.drawCircle(50f, 50f, 7f, fill)
                        fill.color = Color.WHITE
                    }
                    "browser" -> {
                        canvas.drawCircle(50f, 50f, 30f, line)
                        canvas.drawOval(36f, 20f, 64f, 80f, line)
                        canvas.drawLine(21f, 43f, 79f, 43f, line)
                        canvas.drawLine(21f, 57f, 79f, 57f, line)
                        val compass = Path().apply {
                            moveTo(63f, 32f)
                            lineTo(54f, 55f)
                            lineTo(36f, 67f)
                            lineTo(45f, 44f)
                            close()
                        }
                        canvas.drawPath(compass, fill)
                    }
                    "clock" -> {
                        canvas.drawCircle(50f, 50f, 31f, line)
                        canvas.drawLine(50f, 50f, 50f, 29f, line)
                        canvas.drawLine(50f, 50f, 67f, 58f, line)
                        canvas.drawCircle(50f, 50f, 4f, fill)
                    }
                    "calendar" -> {
                        canvas.drawRoundRect(22f, 23f, 78f, 78f, 9f, 9f, line)
                        canvas.drawLine(22f, 40f, 78f, 40f, line)
                        canvas.drawLine(36f, 18f, 36f, 31f, line)
                        canvas.drawLine(64f, 18f, 64f, 31f, line)
                        canvas.drawCircle(39f, 53f, 3f, fill)
                        canvas.drawCircle(51f, 53f, 3f, fill)
                        canvas.drawCircle(63f, 53f, 3f, fill)
                        canvas.drawCircle(39f, 66f, 3f, fill)
                        canvas.drawCircle(51f, 66f, 3f, fill)
                    }
                    "music" -> {
                        val path = Path().apply {
                            moveTo(59f, 26f)
                            lineTo(59f, 65f)
                            cubicTo(59f, 77f, 39f, 81f, 37f, 69f)
                            cubicTo(35f, 58f, 49f, 54f, 59f, 59f)
                            moveTo(59f, 26f)
                            lineTo(77f, 22f)
                            lineTo(77f, 60f)
                        }
                        canvas.drawPath(path, line)
                        canvas.drawLine(59f, 34f, 77f, 30f, line)
                    }
                    "files" -> {
                        canvas.drawPath(Path().apply {
                            moveTo(17f, 34f)
                            lineTo(43f, 34f)
                            lineTo(51f, 43f)
                            lineTo(83f, 43f)
                            lineTo(78f, 75f)
                            lineTo(21f, 75f)
                            close()
                        }, fill)
                    }
                    "contacts" -> {
                        canvas.drawCircle(50f, 38f, 13f, fill)
                        canvas.drawArc(22f, 49f, 78f, 91f, 180f, 180f, true, fill)
                    }
                    "calculator" -> {
                        canvas.drawRoundRect(25f, 17f, 75f, 83f, 8f, 8f, line)
                        canvas.drawRoundRect(33f, 25f, 67f, 39f, 3f, 3f, fill)
                        for (y in listOf(50f, 64f, 76f)) {
                            for (x in listOf(37f, 50f, 63f)) {
                                canvas.drawCircle(x, y, 3f, fill)
                            }
                        }
                    }
                    "play" -> {
                        canvas.drawPath(Path().apply {
                            moveTo(34f, 20f)
                            lineTo(79f, 50f)
                            lineTo(34f, 80f)
                            close()
                        }, fill)
                    }
                    else -> {
                        for (y in listOf(36f, 60f)) {
                            for (x in listOf(36f, 60f)) {
                                canvas.drawRoundRect(
                                    x - 8f, y - 8f, x + 8f, y + 8f,
                                    5f, 5f, fill
                                )
                            }
                        }
                    }
                }

                canvas.restoreToCount(save)
            }
        }
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
            isVerticalScrollBarEnabled = false
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
                setPadding(dp(3), dp(4), dp(3), dp(5))
                isClickable = true
                isFocusable = true
                setOnClickListener { launch(app) }
            }

            cell.tag = app.loadLabel(packageManager).toString().lowercase()
            cell.addView(
                liquidIcon(app),
                LinearLayout.LayoutParams(dp(58), dp(58))
            )

            val label = TextView(this).apply {
                text = app.loadLabel(packageManager).toString()
                setTextColor(Color.WHITE)
                textSize = 10.5f
                gravity = Gravity.CENTER
                maxLines = 1
                ellipsize = android.text.TextUtils.TruncateAt.END
            }
            cell.addView(
                label,
                LinearLayout.LayoutParams(-1, dp(24))
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

            tile.addView(
                liquidIcon(app),
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
            setPadding(dp(18), dp(16), dp(18), dp(12))
            background = glass()
        }

        val heading = TextView(this).apply {
            text = "Control Center"
            setTextColor(Color.WHITE)
            textSize = 23f
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        }
        panel.addView(heading)

        val subtitle = TextView(this).apply {
            text = "ROG PHONE 5s  •  LIQUID GLASS"
            setTextColor(Color.argb(190, 255, 255, 255))
            textSize = 11f
            setPadding(0, dp(3), 0, dp(14))
        }
        panel.addView(subtitle)

        fun makeButton(label: String, action: String): Button {
            return Button(this).apply {
                text = label
                setTextColor(Color.WHITE)
                textSize = 13f
                isAllCaps = false
                background = rounded(Color.argb(75, 190, 215, 255), 18f)
                setOnClickListener {
                    try {
                        startActivity(Intent(action))
                    } catch (_: Exception) {
                        startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
                    }
                }
            }
        }

        fun addPair(left: Pair<String, String>, right: Pair<String, String>) {
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }
            row.addView(
                makeButton(left.first, left.second),
                LinearLayout.LayoutParams(0, dp(58), 1f).apply {
                    setMargins(dp(3), dp(3), dp(5), dp(3))
                }
            )
            row.addView(
                makeButton(right.first, right.second),
                LinearLayout.LayoutParams(0, dp(58), 1f).apply {
                    setMargins(dp(5), dp(3), dp(3), dp(3))
                }
            )
            panel.addView(row)
        }

        addPair(
            "◉  Wi-Fi" to android.provider.Settings.ACTION_WIFI_SETTINGS,
            "ᛒ  Bluetooth" to android.provider.Settings.ACTION_BLUETOOTH_SETTINGS
        )
        addPair(
            "✈  Mode pesawat" to android.provider.Settings.ACTION_WIRELESS_SETTINGS,
            "▣  Layar" to android.provider.Settings.ACTION_DISPLAY_SETTINGS
        )
        addPair(
            "♫  Suara" to android.provider.Settings.ACTION_SOUND_SETTINGS,
            "⚙  Pengaturan" to android.provider.Settings.ACTION_SETTINGS
        )

        val brightnessLabel = TextView(this).apply {
            text = "☀  Kecerahan launcher"
            setTextColor(Color.WHITE)
            textSize = 14f
            setPadding(dp(4), dp(12), 0, 0)
        }
        panel.addView(brightnessLabel)

        val brightness = SeekBar(this).apply {
            max = 100
            progress = 50
            progressTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
            thumbTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                    val params = window.attributes
                    params.screenBrightness =
                        if (value == 0) 0.01f else value / 100f
                    window.attributes = params
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        panel.addView(brightness)

        val audio = getSystemService(AUDIO_SERVICE) as android.media.AudioManager
        val volumeLabel = TextView(this).apply {
            text = "♫  Volume media"
            setTextColor(Color.WHITE)
            textSize = 14f
            setPadding(dp(4), dp(8), 0, 0)
        }
        panel.addView(volumeLabel)

        val volume = SeekBar(this).apply {
            max = audio.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC)
            progress = audio.getStreamVolume(android.media.AudioManager.STREAM_MUSIC)
            progressTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
            thumbTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                    if (fromUser) {
                        audio.setStreamVolume(
                            android.media.AudioManager.STREAM_MUSIC,
                            value,
                            0
                        )
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
        panel.addView(volume)

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(panel)
            .setNegativeButton("Tutup", null)
            .create()

        dialog.show()

        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0.38f)

            if (android.os.Build.VERSION.SDK_INT >= 31) {
                addFlags(android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                setBackgroundBlurRadius(dp(28))
            }
        }

        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
            ?.setTextColor(Color.WHITE)
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
