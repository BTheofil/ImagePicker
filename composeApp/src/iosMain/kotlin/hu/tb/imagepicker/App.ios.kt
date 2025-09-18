package hu.tb.imagepicker

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.uikit.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIImage
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.UniformTypeIdentifiers.UTTypeItem
import platform.darwin.NSObject
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun testingCompose(
    result: (ByteArray) -> Unit
): () -> Unit {
    val uiViewController = LocalUIViewController.current
    val pickerDelegate = remember {
        object : NSObject(), PHPickerViewControllerDelegateProtocol {
            override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                picker.dismissViewControllerAnimated(flag = false, completion = {})
                didFinishPicking.forEach { it ->
                    val result = it as? PHPickerResult ?: return@forEach
                    if (result.itemProvider.hasItemConformingToTypeIdentifier(UTTypeItem.identifier)) {
                        result.itemProvider.loadDataRepresentationForTypeIdentifier(
                            typeIdentifier = UTTypeImage.identifier,
                            completionHandler = { nsData, error ->
                                val converted = ByteArray(nsData!!.length.toInt()).apply {
                                    usePinned { pinned ->
                                        memcpy(pinned.addressOf(0), nsData.bytes, nsData.length)
                                    }
                                }
                                result(converted)
                            }
                        )
                    }
                }
            }
        }
    }

    return remember {
        {
            val configuration = PHPickerConfiguration()
            configuration.setSelectionLimit(1)

            val pickerController = PHPickerViewController(configuration)
            pickerController.setDelegate(pickerDelegate)
            uiViewController.presentViewController(
                viewControllerToPresent = pickerController,
                animated = true,
                completion = null
            )
        }
    }

}