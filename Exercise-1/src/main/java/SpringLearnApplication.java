package com.cognizant.springlearn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SpringBootApplication
public class SpringLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringLearnApplication.class);

    public static void main(String[] args) {
        LOGGER.info("START main()");

        SpringApplication.run(SpringLearnApplication.class, args);

        SpringLearnApplication app = new SpringLearnApplication();
        app.displayDate();
        app.displayCountry();
        app.displayCountries();

        LOGGER.info("END main()");
    }

    // Hands on 2: load SimpleDateFormat bean from date-format.xml
    public void displayDate() {
        LOGGER.info("START");

        ApplicationContext context = new ClassPathXmlApplicationContext("date-format.xml");
        SimpleDateFormat format = context.getBean("dateFormat", SimpleDateFormat.class);

        try {
            Date date = format.parse("31/12/2018");
            LOGGER.debug("{}", date);
        } catch (ParseException e) {
            LOGGER.error("Error parsing date", e);
        }

        LOGGER.info("END");
    }

    // Hands on 4 & 5: load Country bean from country.xml, demonstrate singleton/prototype scope
    public void displayCountry() {
        LOGGER.info("START");

        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        Country country = context.getBean("country", Country.class);
        Country anotherCountry = context.getBean("country", Country.class);

        LOGGER.debug("Country : {}", country.toString());
        LOGGER.debug("Another Country : {}", anotherCountry.toString());

        LOGGER.info("END");
    }

    // Hands on 6: load list of Country beans from country.xml
    @SuppressWarnings("unchecked")
    public void displayCountries() {
        LOGGER.info("START");

        ApplicationContext context = new ClassPathXmlApplicationContext("country.xml");
        List<Country> countryList = (List<Country>) context.getBean("countryList");

        for (Country country : countryList) {
            LOGGER.debug("Country : {}", country.toString());
        }

        LOGGER.info("END");
    }
}
