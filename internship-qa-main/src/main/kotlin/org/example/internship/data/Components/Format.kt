package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.InlineComponent

@Serializable
@SerialName("format")
class Format : Component(), InlineComponent {
    var style: String? by Properties()
    var color: String? by Properties()
}