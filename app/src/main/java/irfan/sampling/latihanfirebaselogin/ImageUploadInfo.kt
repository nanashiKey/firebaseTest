package irfan.sampling.latihanfirebaselogin

/**
 * Created by AndroidJSon.com on 6/10/2017.
 */


class ImageUploadInfo {

    lateinit var imageName: String

    lateinit var imageURL: String

    constructor() {

    }

    constructor(name: String, url: String) {

        this.imageName = name
        this.imageURL = url
    }

}
