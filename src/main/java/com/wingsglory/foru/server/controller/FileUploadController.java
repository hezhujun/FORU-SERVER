package com.wingsglory.foru.server.controller;

import com.wingsglory.foru.server.model.Result;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hezhujun on 2017/7/14.
 * 处理文件上传请求
 */
@RestController
public class FileUploadController {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final String DIRECTORY = "image";

    /**
     * 上传图片
     *
     * @param file
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        Result result = new Result();
        try {
            String extension = null;
            if ("image/jpeg".equals(file.getContentType())) {
                extension = ".jpg";
            } else if ("image/png".equals(file.getContentType())) {
                extension = ".png";
            } else {
                throw new Exception("上传文件必须为jpg或png");
            }
            File directory = new File(System.getProperty("foru.root.path") +
                    File.separator + DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }
//            System.out.println(directory);
            Date date = new Date();
            File img = File.createTempFile(sdf.format(date), extension, directory);
            FileUtils.writeByteArrayToFile(img, file.getBytes());
            map.put("image", DIRECTORY + "/" + img.getName());
        } catch (Exception e) {
            e.printStackTrace();
            result.setSuccess(false);
            result.setErr(e.getMessage());
        }
        map.put("result", result);
        return map;
    }

}
