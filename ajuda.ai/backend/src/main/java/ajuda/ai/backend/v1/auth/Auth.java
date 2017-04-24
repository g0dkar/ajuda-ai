package ajuda.ai.backend.v1.auth;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
/**
 * Marca um método como restrito apenas a usuários autenticados. Isso é imposto por {@link AuthInterceptor}.
 * @author Rafael Lins
 *
 */
public @interface Auth {
	
}
