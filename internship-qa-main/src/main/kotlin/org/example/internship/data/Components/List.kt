package org.example.internship.data.Components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.internship.data.BlockComponent

@Serializable
@SerialName("list")
class List : Component(), BlockComponent {
    var style: String? by Properties()
    var startWith: String? by Properties()
    var bulletType: String? by Properties()
}