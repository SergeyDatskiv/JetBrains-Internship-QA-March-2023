package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport

// Message constants.
private const val BULLET = "bullet"
private const val NUMERICAL = "numerical"
private const val PLAIN = "plain"
private const val CIRCLE = "circle"
private const val SQUARE = "square"
private const val MISSING_LIST_STYLE = "Missing list style. "
private const val EMPTY_LIST = "A list does not have children (i.e. empty list). "
private const val AT_LEAST_ONE_CHILD = 1

@Serializable
@SerialName("list")
class List : Component(), BlockComponent {
    var style: String? by Properties()
    var startWith: String? by Properties()
    var bulletType: String? by Properties()
    private var allowedStyles: MutableSet<String> = mutableSetOf(BULLET, NUMERICAL, PLAIN)
    private var allowedBulletType: MutableSet<String> = mutableSetOf(CIRCLE, SQUARE)

    /**
     * [validate] is List specific function which checks the validity of a List.
     * [checkStyle] checks style requirements of a List.
     * [checkNumericalStyle] checks numerical style requirements of a List.
     * [checkBulletStyle] checks bullet style requirements of a List.
     * [checkStyleIsAllowed] checks if a list is of allowed type.
     * [checkChildren] check requirements related to the children of a List.
     * Returns a MutableList of [ValidityReport].
     */
    override fun validate(): MutableList<ValidityReport> {
        var message = ""
        val reports: MutableList<ValidityReport> = mutableListOf()
        message = checkStyle(message)
        message = checkNumericalStyle(message)
        message = checkBulletStyle(message)
        message = checkStyleIsAllowed(message)
        message = checkChildren(message, reports)
        reports.add(ValidityReport(this, message, isValid))
        return reports
    }

    /**
     * [checkStyle] checks style requirements of a List.
     * Returns an informative message.
     */
    private fun checkStyle(message: String): String {
        var outputMessage = message
        if (style.isNullOrBlank()) {
            outputMessage = invalidate(outputMessage, MISSING_LIST_STYLE)
        }
        return outputMessage
    }

    /**
     * [checkNumericalStyle] checks numerical style requirements of a List.
     * Returns an informative message.
     */
    private fun checkNumericalStyle(message: String): String {
        var outputMessage = message
        if (style.equals(NUMERICAL)) {
            val startInt: Int? = startWith?.toInt()
            if (startInt != null && startInt < AT_LEAST_ONE_CHILD) {
                outputMessage = invalidate(outputMessage, "Starting integer ($startWith) is invalid. ")
            }
        }
        return outputMessage
    }

    /**
     * [checkBulletStyle] checks bullet style requirements of a List.
     * Returns an informative message.
     */
    private fun checkBulletStyle(message: String): String {
        var outputMessage = message
        if (style.equals(BULLET)) {
            if (bulletType != null && !allowedBulletType.contains(bulletType)) {
                outputMessage = invalidate(outputMessage, "This bullet type ($bulletType) is invalid.")
            }
        }
        return outputMessage
    }

    /**
     * [checkStyleIsAllowed] checks if a list is of allowed type.
     * Returns an informative message.
     */
    private fun checkStyleIsAllowed(message: String): String {
        var outputMessage = message
        if (!allowedStyles.contains(style)) {
            outputMessage = invalidate(outputMessage, "This style ($style) is not supported for the list. ")
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
        outputMessage = if (children.size < AT_LEAST_ONE_CHILD) {
            invalidate(outputMessage, EMPTY_LIST)
        } else {
            checkEachChild(outputMessage, reports)
        }
        return outputMessage
    }

    /**
     * [checkEachChild] checks validity of individual child component.
     * [checkChildIsBlockComponent] checks if a child is of [BlockComponent] type.
     * [generateChildValidityReports] generates a [ValidityReport] for each child.
     * Returns an informative message.
     */
    private fun checkEachChild(message: String, reports: MutableList<ValidityReport>): String {
        var outputMessage = message
        for (child in children) {
            outputMessage = checkChildIsBlockComponent(child, outputMessage)
            outputMessage = generateChildValidityReports(child, reports, outputMessage)
        }
        return outputMessage
    }

    /**
     * [checkChildIsBlockComponent] checks if a child is of [BlockComponent] type.
     * Returns an informative message.
     */
    private fun checkChildIsBlockComponent(child: Component, message: String): String {
        var outputMessage = message
        if (child !is BlockComponent) {
            outputMessage = invalidate(outputMessage, "A child ($child) is not a block component. Only block components are allowed.\n")
        }
        return outputMessage
    }
}
