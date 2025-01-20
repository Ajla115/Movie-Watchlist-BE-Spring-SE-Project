# Movies-Watchlist-BE-Spring-SE-Project
- This is a repository for the backend of my project called Movies Watchlist.
- Backend was built using Spring.
- Additionally, tests were written and cover about 90% of the code.
- Frontend was built using React, and it can be found in the frontend repository (https://github.com/Ajla115/Movies-Watchlist-FE-React-SE-Project.git)
- Frontend repository contains UI screenshots, as well as application video flow.
- Full project documentation can be found in the file "Movies_Watchlist_SRS_Documentation.pdf".
- Application has two external interfaces implemented:
  1. Email Sending Service - sends emails with new movie suggestions form the same genre, when the movie gets marked as "Watched", if the notifications are not disabled.
  2. OPENAI API - recommends a genre for the movie based on the title when adding a new movie. Also, it suggest five similiar movies from the same genre to be sent as email recommendation.
- Application was deployed using Render.
