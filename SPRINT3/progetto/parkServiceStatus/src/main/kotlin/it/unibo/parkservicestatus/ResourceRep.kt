package it.unibo.parkservicestatus

class ResourceRep {
    var content: String? = null
        private set

    constructor() {}
    constructor(content: String?) {
        this.content = content
    }
}