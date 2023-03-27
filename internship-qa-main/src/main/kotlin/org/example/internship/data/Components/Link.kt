package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport

// Message constants.
private const val MISSING_HREF = "There is no href."
private const val EMPTY_LINK = "The link is empty. "
private const val AT_LEAST_ONE_CHILD = 1
private const val HTTP = "http"
private const val HTTPS = "https"

@Serializable
@SerialName("link")
class Link : Component(), InlineComponent {
    var href: String? by Properties()
    private var allowedProtocols: MutableSet<String> = mutableSetOf(HTTP, HTTPS)

    /**
     * [validate] is Link specific function which checks the validity of a Link.
     * [checkHref] checks the requirements href of a Link.
     * [checkChildren] check requirements related to the children of a Link.
     * Returns a MutableList of [ValidityReport].
     */
    override fun validate(): MutableList<ValidityReport> {
        var message = ""
        val reports: MutableList<ValidityReport> = mutableListOf()
        message = checkHref(message)
        message = checkChildren(message, reports)
        reports.add(ValidityReport(this, message, isValid))
        return reports
    }

    /**
     * [checkHref] checks if a link has a supported protocol ([allowedProtocols]), and if it is a link.
     * Returns an informative message.
     */
    private fun checkHref(message: String): String {
        var outputMessage = message
        if (href.isNullOrBlank()) {
            outputMessage = invalidate(outputMessage, MISSING_HREF)
        } else {
            for (protocol in allowedProtocols) {
                // TODO: Check if URL is valid.
                val checkProtocol: Boolean = href!!.contains(protocol)
                if (checkProtocol) {
                    break
                }
                outputMessage = invalidate(outputMessage, "The protocol of a link is not http or https. ($href) ")
            }
        }
        return outputMessage
    }

    /**
     * [checkChildren] checks requirements related to the children of a Link.
     * [AT_LEAST_ONE_CHILD] checks if a Link has at least one child.
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
     * [checkChildTextType] checks if a child is of [Text] type.
     * [generateChildValidityReports] generates a [ValidityReport] for each child.
     * Returns an informative message.
     */
    private fun checkEachChild(message: String, reports: MutableList<ValidityReport>): String {
        var outputMessage = message
        for (child in children) {
            outputMessage = checkChildTextType(child, outputMessage)
            outputMessage = generateChildValidityReports(child, reports, outputMessage)
        }
        return outputMessage
    }

    /**
     * [checkChildTextType] checks if a child is of [Text] type.
     * Returns an informative message.
     */
    private fun checkChildTextType(child: Component, message: String): String {
        var outputMessage = message
        if (child !is Text) {
            outputMessage = invalidate(
                outputMessage,
                "A child ($child) is not a Text component. Only Text components are allowed.\n"
            )
        }
        return outputMessage
    }
}
