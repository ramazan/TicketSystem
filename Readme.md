## 32Bit 2016 Grup 1  - Raporlama Sistemi

##Nasıl Kurulur?  
1. Projeyi git üzerinden clone edin.

2. **Eclipse** üzerinden aşağıdaki adımlarla projeyi import edin.
>File -> import -> General -> Existing Project  

3. Proje klasöründe **db_dumps** içindeki database yedeklerini Mysql workbench üzerinden import edin.
	* Database adının **TicketDB** olduğuna dikkat edin.

4.  Database bağlantısı için tüm yetkilere sakip bir kullanıcı oluşturun.
> username : testUser
> password : testUser

5. Tomcat **server.xml** dosyasına aşağıdaki _realm_ bilgilerini ekleyin.
>< Realm className="org.apache.catalina.realm.JDBCRealm"  
	connectionName="testUser"  
	connectionPassword="testUser"  
	connectionURL="jdbc:mysql://127.0.0.1:3306/TicketDB"  
	driverName="org.gjt.mm.mysql.Driver"  
	userTable="users"  
	userCredCol="password"  
	userNameCol="email"  
	userRoleTable="users"  
	roleNameCol="role"  />

6. Projeyi çalıştırıp tarayıcı aracılığıyla testleri yapabilirsiniz.

#### Test Kullanıcıları
> admin :   *email*: **hm** *password*:**1**
> client :   *email*: **c** *password*:**1**
> supporter :   *email*: **s** *password*:**1**


### Katkıda Bulunanlar
> Hasan Men - GTU  
> Nur Sinem Dere - ITU
