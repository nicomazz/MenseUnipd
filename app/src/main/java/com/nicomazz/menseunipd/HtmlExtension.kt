package com.nicomazz.menseunipd

import android.os.Build
import android.text.Html
import android.text.Spanned

/**
 * Created by Nicolò Mazzucato on 08/10/2017.
 */
fun String.toHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT);
} else
    Html.fromHtml("<h2>Title</h2><br><p>Description here</p>");
