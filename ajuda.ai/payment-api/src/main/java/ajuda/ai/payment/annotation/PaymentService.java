package ajuda.ai.payment.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

/**
 * Define uma classe como implementação de um serviço de pagamento
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface PaymentService {
    String value() default "";
}