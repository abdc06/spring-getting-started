package me.abdc.relationaldataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class RelationalDataAccessApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RelationalDataAccessApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RelationalDataAccessApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("테이블 생성..");

        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

        // 전체 이름 배열을 이름/성 배열로 분할
        List<Object[]> splitUpNames = Arrays.asList("태호 김", "호진 이", "진우 박", "원식 우", "태호 이").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Java 8 스트림을 사용하여 목록의 각 튜플을 출력
        splitUpNames.forEach(name -> log.info(String.format("%s %s 고객에 대한 레코드 삽입.. ", name[0], name[1])));

        // JdbcTemplate의 batchUpdate 작업을 사용하여 데이터를 한번에 로드
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        log.info("이름이 '태호'인 고객의 레코드를 쿼리하여 결과를 출력");
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "태호" },
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
    }

}
