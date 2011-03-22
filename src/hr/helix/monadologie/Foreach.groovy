package hr.helix.monadologie

class Foreach {
    static foreach(Closure action) {
        action.delegate = new Each()
        action.resolveStrategy = Closure.DELEGATE_ONLY
        action()
    }
}
