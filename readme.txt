Name - Nitin Dua
USC ID - 4563947761
Username - nitindua@usc.edu


List of submitted files
-----------------------
1. createdb.sql
2. dropdb.sql
3. populate.java
4. HW2.java
5. map.jpg
6. sdoapi.jar
7. readme.txt

My java files structure is same as in the zip. Both my java files are inside the src folder

The assignment has developed using Eclipse.

Resolution of Homework
----------------------
Using createdb.sql, I am creating three tables: Buildings, Students and AnnouncementSystems. Firstly, in case of Buildings, each building shape is being stored as a polygon of type SDO_GEOMETRY. Secondly, for students, the (x,y) co-ordinate of the position of the student is  being stored as a point using SDO_GEOMETRY data type. Lastly, the center's of each announcement system is being stored as a point of type SDO_GEOMETRY and the shape, ie coverage area in the form of a circle(special polygon), is also being stored of type SDO_GEOMETRY.
I am adding the description of these spatial data columns to the metadata 'USER_SDO_GEOM_METADATA'. Thus for each spatial data column there will be an insert statement. 
Finally, index of each spatial data column is being created.

populate.java takes in the file names as command line arguments. Before inserting, all tables rows are first deleted. Insertions are according to the schema declared in createdb.sql, for each file, by making use of SDO_GEOMETRY to insert the spatial data objects.

HW2.java consists of the GUI which has been designed using Swing api. It also consists of the resolution of all the 5 required query types. A summary of eqach query type is as below:

A) Whole Region - Based on the active elements which are checked, I am running a simple query to output the spatial data of those selected objects. 

B) Point Query - When the user selects Point Query, he/she can choose any point on the map which is promptly displayed along with a circle of radius 50. For each active element, I query those rows which have any interation with the user's point-circle. This is done using SDO_ANYINTERACT. For finding closest items, I am using SDO_NN
In case of buildings, I compare the user point-circle to any point on the polygon representing each building. If any part of the building lies inside the user point-circle, it is displayed.
In case of students, using ONLY the co-ordinates of the student as present in the database table, I find all the students present inside the point-circle and display them. I wish to point out that I am not querying against the student's 10x10 box as this does not represent the precise location of the student and hence I am using only the exact co-ordinate.
In the case of announcement systems, I check whether the coverage area of any of the AS are either inside or interact with the user's point-circle. 
For all three cases, if the particular spatial object is the closest of it's type to the point-circle, it is displayed in yellow, else in green.

C) Range Query - User is allowed to make a closed polygon/region based on the mouse clicks are per the homework specification document. After clicking on right button to close the polygon, if a new left click is made then the old polygon is discarded and a new polygon can be created. If a student lies inside or at the border of the region, it is displayed. If a building has any part within the region or intersecting it, it is displayed and similarly, if the coverage area of the AS has any part inside or at the border, it is displayed.

D) Surrounding Student Query - A user can select any point on the map based off which the closest AS is displayed. All students lying within or at the border the coverage area of this AS are displayed using SDO_ANYINTERACT. The actual co-ordinates of each student are taken into consideration and NOT the 10x10 box.

E) Emergency Query - A user can select any point on the map based off which the closest AS is displayed. This is considered broken. When the user clicks on submit, the broken AS is removed and replaced by all of its surrounding students (found in a similar way as the surrounding students query) and the closest functional AS for each of those students (found using SDO_NN). A student is deemed closest to the AS whose coverage area is closer. I will be displaying only the new AS for the surrounding students.

dropdb.sql drops/deletes as the database elements created initially.


Points to note during compile/run
---------------------------------

For populate.java, files should be placed in the main project folder and command line arguments MUST be provided in the following order - Buildings.xy Students.xy AnnouncementSystems.xy 

External jars being used for populate.java - classes111.jar  

External jars being used for HW2.java - classes111.jar and sdoapi.jar.
 
Both these JAR files need to be added to the build path by configuring the build path and selecting 'add external jars' option.



In case of any issues, please contact me at nitindua@usc.edu


