package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport

// Message constants.
private const val EMPTY_TEXT = "There is no text."
private const val HAS_CHILDREN = "This component should not have children."

@Serializable
@SerialName("text")
class Text : Component(), InlineComponent {
    var value: String? by Properties()

    operator fun String.unaryPlus() {
        value = value?.plus(this) ?: this
    }

    /**
     * [validate] is Text specific function which checks the validity of a Text.
     * [checkTextNotEmpty] checks that Text is not empty.
     * [checkNoChildren] checks that Text component does not have children.
     * Returns a MutableList of [ValidityReport].
     */
    override fun validate(): MutableList<ValidityReport> {
        var message = ""
        val reports: MutableList<ValidityReport> = mutableListOf()
        message = checkTextNotEmpty(message)
        message = checkNoChildren(message)
        reports.add(ValidityReport(this, message, isValid))
        return reports
    }

    /**
     * [checkTextNotEmpty] checks that Text is not empty.
     * Returns an informative message.
     */
    private fun checkNoChildren(message: String): String {
        var outputMessage = message
        if (children.size != 0) {
            outputMessage = invalidate(outputMessage, HAS_CHILDREN)
        }
        return outputMessage
    }

    /**
     * [checkNoChildren] checks that Text component does not have children.
     * Returns an informative message.
     */
    private fun checkTextNotEmpty(message: String): String {
        var outputMessage = message
        if (value.isNullOrBlank()) {
            outputMessage = invalidate(outputMessage, EMPTY_TEXT)
        }
        return outputMessage
    }
}
