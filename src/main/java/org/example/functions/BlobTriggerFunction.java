package org.example.functions;

import static org.example.functions.utils.Constants.CONTAINER_NAME;
import static org.example.functions.utils.Utils.getContentFromBlob;
import static org.example.functions.utils.Utils.getFileName;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.BlobTrigger;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.StorageAccount;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.example.functions.utils.Constants;
import org.example.functions.utils.FileNames;
import org.example.functions.utils.Pair;

/**
 * Azure Functions with Azure Blob trigger.
 */
public class BlobTriggerFunction {
    /**
     * This function will be invoked when a new or updated blob is detected at the specified path. The blob contents are provided as input to this function.
     */
    @FunctionName("BlobTriggerFunction")
    @StorageAccount("daslogicappstorage_STORAGE")
    public void run(
        @BlobTrigger(name = "content", path = "attachments/{name}", dataType = "binary") byte[] content,
        @BindingName("name") String name,
        final ExecutionContext context
    ) {
        context.getLogger().info("Java Blob trigger function processed a blob. Name: " + name + "\n  Size: " + content.length + " Bytes");
        context.getLogger().info("Writing text of the file");
        String contentText = new String(content);
        context.getLogger().info(contentText);
        Pair<String, Map<String, String>> dateDataMap = createKeyValueMapFromMailContent(contentText);

        String date = dateDataMap.getKey();
        Map<String, String> valueMap = dateDataMap.getValue();
        try {
            for(Entry<String, String> entry : valueMap.entrySet()) {
                String fileName = getFileName(entry.getKey());
                String data = entry.getValue();
                writeMailContentToBlobStorage(context, fileName, data, date);
            }
        } catch (Exception e) {
            context.getLogger().severe(e.getMessage());
            for(StackTraceElement element : e.getStackTrace()) {
                context.getLogger().severe(element.toString());
            }
        }

    }

    private void writeMailContentToBlobStorage(ExecutionContext context, String fileName, String data, String date)
        throws IOException, StorageException, URISyntaxException, InvalidKeyException {

        CloudStorageAccount storageAccountDest = CloudStorageAccount.parse(Constants.CONNECTION_STRING);
        CloudBlobClient blobClientDest = storageAccountDest.createCloudBlobClient();
        CloudBlobContainer containerDest = blobClientDest.getContainerReference(CONTAINER_NAME);
        CloudBlob blobDest = containerDest.getBlockBlobReference(fileName);

        // Create the container if it does not exist with public access.
        context.getLogger().info("Creating container: " + containerDest.getName());
        containerDest.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
            new OperationContext());

        String fileContent = "[";
        if (blobDest.exists()) {
            fileContent = getContentFromBlob(blobDest);
        }

        try {
            byte[] content = (fileContent + "\n" + "[" + "\"" + date + "\"" + "," + data + "],").getBytes();
            context.getLogger().info("Start Uploading blob: " + fileName);
            blobDest.uploadFromByteArray(content, 0, content.length);
            context.getLogger().info("Finished Uploading blob: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pair<String, Map<String, String>> createKeyValueMapFromMailContent(String str) {
        int startIndex = str.indexOf("Hi");
        int endIndex = str.indexOf("\\\"}");
        str = str.substring(startIndex, endIndex);

        startIndex = str.indexOf("as of ") + 6;
        str = str.substring(startIndex);

        int dateEndIndex = str.indexOf(" @");
        String date = str.substring(0, dateEndIndex);

        //Time in the mail could be 19.00 or 19.01 or 19.02 so taking xx.
        str = str.substring(dateEndIndex + (" @ 19:"+"xx ").length());

        List<String> keyList = new ArrayList<>();
        for(FileNames name : FileNames.values()) {
            keyList.add(name.description());
        }

        Map<String, String> keyValueMap = new HashMap<>();

        for(String key:keyList){
            int index = str.indexOf(key);
            int valueIndex = index + key.length();
            String tmp = str.substring(valueIndex);
            int valueEndIndex = tmp.indexOf(" ");
            String value = tmp.substring(0, valueEndIndex);
            keyValueMap.put(key, value);
        }

        return new Pair<>(date, keyValueMap);
    }

}
