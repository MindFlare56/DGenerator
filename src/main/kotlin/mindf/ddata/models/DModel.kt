package mindf.ddata.models

@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)

annotation class DModel(val method: String = "[unassigned]")