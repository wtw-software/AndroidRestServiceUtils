package no.wtw.android.annotation.linky

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import no.wtw.android.restserviceutils.resource.Link
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class LinkyProcessor : AbstractProcessor() {

    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var elements: Elements

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        messager = processingEnv.messager
        elements = processingEnv.elementUtils;
    }

    override fun process(annotations: Set<TypeElement?>, roundEnvironment: RoundEnvironment): Boolean {

/*
    fun RootResource.getDeviceRegistrationLink(): Link<DeviceRegistrationResponse> {
        return this.getLink(DeviceRegistrationResponse::class.java, "deviceRegistration")
    }
*/

        roundEnvironment.getElementsAnnotatedWith(Linky::class.java)
                .filter { it.kind === ElementKind.CLASS }
                .forEach { element ->
                    val typeElement = element as TypeElement
                    val linkName = element.getAnnotation(Linky::class.java).linkName
                    // val returnClass = element.getAnnotation(Linky::class.java).clazz.simpleName
                    val file = FileSpec.builder(elements.getPackageOf(element).toString(), typeElement.simpleName.toString() + "Linky" + linkName.capitalize())

                    file.addImport(Link::class.java, "")

                    file.addFunction(

                            FunSpec.builder("get" + linkName.capitalize() + "Link")
                                    .addCode(
                                            " // return getLink(\"" + linkName + "\", DeviceRegistrationResponse::class.java)".trimMargin()
                                    )
                                    .build()
                    )
                    file.build().writeTo(filer)
                }
        return true
    }

}
/*

fun TypeElement.packageName() = enclosingElement.packageName()

private fun Element?.packageName(): String {
    return when (this) {
        is TypeElement -> packageName()
        is PackageElement -> qualifiedName.toString()
        else -> this?.enclosingElement?.packageName() ?: ""
    }
}*/
