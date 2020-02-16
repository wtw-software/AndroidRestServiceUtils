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
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
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
        roundEnvironment.getElementsAnnotatedWith(Linkys::class.java)
                .filter { it.kind === ElementKind.CLASS }
                .forEach { element ->
                    val packageName = elements.getPackageOf(element).toString()
                    val annotatedClassName = element.simpleName.toString()

                    val builder = FileSpec.builder(packageName, annotatedClassName + "_LinkyExt")

                    element.getAnnotation(Linkys::class.java).value.forEach { linky ->

                        val linkValue = linky.linkName
                        val clazzValue = typeUtils.asElement(linky.getClazz())

                        val receiverClassName = ClassName(packageName, annotatedClassName)
                        val linkClassName = ClassName.bestGuess(Link::class.qualifiedName.toString())
                        val paramClassName = ClassName.bestGuess(clazzValue.toString())

                        builder.addFunction(FunSpec.builder("get" + linkValue.toCamelCase() + "Link")
                                .receiver(receiverClassName)
                                .throws(LinkNotResolvedException::class)
                                .addStatement("return this.getLink(%T::class.java, %S)", clazzValue, linkValue)
                                .returns(linkClassName.parameterizedBy(paramClassName))
                                .build()
                        )

                    }
                    builder.build().writeTo(filer)
                }
        return true
    }

}

fun Linky.getClazz(): TypeMirror {
    try {
        this.clazz
    } catch (mte: MirroredTypeException) {
        return mte.typeMirror
    }
    throw RuntimeException("Type fetching failed")
}

fun String.toCamelCase(): String {
    var result = ""
    val split = this.split("-", "_")
    split.forEach { s -> result += s.capitalize() }
    return result
}