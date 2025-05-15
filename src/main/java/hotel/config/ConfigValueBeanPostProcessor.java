package hotel.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
public class ConfigValueBeanPostProcessor implements BeanPostProcessor {
	private final Environment environment;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			setAnnotatedField(bean, field);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	private void setAnnotatedField(Object bean, Field field) {
		Annotation annotation = field.getAnnotation(ConfigValue.class);
		if (annotation != null) {
			field.setAccessible(true);
			String defaultPropertyName = bean.getClass().getName() + "." + field.getName();
			Object value = environment.getProperty(defaultPropertyName, field.getType());
			if (value != null) {
				ReflectionUtils.setField(field, bean, value);
			}
		}
	}
}
