# Email to Chart Conversion #

This project uses Azure serverless offerings to read an email which sends data in text in 
tabular format, and convert it to a chart.

It uses a logic app to intercept the email and write the contents in form of a text file and
save it in Azure Blob Storage. Once the text file is saved in blob storage, a blob trigger function
(Azure function) is triggered. 

This function, reads the text file and writes values for different headings in different text files
and stores in the blob storage. For example, table has data for 'Onboarding Completed', it will be
written to OnboardingCompleted.txt. 

Finally there is an HttpTriggerFunction, which is used to serve the data by reading the files and
responding to Http requests (GET API)

https://daslogicappstorage.z19.web.core.windows.net/ 

