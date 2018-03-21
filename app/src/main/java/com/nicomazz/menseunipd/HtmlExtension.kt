package com.nicomazz.menseunipd

import android.os.Build
import android.text.Html
import android.text.Spanned

/**
 * Created by Nicolò Mazzucato on 08/10/2017.
 */
fun String.toHtml(): Spanned =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(makePotatosRed(), Html.FROM_HTML_MODE_COMPACT);
        } else
            Html.fromHtml(this)

fun String.makePotatosRed() = replace("patate","<b><font color=red>patate</font></b>").replace("Patate","<b><font color=red>Patate</font></b>")
