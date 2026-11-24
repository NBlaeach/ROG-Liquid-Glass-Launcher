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
                val side = minOf(width, height).toFloat()
                if (side <= 0f) return

                val save = canvas.save()
                canvas.translate((width - side) / 2f, (height - side) / 2f)
                canvas.scale(side / 100f, side / 100f)

                val tile = RectF(3f, 3f, 97f, 97f)
                val tilePath = Path().apply {
                    moveTo(50f, 3f)
                    cubicTo(72f, 3f, 97f, 28f, 97f, 50f)
                    cubicTo(97f, 72f, 72f, 97f, 50f, 97f)
                    cubicTo(28f, 97f, 3f, 72f, 3f, 50f)
                    cubicTo(3f, 28f, 28f, 3f, 50f, 3f)
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
                    colors[0], colors[1], Shader.TileMode.CLAMP
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
                line.strokeWidth = 7f

                when (kind) {
                    "binance" -> {
                        fill.color = Color.rgb(245, 190, 55)
                        fun diamond(x: Float, y: Float, r: Float) {
                            canvas.drawPath(Path().apply {
                                moveTo(x, y - r)
                                lineTo(x + r, y)
                                lineTo(x, y + r)
                                lineTo(x - r, y)
                                close()
                            }, fill)
                        }
                        diamond(50f, 50f, 10f)
                        diamond(50f, 30f, 7f)
                        diamond(50f, 70f, 7f)
                        diamond(30f, 50f, 7f)
                        diamond(70f, 50f, 7f)
                        diamond(36f, 36f, 5f)
                        diamond(64f, 36f, 5f)
                        diamond(36f, 64f, 5f)
                        diamond(64f, 64f, 5f)
                        fill.color = Color.WHITE
                    }

                    "shopee" -> {
                        fill.color = Color.WHITE
                        canvas.drawRoundRect(27f, 39f, 73f, 78f, 8f, 8f, fill)
                        line.color = colors[1]
                        canvas.drawArc(35f, 19f, 65f, 53f, 180f, 180f, false, line)
                        fill.color = colors[1]
                        canvas.drawText("S", 39f, 66f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = colors[1]; textSize = 27f; isFakeBoldText = true
                        })
                    }
                    "tokopedia" -> {
                        fill.color = Color.WHITE
                        canvas.drawRoundRect(25f, 34f, 75f, 77f, 10f, 10f, fill)
                        line.color = colors[1]
                        canvas.drawArc(35f, 17f, 65f, 52f, 180f, 180f, false, line)
                        fill.color = colors[1]
                        canvas.drawCircle(41f, 52f, 4f, fill)
                        canvas.drawCircle(59f, 52f, 4f, fill)
                        canvas.drawArc(39f, 52f, 61f, 69f, 10f, 160f, false, line)
                    }
                    "termux" -> {
                        line.color = Color.rgb(100, 235, 145)
                        val prompt = Path().apply {
                            moveTo(27f, 35f); lineTo(48f, 50f); lineTo(27f, 65f)
                        }
                        canvas.drawPath(prompt, line)
                        canvas.drawLine(53f, 65f, 76f, 65f, line)
                    }
                    "magisk" -> {
                        fill.color = Color.WHITE
                        canvas.drawPath(Path().apply {
                            moveTo(50f, 20f); lineTo(77f, 34f)
                            lineTo(72f, 62f); lineTo(50f, 82f)
                            lineTo(28f, 62f); lineTo(23f, 34f); close()
                        }, fill)
                        fill.color = colors[1]
                        canvas.drawCircle(40f, 48f, 4f, fill)
                        canvas.drawCircle(60f, 48f, 4f, fill)
                        canvas.drawLine(40f, 62f, 60f, 62f, line)
                    }
                    "root" -> {
                        fill.color = Color.rgb(255, 194, 75)
                        canvas.drawPath(Path().apply {
                            moveTo(17f, 35f); lineTo(42f, 35f)
                            lineTo(50f, 44f); lineTo(83f, 44f)
                            lineTo(78f, 75f); lineTo(21f, 75f); close()
                        }, fill)
                        line.color = Color.WHITE
                        canvas.drawLine(27f, 51f, 70f, 51f, line)
                    }
                    "aida" -> {
                        line.color = Color.WHITE
                        canvas.drawArc(23f, 24f, 77f, 78f, 135f, 270f, false, line)
                        canvas.drawLine(50f, 51f, 68f, 36f, line)
                        fill.color = Color.WHITE
                        canvas.drawCircle(50f, 51f, 5f, fill)
                    }
                    "cpuz" -> {
                        line.color = Color.WHITE
                        canvas.drawRoundRect(31f, 31f, 69f, 69f, 5f, 5f, line)
                        for (v in listOf(38f, 50f, 62f)) {
                            canvas.drawLine(v, 21f, v, 31f, line)
                            canvas.drawLine(v, 69f, v, 79f, line)
                            canvas.drawLine(21f, v, 31f, v, line)
                            canvas.drawLine(69f, v, 79f, v, line)
                        }
                        fill.color = Color.WHITE
                        canvas.drawText("CPU", 34f, 54f, Paint(Paint.ANTI_ALIAS_FLAG).apply {
                            color = colors[1]; textSize = 13f; isFakeBoldText = true
                        })
                    }
                    "devcheck" -> {
                        line.color = Color.WHITE
                        val pulse = Path().apply {
                            moveTo(18f, 53f); lineTo(34f, 53f)
                            lineTo(42f, 35f); lineTo(53f, 68f)
                            lineTo(63f, 44f); lineTo(70f, 53f); lineTo(82f, 53f)
                        }
                        canvas.drawPath(pulse, line)
                    }
                    "mt4" -> {
                        line.color = Color.WHITE
                        canvas.drawLine(22f, 76f, 22f, 27f, line)
                        canvas.drawLine(22f, 76f, 80f, 76f, line)
                        fill.color = Color.rgb(90, 230, 160)
                        canvas.drawRect(32f, 47f, 40f, 66f, fill)
                        canvas.drawRect(48f, 34f, 56f, 61f, fill)
                        canvas.drawRect(64f, 24f, 72f, 52f, fill)
                    }
                    "auth" -> {
                        fill.color = Color.WHITE
                        canvas.drawPath(Path().apply {
                            moveTo(50f, 19f); lineTo(77f, 30f)
                            lineTo(73f, 59f); lineTo(50f, 81f)
                            lineTo(27f, 59f); lineTo(23f, 30f); close()
                        }, fill)
                        line.color = colors[1]
                        val check = Path().apply {
                            moveTo(37f, 50f); lineTo(46f, 60f); lineTo(65f, 39f)
                        }
                        canvas.drawPath(check, line)
                    }
                    "link2sd" -> {
                        line.color = Color.WHITE
                        canvas.drawRoundRect(20f, 31f, 57f, 61f, 14f, 14f, line)
                        canvas.drawRoundRect(43f, 43f, 80f, 73f, 14f, 14f, line)
                        canvas.drawLine(40f, 57f, 60f, 47f, line)
                    }
                    "zedge" -> {
                        line.color = Color.WHITE
                        val z = Path().apply {
                            moveTo(26f, 34f); lineTo(75f, 34f)
                            lineTo(30f, 68f); lineTo(75f, 68f)
                        }
                        canvas.drawPath(z, line)
                    }
                    "pintu" -> {
                        line.color = Color.WHITE
                        canvas.drawRoundRect(29f, 22f, 71f, 78f, 18f, 18f, line)
                        canvas.drawCircle(50f, 43f, 9f, line)
                        canvas.drawLine(50f, 52f, 50f, 68f, line)
                    }
                    "tiktok" -> {
                        val note = Path().apply {
                            moveTo(57f, 23f)
                            lineTo(57f, 63f)
                            cubicTo(57f, 77f, 36f, 81f, 34f, 67f)
                            cubicTo(32f, 55f, 45f, 49f, 57f, 55f)
                            moveTo(57f, 25f)
                            cubicTo(60f, 38f, 67f, 43f, 77f, 44f)
                        }
                        line.color = Color.rgb(0, 230, 255)
                        canvas.save()
                        canvas.translate(-3f, 1f)
                        canvas.drawPath(note, line)
                        canvas.restore()
                        line.color = Color.rgb(255, 55, 90)
                        canvas.save()
                        canvas.translate(3f, -1f)
                        canvas.drawPath(note, line)
                        canvas.restore()
                        line.color = Color.WHITE
                        canvas.drawPath(note, line)
                        fill.color = Color.WHITE
                        canvas.drawCircle(43f, 67f, 8f, fill)
                    }
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
                        val handset = Path().apply {
                            moveTo(34f, 23f)
                            cubicTo(30f, 23f, 26f, 27f, 27f, 33f)
                            cubicTo(30f, 54f, 46f, 70f, 67f, 74f)
                            cubicTo(73f, 75f, 78f, 71f, 78f, 65f)
                            cubicTo(78f, 63f, 77f, 61f, 75f, 60f)
                            lineTo(64f, 52f)
                            cubicTo(62f, 50f, 59f, 51f, 57f, 53f)
                            lineTo(51f, 59f)
                            cubicTo(43f, 55f, 38f, 49f, 35f, 41f)
                            lineTo(41f, 35f)
                            cubicTo(43f, 33f, 43f, 30f, 41f, 28f)
                            lineTo(37f, 24f)
                            cubicTo(36f, 23f, 35f, 23f, 34f, 23f)
                            close()
                        }
                        canvas.drawPath(handset, fill)
                    }
                    "message" -> {
                        val bubble = Path().apply {
                            moveTo(36f, 22f)
                            lineTo(64f, 22f)
                            cubicTo(74f, 22f, 80f, 29f, 80f, 39f)
                            lineTo(80f, 53f)
                            cubicTo(80f, 63f, 74f, 69f, 64f, 69f)
                            lineTo(46f, 69f)
                            lineTo(29f, 81f)
                            lineTo(33f, 66f)
                            cubicTo(25f, 63f, 20f, 57f, 20f, 48f)
                            lineTo(20f, 39f)
                            cubicTo(20f, 29f, 26f, 22f, 36f, 22f)
                            close()
                        }
                        canvas.drawPath(bubble, fill)
                        fill.color = colors[1]
                        canvas.drawCircle(37f, 46f, 3f, fill)
                        canvas.drawCircle(50f, 46f, 3f, fill)
                        canvas.drawCircle(63f, 46f, 3f, fill)
                        fill.color = Color.WHITE
                    }
                                        "camera" -> {
                                            canvas.drawRoundRect(17f, 32f, 83f, 75f, 12f, 12f, fill)
                                            canvas.drawRoundRect(31f, 25f, 49f, 37f, 5f, 5f, fill)
                                            fill.color = 0xFF26364F.toInt()
                                            canvas.drawCircle(51f, 53f, 16f, fill)
                                            fill.color = 0xFFDDEBFA.toInt()
                                            canvas.drawCircle(51f, 53f, 12f, fill)
                                            fill.color = 0xFF3C91D8.toInt()
                                            canvas.drawCircle(51f, 53f, 9f, fill)
                                            fill.color = 0xFF152D50.toInt()
                                            canvas.drawCircle(51f, 53f, 5f, fill)
                                            fill.color = Color.WHITE
                                            canvas.drawCircle(70f, 41f, 3f, fill)
                                        }
                    "settings" -> {
                        val gear = Path().apply {
                            for (i in 0 until 32) {
                                val angle = Math.toRadians(i * 11.25 - 90.0)
                                val radius = when (i % 4) {
                                    1, 2 -> 35f
                                    else -> 27f
                                }
                                val x = 50f + radius * kotlin.math.cos(angle).toFloat()
                                val y = 50f + radius * kotlin.math.sin(angle).toFloat()
                                if (i == 0) moveTo(x, y) else lineTo(x, y)
                            }
                            close()
                        }
                        fill.shader = LinearGradient(
                            20f, 15f, 80f, 85f,
                            colors[0], colors[1], Shader.TileMode.CLAMP
                        )
                        canvas.drawPath(gear, fill)
                        fill.shader = null
                        fill.color = colors[1]
                        canvas.drawCircle(50f, 50f, 17f, fill)
                        line.color = Color.WHITE
                        line.alpha = 210
                        line.strokeWidth = 2.5f
                        canvas.drawCircle(50f, 50f, 17f, line)
                        fill.color = 0xFFEAF4FF.toInt()
                        canvas.drawCircle(50f, 50f, 8f, fill)
                    }
                    "photos" -> {
                                            val petalColors = intArrayOf(
                                                0xFF35B7F1.toInt(), 0xFF5B65D8.toInt(),
                                                0xFFB45BCB.toInt(), 0xFFEF5D9B.toInt(),
                                                0xFFFFA33D.toInt(), 0xFF70C86B.toInt()
                                            )
                                            for (i in 0 until 6) {
                                                val angle = Math.toRadians(i * 60.0 - 90.0)
                                                fill.color = petalColors[i]
                                                val x = 50f + 16f * kotlin.math.cos(angle).toFloat()
                                                val y = 50f + 16f * kotlin.math.sin(angle).toFloat()
                                                canvas.drawCircle(x, y, 12f, fill)
                                            }
                                            fill.color = Color.WHITE
                                            canvas.drawCircle(50f, 50f, 6f, fill)
                                        }
                                        "browser" -> {
                                            canvas.drawCircle(50f, 50f, 30f, line)
                                            canvas.drawOval(36f, 20f, 64f, 80f, line)
                                            canvas.drawLine(21f, 43f, 79f, 43f, line)
                                            canvas.drawLine(21f, 57f, 79f, 57f, line)
                                            val compass = Path().apply {
                                                moveTo(64f, 29f)
                                                lineTo(55f, 54f)
                                                lineTo(35f, 70f)
                                                lineTo(44f, 45f)
                                                close()
                                            }
                                            fill.color = Color.WHITE
                                            canvas.drawPath(compass, fill)
                                            fill.color = 0xFFE94E62.toInt()
                                            val needle = Path().apply {
                                                moveTo(64f, 29f)
                                                lineTo(55f, 54f)
                                                lineTo(49f, 49f)
                                                close()
                                            }
                                            canvas.drawPath(needle, fill)
                                            fill.color = Color.WHITE
                                        }
                                        "clock" -> {
                                            canvas.drawCircle(50f, 50f, 31f, line)
                                            for (i in 0 until 12) {
                                                val angle = Math.toRadians(i * 30.0 - 90.0)
                                                val x1 = 50f + 25f * kotlin.math.cos(angle).toFloat()
                                                val y1 = 50f + 25f * kotlin.math.sin(angle).toFloat()
                                                val x2 = 50f + 28f * kotlin.math.cos(angle).toFloat()
                                                val y2 = 50f + 28f * kotlin.math.sin(angle).toFloat()
                                                canvas.drawLine(x1, y1, x2, y2, line)
                                            }
                                            canvas.drawLine(50f, 50f, 50f, 29f, line)
                                            canvas.drawLine(50f, 50f, 67f, 58f, line)
                                            canvas.drawCircle(50f, 50f, 4f, fill)
                                        }
                                        "calendar" -> {
                                            canvas.drawRoundRect(22f, 23f, 78f, 78f, 9f, 9f, line)
                                            fill.color = 0xFFE94B58.toInt()
                                            canvas.drawRoundRect(22f, 23f, 78f, 43f, 9f, 9f, fill)
                                            canvas.drawRect(22f, 34f, 78f, 43f, fill)
                                            fill.color = Color.WHITE
                                            canvas.drawLine(36f, 18f, 36f, 31f, line)
                                            canvas.drawLine(64f, 18f, 64f, 31f, line)
                                            for (i in 0 until 3) {
                                                canvas.drawCircle(37f + i * 13f, 54f, 3f, fill)
                                            }
                                            for (i in 0 until 3) {
                                                canvas.drawCircle(37f + i * 13f, 67f, 3f, fill)
                                            }
                                        }
                    "music" -> {
                        fill.color = Color.WHITE
                        val note = Path().apply {
                            moveTo(60f, 25f)
                            lineTo(60f, 66f)
                            cubicTo(60f, 78f, 40f, 82f, 37f, 70f)
                            cubicTo(34f, 59f, 49f, 54f, 60f, 61f)
                            moveTo(60f, 25f)
                            lineTo(79f, 20f)
                            lineTo(79f, 59f)
                            lineTo(60f, 64f)
                        }
                        line.color = Color.WHITE
                        line.strokeWidth = 7f
                        line.strokeCap = Paint.Cap.ROUND
                        line.strokeJoin = Paint.Join.ROUND
                        canvas.drawPath(note, line)
                        canvas.drawCircle(47f, 69f, 8f, fill)
                        canvas.drawCircle(68f, 62f, 8f, fill)
                    }
                    "files" -> {
                        fill.color = 0xFFFFD66B.toInt()
                        canvas.drawRoundRect(17f, 34f, 83f, 77f, 9f, 9f, fill)
                        val folder = Path().apply {
                            moveTo(17f, 39f)
                            lineTo(17f, 30f)
                            cubicTo(17f, 27f, 20f, 26f, 23f, 26f)
                            lineTo(43f, 26f)
                            lineTo(52f, 36f)
                            lineTo(77f, 36f)
                            cubicTo(82f, 36f, 84f, 40f, 83f, 45f)
                            lineTo(77f, 73f)
                            lineTo(22f, 73f)
                            close()
                        }
                        fill.shader = LinearGradient(
                            20f, 28f, 75f, 76f,
                            0xFFFFE69A.toInt(), 0xFFFFB936.toInt(),
                            Shader.TileMode.CLAMP
                        )
                        canvas.drawPath(folder, fill)
                        fill.shader = null
                        fill.color = 0x55FFFFFF
                        canvas.drawRoundRect(25f, 43f, 74f, 47f, 2f, 2f, fill)
                    }
                    "contacts" -> {
                        fill.color = 0xFFFFFFFF.toInt()
                        canvas.drawCircle(50f, 39f, 13f, fill)
                        canvas.drawRoundRect(25f, 55f, 75f, 84f, 22f, 22f, fill)
                        fill.color = 0x40FFFFFF
                        canvas.drawCircle(50f, 39f, 17f, fill)
                    }
                    "calculator" -> {
                        fill.color = 0xFF29364A.toInt()
                        canvas.drawRoundRect(24f, 16f, 76f, 84f, 10f, 10f, fill)
                        fill.color = 0xFFB9D9F5.toInt()
                        canvas.drawRoundRect(31f, 24f, 69f, 40f, 4f, 4f, fill)
                        fill.color = Color.WHITE
                        for (y in listOf(51f, 65f, 77f)) {
                            for (x in listOf(37f, 50f, 63f)) {
                                canvas.drawCircle(x, y, 4f, fill)
                            }
                        }
                        fill.color = 0xFFFFA943.toInt()
                        canvas.drawCircle(63f, 51f, 4f, fill)
                        canvas.drawCircle(63f, 65f, 4f, fill)
                        canvas.drawCircle(63f, 77f, 4f, fill)
                    }
                    "play" -> {
                        val green = Path().apply {
                            moveTo(30f, 20f)
                            lineTo(57f, 47f)
                            lineTo(45f, 59f)
                            lineTo(30f, 50f)
                            close()
                        }
                        val yellow = Path().apply {
                            moveTo(30f, 50f)
                            lineTo(45f, 59f)
                            lineTo(57f, 53f)
                            lineTo(30f, 80f)
                            close()
                        }
                        val blue = Path().apply {
                            moveTo(57f, 47f)
                            lineTo(81f, 50f)
                            lineTo(57f, 53f)
                            lineTo(49f, 59f)
                            lineTo(45f, 59f)
                            close()
                        }
                        val red = Path().apply {
                            moveTo(45f, 59f)
                            lineTo(57f, 53f)
                            lineTo(81f, 50f)
                            lineTo(57f, 66f)
                            close()
                        }
                        fill.color = 0xFF35C978.toInt()
                        canvas.drawPath(green, fill)
                        fill.color = 0xFFFFD34E.toInt()
                        canvas.drawPath(yellow, fill)
                        fill.color = 0xFF42A5F5.toInt()
                        canvas.drawPath(blue, fill)
                        fill.color = 0xFFEF5350.toInt()
                        canvas.drawPath(red, fill)
                    }
                    else -> {
                        val appDrawable = app.loadIcon(packageManager)
                        val iconRect = RectF(5f, 5f, 95f, 95f)
                        val iconSave = canvas.save()
                        val iconClip = Path().apply {
                            addRoundRect(
                                iconRect, 22f, 22f, Path.Direction.CW
                            )
                        }
                        canvas.clipPath(iconClip)
                        appDrawable.setBounds(5, 5, 95, 95)
                        appDrawable.draw(canvas)
                        canvas.restoreToCount(iconSave)

                        fill.color = Color.WHITE
                        fill.shader = LinearGradient(
                            0f, 5f, 0f, 65f,
                            intArrayOf(0x48FFFFFF, 0x08FFFFFF, 0x00FFFFFF),
                            null, Shader.TileMode.CLAMP
                        )
                        canvas.drawRoundRect(iconRect, 22f, 22f, fill)
                        fill.shader = null

                        line.color = Color.WHITE
                        line.alpha = 125
                        line.strokeWidth = 1.5f
                        canvas.drawRoundRect(iconRect, 22f, 22f, line)
                        line.alpha = 235
                        line.strokeWidth = 7f
                    }
                }

                // Liquid Glass overlay untuk semua ikon
                val glassSave = canvas.save()
                val glassClip = Path().apply {
                    addRoundRect(tile, 23f, 23f, Path.Direction.CW)
                }
                canvas.clipPath(tilePath)

                fill.color = Color.WHITE
                fill.shader = LinearGradient(
                    12f, 8f, 88f, 72f,
                    intArrayOf(0x25FFFFFF, 0x08FFFFFF, 0x00FFFFFF),
                    floatArrayOf(0f, 0.48f, 1f),
                    Shader.TileMode.CLAMP
                )
                canvas.drawPath(tilePath, fill)
                fill.shader = null

                fill.shader = LinearGradient(
                    0f, 68f, 0f, 96f,
                    intArrayOf(0x00FFFFFF, 0x0AFFFFFF),
                    null,
                    Shader.TileMode.CLAMP
                )
                canvas.drawPath(tilePath, fill)
                fill.shader = null

                line.color = Color.WHITE
                line.alpha = 115
                line.strokeWidth = 1.4f
                canvas.drawPath(tilePath, line)
                line.alpha = 235
                line.strokeWidth = 7f

                canvas.restoreToCount(glassSave)
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
                                    0f, 0f, 0f, 0f
                                ).apply {
                                    duration = 240
                                    interpolator = android.view.animation.DecelerateInterpolator(1.5f)
                                }
                                outAnimation = android.view.animation.TranslateAnimation(
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 0f,
                                    android.view.animation.Animation.RELATIVE_TO_SELF, -1f,
                                    0f, 0f, 0f, 0f
                                ).apply {
                                    duration = 240
                                    interpolator = android.view.animation.DecelerateInterpolator(1.5f)
                                }
                                showNext()
                            } else if (dx > 0 && displayedChild > 0) {
                                inAnimation = android.view.animation.TranslateAnimation(
                                    android.view.animation.Animation.RELATIVE_TO_SELF, -1f,
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 0f,
                                    0f, 0f, 0f, 0f
                                ).apply {
                                    duration = 240
                                    interpolator = android.view.animation.DecelerateInterpolator(1.5f)
                                }
                                outAnimation = android.view.animation.TranslateAnimation(
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 0f,
                                    android.view.animation.Animation.RELATIVE_TO_SELF, 1f,
                                    0f, 0f, 0f, 0f
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
                            if (cell.tag.toString().contains(query)) View.VISIBLE
                            else View.GONE
                    }
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
            background = glass()
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
                background = rounded(Color.argb(85, 115, 155, 215), 20f)
                elevation = dp(4).toFloat()
                isClickable = true
                isFocusable = true
            }

            val iconHolder = FrameLayout(this).apply {
                background = rounded(Color.argb(125, 155, 195, 250), 24f)
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
            Triple("Wi-Fi", android.provider.Settings.ACTION_WIFI_SETTINGS, "wifi"),
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
                background = rounded(
                    Color.argb(75, 145, 180, 230), 18f
                )
                addView(
                    slider,
                    LinearLayout.LayoutParams(
                        -1, dp(42)
                    )
                )
            }
        }

        addSectionLabel("☀  Kecerahan launcher")

        val currentBrightness = window.attributes.screenBrightness
        val brightness = SeekBar(this).apply {
            max = 100
            progress = if (currentBrightness < 0f) {
                50
            } else {
                (currentBrightness * 100).toInt().coerceIn(1, 100)
            }
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
                        val params = window.attributes
                        params.screenBrightness =
                            if (value == 0) 0.01f else value / 100f
                        window.attributes = params
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
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
            setDimAmount(0.38f)

            if (android.os.Build.VERSION.SDK_INT >= 31) {
                addFlags(android.view.WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                setBackgroundBlurRadius(dp(28))
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
