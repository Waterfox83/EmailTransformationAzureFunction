package org.example.functions;

import static org.example.functions.utils.Utils.getContentFromFileName;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import org.example.functions.utils.FileNames;
import org.example.functions.utils.Utils;

/**
 * Azure Functions with HTTP Trigger.
 */
public class HttpTriggerFunction {
    /**
     * This function listens at endpoint "/api/HttpTriggerFunction". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTriggerFunction
     * 2. curl {your host}/api/HttpTriggerFunction?name=HTTP%20Query
     */
    @FunctionName("HttpTriggerFunction")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        String query = request.getQueryParameters().get("name");
        String name = request.getBody().orElse(query);

        String keyName = "";
        keyName = getKeyNameFromRequestParam(name);

        String fileContent = "";
        try {
            fileContent = getContentFromFileName(Utils.getFileName(keyName));
            context.getLogger().info("File Content Read:" + fileContent);
            //format
            int endIndex = fileContent.lastIndexOf(",");
            fileContent = fileContent.substring(0, endIndex) + "]";
        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
            for(StackTraceElement element : e.getStackTrace()) {
                context.getLogger().severe(element.toString());
            }
        }

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body(fileContent).build();
        }
    }

    private String getKeyNameFromRequestParam(String name) {
        String keyName;
        switch(name) {
            case "0":
                keyName = FileNames.FIELD_0.description();
                break;
            case "1":
                keyName = FileNames.FIELD_1.description();
                break;
            case "2":
                keyName = FileNames.FIELD_2.description();
                break;
            case "3":
                keyName = FileNames.FIELD_3.description();
                break;
            case "4":
                keyName = FileNames.FIELD_4.description();
                break;
            case "5":
                keyName = FileNames.FIELD_5.description();
                break;
            case "6":
                keyName = FileNames.FIELD_6.description();
                break;
            case "7":
                keyName = FileNames.FIELD_7.description();
                break;
            case "8":
                keyName = FileNames.FIELD_8.description();
                break;
            case "9":
                keyName = FileNames.FIELD_9.description();
                break;
            case "10":
                keyName = FileNames.FIELD_10.description();
                break;
            case "11":
                keyName = FileNames.FIELD_11.description();
                break;
            case "12":
                keyName = FileNames.FIELD_12.description();
                break;
            case "13":
                keyName = FileNames.FIELD_13.description();
                break;
            case "14":
                keyName = FileNames.FIELD_14.description();
                break;
            case "15":
                keyName = FileNames.FIELD_15.description();
                break;
            case "16":
                keyName = FileNames.FIELD_16.description();
                break;
            case "17":
                keyName = FileNames.FIELD_17.description();
                break;
            case "18":
                keyName = FileNames.FIELD_18.description();
                break;
            default:
                keyName = FileNames.FIELD_19.description();
                break;
        }
        return keyName;
    }
}
