package ajuda.ai.payment.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Stereotype;

/**
 * Define uma classe como implementação de um serviço de pagamento
 */
@Target(ElementType.TYPE)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Stereotype
@RequestScoped
public @interface PaymentServiceImpl {
    String value() default "";
}