package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport

// Message constants.
private const val MISSING_FORMAT_STYLE = "Missing format style. "
private const val COLOR = "color"
private const val ITALIC = "italic"
private const val BOLD = "bold"
private const val UNSPECIFIED_COLOR = "The color has to be specified."
private const val EMPTY_LINK = "The link is empty. "
private const val AT_LEAST_ONE_CHILD = 1
private const val HEX_LENGTH = 6

@Serializable
@SerialName("format")
class Format : Component(), InlineComponent {
    var style: String? by Properties()
    var color: String? by Properties()
    private var allowedStyles: MutableSet<String> = mutableSetOf(BOLD, ITALIC, COLOR)

    /**
     * [validate] is Format specific function which checks the validity of a Format.
     * [checkStylePresence] checks the title requirements of a Format.
     * [checkStyleType] checks if the styles are correct.
     * [checkChildren] check requirements related to the children of a Format.
     * Returns a MutableList of [ValidityReport].
     */
    override fun validate(): MutableList<ValidityReport> {
        var message = ""
        val reports: MutableList<ValidityReport> = mutableListOf()
        message = checkStylePresence(message)
        message = checkStyleType(message)
        message = checkChildren(message, reports)
        reports.add(ValidityReport(this, message, isValid))
        return reports
    }

    /**
     * [invalidate] is a function that sets the [isValid] variable to false.
     * It also appends a specified [errorMessage] to the [inputMessage].
     * Returns an informative message.
     */
    private fun invalidate(inputMessage: String, errorMessage: String): String {
        isValid = false
        return inputMessage + errorMessage
    }

    /**
     * [checkStylePresence] checks the title requirements of a Format.
     * Returns an informative message.
     */
    private fun checkStylePresence(message: String): String {
        var outputMessage = message
        if (style.isNullOrBlank()) {
            outputMessage = invalidate(outputMessage, MISSING_FORMAT_STYLE)
        }
        return outputMessage
    }

    /**
     * [checkStyleType] checks if the styles are correct.
     * Returns an informative message.
     */
    private fun checkStyleType(message: String): String {
        var outputMessage = message
        if (!allowedStyles.contains(style)) {
            outputMessage = invalidate(outputMessage, "This style ($style) is not supported. ")
        } else if (style.equals(COLOR)) {
            if (color.isNullOrBlank()) {
                outputMessage = invalidate(outputMessage, UNSPECIFIED_COLOR)
            } else if (color!!.length != HEX_LENGTH) {
                // TODO: Check that it is hexadecimal, not just length.
                outputMessage = invalidate(outputMessage, "Current color format ($color) is not hexadecimal.")
            }
        }
        return outputMessage
    }

    /**
     * [checkChildren] checks requirements related to the children of a Format.
     * [AT_LEAST_ONE_CHILD] checks if a Format has at least one child.
     * [checkEachChild] checks validity of individual child component.
     * Returns an informative message.
     */
    private fun checkChildren(message: String, reports: MutableList<ValidityReport>): String {
        var outputMessage = message
        outputMessage = if (children.size >= AT_LEAST_ONE_CHILD) {
            checkEachChild(outputMessage, reports)
        } else {
            invalidate(outputMessage, EMPTY_LINK)
        }
        return outputMessage
    }

    /**
     * [checkEachChild] checks validity of individual child component.
     * [checkChileTextType] checks if a child is of [Text] type.
     * [generateChildValidityReports] generates a [ValidityReport] for each child.
     * Returns an informative message.
     */
    private fun checkEachChild(message: String, reports: MutableList<ValidityReport>): String {
        var outputMessage = message
        for (child in children) {
            outputMessage = checkChileTextType(child, outputMessage)
            outputMessage = generateChildValidityReports(child, reports, outputMessage)
        }
        return outputMessage
    }

    /**
     * [checkChileTextType] checks if a child is of [Text] type.
     * Returns an informative message.
     */
    private fun checkChileTextType(child: Component, message: String): String {
        var outputMessage = message
        if (child !is Text) {
            outputMessage = invalidate(
                outputMessage,
                "A child ($child) is not a Text component. Only Text components are allowed.\n"
            )
        }
        return outputMessage
    }

    /**
     * [generateChildValidityReports] generates a [ValidityReport] for each child.
     * Returns an informative message.
     */
    private fun generateChildValidityReports(
        child: Component,
        reports: MutableList<ValidityReport>,
        message: String
    ): String {
        var outputMessage = message
        val childValidityReport: MutableList<ValidityReport> = child.validate()
        reports.addAll(childValidityReport)
        for (childReport in childValidityReport) {
            if (!childReport.isValid) {
                outputMessage =
                    invalidate(outputMessage, "This child component (${childReport.component}) is invalid.\n")
            }
        }
        return outputMessage
    }
}
