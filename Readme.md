Tomcat JDBC Realm

<Realm className="org.apache.catalina.realm.JDBCRealm" connectionName="root" 
		connectionPassword="9415" connectionURL="jdbc:mysql://127.0.0.1:3306/TicketDB"
		driverName="org.gjt.mm.mysql.Driver"  
		userTable="users" userCredCol="password" userNameCol="email" 
		userRoleTable="user_roles" roleNameCol="role_name"  />

		