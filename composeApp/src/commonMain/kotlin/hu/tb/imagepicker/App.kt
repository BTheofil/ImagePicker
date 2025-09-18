package hu.tb.imagepicker

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.decodeToImageBitmap

@Composable
expect fun testingCompose(
    result: (ByteArray) -> Unit
): () -> Unit

@Composable
fun App() {
    MaterialTheme {
        Box(
            Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            var image by remember { mutableStateOf<ByteArray?>(null) }

            Button(
                onClick = testingCompose(
                    result = {
                        image = it
                    }
                ),
                content = {
                    Text("open gallery picker")
                }
            )

            image?.let {
                Image(
                    bitmap = it.decodeToImageBitmap(),
                    contentDescription = "image"
                )
            }
        }
    }
}