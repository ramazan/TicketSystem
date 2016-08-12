## 32Bit 2016 Grup 1  - Raporlama Sistemi

##Nasıl Kurulur?  
1. Projeyi git üzerinden clone edin.

2. **Eclipse** üzerinden aşağıdaki adımlarla projeyi import edin.
>File -> import -> General -> Existing Project  

3. Database uzak sunucuya kurulmuştur. IP bilgileri aşağıda mevcuttur.

4. (Seçenek) MySQL workbench üzerinden db ye ulaşmak için aşağıdaki bilgileri kullanbilirsiniz.
> url : 188.213.175.52:3306  
> username : ticket  
> password : Ticket32++  

6. ***Buradan sonrası seçmelidir. ServerFiles dizinindeki contex.xml ve server.xml dosyalarını server dizinine atın yada aşağıdaki adımlar ile tek tek ekleme yapım.***
7. Tomcat **server.xml** dosyasına aşağıdaki _realm_ bilgilerini ekleyin.
>     <Realm className="org.apache.catalina.realm.JDBCRealm"  
	connectionName="ticket"  
	connectionPassword="Ticket32++"  
	connectionURL="jdbc:mysql://188.213.175.52:3306/TICKET"  
	driverName="org.gjt.mm.mysql.Driver"  
	userTable="users"  
	userCredCol="password"  
	userNameCol="email"  
	userRoleTable="user_roles"  
	roleNameCol="role"  />
8. Connection Pool ayarlar için aşağıyı takip edin.
	* Tomcat **contex.xml** dosyasina aşağıdaki kodları ekleyin.
>     <ResourceLink type="javax.sql.DataSource"
                name="jdbc/TicketDB"
                global="jdbc/TicketDB"/>
	* Tomcat **server.xml** dosyasina aşağıdaki kodları ekleyin.
>     <Resource type="javax.sql.DataSource"
			auth="Container"
            name="jdbc/TicketDB"
            driverClassName="com.mysql.jdbc.Driver"
			factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
            url="jdbc:mysql://188.213.175.52:3306/TICKET"
            username="ticket"
            password="Ticket32++"
			initialSize="2"
            maxActive="20"
            maxIdle="10"
            minIdle="5"/>
	
9. Projeyi çalıştırıp tarayıcı aracılığıyla testleri yapabilirsiniz.

#####NOT: Log dosyasi home dizininde oluşturulur.

#### Test Kullanıcıları
> admin :   *email*: **a** *password*:**1**  
> client :   *email*: **c** *password*:**1**  
> supporter :   *email*: **s** *password*:**1**  

### Katkıda Bulunanlar
> Hasan Men - GTU  
> Nur Sinem Dere - ITU
> Ramazan Demir - KOÜ
