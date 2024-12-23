
import android.app.Activity
import android.content.Context

/**
 * Copyright(c) 2024 Kamil Dąbrowski. All rights reserved
 */

fun Context?.finishIfPossible() {
    if (this == null) {
        return
    }

    (this as Activity).finish()
}
