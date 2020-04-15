import java.util.*;
import java.lang.*;
import java.sql.*;
import java.time.*;
abstract class central
{
final String pwd_official="Ansh!2345";
 abstract void addAccount();
 abstract void deleteAccount();
 abstract void view() throws Exception;
 
}
class Customer
{
int row;
String name=new String();
static int x=3;
Statement stmt;
Statement stmt1;
Connection con;
private ResultSet viewtrans;
private ResultSet viewcdbase;
Scanner s=new Scanner(System.in);
Bank b=new Bank();
Customer()
{
this.name=name;
try
{
	Class.forName("oracle.jdbc.driver.OracleDriver");
	Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","system");
	Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	Statement stmt1=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	ResultSet r=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident,pwd,acctype from customerdbase1");	
	ResultSet r1=stmt1.executeQuery("select acc_no,type,amount,balance from finaltrans");
this.con=con;	
this.stmt=stmt;
this.stmt1=stmt1;
this.viewcdbase=r;
this.viewtrans=r1;
}
catch(Exception e)
{
System.out.println(e);
}
}
protected final void pincheck(int rowno) throws Exception
{
System.out.println("You have "+x+ " attempts to enter the correct pin!.");
viewcdbase.absolute(rowno);
System.out.print("Please enter the 4 digit pin: ");
String pin=new String();
pin=s.next();
if(pin.equals(viewcdbase.getString(7)))
{
if(viewcdbase.getInt(8)==1)
{
System.out.println("As this is your first login. Please change the password.");
System.out.print("Enter your new 4 digit password:");
pin=s.next();
if(pin.length()==4)
{
viewcdbase.updateString(7,pin);
viewcdbase.updateInt(8,0);
viewcdbase.updateRow();
portal(rowno);
}
}
else
portal(rowno);
}
else
{
x--;
System.out.println("Password wrong.login denied.");
pincheck(rowno);
if(x==0)
{
System.out.println("Going back to main menu.");
consumer();
}
}//password incoorect loop
}
protected void addtrans(String acc_no,float am,float balance,String note) throws Exception
{
viewtrans=stmt1.executeQuery("select acc_no,type,amount,balance,dot from finaltrans");
viewtrans.last();
LocalDate today=LocalDate.now();
LocalTime time=LocalTime.now();
String dandt=today+"  "+time.getHour()+":"+time.getMinute();
viewtrans.moveToInsertRow();
viewtrans.updateString(1,acc_no);
viewtrans.updateString(2,note);
viewtrans.updateFloat(3,am);
viewtrans.updateFloat(4,balance);
viewtrans.updateString(5,dandt);
viewtrans.insertRow();
if(viewcdbase.getFloat(5)==viewtrans.getFloat(4))
{System.out.print("TRANSACTION SUCCESSFUL.Rs. "+viewtrans.getFloat(3));
if(note.equals("cr") || note.equals("in")){System.out.print("  credited to your account.\nUpdated balance=");}
else
System.out.print("debited from your account.\nUpdated balance= ");
System.out.println(viewcdbase.getFloat(5));
portal(row);
}
}
protected void modify() throws Exception
{
System.out.print("Hello "+name+" .What details you want to change or update\n1.Update Phone number\t2.Change pin\t3.Exit\nPlease select");
int cho=s.nextInt();
switch(cho)
{
case 1:{
System.out.print("Enter the unique Id submitted at the time of registration:");
String idc=new String();
idc=s.next();
String checkme=viewcdbase.getString(6);
if(idc.equals(checkme))
{
System.out.print("Enter the new Mobile number :");
idc=s.next();
viewcdbase.updateString(4,idc);
viewcdbase.updateRow();
System.out.println("Mobile number updated successfully !!!");
portal(row);
}
else
{
System.out.println("Ids do not match.Exiting...");
portal(row);
}
}
break;
case 2:{
System.out.print("Enter the previous pin to continue: ");
String op=new String();
op=s.next();
if(op.equals(viewcdbase.getString(7)))
{
String op1=new String();
	System.out.print("Please enter the 4 digit new pin.");
	op=s.next();
	System.out.print("Please reenter the 4 digit new pin.");
	op1=s.next();
	if(op.equals(op1))
	{
	viewcdbase.updateString(7,op);
	viewcdbase.updateRow();
	System.out.println("New pin updated");
	consumer();
	}
	else
	{
	System.out.println("pins do not match.try again !!");
	modify();
	}
}
else
{
System.out.println("Wrong pin entered.Exiting the portal");
consumer();
}
}
break;
case 3:consumer();
break;
default:consumer();
break;
}
}
protected void genstate(int ch) throws Exception
{
int n=0;
if(ch==1)
{
viewtrans=stmt1.executeQuery("select acc_no,type,amount,balance,dot from finaltrans");
String acc=viewcdbase.getString(1);
System.out.println("Generating passbook for account number :"+acc);
while(viewtrans.next())
{
if(viewtrans.getString(1).equals(acc))
{
n++;
System.out.printf("%d\t%20s\t%3s\t%6.3f\t%6.3f\n",n,viewtrans.getString(5),viewtrans.getString(2),viewtrans.getFloat(3),viewtrans.getFloat(4));
}
}
}
System.out.println("********************end*******************");
portal(row);
}

protected final void checkforinterest() throws Exception
{
int flag=0;
float openbal=viewcdbase.getFloat(5);
System.out.println("OPENING BALANCE= "+openbal+"\n"+"*******************");
viewtrans=stmt1.executeQuery("select acc_no,type,amount,balance,dot from finaltrans");
String acc=viewcdbase.getString(1);
String datecheck=new String();
LocalDate today=LocalDate.now();
while(viewtrans.next())
{
if(viewtrans.getString(1).equals(acc))
{
if(viewtrans.getString(2).equals("in"))
{
flag=0;
break;
}
else
{
datecheck=viewtrans.getString(5);
flag=1;
}
}
}
if(flag==1)//to check whether interest for the month already credited or not.
{
String mm=datecheck.substring(5,7);
int mnt=Integer.parseInt(mm);
int dd=Integer.parseInt(datecheck.substring(8,10));
int a=today.getMonthValue();
if(a-mnt==1 && dd<21)
{
float interest=(float).0325*openbal;
openbal+=interest;
System.out.println("Interest credited :"+interest);
viewcdbase.updateFloat(5,openbal);
viewcdbase.updateRow();
addtrans(acc,interest,openbal,"in");
}
}
}
protected void portal(int rownoport) throws Exception
{
row=rownoport;
viewcdbase.absolute(rownoport);
name=viewcdbase.getString(2)+" "+viewcdbase.getString(3);
System.out.println("Hello"+name+", Account number: "+viewcdbase.getString(1));
checkforinterest();
System.out.print("What do you want to do today?1.Add money\t2.Withdraw money.\t3.Change Details\t4.Get bank statement\t5.Exit\n Please enter your choice:");
int ch=s.nextInt();
switch(ch)
{
case 1:{
float a=viewcdbase.getFloat(5);
System.out.println("Enter the amount of money you want to add:");
float b=s.nextFloat();
float fin=a+b;
viewcdbase.updateFloat(5,fin);
viewcdbase.updateRow();
addtrans(viewcdbase.getString(1),b,fin,"cr");
}
break;
case 2:{
float a=viewcdbase.getFloat(5);
System.out.println("Enter the amount of money you want to withdraw");
float b=s.nextFloat();
if(a-b>500)
{
float fin=a-b;
viewcdbase.updateFloat(5,fin);
viewcdbase.updateRow();
addtrans(viewcdbase.getString(1),b,fin,"dr");
}
else
System.out.println("Minimum balance left. Please add money");
portal(row);
}
case 3:{
modify();
}
break;
case 4:{
genstate(1);
}
break;
default:{
con.close();
b.port();
}
break;
}
}
protected void consumer() throws Exception
{
viewcdbase=stmt.executeQuery("select acc_no,acc_fname,acc_lname,mobile,balance,useident,pwd,acctype from customerdbase1");
LocalTime today=LocalTime.now();
if(today.getHour()>=12)
{
System.out.print("Good Afternoon ");
}
else
{
System.out.print("Good Morning ");
}
System.out.print(name+".\nPlease enter your 5 digit account number :");
String accno=s.next();
int checker=0;
while(viewcdbase.next())
{
if(viewcdbase.getString(1).equals(accno))
{
checker=1;
int rowno=viewcdbase.getRow();
pincheck(rowno);
}//end of if accno found
}//end of while loop
if(checker==0)
{
System.out.println("No such account found.Please try again");
b.port();
}
}
}
class Bank {

	public static void main(String args[]) throws Exception
	{
	port();
	}
	static void port() throws Exception
	{
		Scanner s=new Scanner(System.in);
	System.out.print("Welcome to Anshul Bank online portal:");
	System.out.print("Who are you?\n1.Bank Official\t2.Customer\t3.Exit\nPlease select your option: ");
	int mychoice=s.nextInt();
	switch(mychoice)
	{
	case 1:{
	try{
	Banker b=new Banker();
	System.out.println("Redirecting to the official portal");
	b.official();
	}
	catch(Exception a)
	{
	}
	}
	break;
	case 2:{
	Customer c=new Customer();
	System.out.println("Redirecting to the consumer portal");
	c.consumer();
	}
		break;
	default:{
	System.out.println("Thank you!, Please visit us again.");
	System.exit(0);}
	break;
	}
	}

}
