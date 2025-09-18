package hu.tb.imagepicker

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun testingCompose(
    result: (ByteArray) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val bite =
                    context.contentResolver.openInputStream(uri)?.use { it.buffered().readBytes() }

                result(bite!!)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    return { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
}