package no.wtw.android.annotation.linky

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.jvm.throws
import no.wtw.android.restserviceutils.exceptions.LinkNotResolvedException
import no.wtw.android.restserviceutils.resource.Link
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class LinkyProcessor : AbstractProcessor() {

    private lateinit var typeUtils: Types
    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var elements: Elements

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        messager = processingEnv.messager
        elements = processingEnv.elementUtils
        typeUtils = processingEnv.typeUtils
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
                    val packageName = elements.getPackageOf(element).toString()
                    val annotatedClassName = element.simpleName.toString()

                    val linkValue = element.getAnnotation(Linky::class.java).linkName
                    val clazzValue = typeUtils.asElement(element.getAnnotation(Linky::class.java).getClazz())

                    val receiverClassName = ClassName(packageName, annotatedClassName)
                    val linkClassName = ClassName.bestGuess(Link::class.qualifiedName.toString())
                    val paramClassName = ClassName.bestGuess(clazzValue.toString())
                    val returnClassName = linkClassName.parameterizedBy(paramClassName)

                    FileSpec.builder(packageName, annotatedClassName + "Linky" + linkValue.capitalize())
                            .addFunction(FunSpec.builder("get" + linkValue.capitalize() + "Link")
                                    .receiver(receiverClassName)
                                    .throws(LinkNotResolvedException::class)
                                    .addStatement("val a = 1")
                                    .addStatement("return this.getLink(%T::class.java, %S)", clazzValue, linkValue)
                                    .returns(returnClassName)
                                    .build()
                            )
                            .build()
                            .writeTo(filer)
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


