import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Set;

public  class summary 
{

 ArrayList als;
 Hashtable hs;
 double scnt;
 String strBody;
 String summaryText;
 public summary(String str)
  {
    strBody = new String(str);
	als=new ArrayList();
	hs=new Hashtable();
  }

 
public void removestopwords() {
    
    Stopword sp=new Stopword();
    Special spl=new Special();	
   
    for(int i=0;i<als.size(); i++) {    
	slist sl=(slist)als.get(i);    
	sl.setsrsentence(spl.remove(sl.getrawsentense()));
	sl.setsrsentence(sp.remove(sl.getsrsentence()));
    }
}


public void separatesentense(String str)
 {	
	Sentence sc=new Sentence(str);
	sc.separatesentense(als);
	scnt = als.size();
 }



public double difpos(String str1,String str2)
{     
  int s1=str1.length();
  int s2=str2.length(); 
  int sz=(s1 > s2 ) ? s1 : s2 ;
  int mz=(s1 < s2 ) ? s1 : s2 ;
  double dp=mz,sm=0;  
  for ( int i=0;i<mz; i++ )
   {
       if( str1.charAt(i) != str2.charAt(i) )
        {
	       dp=i+1;
           break;
	}
       else
	  sm++;	
   }   
   return(sm*(dp/sz));             
}


private void addword(String tok,int sp,int wp)
{
    wlist wl=new wlist(tok);
    wl.incrcount(sp+1,wp);    
    hs.put(tok,wl);
}
private void upword(String tok,int sp,int wp)
{    
     wlist wl=(wlist)hs.remove(tok);
     wl.incrcount(sp+1,wp);
     hs.put(tok,wl);     		
}

private void delword(Object tok)
{    
     wlist wl=(wlist)hs.remove(tok);
     if( wl.getcount() > 3 )
         hs.put(tok,wl);          
}


private void setwight(Object tok)
{    
     double wg=0.0;
     wlist wl=(wlist)hs.get(tok);
     double tf=wl.getcount();
     double df=wl.sentensecount(); 
     wg=tf*Math.log10(scnt/df); 
     wl.weight(wg);         	      
}


public void Uniquewords()
 {     	        
   for(int i=0;i<als.size(); i++) 
     {    
	slist sl=(slist)als.get(i);    			
	String sen=sl.getsrsentence();	
        int wc=0;
	StringTokenizer stk=new StringTokenizer(sen," ");	
	while ( stk.hasMoreElements() )
	 {			   
	   String tok=(String)stk.nextElement();
	   tok=tok.trim(); 
	   wc++;	   	
	   if(!hs.containsKey(tok) && tok.length() >=3)	              					   		    
	       addword(tok,i,wc);
	    else if(hs.containsKey(tok) )
	       upword(tok,i,wc);	         		    
	 }	         
      }      	
 
}

public void  stemword(String w1,String w2)
{

  if( !hs.containsKey(w2) || !hs.containsKey(w1) )
     {
       	return; 
     }
   wlist wl1=(wlist)hs.remove(w1);
   wlist wl2=(wlist)hs.remove(w2);
	
   ArrayList wp=wl2.getwordpos();
   ArrayList sp=wl2.getsentensepos();
  
	
  for(int i=0;i<wp.size();i++)
   {
    String wp2=(String)wp.get(i); 
    String sp2=(String)sp.get(i); 	
    wl1.incrcount(Integer.parseInt(wp2),Integer.parseInt(sp2));
   }

   hs.put(w1,wl1);       

}


public void stemming()
 {     	        

   int sz=hs.size();
   double wdis[][]=new double[sz][sz];
   Set s1=hs.keySet();
   Object obj[]=s1.toArray();   
  
    for(int i=0;i<sz;i++) {        
	String str1=(String) obj[i];
      for(int j=0;j<sz;j++) {
	String str2=(String) obj[j];
	if(i!=j)
	wdis[i][j]=difpos(str1,str2);		
      }
    } 

    for(int i=0;i<sz;i++) {        
	String str1=(String)obj[i];
      for(int j=0;j<sz;j++) {
	String str2=(String)obj[j];	
	if(i!=j && wdis[i][j] >= 3.0 ) {              
	   stemword(str1,str2); 		
	 }	  			
      }
    } 
      
}


public void significant()
{

  Enumeration key=hs.keys();	    
  while (key.hasMoreElements() )   
     delword(key.nextElement());
      
}


public void weight()
{    
  Enumeration key=hs.keys();	    
  while (key.hasMoreElements())     
    setwight(key.nextElement());
       
}


public String ranking()
{  
 slist  sl=null;  double max=0.0;int mi=0;
  
 for(int i=0;i<als.size();i++)
  { 	
   sl=(slist)als.get(i);    			
   String sen=sl.getsrsentence();	
   Enumeration key=hs.keys();	    
   while(key.hasMoreElements())     
    {	
     String str=(String)key.nextElement();         	
     if(sen.indexOf(str) != -1 )
      {
       wlist wl=(wlist)hs.get(str); 
       sl.weight(wl.weight());
      } 	       	
    }    
  }	

  
  for(int i=0;i<als.size();i++) { 	
      sl=(slist)als.get(i); 
      if( sl.weight() > max )
       {	
	max =  sl.weight();   	
	mi=i;
       }
  }
  sl=(slist)als.get(mi);
  String str1=sl.getrawsentense();
  return str1;
}

public void summarize()
{
	separatesentense(strBody);	
	removestopwords();
	Uniquewords();		
	stemming();
	significant();	
	weight();		
	String str=ranking();
	summaryText = new String(str);
	System.out.println(str);
}
 /*
 public static void main(String argv[])
 {
	 SAXParserFactory factory = SAXParserFactory.newInstance();
     try {
         File xmlInput = new File("C:/data/Final.xml");
         SAXParser saxParser = factory.newSAXParser();
         saxHandler handler   = new saxHandler();
         saxParser.parse(xmlInput, handler);
    
     } catch (Throwable err) {
         err.printStackTrace ();
     }

 }  
 */
public String getSummaryText() {
	// TODO Auto-generated method stub
	return summaryText;
} 

}