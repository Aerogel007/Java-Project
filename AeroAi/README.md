**this is an a java fx app with spring boot for backend. and it runs as utiltiy inside system tray. and uses maven project structure.**  
-------------------------------------------------------------------------------------------------------------------------------------  
what this does.
* converse with jurassic2 AI, and ask it to paraphrase sentences to make it more polite, flirty, witty, etc...
* opens up a fx UI window. which runs on back ground. creates a system tray icon. which when right clicked pops up open and exit menu.
* the close x button is modified to only hide the window instead of closing the application. double clicking the system tray icon.
  or pressing on open button will show the window.
* additionally. running the .exe again will show the window too. i implemented this my using http request to be send to 8080 port.
  which the springboot uses. and send a push command to show the java fx UI
* in th UI, it has input and output script for text area. you can enter your sentences in input and press the appropriate button for processing.
  which, will show the result in output textarea. and there are three copy button. if you press them it will then copy the consequtive line to
  the clipboard. (note: it removes quotation marks, if numbers followed by ) or . and spaces when copying)
-------------------------------------------------------------------------------------------------------------------------------------
to use this app. load this in vs code under maven project structure. and build a .jar file. don't forget to modify the code to include you.
API key. which is in BackEnd.java section. and then you can use launch4j to convert it into an exe. you can even write ahky scrip to make 
the window pop up, so it is convinent to use while having a coversation.


