package whtsAppClone.models

/**
 * Created by Kush on 26/08/2020.
 */
class FriendlyMessage() {
    var id: String? = null
    var text: String? = null
    var name: String? = null

    constructor(id: String, text: String, name: String) : this() {
        this.id = id
        this.text = text
        this.name = name
    }
}