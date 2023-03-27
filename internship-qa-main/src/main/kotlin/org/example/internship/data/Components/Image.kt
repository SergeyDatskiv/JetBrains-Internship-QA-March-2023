package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport
import kotlin.collections.List

// Message constants.
private const val CHILDREN_PRESENT = "An image should not have children. "
private const val PNG = "png"
private const val JPG = "jpg"
private const val JPEG = "jpeg"
private const val SVG = "svg"

@Serializable
@SerialName("image")
class Image : Component(), BlockComponent {
    var src: String? by Properties()
    var darkSrc: String? by Properties()
    private var validImageFormats: Set<String> = setOf(PNG, JPG, JPEG, SVG)

    /**
     * [validate] is Image specific function which checks the validity of an Image.
     * [checkSrcImage] checks the source image requirements of an Image.
     * [checkChildren] check requirements related to the children of an Image.
     * [checkDarkSrcImage] checks the dark source image requirements of an Image.
     * Returns a MutableList of [ValidityReport].
     */
    override fun validate(): MutableList<ValidityReport> {
        var message = ""
        val srcCheck: List<String>? = src?.split(".")
        val darkSrcCheck: List<String>? = darkSrc?.split(".")
        message = checkSrcImage(srcCheck, message)
        message = checkChildren(message)
        message = checkDarkSrcImage(darkSrcCheck, message)
        return mutableListOf(ValidityReport(this, message, isValid))
    }

    /**
     * [checkSrcImage] checks if the dark source image adheres to the requirements.
     * The image format is supported by [validImageFormats].
     * Checks that a source is specified for an Image.
     * Also checks if a name of an image does not have ".".
     * Returns an informative message.
     */
    private fun checkSrcImage(srcCheck: List<String>?, message: String): String {
        var outputMessage = message
        if (srcCheck != null && srcCheck.size == 2) {
            if (!validImageFormats.contains(srcCheck[1])) {
                outputMessage = invalidate(outputMessage, "Unsupported (${srcCheck[1]}) image format. ")
            }
        } else {
            outputMessage = invalidate(outputMessage, "Missing or invalid name of an image ($src). ")
        }
        return outputMessage
    }

    /**
     * [checkChildren] checks requirements related to the children of an Image.
     * [AT_LEAST_ONE_CHILD] checks if an Image has no children.
     * Returns an informative message.
     */
    private fun checkChildren(message: String): String {
        var outputMessage = message
        if (children.isNotEmpty()) {
            outputMessage = invalidate(outputMessage, CHILDREN_PRESENT)
        }
        return outputMessage
    }

    /**
     * [checkDarkSrcImage] checks if the dark source image adheres to the requirements.
     * The image format is supported by [validImageFormats].
     * Also checks if a name of an image does not have ".".
     * Returns an informative message.
     */
    private fun checkDarkSrcImage(darkSrcCheck: List<String>?, message: String): String {
        var outputMessage = message
        if (darkSrcCheck != null && darkSrcCheck.size == 2) {
            if (!validImageFormats.contains(darkSrcCheck[1])) {
                outputMessage = invalidate(outputMessage, "Dark version has unsupported (${darkSrcCheck[1]}) image format. ")
            }
        } else if (darkSrcCheck != null && darkSrcCheck.size != 2) {
            outputMessage = invalidate(outputMessage, "invalid name of an image ($darkSrc). ")
        }
        return outputMessage
    }
}
