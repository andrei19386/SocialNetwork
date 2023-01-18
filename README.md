# The name of Project
     The project is named "SocialNetwork"
 ## 1. About Project
        	The project is designed to implement a simple social network which have function authorization, brows of news (posts), adding/editing/deleting posts or comments, search of posts with given criterias, work with autor profile and adding friends. The project has been done in the command of developers.
        	

 ## 2. Technologies. 
		The system uses SpringBoot technology to implement responses with GET,POST,PUT and DELETE requests.
        	Storing, adding, deleting data is carried out using the PostgreSQL database management system.

 ## 3. Instruction.
		To run the project locally, you need to run the program in java for execution - run the main program: impl/src/main/java/ru/skillbox/SocialNetworkApplication.java. Run docker and load image from dockerHub: docker pull andrei19386/myrepository:v.4.1.1. Run the docker container on port 8086: docker run -d -p 8086:8086 andrei19386/myrepository:v.4.1.1.
        	Then open the browser and type into url line: http://localhost:8086/.
        	On first time, registrate yout account and then login. You can see the news:
![News](https://github.com/andrei19386/SocialNetwork/blob/main/news.png)
		The news are posts of the authorized person, his friends, and other people on which the authorized person is subscribed.
        	To search posts on the given criteria type the search button on the top of the window (marked with a red frame on the picture):
![Search_button_image](https://github.com/andrei19386/SocialNetwork/blob/main/search_button.png)
        	Input the criteria of search. If no criteria typed, all posts of the database will be shown.
![Posts_for_search_criteria](https://github.com/andrei19386/SocialNetwork/blob/main/search.png)
        	On the author profile we can see posts of the author only:
![Author_posts](https://github.com/andrei19386/SocialNetwork/blob/main/author.png)
