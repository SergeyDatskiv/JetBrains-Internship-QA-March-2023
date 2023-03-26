package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.BlockComponent
import org.example.internship.data.ValidityReport

@Serializable
@SerialName("image")
class Image : Component(), BlockComponent {
    var src: String? by Properties()
    var darkSrc: String? by Properties()
    var validImageFormats: Set<String> = setOf("png", "jpg", "jpeg", "svg")

    /**
     * Validate is Image specific function which checks the validity of an Image.
     */
    override fun validate(): MutableList<ValidityReport> {
        var message: String = ""
        val srcCheck: kotlin.collections.List<String>? = src?.split(".")
        val darkSrcCheck: kotlin.collections.List<String>? = darkSrc?.split(".")
        if (srcCheck != null && srcCheck.size == 2) {
            if (!validImageFormats.contains(srcCheck[1])) {
                isValid = false
                message += "Unsupported (${srcCheck[1]}) image format. "
            }
        } else {
            isValid = false
            message += "Missing or invalid name of an image ($src). "
        }
        if (children.isNotEmpty()) {
            isValid = false
            message += "An image should not have children. "
        }
        if (darkSrcCheck != null && darkSrcCheck.size == 2) {
            if (!validImageFormats.contains(darkSrcCheck[1])) {
                isValid = false
                message += "Dark version has unsupported (${darkSrcCheck[1]}) image format. "
            }
        } else if (darkSrcCheck != null) {
            if (darkSrcCheck.size != 2) {
                isValid = false
                message += "invalid name of an image ($darkSrc). "
            }
        }
        return mutableListOf<ValidityReport>(ValidityReport(this, message, isValid))
    }
}