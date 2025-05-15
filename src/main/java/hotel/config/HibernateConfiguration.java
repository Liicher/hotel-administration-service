package hotel.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class HibernateConfiguration {
	@Value("${spring.jpa.properties.hibernate.dialect}")
	private String hibernateDialect;
	@Value("${spring.jpa.properties.hibernate.show_sql}")
	private String hibernateShowSql;
	@Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
	private String hibernateHbm2ddlAuto;
}
