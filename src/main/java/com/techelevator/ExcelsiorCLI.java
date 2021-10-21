package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class ExcelsiorCLI {

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/excelsior_venues");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		Menu menu = new Menu(dataSource);
		menu.runMainMenu();

//		ExcelsiorCLI application = new ExcelsiorCLI();
//		application.run();
	}

//	public ExcelsiorCLI(DataSource datasource) {
//		// create your DAOs here
//
//	}

//	public void run() {
//
//		//TODO call menu()
//		Menu menu = new Menu(datasource);
//		//menu.
//
//	}
}
