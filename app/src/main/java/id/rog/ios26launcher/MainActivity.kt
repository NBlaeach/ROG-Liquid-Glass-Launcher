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
import android.graphics.drawable.AdaptiveIconDrawable
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
    private var knownPackages: Set<String>? = null

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
            listOf("tiktok", "musically", "ss.android.ugc.trill").any { key.contains(it) } -> "tiktok"
            listOf("binance").any { key.contains(it) } -> "binance"
            listOf("shopee", "com.shopee.id").any { key.contains(it) } -> "shopee"
            listOf("tokopedia", "com.tokopedia.tkpd").any { key.contains(it) } -> "tokopedia"
            listOf("termux").any { key.contains(it) } -> "termux"
            listOf("magisk").any { key.contains(it) } -> "magisk"
            listOf("root explorer", "speedsoftware").any { key.contains(it) } -> "root"
            listOf("aida64").any { key.contains(it) } -> "aida"
            listOf("cpu-z", "cpu_z").any { key.contains(it) } -> "cpuz"
            listOf("devcheck").any { key.contains(it) } -> "devcheck"
            listOf("metatrader", "metaquotes").any { key.contains(it) } -> "mt4"
            listOf("authenticator").any { key.contains(it) } -> "auth"
            listOf("link2sd").any { key.contains(it) } -> "link2sd"
            listOf("zedge").any { key.contains(it) } -> "zedge"
            listOf("pintu").any { key.contains(it) } -> "pintu"
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
            "tiktok" -> intArrayOf(0xFF252535.toInt(), 0xFF050509.toInt())
            "binance" -> intArrayOf(0xFF34302A.toInt(), 0xFF111111.toInt())
            "shopee" -> intArrayOf(0xFFFF9850.toInt(), 0xFFE94B25.toInt())
            "tokopedia" -> intArrayOf(0xFF58D6A0.toInt(), 0xFF078B60.toInt())
            "termux" -> intArrayOf(0xFF303C35.toInt(), 0xFF101812.toInt())
            "magisk" -> intArrayOf(0xFF4B83E8.toInt(), 0xFF172F70.toInt())
            "root" -> intArrayOf(0xFF5279C9.toInt(), 0xFF182D58.toInt())
            "aida" -> intArrayOf(0xFF36B9D8.toInt(), 0xFF17466E.toInt())
            "cpuz" -> intArrayOf(0xFF7188E8.toInt(), 0xFF283A91.toInt())
            "devcheck" -> intArrayOf(0xFF36C9B0.toInt(), 0xFF126B72.toInt())
            "mt4" -> intArrayOf(0xFF4C9BDF.toInt(), 0xFF183D78.toInt())
            "auth" -> intArrayOf(0xFF5599F5.toInt(), 0xFF234A9D.toInt())
            "link2sd" -> intArrayOf(0xFFAB83E8.toInt(), 0xFF54318D.toInt())
            "zedge" -> intArrayOf(0xFFE878D0.toInt(), 0xFF7135A9.toInt())
            "pintu" -> intArrayOf(0xFF55C9D4.toInt(), 0xFF176B9A.toInt())
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

                // Reset Paint agar gaya satu ikon tidak bocor ke ikon berikutnya
                fill.shader = null
                fill.alpha = 255
                fill.style = Paint.Style.FILL
                fill.color = Color.WHITE

                line.shader = null
                line.alpha = 255
                line.style = Paint.Style.STROKE
                line.color = Color.WHITE
                line.strokeWidth = 7f
                line.strokeCap = Paint.Cap.ROUND
                line.strokeJoin = Paint.Join.ROUND

                val side = minOf(width, height).toFloat()
                if (side <= 0f) return

                val save = canvas.save()
                canvas.translate((width - side) / 2f, (height - side) / 2f)
                canvas.scale(side / 100f, side / 100f)

                val tile = RectF(3f, 3f, 97f, 97f)
                val tilePath = Path().apply {
                    moveTo(34f, 3f)
                    cubicTo(18f, 3f, 3f, 18f, 3f, 34f)
                    lineTo(3f, 66f)
                    cubicTo(3f, 82f, 18f, 97f, 34f, 97f)
                    lineTo(66f, 97f)
                    cubicTo(82f, 97f, 97f, 82f, 97f, 66f)
                    lineTo(97f, 34f)
                    cubicTo(97f, 18f, 82f, 3f, 66f, 3f)
                    close()
                }
                // Bayangan tipis untuk memberi kedalaman pada ikon
                fill.shader = null
                fill.color = 0x50000000
                canvas.save()
                canvas.translate(0f, 2f)
                canvas.drawPath(tilePath, fill)
                canvas.restore()
                fill.color = Color.WHITE

                fill.shader = LinearGradient(
                    8f, 4f, 92f, 96f,
                    0xFFFFFFFF.toInt(), 0xFFEAF1FA.toInt(),
                    Shader.TileMode.CLAMP
                )
                canvas.drawPath(tilePath, fill)
                fill.shader = null

                line.alpha = 180
                line.strokeWidth = 1.5f
                canvas.drawPath(tilePath, line)

                fill.shader = LinearGradient(
                    0f, 4f, 0f, 60f,
                    intArrayOf(0x30FFFFFF, 0x00FFFFFF, 0x08FFFFFF),
                    null, Shader.TileMode.CLAMP
                )
                canvas.drawPath(tilePath, fill)
                fill.shader = null

                line.alpha = 235
                 // Logo asli aplikasi Android pada tile iOS 26.
                val appDrawable = app.loadIcon(packageManager)
                val iconSave = canvas.save()
                canvas.clipPath(tilePath)

                if (appDrawable is AdaptiveIconDrawable) {
                    appDrawable.foreground?.let { foreground ->
                        foreground.setBounds(13, 13, 87, 87)
                        foreground.draw(canvas)
                    }
                } else {
                    appDrawable.setBounds(13, 13, 87, 87)
                    appDrawable.draw(canvas)
                }

                canvas.restoreToCount(iconSave)

                fill.color = Color.WHITE
                fill.shader = LinearGradient(
                    0f, 5f, 0f, 70f,
                    intArrayOf(0x18FFFFFF, 0x06FFFFFF, 0x00FFFFFF),
                    null, Shader.TileMode.CLAMP
                )
                canvas.drawPath(tilePath, fill)
                fill.shader = null

                line.color = Color.WHITE
                line.alpha = 125
                line.strokeWidth = 1.5f
                canvas.drawPath(tilePath, line)
                line.alpha = 235
                line.strokeWidth = 7f

                }

                // Bersihkan bayangan sebelum menggambar permukaan kaca
                fill.clearShadowLayer()

                // Liquid Glass overlay untuk semua ikon
                val glassSave = canvas.save()
                canvas.clipPath(tilePath)

                // Kilau kaca utama
                fill.color = Color.WHITE
                fill.shader = LinearGradient(
                    8f, 4f, 92f, 68f,
                    intArrayOf(0x30FFFFFF, 0x12FFFFFF, 0x00FFFFFF),
                    floatArrayOf(0f, 0.42f, 1f),
                    Shader.TileMode.CLAMP
                )
                canvas.drawPath(tilePath, fill)
                fill.shader = null

                // Refleksi diagonal seperti permukaan kaca
                val reflection = Path().apply {
                    moveTo(9f, 9f)
                    cubicTo(27f, 1f, 49f, 5f, 68f, 17f)
                    lineTo(39f, 47f)
                    cubicTo(24f, 39f, 14f, 25f, 9f, 9f)
                    close()
                }
                fill.shader = LinearGradient(
                    10f, 8f, 68f, 48f,
                    intArrayOf(0x28FFFFFF, 0x12FFFFFF, 0x00FFFFFF),
                    floatArrayOf(0f, 0.55f, 1f),
                    Shader.TileMode.CLAMP
                )
                canvas.drawPath(reflection, fill)
                fill.shader = null

                // Pantulan cahaya di bagian bawah
                fill.shader = LinearGradient(
                    0f, 68f, 0f, 96f,
                    intArrayOf(0x00FFFFFF, 0x0AFFFFFF, 0x18FFFFFF),
                    null,
                    Shader.TileMode.CLAMP
                )
                canvas.drawPath(tilePath, fill)
                fill.shader = null

                // Garis tepi kaca berlapis
                line.color = Color.WHITE
                line.alpha = 75
                line.strokeWidth = 1.2f
                canvas.drawPath(tilePath, line)
                line.alpha = 145
                line.strokeWidth = 2f
                canvas.drawPath(tilePath, line)

                // Kilau spekular tipis pada tepi atas kaca
                val topSheen = Path().apply {
                    moveTo(15f, 12f)
                    cubicTo(29f, 5f, 48f, 6f, 63f, 14f)
                }
                line.color = Color.WHITE
                line.alpha = 82
                line.strokeWidth = 1.15f
                line.strokeCap = Paint.Cap.ROUND
                canvas.drawPath(topSheen, line)

                // Pantulan kecil agar permukaan terasa lebih seperti kaca
                fill.color = 0x16FFFFFF
                canvas.drawRoundRect(
                    RectF(19f, 9f, 39f, 10.5f),
                    1f, 1f, fill
                )

                canvas.restoreToCount(glassSave)
                canvas.restoreToCount(save)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER)
        window.setBackgroundDrawable(
            android.graphics.drawable.ColorDrawable(Color.TRANSPARENT)
        )
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

        val currentPackages = loadApps().map {
            it.activityInfo.packageName
        }.toSet()

        if (knownPackages != null && currentPackages != knownPackages) {
            renderHome()
        }
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
            background = android.graphics.drawable.ColorDrawable(Color.TRANSPARENT)
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
        val quickActions = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        quickActions.addView(
            controlButton,
            LinearLayout.LayoutParams(0, dp(48), 1f).apply {
                rightMargin = dp(5)
            }
        )

        val wallpaperButton = Button(this).apply {
            text = "▧  WALLPAPER"
            setTextColor(Color.WHITE)
            background = glass()
            isAllCaps = false
            setOnClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_SET_WALLPAPER))
                } catch (_: Exception) {
                    android.widget.Toast.makeText(
                        this@MainActivity,
                        "Pemilih wallpaper tidak tersedia",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        quickActions.addView(
            wallpaperButton,
            LinearLayout.LayoutParams(0, dp(48), 1f).apply {
                leftMargin = dp(5)
            }
        )

        root.addView(
            quickActions,
            LinearLayout.LayoutParams(-1, dp(48)).apply {
                bottomMargin = dp(10)
            }
        )

        val apps = loadApps()
        knownPackages = apps.map {
            it.activityInfo.packageName
        }.toSet()
        val pageGrids = mutableListOf<GridLayout>()
        val appPages = apps.chunked(16).ifEmpty {
            listOf(emptyList<ResolveInfo>())
        }

        val pageDots = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 17f
            gravity = Gravity.CENTER
            setPadding(0, dp(3), 0, dp(5))
        }

        val pages = object : ViewFlipper(this) {
            private var downX = 0f
            private var downY = 0f
            private val swipeDistance = dp(45)

            override fun onInterceptTouchEvent(event: android.view.MotionEvent): Boolean {
                when (event.actionMasked) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        downX = event.x
                        downY = event.y
                    }
                    android.view.MotionEvent.ACTION_MOVE -> {
                        val dx = event.x - downX
                        val dy = event.y - downY
                        if (kotlin.math.abs(dx) > swipeDistance &&
                            kotlin.math.abs(dx) > kotlin.math.abs(dy) * 1.2f &&
                            childCount > 1
                        ) return true
                    }
                }
                return super.onInterceptTouchEvent(event)
            }

            override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
                when (event.actionMasked) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        downX = event.x
                        downY = event.y
                        return true
                    }
                    android.view.MotionEvent.ACTION_UP -> {
                        val dx = event.x - downX
                        if (kotlin.math.abs(dx) > swipeDistance && childCount > 1) {
                            if (dx < 0 && displayedChild < childCount - 1) {
                                inAnimation = android.view.animation.TranslateAnimation(
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 1f,
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 0f,
                                    0, 0f, 0, 0f
                                ).apply {
                                    duration = 240
                                    interpolator = android.view.animation.DecelerateInterpolator(1.5f)
                                }
                                outAnimation = android.view.animation.TranslateAnimation(
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 0f,
                                    android.view.animation.Animation.RELATIVE_TO_SELF, -1f,
                                    0, 0f, 0, 0f
                                ).apply {
                                    duration = 240
                                    interpolator = android.view.animation.DecelerateInterpolator(1.5f)
                                }
                                showNext()
                            } else if (dx > 0 && displayedChild > 0) {
                                inAnimation = android.view.animation.TranslateAnimation(
                                    android.view.animation.Animation.RELATIVE_TO_SELF, -1f,
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 0f,
                                    0, 0f, 0, 0f
                                ).apply {
                                    duration = 240
                                    interpolator = android.view.animation.DecelerateInterpolator(1.5f)
                                }
                                outAnimation = android.view.animation.TranslateAnimation(
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 0f,
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 1f,
                                    0, 0f, 0, 0f
                                ).apply {
                                    duration = 240
                                    interpolator = android.view.animation.DecelerateInterpolator(1.5f)
                                }
                                showPrevious()
                            }
                            pageDots.text = (0 until childCount).joinToString("  ") {
                                if (it == displayedChild) "●" else "○"
                            }
                        }
                        return true
                    }
                }
                return true
            }
        }.apply {
            setPadding(0, dp(4), 0, dp(4))
            clipChildren = false
            clipToPadding = false
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
        }

        appPages.forEach { pageApps ->
            val grid = GridLayout(this).apply {
                columnCount = 4
                useDefaultMargins = false
                alignmentMode = GridLayout.ALIGN_BOUNDS
            }

            pageApps.forEach { app ->
                val cell = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER
                    setPadding(dp(3), dp(4), dp(3), dp(5))
                    isClickable = true
                    isFocusable = true
                    setOnClickListener { launch(app) }
                }

                cell.tag = (
                    app.loadLabel(packageManager).toString() + " " +
                        app.activityInfo.packageName
                ).lowercase()

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

                cell.addView(label, LinearLayout.LayoutParams(-1, dp(24)))

                grid.addView(
                    cell,
                    GridLayout.LayoutParams().apply {
                        width = 0
                        height = GridLayout.LayoutParams.WRAP_CONTENT
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    }
                )
            }

            pageGrids.add(grid)
            pages.addView(
                grid,
                FrameLayout.LayoutParams(-1, -1)
            )
        }

        pageDots.text = (0 until pages.childCount).joinToString("  ") {
            if (it == 0) "●" else "○"
        }

        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(
                text: CharSequence?, start: Int, count: Int, after: Int
            ) {}

            override fun onTextChanged(
                text: CharSequence?, start: Int, before: Int, count: Int
            ) {
                val query = text?.toString()?.trim()?.lowercase().orEmpty()

                pageGrids.forEach { grid ->
                    for (i in 0 until grid.childCount) {
                        val cell = grid.getChildAt(i)
                        cell.visibility =
                            if (query.isEmpty() || cell.tag.toString().contains(query))
                                View.VISIBLE
                            else
                                View.GONE
                    }
                }

                val matchingPage = if (query.isEmpty()) {
                    0
                } else {
                    appPages.indexOfFirst { pageApps ->
                        pageApps.any { app ->
                            (
                                app.loadLabel(packageManager).toString() + " " +
                                    app.activityInfo.packageName
                            ).lowercase().contains(query)
                        }
                    }
                }

                pages.displayedChild = matchingPage.coerceAtLeast(0)
                pageDots.text = (0 until pages.childCount).joinToString("  ") {
                    if (it == pages.displayedChild) "●" else "○"
                }
            }

            override fun afterTextChanged(text: android.text.Editable?) {}
        })

        root.addView(
            pages,
            LinearLayout.LayoutParams(-1, 0, 1f)
        )
        root.addView(
            pageDots,
            LinearLayout.LayoutParams(-1, dp(25))
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
        updateClock()
    }

    private fun controlGlyph(kind: String): View {
        return object : View(this) {
            private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                style = Paint.Style.STROKE
                strokeWidth = 3.2f
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
            }

            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                val save = canvas.save()
                canvas.scale(width / 48f, height / 48f)

                when (kind) {
                    "wifi" -> {
                        canvas.drawArc(RectF(7f, 9f, 41f, 39f), 220f, 100f, false, paint)
                        canvas.drawArc(RectF(13f, 17f, 35f, 39f), 220f, 100f, false, paint)
                        canvas.drawArc(RectF(20f, 25f, 28f, 33f), 220f, 100f, false, paint)
                        canvas.drawCircle(24f, 37f, 2f, paint.apply { style = Paint.Style.FILL })
                        paint.style = Paint.Style.STROKE
                    }
                    "bluetooth" -> {
                        val path = Path().apply {
                            moveTo(24f, 5f)
                            lineTo(35f, 16f)
                            lineTo(13f, 34f)
                            moveTo(24f, 5f)
                            lineTo(24f, 43f)
                            lineTo(35f, 32f)
                            lineTo(13f, 14f)
                        }
                        canvas.drawPath(path, paint)
                    }
                    "airplane" -> {
                        val path = Path().apply {
                            moveTo(24f, 4f)
                            lineTo(28f, 21f)
                            lineTo(42f, 31f)
                            lineTo(41f, 35f)
                            lineTo(27f, 29f)
                            lineTo(27f, 42f)
                            lineTo(33f, 46f)
                            lineTo(32f, 48f)
                            lineTo(24f, 45f)
                            lineTo(16f, 48f)
                            lineTo(15f, 46f)
                            lineTo(21f, 42f)
                            lineTo(21f, 29f)
                            lineTo(7f, 35f)
                            lineTo(6f, 31f)
                            lineTo(20f, 21f)
                            close()
                        }
                        canvas.drawPath(path, paint.apply { style = Paint.Style.FILL })
                        paint.style = Paint.Style.STROKE
                    }
                    "display" -> {
                        canvas.drawRoundRect(RectF(5f, 7f, 43f, 34f), 4f, 4f, paint)
                        canvas.drawLine(24f, 34f, 24f, 41f, paint)
                        canvas.drawLine(15f, 42f, 33f, 42f, paint)
                    }
                    "sound" -> {
                        val speaker = Path().apply {
                            moveTo(7f, 19f)
                            lineTo(16f, 19f)
                            lineTo(27f, 10f)
                            lineTo(27f, 38f)
                            lineTo(16f, 29f)
                            lineTo(7f, 29f)
                            close()
                        }
                        canvas.drawPath(speaker, paint)
                        canvas.drawArc(RectF(22f, 12f, 42f, 36f), -65f, 130f, false, paint)
                        canvas.drawArc(RectF(24f, 5f, 48f, 43f), -65f, 130f, false, paint)
                    }
                    "settings" -> {
                        for (i in 0 until 8) {
                            val angle = Math.toRadians(i * 45.0)
                            val x1 = 24f + kotlin.math.cos(angle).toFloat() * 13f
                            val y1 = 24f + kotlin.math.sin(angle).toFloat() * 13f
                            val x2 = 24f + kotlin.math.cos(angle).toFloat() * 19f
                            val y2 = 24f + kotlin.math.sin(angle).toFloat() * 19f
                            canvas.drawLine(x1, y1, x2, y2, paint)
                        }
                        canvas.drawCircle(24f, 24f, 14f, paint)
                        canvas.drawCircle(24f, 24f, 5f, paint)
                    }
                }
                canvas.restoreToCount(save)
            }
        }
    }

    private fun showControlCenter() {
        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(14))
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.argb(225, 30, 58, 100),
                    Color.argb(195, 65, 105, 165),
                    Color.argb(220, 22, 40, 76)
                )
            ).apply {
                cornerRadius = dp(30).toFloat()
                setStroke(dp(1), Color.argb(175, 225, 242, 255))
            }
        }

        val heading = TextView(this).apply {
            text = "Control Center"
            setTextColor(Color.WHITE)
            textSize = 25f
            typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
        }
        panel.addView(heading)

        val subtitle = TextView(this).apply {
            text = "ROG PHONE 5s  •  LIQUID GLASS"
            setTextColor(Color.argb(175, 255, 255, 255))
            textSize = 10f
            letterSpacing = 0.12f
            setPadding(0, dp(4), 0, dp(17))
        }
        panel.addView(subtitle)

        fun makeTile(label: String, action: String, kind: String): LinearLayout {
            val tile = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(5), dp(8), dp(5), dp(7))
                background = GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                        Color.argb(115, 190, 220, 255),
                        Color.argb(65, 115, 165, 235),
                        Color.argb(95, 65, 105, 175)
                    )
                ).apply {
                    cornerRadius = dp(20).toFloat()
                    setStroke(dp(1), Color.argb(155, 235, 247, 255))
                }
                elevation = dp(4).toFloat()
                isClickable = true
                isFocusable = true
            }

            val iconHolder = FrameLayout(this).apply {
                background = GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(
                        Color.argb(190, 220, 240, 255),
                        Color.argb(105, 115, 165, 235),
                        Color.argb(135, 70, 115, 190)
                    )
                ).apply {
                    cornerRadius = dp(24).toFloat()
                    setStroke(dp(1), Color.argb(190, 255, 255, 255))
                }
                elevation = dp(2).toFloat()
                addView(
                    controlGlyph(kind),
                    FrameLayout.LayoutParams(dp(29), dp(29), Gravity.CENTER)
                )
            }

            tile.addView(
                iconHolder,
                LinearLayout.LayoutParams(dp(48), dp(48)).apply {
                    bottomMargin = dp(5)
                }
            )

            val caption = TextView(this).apply {
                text = label
                setTextColor(Color.WHITE)
                textSize = 11f
                gravity = Gravity.CENTER
                maxLines = 1
                typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            }
            tile.addView(caption)

            tile.setOnTouchListener { view, event ->
                when (event.actionMasked) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        view.animate().scaleX(0.94f).scaleY(0.94f)
                            .alpha(0.86f).setDuration(90).start()
                    }
                    android.view.MotionEvent.ACTION_UP,
                    android.view.MotionEvent.ACTION_CANCEL -> {
                        view.animate().scaleX(1f).scaleY(1f)
                            .alpha(1f).setDuration(180)
                            .setInterpolator(
                                android.view.animation.OvershootInterpolator(1.2f)
                            ).start()
                    }
                }
                false
            }

            tile.setOnClickListener {
                try {
                    startActivity(Intent(action))
                } catch (_: Exception) {
                    startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
                }
            }
            return tile
        }

        fun addPair(
            left: Triple<String, String, String>,
            right: Triple<String, String, String>
        ) {
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
            }

            row.addView(
                makeTile(left.first, left.second, left.third),
                LinearLayout.LayoutParams(0, dp(92), 1f).apply {
                    setMargins(dp(3), dp(3), dp(5), dp(3))
                }
            )

            row.addView(
                makeTile(right.first, right.second, right.third),
                LinearLayout.LayoutParams(0, dp(92), 1f).apply {
                    setMargins(dp(5), dp(3), dp(3), dp(3))
                }
            )

            panel.addView(row)
        }

        addPair(
            Triple("Wi-Fi", android.provider.Settings.Panel.ACTION_WIFI, "wifi"),
            Triple("Bluetooth", android.provider.Settings.ACTION_BLUETOOTH_SETTINGS, "bluetooth")
        )

        addPair(
            Triple("Pesawat", android.provider.Settings.ACTION_WIRELESS_SETTINGS, "airplane"),
            Triple("Layar", android.provider.Settings.ACTION_DISPLAY_SETTINGS, "display")
        )

        addPair(
            Triple("Suara", android.provider.Settings.ACTION_SOUND_SETTINGS, "sound"),
            Triple("Pengaturan", android.provider.Settings.ACTION_SETTINGS, "settings")
        )

        fun addSectionLabel(text: String) {
            val label = TextView(this).apply {
                this.text = text
                setTextColor(Color.WHITE)
                textSize = 13f
                typeface = Typeface.create(
                    "sans-serif-medium", Typeface.NORMAL
                )
                setPadding(dp(5), dp(15), 0, dp(5))
            }
            panel.addView(label)
        }

        fun makeSliderCard(slider: SeekBar): LinearLayout {
            return LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(dp(12), dp(5), dp(12), dp(5))
                background = GradientDrawable(
                    GradientDrawable.Orientation.TL_BR,
                    intArrayOf(
                        Color.argb(105, 195, 225, 255),
                        Color.argb(65, 100, 150, 220),
                        Color.argb(90, 55, 95, 160)
                    )
                ).apply {
                    cornerRadius = dp(18).toFloat()
                    setStroke(dp(1), Color.argb(145, 235, 247, 255))
                }
                addView(
                    slider,
                    LinearLayout.LayoutParams(
                        -1, dp(42)
                    )
                )
            }
        }

        addSectionLabel("☀  Kecerahan sistem")

        val brightness = SeekBar(this).apply {
            max = 255
            progress = try {
                android.provider.Settings.System.getInt(
                    contentResolver,
                    android.provider.Settings.System.SCREEN_BRIGHTNESS
                ).coerceIn(1, 255)
            } catch (_: Exception) {
                128
            }
            progressTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)
            thumbTintList = android.content.res.ColorStateList.valueOf(Color.WHITE)

            setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        value: Int,
                        fromUser: Boolean
                    ) {
                        if (fromUser && android.provider.Settings.System.canWrite(this@MainActivity)) {
                            runCatching {
                                android.provider.Settings.System.putInt(
                                    contentResolver,
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
                                )
                                android.provider.Settings.System.putInt(
                                    contentResolver,
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS,
                                    value.coerceIn(1, 255)
                                )
                            }
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {
                        if (!android.provider.Settings.System.canWrite(this@MainActivity)) {
                            Toast.makeText(
                                this@MainActivity,
                                "Izinkan akses ubah pengaturan agar kecerahan sistem bisa diatur",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(
                                Intent(
                                    android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                    android.net.Uri.parse("package:$packageName")
                                )
                            )
                        }
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                }
            )
        }
        panel.addView(makeSliderCard(brightness))

        addSectionLabel("♫  Volume media")

        val audio = getSystemService(AUDIO_SERVICE) as android.media.AudioManager

        val volume = SeekBar(this).apply {
            max = audio.getStreamMaxVolume(
                android.media.AudioManager.STREAM_MUSIC
            )
            progress = audio.getStreamVolume(
                android.media.AudioManager.STREAM_MUSIC
            )
            progressTintList =
                android.content.res.ColorStateList.valueOf(Color.WHITE)
            thumbTintList =
                android.content.res.ColorStateList.valueOf(Color.WHITE)

            setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(
                        seekBar: SeekBar?,
                        value: Int,
                        fromUser: Boolean
                    ) {
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
                }
            )
        }
        panel.addView(makeSliderCard(volume))

        val dialog = android.app.AlertDialog.Builder(this)
            .setView(panel)
            .setNegativeButton("Tutup", null)
            .create()

        panel.alpha = 0f
        panel.scaleX = 0.88f
        panel.scaleY = 0.88f

        dialog.show()

        dialog.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)
            setDimAmount(0.28f)

            if (android.os.Build.VERSION.SDK_INT >= 31) {
                addFlags(android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                setBackgroundBlurRadius(dp(48))
            }
        }

        panel.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(320)
            .setInterpolator(
                android.view.animation.DecelerateInterpolator(1.7f)
            )
            .start()

        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)?.apply {
            setTextColor(Color.WHITE)
            setOnClickListener {
                panel.animate()
                    .alpha(0f)
                    .scaleX(0.92f)
                    .scaleY(0.92f)
                    .setDuration(180)
                    .setInterpolator(
                        android.view.animation.DecelerateInterpolator()
                    )
                    .withEndAction {
                        dialog.dismiss()
                    }
                    .start()
            }
        }
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
