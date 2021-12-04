package org.example.functions.utils;

import static org.example.functions.utils.Constants.CONTAINER_NAME;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobInputStream;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class Utils {

  public static String getContentFromFileName(String fileName)
      throws URISyntaxException, InvalidKeyException, StorageException, IOException {
    // Parse the connection string and create a blob client to interact with Blob storage
    CloudStorageAccount storageAccountDest = CloudStorageAccount.parse(Constants.CONNECTION_STRING);
    CloudBlobClient blobClientDest = storageAccountDest.createCloudBlobClient();
    CloudBlobContainer containerDest = blobClientDest.getContainerReference(CONTAINER_NAME);
    CloudBlob blobDest = containerDest.getBlockBlobReference(fileName);

    return getContentFromBlob(blobDest);
  }

  public static String getContentFromBlob(CloudBlob blobDest) throws StorageException, IOException {
    String fileContent;
    StringBuilder sb = new StringBuilder();
    BlobInputStream blobIS = blobDest.openInputStream();
    BufferedReader br = new BufferedReader(new InputStreamReader(blobIS));

    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line).append(System.lineSeparator());
    }
    fileContent = sb.toString();
    return fileContent;
  }

  public static String getFileName(String keyName) {
    return keyName.trim().replace("/","")+".txt";
  }

}
