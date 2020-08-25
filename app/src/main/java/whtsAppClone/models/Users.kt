package whtsAppClone.models

/**
 * Created by Kush on 24/08/2020.
 */
data class Users(var DisplayName: String, var image: String,
    var thumb_image: String, var Status: String) {
    constructor() : this("", "", "", "")
}