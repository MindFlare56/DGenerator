package mindf.ddata.models

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)

annotation class DData constructor(val name: String = "[unassigned]", val type: String = "[unassigned]")
