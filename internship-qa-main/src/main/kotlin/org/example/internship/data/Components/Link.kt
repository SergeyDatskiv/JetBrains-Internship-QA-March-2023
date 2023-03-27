package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.ValidityReport
import java.util.regex.Matcher
import java.util.regex.Pattern

// Message constants.
private const val MISSING_HREF = "There is no href."
private const val EMPTY_LINK = "The link is empty. "
private const val AT_LEAST_ONE_CHILD = 1
private const val HTTP = "http"
private const val HTTPS = "https"
// Source: https://www.geeksforgeeks.org/check-if-an-url-is-valid-or-not-using-regular-expression/
private const val URL_REGEX = (
    "((http|https)://)(www.)?" +
        "[a-zA-Z0-9@:%._\\+~#?&//=]" +
        "{2,256}\\.[a-z]" +
        "{2,6}\\b([-a-zA-Z0-9@:%" +
        "._\\+~#?&//=]*)"
    )

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
            // URL validation is inspired by https://www.geeksforgeeks.org/check-if-an-url-is-valid-or-not-using-regular-expression/.
            val p: Pattern = Pattern.compile(URL_REGEX)
            val match: Matcher = p.matcher(href)
            if (!match.matches()) {
                outputMessage = invalidate(outputMessage, "Invalid URL. ($href) ")
                return outputMessage
            }
            outputMessage = checkHrefProtocols(outputMessage)
        }
        return outputMessage
    }

    /**
     * [checkHrefProtocols] checks if allowed protocol is used.
     * This function could be changed/removed since protocls can be checked in regex.
     * Returns an informative message.
     */
    private fun checkHrefProtocols(message: String): String {
        var outputMessage = message
        for (protocol in allowedProtocols) {
            val checkProtocol: Boolean = href!!.contains(protocol)
            if (checkProtocol) {
                break
            }
            outputMessage = invalidate(outputMessage, "The protocol of a link is not http or https. ($href) ")
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
