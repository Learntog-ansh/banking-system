import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

class Banker extends central
{
Connection con;//Global variable for the whole class
Connection con1;//Global variable for the whole class
Statement stmt;//Global variable for the whole class
Statement stmt1;
ResultSet view;
ResultSet r1;
//Global variable for the whole class
Scanner s=new Scanner(System.in);
Bank ba=new Bank();
Customer c=new Customer();
private String name;
Banker()
{
String name=new String();
System.out.print("\n\n\n******************************************");
System.out.print("\nEnter Your name please: ");
name=s.nextLine();
this.name=name;
}
protected void estabilish(int i)
{
try{
	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","system");
	Connection con1=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","system");
	Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	Statement stmt1=con1.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	ResultSet r=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident from customerdbase1");	
	ResultSet w=stmt.executeQuery("select acc_no,type,amount,balance,dot from finaltrans");	
this.con=con;	
this.stmt=stmt;
this.view=r;
this.stmt1=stmt1;
this.r1=w;
this.con1=con1;
}
catch(Exception e){System.out.println(e);}
}
protected void transforcust()
{
try{
//view=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident from customerdbase1");
con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","system");
stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
view=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident,pwd,acctype from customerdbase1");
System.out.print("Enter the five digit account number");
String st=s.next();
System.out.print("Enter central database password to continue");
String pw=s.next();
if(pw.equals(pwd_official))
{
while(view.next())
{
if(st.equals(view.getString(1)))
{
int rown=view.getRow();
c.portal(rown);
break;
}
}
official();
}
}
catch(Exception e)
{System.out.println(e);}
}
protected void official() throws Exception
{
System.out.print("Please enter the password for accessing central database :");
String pwd=s.next();
if(pwd.equals(pwd_official))//password authentication
{
estabilish(1);//to estabilish connection only when password authenticated.
while(true){
System.out.println("*******************************\nHello Official : "+name);
System.out.println("\n*******************************\nWhat do you want to do today?\t1.Add accounts\t2.Delete Accounts\t3.View/List Accounts\t4.Transact for customer\t 5.Go back to main option\nPlease choose your option");
int mc=s.nextInt();
switch(mc)//menu driver
{
case 1:addAccount();//adding new customer
break;
case 2:deleteAccount();//delete account
break;
case 3:view();//view the saved accounts
break;
case 4:transforcust();
break;
case 5:ba.port();
break;
default:{
System.out.println("No  such choice!!Exiting the portal");
System.exit(0);
}
break;
}
}
}
else
{
System.out.println("\n*****************\nUnsuccessful attempt reported*****************\n");
ba.port();
}
}
protected void validator(int i,int n) throws Exception
{
String choice=new String();
int sno=0;
switch(n)
{
case 1:
{
System.out.println("Enter the unique Id no.");
choice=s.next();
view=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident from customerdbase1");
System.out.println("S no.\tAccount Number\tfirst name\tlast name\tmobile\tbalance\tuser id");
int c=0;
while(view.next())
{
c++;
if(choice.equals(view.getString(6)))
{
System.out.printf("%2d\t%10s\t%10s\t%10s\t%6.3f\t%6.3f\t",s,view.getString(1),view.getString(2),view.getString(3),view.getString(4),view.getFloat(5),view.getString(6));
if(i==2)
{
ResultSet r1=stmt1.executeQuery("select acc_no,type,amount,balance from finaltrans");
String andel=view.getString(1);
int trand=0;
while(r1.next())
{
if(andel.equals(r1.getString(1)))
{
trand++;
r1.deleteRow();
}
}
System.out.println("removing "+trand+"transactions");
view.deleteRow();
System.out.println("Row deleted");
break;
}
break;
}
}
}
break;
case 2:{
System.out.println("Enter the 10 digit unique account no.");
choice=s.next();
view=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident from customerdbase1");
while(view.next())
{
if(choice.equals(view.getString(1)))
{
System.out.printf("%2d\t%10s\t%10s\t%10s\t%6.3f\t%6.3f\t",s,view.getString(1),view.getString(2),view.getString(3),view.getString(4),view.getFloat(5),view.getString(6));
if(i==2)
{
ResultSet r1=stmt1.executeQuery("select acc_no,type,amount,balance from finaltrans");
String andel=view.getString(1);
int trand=0;
while(r1.next())
{
if(andel.equals(r1.getString(1)))
{
trand++;
r1.deleteRow();
}
}
System.out.println("removing "+trand+"transactions");
view.deleteRow();System.out.println("Row deleted");
break;
}
break;
}
}
break;
}
case 3:{
System.out.println("Enter the mobile no.");
choice=s.next();
view=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident from customerdbase1");
System.out.println("S no.\tAccountNumber\tfirst name\tlast name\tmobile\tbalance\tuser id");
while(view.next())
{
if(choice.equals(view.getString(4)))
{
sno++;
System.out.printf("%2d\t%10s\t%10s\t%10s\t%6.3f\t%6.3f\t",s,view.getString(1),view.getString(2),view.getString(3),view.getString(4),view.getFloat(5),view.getString(6));
if(i==2)
{
ResultSet r1=stmt1.executeQuery("select acc_no,type,amount,balance from finaltrans");
String andel=view.getString(1);
int trand=0;
while(r1.next())
{
if(andel.equals(r1.getString(1)))
{
trand++;
r1.deleteRow();
}
}
System.out.println("removing "+trand+"transactions");
System.out.println("Account number "+view.getString(1)+" deleted");view.deleteRow();break;
}
break;
}
}
}//end of case 3
}//end of switch
}//end of validator
 static char[] OTP(int len) 
    { 
  
        // Using numeric values 
        String numbers = "0123456789"; 
  
        // Using random method 
        Random rndm_method = new Random(); 
  
        char[] otp = new char[len]; 
  
        for (int i = 0; i < len; i++) 
        { 
            // Use of charAt() method : to get character value 
            // Use of nextInt() as it is scanning the value as int 
            otp[i] = 
             numbers.charAt(rndm_method.nextInt(numbers.length())); 
        } 
        return otp; 
    }
 void addAccount() 
{
try{
String ano=new String();
String amobile=new String();
String aid=new String();
view=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident,pwd,acctype from customerdbase1");
view.moveToInsertRow();
System.out.print("Enter the unique account number: ");
ano=s.next();
view.updateString(1,ano);
System.out.print("Enter account holder first name: ");
String aname=s.next();
view.updateString(2,aname);
System.out.print("Enter account holder last name: ");
String alname=s.next();
view.updateString(3,alname);
System.out.print("Enter account holder mobile number: ");
amobile=s.next();
view.updateString(4,amobile);
System.out.print("Enter account holder initial balance: ");
float abal=s.nextFloat();
view.updateFloat(5,abal);
System.out.print("Enter account holder unique id: ");
aid=s.next();
view.updateString(6,aid);
String x=new String(OTP(4));
view.updateString(7,x);
view.updateInt(8,1);
view.insertRow();
view.last();
System.out.println("new Account added");
System.out.println(x+" is the OTP for login. To be changed on first login");
official();
}
catch(Exception e)
{}
}
protected void deleteAccount()
{
try
{
Thread.sleep(2000);
System.out.print("Delete entry by :\n1Id no.\t2.Account no.\t3.Registered Mobile number\nPlease select your option:");
int n=s.nextInt();
validator(2,n);
}
catch(Exception e)
{
System.out.println(e);
}
}
protected void view() throws Exception
{
int opt=0;
try
{
Thread.sleep(1000);
System.out.print("How do you want to view the database\n1.All entries.\t2. View Specific Entry\nPlease choose your option:");
opt=s.nextInt();
	}
catch(Exception e)
{
System.out.println(e);
}
switch(opt)
{
case 1:{
System.out.println("S no.\tAccount Number\tname\t mobile\t balance\t userid");
view=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident from customerdbase1");
int i=0;
while(view.next())
{i++;
System.out.print(i+"\t");
System.out.println(view.getString(1)+"\t"+view.getString(2)+"\t"+view.getString(3)+"\t"+view.getString(4)+"\t"+view.getFloat(5)+"\t"+view.getString(6));
}
}
break;
case 2:{
try
{
Thread.sleep(2000);
System.out.print("View entry by :\n1Id no.\t2.Account no.\t3.Registered Mobile number\nPlease select your option:");
int n=s.nextInt();
validator(1,n);
}
catch(Exception e)
{
System.out.println(e);
}
}
}
}
}

