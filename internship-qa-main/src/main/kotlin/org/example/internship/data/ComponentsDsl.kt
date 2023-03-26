package org.example.internship.data

import org.example.internship.data.Components.Chapter
import org.example.internship.data.Components.Image
import org.example.internship.data.Components.List
import org.example.internship.data.Components.Paragraph
import org.example.internship.data.Components.Text

fun list(init: List.() -> Unit): List {
    return List().also(init)
}

fun chapter(init: Chapter.() -> Unit): Chapter {
    return Chapter().also(init)
}

fun paragraph(init: Paragraph.() -> Unit): Paragraph {
    return Paragraph().also(init)
}

fun image(init: Image.() -> Unit): Image {
    return Image().also(init)
}

fun format(init: Format.() -> Unit): Format {
    return Format().also(init)
}

fun link(init: Link.() -> Unit): Link {
    return Link().also(init)
}

fun text(init: Text.() -> Unit): Text {
    return Text().also(init)
}