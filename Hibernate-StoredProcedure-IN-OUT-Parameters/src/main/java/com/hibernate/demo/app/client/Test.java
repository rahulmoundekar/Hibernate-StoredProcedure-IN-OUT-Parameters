package com.hibernate.demo.app.client;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionImpl;

public class Test {

	public static SessionFactory getSessionFactory() {
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		SessionFactory factory = configuration.buildSessionFactory(builder.build());
		return factory;
	}

	public void storedProcedureCallingOUTParamenter() throws SQLException {

		Session session = getSessionFactory().openSession();
		Connection con = ((SessionImpl) session).connection();
		Transaction tx = session.beginTransaction();

		/*
		 * DELIMITER $$
		 * 
		 * DROP PROCEDURE IF EXISTS `hibernate`.`selectName` $$ CREATE PROCEDURE
		 * `hibernate`.`selectrow` (in eid int,out ename varchar(45)) BEGIN select name
		 * form employee where id=eid; END $$
		 * 
		 * DELIMITER ;
		 */

		// crate callable Statement obj
		CallableStatement cs = con.prepareCall("{call selectName(?,?)}");

		

		// register OUT parameter wiht JDBC Types
		cs.registerOutParameter(2, Types.INTEGER);
		// set values to IN paramters
		// for where employee id=1;
				cs.setInt(1, 1);
		// call PL/SQL procedure
		cs.execute();
		// gather result from OUT params
		System.out.println("nmmber of employees are fired=" + cs.getInt(2));
		tx.commit();
		cs.close();
		con.close();
	}

	public static void main(String[] args) {
		Test test = new Test();
		try {
			test.storedProcedureCallingOUTParamenter();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
