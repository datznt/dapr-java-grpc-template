package com.datntz.daprjavagrpc.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceUtils {
    /**
     * Read resource file by file name
     * 
     * @param filename
     * @return
     * @throws Exception
     */
    public static String read(String filename) throws Exception {

        // get input resource file stream
        var resource = new ClassPathResource(filename);
        var inputStream = resource.getInputStream();

        // get file data
        var fileData = FileCopyUtils.copyToByteArray(inputStream);
        var outputString = new String(fileData);

        return outputString;
    }

    /**
     * Read resource file and unmarshall json to type
     * 
     * @param <T>
     * @param filename
     * @param valueType
     * @return
     * @throws Exception
     */
    public static <T> T loadJson(String filename, Class<T> valueType) throws Exception {

        // read file
        var data = read(filename);

        // unmarshall json
        return new ObjectMapper().readValue(data, valueType);
    }

    /**
     * Read resource file and unmarshall json to type
     * 
     * @param <T>
     * @param filename
     * @param valueType
     * @return
     * @throws Exception
     */
    public static <T> T loadJson(String filename, TypeReference<T> valueType) throws Exception {
        // read file
        var data = read(filename);

        // unmarshall json
        return new ObjectMapper().readValue(data, valueType);
    }
}
