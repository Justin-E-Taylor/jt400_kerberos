
/*    
    jt400_kerberos is an R&D app to explore Kerberos authentication with jt400.jar
    Copyright (C) 2018 Justin Taylor

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/* See link for IBM samples:
 	https://www.ibm.com/developerworks/ibmi/library/i-sso/index.html
 	
 	See the "IBM Toolbox for Java" section and run kinit as instructed.
 	
 	I added the two env variables, but they didn't seem to be necessary,
 	  so I removed them.
*/

public class jdbc_test {

	/**
	 * For parameters 2 and 3:
	 * check "Nav for i" Network Authentication Service -> Realm 
	 * 
	 * @param args[0] IBM i hostname configured for EIM 
	 * 			args[1] Realm 
	 * 			args[2] KDC
	 * 
	 */
	public static void main(String[] args) {
		final String IBM_I = args[0];
		final String REALM = args[1];
		final String KDC = args[2];

		System.out.println("IBM i: " + IBM_I);
		System.out.println("Realm: " + REALM);
		System.out.println("KDC: " + KDC);

		try {
			System.out.println("========");
			System.out.println("JDBC");
			SimpleJDBC(IBM_I, REALM, KDC);

		} finally {
			System.out.println("Nothing follows");
		}

	}

	private static void SimpleJDBC(String ibmI, String realm, String kdc) {
		final String TABLE_NAME = "QIWS.QCUSTCDT";

		try {
			// JAAS Login
			System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");

			// Set Kerberos realm.
			System.setProperty("java.security.krb5.realm", realm);

			// Set KDC.
			System.setProperty("java.security.krb5.kdc", kdc);

			// Set driver & connect.
			Class.forName("com.ibm.as400.access.AS400JDBCDriver");
			Connection connection = DriverManager.getConnection("jdbc:as400://" + ibmI + ";translate binary=true;");

			// Run statement to test connection.
			Statement sqlStmt = connection.createStatement();
			ResultSet rs = sqlStmt.executeQuery("select * from " + TABLE_NAME);
			if (rs.next() == true) {
				System.out.println(rs.getString(1));
			}

			connection.close();
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
