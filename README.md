# Information Security Search Engine (InfoSec)
searchengine

#Quick start apache solr

Step 1: Open command prompt "run-->cmd" <br/>
Step 2: Switch to directory "infosec\apache-solr\example" <br/>
Step 3: Execute "java -jar start.jar" , this will start apache solr <br/>
Step 4  Go to directory exampledocs "cd exampledocs" <br/>
Step 5  Unzip file FinalTransformed.zip <br/>
Step 6: Index data file "java -Xms1g -jar post.jar exampledocs\FinalTransformed.xml" <br/>
Step 7: Open URL "http://localhost:8983/solr/" to check solr is working 

#Search page

1. Open "InfoSecUI\InfoSecSearchEngineUI.html in Internet Explorer <br/>
2. search any value this will query apache solr and present the search results.

#Delete indexed files

1. "localhost:8983/solr/update?stream.body=<delete><query>*:*</query></delete>"
2. "localhost:8983/solr/update?stream.body=<commit/>"