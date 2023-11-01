Instructions for Running ScalaBuild.
Run the scalabuild.jar file with the java command. 
$ java -jar scalabuild.jar

* Enter he Scala Play! Directory. 
* Enter the location of the DDL sql file that contains the Create Table statements used to build the application.

ScalaBuild will read all the tables which have Create Table statements and present them in this next screen.

The All Tables box contains all of the tables read from the SQL file.   

To Add or Delete from the Table List
* The Selected Tables originally contains the same set of tables which can deleted by selecting a table in the All Tables list and selecting the Table Delete button.

Deleted tables can be re-added by selecting the table in the All Tables list and selecting the Table Add button.

To Add or Delete fields from a Table\
NOTE:   The Primary Key field can not be deleted since it is necessary for building the Scala code per table as well as the Scala code for joins.     The primary key is shown beneath the Fields labels and can not be edited.   ( The example shows the primary key as ‘id’ ). 
* Select the table in the Selected Tables list.
* The fields will show in both of the All Fields and Selected Fields lists.
* To Delete a field select the field in the Selected Fields list and select the Delete button.

The field can be re-added by selecting the field in the All Fields list and selecting the Add button.

After filtering is completed select the Done button.  

Test classes are created as part of Scalabuild, one test class is created for each table.     Scala Play! Includes two default test classes that call default controllers.     These default test classes use the /messages and /count to call the CountController and AsyncController respectively.   Since Scalabuild builds a navigation web page at the root ( / )   two tests will fail once Scalabuild is run since these tests are looking for the string “Your new application is ready”.    Changing this string in the test classes to one of the table names will enable these tests to pass.   

/home/dkagle/demo/release4.2/bootstrap.bash

Modify build.sbt name := """name"""

cd ( directory you specified as the scala play directory ) 
